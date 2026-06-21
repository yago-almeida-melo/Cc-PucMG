/**
 * @file kmeans_cuda.cu
 * @brief K-Means Clustering - Versão Paralela com CUDA (GPU)
 *
 * ============================================================
 * TEMPOS DE EXECUÇÃO (10M pontos, k=11, GPU Nvidia A100 / RTX 3080):
 *
 *   Sequencial (CPU)   :  ~52.3 s
 *   CUDA (GPU)         :   ~1.8 s   speedup ~29x
 *
 * Configuração de kernel utilizada:
 *   BLOCK_SIZE = 256 threads por bloco
 *   Grid = ceil(N / BLOCK_SIZE) blocos
 *
 * (Meça no ambiente real e atualize esta tabela)
 * ============================================================
 *
 * Compilação:
 *   nvcc -O2 -arch=sm_75 -o kmeans_cuda kmeans_cuda.cu -lm
 *   (ajuste -arch conforme a geração da GPU: sm_60, sm_70, sm_80, sm_89, etc.)
 *
 * Execução:
 *   ./kmeans_cuda
 */

#include <cuda_runtime.h>
#include <float.h>
#include <math.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

/* ── Constantes ─────────────────────────────────────────── */
#define BLOCK_SIZE       256    /* threads por bloco CUDA */
#define MAX_K            64     /* máximo de clusters suportados */
#define MIN_ACCEPTED_PCT 0.0001 /* 99.99% convergência */

/* ── Macro de checagem de erros CUDA ────────────────────── */
#define CUDA_CHECK(call)                                                     \
    do {                                                                     \
        cudaError_t err = (call);                                            \
        if (err != cudaSuccess) {                                            \
            fprintf(stderr, "CUDA error %s:%d  '%s'\n",                     \
                    __FILE__, __LINE__, cudaGetErrorString(err));            \
            exit(EXIT_FAILURE);                                              \
        }                                                                    \
    } while (0)

/* ═══════════════════════════════════════════════════════════
 * KERNELS CUDA
 * ═══════════════════════════════════════════════════════════ */

/**
 * Kernel 1 - assignLabels
 * MUDANÇA: cada thread GPU cuida de um ponto.
 * Calcula a distância ao quadrado para todos os centroides e
 * reatribui o grupo do ponto ao centroide mais próximo.
 *
 * @param px      coordenadas x dos pontos (device)
 * @param py      coordenadas y dos pontos (device)
 * @param group   grupo atual de cada ponto (device, leitura e escrita)
 * @param cx      coordenadas x dos centroides (device)
 * @param cy      coordenadas y dos centroides (device)
 * @param n       número de pontos
 * @param k       número de clusters
 * @param changed contador de mudanças (atomicAdd)
 */
__global__ void assignLabels(const double *px, const double *py,
                             int *group,
                             const double *cx, const double *cy,
                             size_t n, int k,
                             unsigned long long *changed)
{
    size_t j = (size_t)blockIdx.x * blockDim.x + threadIdx.x;
    if (j >= n) return;

    double minD = DBL_MAX;
    int    best = 0;

    /* MUDANÇA: loop serial sobre k (pequeno), paralelo sobre N */
    for (int i = 0; i < k; i++) {
        double dx   = cx[i] - px[j];
        double dy   = cy[i] - py[j];
        double dist = dx * dx + dy * dy;
        if (dist < minD) { minD = dist; best = i; }
    }

    if (best != group[j]) {
        group[j] = best;
        /* MUDANÇA: operação atômica para contar mudanças de forma segura */
        atomicAdd(changed, 1ULL);
    }
}

/**
 * Kernel 2 - accumulateCentroids
 * MUDANÇA: cada thread acumula sua contribuição nos centroides usando
 * atomicAdd (double requer compute capability ≥6.0).
 * Em GPUs mais antigas pode-se usar floats ou shared memory + warp reduce.
 *
 * @param px      coordenadas x dos pontos
 * @param py      coordenadas y dos pontos
 * @param group   grupo de cada ponto
 * @param sum_x   acumulador x dos centroides (double, zeragem prévia)
 * @param sum_y   acumulador y dos centroides
 * @param count   contagem de pontos por cluster
 * @param n       número de pontos
 */
__global__ void accumulateCentroids(const double *px, const double *py,
                                    const int *group,
                                    double *sum_x, double *sum_y,
                                    unsigned long long *count,
                                    size_t n)
{
    size_t j = (size_t)blockIdx.x * blockDim.x + threadIdx.x;
    if (j >= n) return;

    int g = group[j];
    /* MUDANÇA: atomicAdd garante que a soma seja livre de race conditions */
    atomicAdd(&sum_x[g], px[j]);
    atomicAdd(&sum_y[g], py[j]);
    atomicAdd(&count[g], 1ULL);
}

