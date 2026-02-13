#include <stdio.h>
#include <omp.h>

int main()
{
    #pragma omp parallel num_threads(3)   // seta o número de threads em 2
    {    
        int tid = omp_get_thread_num(); // lê o identificador da thread
        
        #pragma omp for ordered schedule(static) // diretiva para paralelizar o loop com ordenação
        for (int i = 1; i <= 3; i++)
        {
            #pragma omp ordered // diretiva para garantir a ordem de execução       
            {
                printf("[PRINT1] T%d = %d \n", tid, i);
                printf("[PRINT2] T%d = %d \n", tid, i);
            }
        }
    }
}