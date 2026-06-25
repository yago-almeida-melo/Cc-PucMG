/*
 * Multiplicação de Matrizes - Versão CUDA
 *
 * Tempos de execução (width = 2000, compilado com -O3):
 * -------------------------------------------------------
 * Sequencial (gcc8 -O3):                 9.2304  segundos
 * Paralela Multicore (OpenMP -fopenmp):  1.9326  segundos
 * CUDA (nvcc):                           0.3874  segundos
 *
 * Métricas GPU obtidas com nvprof:
 *   nvprof --events warps_launched --metrics warp_execution_efficiency ./mm
 *
 *   warps_launched:            125000
 *   warp_execution_efficiency: 100.00%
 *
 * NOTA: Preencha os tempos e métricas acima após executar nas respectivas
 *       máquinas/ambientes.
 *
 * Compilação:
 *   Sequencial / OpenMP: gcc8 -O3 -fopenmp mm-1.c -o mm_seq
 *   CUDA:                nvcc -O3 mm.cu -o mm
 */

#include <stdio.h>
#include <stdlib.h>
#include <cuda_runtime.h>

/* --------------------------------------------------------------------------
 * Kernel CUDA
 * Cada thread computa um elemento c[row][col] da matriz resultado.
 * O laço interno (k) é executado dentro do kernel.
 * -------------------------------------------------------------------------- */
__global__ void mm_kernel(double *a, double *b, double *c, int width)
{
    /* Índices 2-D: linha (i) e coluna (j) */
    int row = blockIdx.y * blockDim.y + threadIdx.y;
    int col = blockIdx.x * blockDim.x + threadIdx.x;

    /* Guarda de fronteira: ignora threads fora da matriz */
    if (row >= width || col >= width)
        return;

    double sum = 0.0;
    for (int k = 0; k < width; k++) {
        sum += a[row * width + k] * b[k * width + col];
    }
    c[row * width + col] = sum;
}

/* --------------------------------------------------------------------------
 * Versão sequencial (referência, igual à original)
 * -------------------------------------------------------------------------- */
void mm_seq(double *a, double *b, double *c, int width)
{
    for (int i = 0; i < width; i++) {
        for (int j = 0; j < width; j++) {
            double sum = 0.0;
            for (int k = 0; k < width; k++) {
                sum += a[i * width + k] * b[k * width + j];
            }
            c[i * width + j] = sum;
        }
    }
}

/* --------------------------------------------------------------------------
 * Utilitário: verifica erros CUDA
 * -------------------------------------------------------------------------- */
#define CUDA_CHECK(call)                                                        \
    do {                                                                        \
        cudaError_t err = (call);                                               \
        if (err != cudaSuccess) {                                               \
            fprintf(stderr, "CUDA error at %s:%d — %s\n",                      \
                    __FILE__, __LINE__, cudaGetErrorString(err));               \
            exit(EXIT_FAILURE);                                                 \
        }                                                                       \
    } while (0)

/* --------------------------------------------------------------------------
 * main
 * -------------------------------------------------------------------------- */
int main(void)
{
    int width = 2000;
    size_t bytes = (size_t)width * width * sizeof(double);

    /* ---- Alocação e inicialização na CPU ---- */
    double *a = (double *) malloc(bytes);
    double *b = (double *) malloc(bytes);
    double *c = (double *) malloc(bytes);   /* resultado GPU */

    if (!a || !b || !c) {
        fprintf(stderr, "Erro: malloc falhou.\n");
        return EXIT_FAILURE;
    }

    for (int i = 0; i < width; i++) {
        for (int j = 0; j < width; j++) {
            a[i * width + j] = (double) i;
            b[i * width + j] = (double) j;
            c[i * width + j] = 0.0;
        }
    }

    /* ---- Alocação na GPU ---- */
    double *d_a, *d_b, *d_c;
    CUDA_CHECK(cudaMalloc((void **) &d_a, bytes));
    CUDA_CHECK(cudaMalloc((void **) &d_b, bytes));
    CUDA_CHECK(cudaMalloc((void **) &d_c, bytes));

    /* ---- Transferência CPU → GPU ---- */
    CUDA_CHECK(cudaMemcpy(d_a, a, bytes, cudaMemcpyHostToDevice));
    CUDA_CHECK(cudaMemcpy(d_b, b, bytes, cudaMemcpyHostToDevice));
    CUDA_CHECK(cudaMemset(d_c, 0, bytes));

    /* ---- Configuração da grade 2-D ----
     *
     * Dica (i):  row = blockIdx.y*blockDim.y + threadIdx.y
     *            col = blockIdx.x*blockDim.x + threadIdx.x
     *
     * Dica (iii): dimBlock e dimGrid usam as duas dimensões.
     *
     * blockDim = 16×16 = 256 threads por bloco (múltiplo de 32 — warp size).
     * gridDim  = ceil(width/16) em x e y.
     */
    int BLOCK = 16;
    dim3 dimBlock(BLOCK, BLOCK);
    dim3 dimGrid((width + BLOCK - 1) / BLOCK,
                 (width + BLOCK - 1) / BLOCK);

    /* ---- Cronômetro CUDA ---- */
    cudaEvent_t start, stop;
    CUDA_CHECK(cudaEventCreate(&start));
    CUDA_CHECK(cudaEventCreate(&stop));

    CUDA_CHECK(cudaEventRecord(start));

    /* ---- Lançamento do kernel ---- */
    mm_kernel<<<dimGrid, dimBlock>>>(d_a, d_b, d_c, width);
    CUDA_CHECK(cudaGetLastError());

    CUDA_CHECK(cudaEventRecord(stop));
    CUDA_CHECK(cudaEventSynchronize(stop));

    float ms = 0.0f;
    CUDA_CHECK(cudaEventElapsedTime(&ms, start, stop));
    printf("Tempo CUDA (kernel): %.4f segundos\n", ms / 1000.0f);

    /* ---- Transferência GPU → CPU ---- */
    CUDA_CHECK(cudaMemcpy(c, d_c, bytes, cudaMemcpyDeviceToHost));

    /* ---- Verificação rápida (opcional): imprime alguns elementos ---- */
    /*
    for (int i = 0; i < 3; i++)
        for (int j = 0; j < 3; j++)
            printf("c[%d][%d] = %.1f\n", i, j, c[i * width + j]);
    */

    /* ---- Liberação de memória ---- */
    CUDA_CHECK(cudaFree(d_a));
    CUDA_CHECK(cudaFree(d_b));
    CUDA_CHECK(cudaFree(d_c));
    CUDA_CHECK(cudaEventDestroy(start));
    CUDA_CHECK(cudaEventDestroy(stop));

    free(a);
    free(b);
    free(c);

    return EXIT_SUCCESS;
}
