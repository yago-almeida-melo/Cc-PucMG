#include <stdio.h>
#include <stdlib.h>
#include <omp.h> 

/*
Tempo Sequencial:         real    27,90s
*                         user    27,89s
*                         sys     0,01s
*                         cpu     99%
------------------------------------------                          
Tempo Paralelo:           real    6,77s
*                         user    27,04s
*                         sys     0,02s
*                         cpu     399% 

SPEEDUP: 27,90s / 6,77s = 4,12x
*/

void mm(double* a, double* b, double* c, int width) 
{
 #pragma omp parallel for  // Paraleliza o loop externo    
 for (int i = 0; i < width; i++) {
    for (int j = 0; j < width; j++) {
      double sum = 0;
      for (int k = 0; k < width; k++) {
	    double x = a[i * width + k];
	    double y = b[k * width + j];
	    sum += x * y;
      }
      c[i * width + j] = sum;
    }
  }
}

int main()
{
  omp_set_num_threads(4); //Deifinido 4 threads a serem usadas
  int width = 2000;
  double *a = (double*) malloc (width * width * sizeof(double));
  double *b = (double*) malloc (width * width * sizeof(double));
  double *c = (double*) malloc (width * width * sizeof(double));

  #pragma omp parallel for // Paraleliza a inicialização das matrizes
  for(int i = 0; i < width; i++) {
    for(int j = 0; j < width; j++) {
      a[i*width+j] = i;
      b[i*width+j] = j;
      c[i*width+j] = 0;
    }
  }

  mm(a,b,c,width);

  /*for(int i = 0; i < width; i++) {
    for(int j = 0; j < width; j++) {
      printf(" c[%d][%d] = %.f",i,j,c[i*width+j]);
    }
    printf("\n");
  }*/ 
}