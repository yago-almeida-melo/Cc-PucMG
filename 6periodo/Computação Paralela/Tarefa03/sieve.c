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
           #pragma omp parallel for schedule(static)  //Paraleliza o loop mais demorado
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
   printf("%d\n",sieveOfEratosthenes(n));
   return 0;
} 
// Tempo Sequencial:        real    0m1.080s
//                          user    0m0.060s
//                          sys     0m0.030s
//-------------------------------------------------
// Tempo Paralelo:          real    0m0.771s
//                          user    0m0.015s
//                          sys     0m0.076s