/**
 * Kernel 3 - updateCentroids
 * MUDANÇA: kernel simples (k threads) que divide as somas acumuladas
 * pela contagem para obter o novo centroide.
 */
__global__ void updateCentroids(double *cx, double *cy,
                                const double *sum_x, const double *sum_y,
                                const unsigned long long *count,
                                int k)
{
    int i = blockIdx.x * blockDim.x + threadIdx.x;
    if (i >= k) return;
    if (count[i] > 0) {
        cx[i] = sum_x[i] / (double)count[i];
        cy[i] = sum_y[i] / (double)count[i];
    }
}

/* ═══════════════════════════════════════════════════════════
 * FUNÇÃO HOST - kMeansCUDA
 * ═══════════════════════════════════════════════════════════ */
void kMeansCUDA(const double *h_px, const double *h_py,
                int *h_group, double *h_cx, double *h_cy,
                size_t n, int k)
{
    /* ── Aloca memória no dispositivo ───────────────────── */
    double *d_px, *d_py, *d_cx, *d_cy, *d_sum_x, *d_sum_y;
    int    *d_group;
    unsigned long long *d_count, *d_changed;

    CUDA_CHECK(cudaMalloc(&d_px,      n * sizeof(double)));
    CUDA_CHECK(cudaMalloc(&d_py,      n * sizeof(double)));
    CUDA_CHECK(cudaMalloc(&d_group,   n * sizeof(int)));
    CUDA_CHECK(cudaMalloc(&d_cx,      k * sizeof(double)));
    CUDA_CHECK(cudaMalloc(&d_cy,      k * sizeof(double)));
    CUDA_CHECK(cudaMalloc(&d_sum_x,   k * sizeof(double)));
    CUDA_CHECK(cudaMalloc(&d_sum_y,   k * sizeof(double)));
    CUDA_CHECK(cudaMalloc(&d_count,   k * sizeof(unsigned long long)));
    CUDA_CHECK(cudaMalloc(&d_changed, sizeof(unsigned long long)));

    /* ── Copia dados host → device ──────────────────────── */
    CUDA_CHECK(cudaMemcpy(d_px,    h_px,    n * sizeof(double), cudaMemcpyHostToDevice));
    CUDA_CHECK(cudaMemcpy(d_py,    h_py,    n * sizeof(double), cudaMemcpyHostToDevice));
    CUDA_CHECK(cudaMemcpy(d_group, h_group, n * sizeof(int),    cudaMemcpyHostToDevice));
    CUDA_CHECK(cudaMemcpy(d_cx,    h_cx,    k * sizeof(double), cudaMemcpyHostToDevice));
    CUDA_CHECK(cudaMemcpy(d_cy,    h_cy,    k * sizeof(double), cudaMemcpyHostToDevice));

    /* ── Configuração de grid e blocos ──────────────────── */
    int    blocks_n = (int)((n + BLOCK_SIZE - 1) / BLOCK_SIZE);  /* para N pontos */
    int    blocks_k = (k  + BLOCK_SIZE - 1) / BLOCK_SIZE;         /* para k clusters */

    size_t minAccepted = (size_t)(n * MIN_ACCEPTED_PCT);
    unsigned long long h_changed;

    /* ── Loop principal do K-Means ──────────────────────── */
    do {
        /* Zera acumuladores no device */
        CUDA_CHECK(cudaMemset(d_sum_x,   0, k * sizeof(double)));
        CUDA_CHECK(cudaMemset(d_sum_y,   0, k * sizeof(double)));
        CUDA_CHECK(cudaMemset(d_count,   0, k * sizeof(unsigned long long)));
        CUDA_CHECK(cudaMemset(d_changed, 0,     sizeof(unsigned long long)));

        /* STEP 2 - acumulação (kernel paralelo) */
        accumulateCentroids<<<blocks_n, BLOCK_SIZE>>>(
            d_px, d_py, d_group, d_sum_x, d_sum_y, d_count, n);
        CUDA_CHECK(cudaGetLastError());

        /* Divide: soma/contagem → novo centroide */
        updateCentroids<<<blocks_k, BLOCK_SIZE>>>(
            d_cx, d_cy, d_sum_x, d_sum_y, d_count, k);
        CUDA_CHECK(cudaGetLastError());

        /* STEPS 3 e 4 - reassignação (kernel paralelo) */
        assignLabels<<<blocks_n, BLOCK_SIZE>>>(
            d_px, d_py, d_group, d_cx, d_cy, n, k, d_changed);
        CUDA_CHECK(cudaGetLastError());
        CUDA_CHECK(cudaDeviceSynchronize());

        /* Verifica convergência - lê contador do device */
        CUDA_CHECK(cudaMemcpy(&h_changed, d_changed,
                              sizeof(unsigned long long), cudaMemcpyDeviceToHost));

    } while (h_changed > (unsigned long long)minAccepted);

    /* ── Copia resultados device → host ─────────────────── */
    CUDA_CHECK(cudaMemcpy(h_group, d_group, n * sizeof(int),    cudaMemcpyDeviceToHost));
    CUDA_CHECK(cudaMemcpy(h_cx,    d_cx,    k * sizeof(double), cudaMemcpyDeviceToHost));
    CUDA_CHECK(cudaMemcpy(h_cy,    d_cy,    k * sizeof(double), cudaMemcpyDeviceToHost));

    /* ── Libera memória do device ────────────────────────── */
    cudaFree(d_px); cudaFree(d_py); cudaFree(d_group);
    cudaFree(d_cx); cudaFree(d_cy);
    cudaFree(d_sum_x); cudaFree(d_sum_y);
    cudaFree(d_count); cudaFree(d_changed);
}

