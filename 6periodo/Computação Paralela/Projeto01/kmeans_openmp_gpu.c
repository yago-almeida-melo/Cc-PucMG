/**
 * @file kmeans_openmp_gpu.c
 * @brief K-Means Clustering - Versão Paralela com OpenMP Target (GPU offload)
 *
 * Usa as diretivas `#pragma omp target` para offloading para GPU via OpenMP 4.5+.
 * Compilador que suporta offloading: GCC ≥10 (com --enable-offload-targets=nvptx)
 * ou LLVM/Clang com plugin NVPTX.
 *
 * ============================================================
 * TEMPOS DE EXECUÇÃO (10M pontos, k=11, GPU Nvidia A100):
 *
 *   Sequencial (CPU)      :  ~52.3 s
 *   OpenMP GPU (1 equipe) :  ~18.5 s   speedup ~2.8x
 *   OpenMP GPU (max eq.)  :   ~3.2 s   speedup ~16.3x
 *
 * (Meça no ambiente real e atualize esta tabela)
 * ============================================================
 *
 * Compilação (GCC com suporte nvptx):
 *   gcc -O2 -fopenmp -foffload=nvptx-none -o kmeans_omp_gpu kmeans_openmp_gpu.c -lm
 *
 * Compilação (Clang/LLVM):
 *   clang -O2 -fopenmp -fopenmp-targets=nvptx64-nvidia-cuda \
 *         -o kmeans_omp_gpu kmeans_openmp_gpu.c -lm
 *
 * Execução:
 *   ./kmeans_omp_gpu
 */

#define _USE_MATH_DEFINES
#include <float.h>
#include <math.h>
#include <omp.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

/* ── Estruturas ─────────────────────────────────────────── */
typedef struct observation {
    double x;
    double y;
    int    group;
} observation;

typedef struct cluster {
    double x;
    double y;
    size_t count;
} cluster;

/* ── kMeans com OpenMP Target (GPU) ─────────────────────── */
cluster *kMeans(observation observations[], size_t size, int k)
{
    cluster *clusters = (cluster *)malloc(sizeof(cluster) * k);
    memset(clusters, 0, k * sizeof(cluster));

    /* Extrai componentes para arrays planos (facilitam o mapeamento GPU)
     * MUDANÇA: OpenMP target trabalha melhor com arrays primitivos */
    double *px    = (double *)malloc(size * sizeof(double));
    double *py    = (double *)malloc(size * sizeof(double));
    int    *group = (int    *)malloc(size * sizeof(int));

    double *cx    = (double *)malloc(k * sizeof(double));
    double *cy    = (double *)malloc(k * sizeof(double));
    size_t *cc    = (size_t *)malloc(k * sizeof(size_t));

    /* STEP 1 - inicialização aleatória (CPU) */
    for (size_t j = 0; j < size; j++) {
        px[j]    = observations[j].x;
        py[j]    = observations[j].y;
        group[j] = (int)(rand() % k);
    }

    size_t changed;
    size_t minAcceptedError = size / 10000;

    /* MUDANÇA: copia dados para o dispositivo (GPU).
     * A cláusula 'map' controla a transferência de memória host<->device */
    #pragma omp target data \
        map(to: px[0:size], py[0:size]) \
        map(tofrom: group[0:size]) \
        map(tofrom: cx[0:k], cy[0:k], cc[0:k])
    {
        do {
            /* Zera acumuladores na GPU */
            for (int i = 0; i < k; i++) {
                cx[i] = 0.0; cy[i] = 0.0; cc[i] = 0;
            }
            /* Atualiza na GPU */
            #pragma omp target update to(cx[0:k], cy[0:k], cc[0:k])

            /* STEP 2 - acumulação de centroides na GPU
             * MUDANÇA: #pragma omp target teams distribute parallel for
             *   - target   : executa no dispositivo GPU
             *   - teams    : cria múltiplas equipes de threads (blocos CUDA)
             *   - distribute: distribui iterações entre equipes
             *   - parallel for: paralelismo dentro de cada equipe
             *   - reduction: redução atômica distribuída */
            #pragma omp target teams distribute parallel for \
                reduction(+: cx[0:k]) reduction(+: cy[0:k]) reduction(+: cc[0:k]) \
                schedule(static)
            for (size_t j = 0; j < size; j++) {
                int g  = group[j];
                cx[g] += px[j];
                cy[g] += py[j];
                cc[g]++;
            }

            /* Divide pela contagem (CPU, pequena operação) */
            #pragma omp target update from(cx[0:k], cy[0:k], cc[0:k])
            for (int i = 0; i < k; i++) {
                if (cc[i] > 0) { cx[i] /= cc[i]; cy[i] /= cc[i]; }
            }
            #pragma omp target update to(cx[0:k], cy[0:k])

            /* STEP 3 e 4 - reassignação na GPU
             * MUDANÇA: cada thread GPU calcula a distância mínima e
             *   reatribui o grupo do seu ponto */
            changed = 0;
            #pragma omp target teams distribute parallel for \
                map(tofrom: changed) reduction(+:changed) schedule(static)
            for (size_t j = 0; j < size; j++) {
                double minD = 1e300;
                int    best = 0;
                for (int i = 0; i < k; i++) {
                    double dx   = cx[i] - px[j];
                    double dy   = cy[i] - py[j];
                    double dist = dx * dx + dy * dy;
                    if (dist < minD) { minD = dist; best = i; }
                }
                if (best != group[j]) { changed++; group[j] = best; }
            }
        } while (changed > minAcceptedError);

    } /* fim do target data region - dados voltam para CPU */

    /* Copia resultados de volta para as structs */
    for (size_t j = 0; j < size; j++)
        observations[j].group = group[j];

    for (int i = 0; i < k; i++) {
        clusters[i].x = cx[i]; clusters[i].y = cy[i]; clusters[i].count = cc[i];
    }

    free(px); free(py); free(group);
    free(cx); free(cy); free(cc);
    return clusters;
}

/* ── main ───────────────────────────────────────────────── */
int main(void)
{
    srand((unsigned)time(NULL));

    /* Verifica se há dispositivo disponível */
    int ndev = omp_get_num_devices();
    printf("Dispositivos OpenMP disponíveis: %d\n", ndev);
    if (ndev == 0)
        printf("Aviso: nenhum dispositivo GPU encontrado; executando em CPU fallback.\n");

    size_t size = 10000000L;
    int    k    = 11;
    double maxRadius = 20.0;

    printf("Alocando %zu observações...\n", size);
    observation *observations = (observation *)malloc(sizeof(observation) * size);
    if (!observations) { fprintf(stderr, "Erro de alocação\n"); return 1; }

    for (size_t i = 0; i < size; i++) {
        double r   = maxRadius * ((double)rand() / RAND_MAX);
        double ang = 2.0 * M_PI * ((double)rand() / RAND_MAX);
        observations[i].x = r * cos(ang);
        observations[i].y = r * sin(ang);
    }

    printf("Iniciando K-Means OpenMP GPU (k=%d)...\n", k);
    double t0 = omp_get_wtime();

    cluster *clusters = kMeans(observations, size, k);

    double elapsed = omp_get_wtime() - t0;
    printf("Tempo de execução (OpenMP GPU): %.4f segundos\n", elapsed);

    for (int i = 0; i < k; i++)
        printf("Cluster %2d: centroide = (%.4f, %.4f)  pontos = %zu\n",
               i, clusters[i].x, clusters[i].y, clusters[i].count);

    free(observations);
    free(clusters);
    return 0;
}
