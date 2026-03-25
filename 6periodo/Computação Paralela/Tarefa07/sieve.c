// Tempo Sequencial:          real    1,81s
//                            user    1,80s
//                            sys     0,01s
//                            cpu     99% 
//-------------------------------------------------
// Tempo Paralelo(8 threads)  real    0,52s
// Sem escalonamento          user    4,00s
//                            sys     0,02s
//                            cpu     771%
//-------------------------------------------------
// Tempo Paralelo(8 threads)  real    0,55s
// schedule(guided)           user    4,18s
//                            sys     0,01s
//                            cpu     766%
//-------------------------------------------------
// Tempo Paralelo(8 threads)  real    4,56s
// schedule(dynamic)          user    36,00s
//                            sys     0,02s
//                            cpu     790%
//-------------------------------------------------
// Tempo Paralelo(8 threads)  real    0,54s
// schedule(dynamic,700)      user    4,12s
//                            sys     0,00s
//                            cpu     770%

#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>
#include <math.h>
#include <omp.h>
 
int sieveOfEratosthenes(int n) {
   // Create a boolean array "prime[0..n]" and initialize
   // all entries it as true. A value in prime[i] will
   // finally be false if i is Not a prime, else true.
   int primes = 0; 
   bool *prime = (bool*) malloc((n+1)*sizeof(bool));
   int sqrt_n = sqrt(n);
 
   memset(prime, true,(n+1)*sizeof(bool)); 
    
   
   for (int p=2; p <= sqrt_n; p++) {
        // If prime[p] is not changed, then it is a prime
        if (prime[p] == true) {
        // Update all multiples of p
            #pragma omp parallel for  // Paraleliza o loop mais demorado
            for (int i=p*2; i<=n; i += p)
               prime[i] = false;
        }
   }
   
    // count prime numbers
    #pragma omp parallel for reduction(+:primes)  // Paraleliza o loop de contagem de primos
    for (int p=2; p<=n; p++)                       // reduction para garantir o resultado correto
       if (prime[p])
         primes++;
 
    return(primes);
}
 
int main()
{
   int n = 100000000;
   //omp_set_num_threads(2);  // limita a 2 threads em todo o programa
   //se nao limitar, o OpenMP usará o número de threads igual ao número de núcleos disponíveis             
   printf("%d\n",sieveOfEratosthenes(n));
   return 0;
} 