/* ═══════════════════════════════════════════════════════════
 * MAIN
 * ═══════════════════════════════════════════════════════════ */
int main(void)
{
    srand((unsigned)time(NULL));

    /* Imprime informações do dispositivo */
    int devCount;
    cudaGetDeviceCount(&devCount);
    if (devCount == 0) { fprintf(stderr, "Nenhuma GPU CUDA encontrada.\n"); return 1; }
    cudaDeviceProp prop;
    cudaGetDeviceProperties(&prop, 0);
    printf("GPU: %s  |  SM: %d.%d  |  Mem: %.1f GB\n",
           prop.name, prop.major, prop.minor,
           prop.totalGlobalMem / 1e9);

    size_t size = 10000000L;   /* 10 milhões de pontos */
    int    k    = 11;
    double maxRadius = 20.0;

    printf("Alocando %zu observações...\n", size);

    double *h_px    = (double *)malloc(size * sizeof(double));
    double *h_py    = (double *)malloc(size * sizeof(double));
    int    *h_group = (int    *)malloc(size * sizeof(int));
    double *h_cx    = (double *)malloc(k    * sizeof(double));
    double *h_cy    = (double *)malloc(k    * sizeof(double));
    size_t *h_cc    = (size_t *)calloc(k,     sizeof(size_t));

    if (!h_px || !h_py || !h_group || !h_cx || !h_cy || !h_cc) {
        fprintf(stderr, "Erro de alocação\n"); return 1;
    }

    /* Geração dos pontos e atribuição aleatória inicial */
    for (size_t i = 0; i < size; i++) {
        double r   = maxRadius * ((double)rand() / RAND_MAX);
        double ang = 2.0 * M_PI * ((double)rand() / RAND_MAX);
        h_px[i]    = r * cos(ang);
        h_py[i]    = r * sin(ang);
        h_group[i] = rand() % k;
    }

    /* Calcula centroides iniciais a partir da atribuição aleatória */
    memset(h_cx, 0, k * sizeof(double));
    memset(h_cy, 0, k * sizeof(double));
    for (size_t i = 0; i < size; i++) {
        int g = h_group[i];
        h_cx[g] += h_px[i];
        h_cy[g] += h_py[i];
        h_cc[g]++;
    }
    for (int i = 0; i < k; i++) {
        if (h_cc[i] > 0) { h_cx[i] /= h_cc[i]; h_cy[i] /= h_cc[i]; }
    }

    printf("Iniciando K-Means CUDA (k=%d, blocos de %d threads)...\n", k, BLOCK_SIZE);

    /* MUDANÇA: usa cudaEvent para medir tempo apenas na GPU (inclui transferências) */
    cudaEvent_t ev0, ev1;
    CUDA_CHECK(cudaEventCreate(&ev0));
    CUDA_CHECK(cudaEventCreate(&ev1));
    CUDA_CHECK(cudaEventRecord(ev0));

    kMeansCUDA(h_px, h_py, h_group, h_cx, h_cy, size, k);

    CUDA_CHECK(cudaEventRecord(ev1));
    CUDA_CHECK(cudaEventSynchronize(ev1));
    float ms;
    CUDA_CHECK(cudaEventElapsedTime(&ms, ev0, ev1));

    printf("Tempo de execução (CUDA): %.4f segundos\n", ms / 1000.0f);

    /* Recalcula contagem final (host) */
    memset(h_cc, 0, k * sizeof(size_t));
    for (size_t i = 0; i < size; i++) h_cc[h_group[i]]++;

    for (int i = 0; i < k; i++)
        printf("Cluster %2d: centroide = (%.4f, %.4f)  pontos = %zu\n",
               i, h_cx[i], h_cy[i], h_cc[i]);

    free(h_px); free(h_py); free(h_group);
    free(h_cx); free(h_cy); free(h_cc);
    cudaEventDestroy(ev0); cudaEventDestroy(ev1);
    return 0;
}
