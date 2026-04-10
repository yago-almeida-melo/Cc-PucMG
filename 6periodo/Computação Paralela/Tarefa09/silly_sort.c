// Tempo Sequencial:        real    1,49s
//                          user    1,49s
//                          sys     0,00s
//                          cpu     99% 
//-------------------------------------------------
// Tempo Paralelo sem       real    0,81s
// escalonamento            user    1,61s
//                          sys     0,00s
//                          cpu     199%
//-------------------------------------------------
// Tempo Paralel com        real    0,79s
// escalonamento            user    1,58s
//                          sys     0,00s
//                          cpu     199%
#include <stdio.h>
#include <stdlib.h>
#include <omp.h>

int main() 
{
   omp_set_num_threads(2); // Define o número de threads a serem usadas
   int i, j, n = 30000; 

   // Allocate input, output and position arrays
   int *in = (int*) calloc(n, sizeof(int));
   int *pos = (int*) calloc(n, sizeof(int));   
   int *out = (int*) calloc(n, sizeof(int));   

   // Initialize input array in the reverse order
   for(i=0; i < n; i++)
      in[i] = n-i;  

   //Print input array
   //    for(i=0; i < n; i++) 
   //      printf("%d ",in[i]);                             
   // Silly sort (you have to make this code parallel)
   #pragma omp parallel for private(j) schedule(static) // Paraleliza o loop externo
   for(i=0; i < n; i++)                                 // Com ou sem escalonamento, o tempo é praticamente o mesmo
      for(j=0; j < n; j++)                              // pois as threads estão balanceadas, cada uma processando a mesma quantidade de elementos
	      if(in[i] > in[j]) 
            pos[i]++;	

   // Move elements to final position
   for(i=0; i < n; i++) 
      out[pos[i]] = in[i];
   
   //print output array
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
}  
