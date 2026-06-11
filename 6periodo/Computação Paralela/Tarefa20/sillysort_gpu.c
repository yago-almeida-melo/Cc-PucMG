// =============================================================
// Silly Sort - Versões: Sequencial, Multicore e GPU (OpenMP)
// Compilador: gcc8
// Flags: -O3 -fopenmp
//
// Compilação GPU:
//   gcc-12 -O3 -fopenmp -foffload=nvptx-none sillysort_gpu.c -o sillysort_gpu
//
// Caso o ambiente não suporte offloading nvptx, o código recai
// automaticamente para execução no host (fallback transparente).
// -------------------------------------------------------------
// Tempo Sequencial:        real    1,49s
//                          user    1,49s
//                          sys     0,00s
//                          cpu     99%
// -------------------------------------------------------------
// Tempo Paralelo Multicore real    0,79s   (2 threads, schedule static)
//                          user    1,58s
//                          sys     0,00s
//                          cpu     199%
// -------------------------------------------------------------
// Tempo Paralelo GPU       real    2,07s   (offload para GPU)
//                          user    1,89s
//                          sys     0,05s
//                          cpu     93%
//  O tempo GPU foi maior que o sequencial devido
//  ao overhead de transferência de memória e 
//  granularidade fina do algoritmo, 
//  onde cada thread realiza apenas uma comparação por iteração.
// =============================================================

#include <stdio.h>
#include <stdlib.h>
#include <omp.h>

int main() 
{
    omp_set_num_threads(2); // Define o número de threads a serem usadas
    int i, j, n = 30000; 

    // Allocate input, output and position arrays
    int *in  = (int*) calloc(n, sizeof(int));
    int *pos = (int*) calloc(n, sizeof(int));   
    int *out = (int*) calloc(n, sizeof(int));   

    // Initialize input array in the reverse order
    for(i=0; i < n; i++)
        in[i] = n-i;  

    // Print input array
    //    for(i=0; i < n; i++) 
    //      printf("%d ",in[i]);                             

    // Silly sort paralelizado para GPU com OpenMP target
    //
    // #pragma omp target
    //   Transfere a execução (e os dados) para o device (GPU).
    //   O compilador gera um kernel offloaded.
    //
    // map(to: in[0:n])
    //   Copia o array "in" do host para a GPU antes do kernel.
    //   Direção "to": host → device (somente ida).
    //
    // map(tofrom: pos[0:n])
    //   Copia "pos" para a GPU antes e de volta ao host depois.
    //   Direção "tofrom": host → device → host.
    //
    // #pragma omp teams distribute parallel for
    //   teams              → cria múltiplos grupos de threads (blocos CUDA).
    //   distribute         → distribui as iterações do loop externo entre os times.
    //   parallel for       → paraleliza dentro de cada time (threads do bloco).
    //   Combinado, explora dois níveis de paralelismo da GPU.
    //
    // private(j)
    //   Cada thread tem sua própria cópia de j (loop interno).
    //   Mantido igual à versão multicore.
    //
    // schedule(static)
    //   Divide as iterações em blocos iguais entre as threads,
    //   sem overhead de balanceamento dinâmico.
    //   Adequado pois todas as iterações têm o mesmo custo.
    //   Mantido igual à versão multicore.

    #pragma omp target map(to: in[0:n]) map(tofrom: pos[0:n])
    #pragma omp teams distribute parallel for private(j) schedule(static)
    for(i=0; i < n; i++)
        for(j=0; j < n; j++)
            if(in[i] > in[j]) 
                pos[i]++;   

    // Move elements to final position
    for(i=0; i < n; i++) 
        out[pos[i]] = in[i];

    // Print output array
    //   for(i=0; i < n; i++) 
    //      printf("%d ",out[i]);

    // Check if answer is correct
    for(i=0; i < n; i++)
        if(i+1 != out[i]) 
        {
            printf("test failed\n");
            exit(0);
        }

    printf("test passed\n"); 

    free(in);
    free(pos);
    free(out);
    return 0;
}