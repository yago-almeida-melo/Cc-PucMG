/*
 * sum_cuda_no_shared.cu
 * Somatório paralelo em CUDA — SEM memória __shared__
 *
 * Cada thread lê diretamente da memória global (d_a) e acumula
 * o resultado também na memória global (d_s).
 *
 * Compilação:
 *   nvcc -O3 sum_cuda_no_shared.cu -o sum_no_shared
 *
 * Execução + tempo:
 *   time ./sum_no_shared
 *
 * Profiling:
 *   nvprof ./sum_no_shared
 */

#include <stdio.h>
#include <stdlib.h>

/* ------------------------------------------------------------------
 * Kernel SEM __shared__
 * A redução é feita lendo e escrevendo direto na memória global d_a.
 * Cada bloco reduz sua fatia usando um stride decrescente.
 * ------------------------------------------------------------------ */
__global__ void sum_cuda(double *a, double *s, int width)
{
    int t = threadIdx.x;
    int b = blockIdx.x * blockDim.x;

    /* Redução na memória global: stride começa em blockDim.x/2 */
    int i;
    for (i = blockDim.x / 2; i > 0; i /= 2) {
        /* Só opera se ambos os índices estiverem dentro do vetor */
        if (t < i && b + t + i < width)
            a[b + t] += a[b + t + i];

        __syncthreads();   /* ainda necessário para consistência global */
    }

    /* Thread 0 de cada bloco grava a soma parcial */
    if (t == 0 && b < width)
        s[blockIdx.x] = a[b];
}

int main(void)
{
    int width      = 40000000;
    int size       = width * sizeof(double);

    int block_size = 1024;
    int num_blocks = (width - 1) / block_size + 1;
    int s_size     = num_blocks * sizeof(double);

    double *a = (double *) malloc(size);
    double *s = (double *) malloc(s_size);

    for (int i = 0; i < width; i++)
        a[i] = (double) i;

    double *d_a, *d_s;

    /* Alocação e cópia dos dados */
    cudaMalloc((void **) &d_a, size);
    cudaMemcpy(d_a, a, size, cudaMemcpyHostToDevice);

    cudaMalloc((void **) &d_s, s_size);

    /* Configuração da grade */
    dim3 dimGrid(num_blocks, 1, 1);
    dim3 dimBlock(block_size, 1, 1);

    /* Chamada do kernel */
    sum_cuda<<<dimGrid, dimBlock>>>(d_a, d_s, width);
    cudaDeviceSynchronize();

    /* Cópia dos resultados para o host */
    cudaMemcpy(s, d_s, s_size, cudaMemcpyDeviceToHost);

    /* Soma das reduções parciais no host */
    for (int i = 1; i < num_blocks; i++)
        s[0] += s[i];

    printf("\nSum = %f\n", s[0]);

    cudaFree(d_a);
    cudaFree(d_s);
    free(a);
    free(s);

    return 0;
}
