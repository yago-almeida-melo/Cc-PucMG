#include <stdio.h>
#include <omp.h>

#define N 42

long fib(long n) {
  long i, j;

  if (n < 2)
    return n;
  else if (n < 30) {
    return fib(n-1) + fib (n-2);
  }
  else {
    #pragma omp parallel sections
    { 
      #pragma omp section 
      i = fib(n-1);
      #pragma omp section 
      j = fib(n-2);
    }
    return i + j;
  }
}

int main() {
  omp_set_nested(1);
  printf("\nFibonacci(%lu) = %lu\n",(long)N,fib((long)N));
}