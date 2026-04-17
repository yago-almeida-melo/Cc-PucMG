// Tempo Sequencial:        real    1,48s
//                          user    1,48s
//                          sys     0,00s
//                          cpu     99% 
//-------------------------------------------------
// Tempo Paralelo           real    1,18s
//                          user    1,73s
//                          sys     0,01s
//                          cpu     147%

//Speedup -> 1,48s/1,18s = 1,25s


#include<stdio.h>
#include<stdlib.h>
#include<omp.h>  

void swap(int* a, int* b)
{
  int t = *a;
  *a = *b;
  *b = t;
}

int partition (int arr[], int low, int high)
{
  int pivot = arr[high];
  int i = (low - 1);

  for (int j = low; j <= high- 1; j++)
  {
    if (arr[j] <= pivot)
    {
      i++;
      swap(&arr[i], &arr[j]);
    }
  }
  swap(&arr[i + 1], &arr[high]);
  return (i + 1);
}

void quickSort(int arr[], int low, int high)
{
  if (low < high)
  {
    int pi = partition(arr, low, high);

      #pragma omp task shared(arr) if(high - low > 10000)
      quickSort(arr, low, pi - 1);
      
      #pragma omp task shared(arr) if(high - low > 10000)
      quickSort(arr, pi + 1, high);
      
      #pragma omp taskwait
  }
}

void printArray(int arr[], int size)
{
  int i;
  for (i=0; i < size; i++)
    printf("%d ", arr[i]);
  printf("\n");
}

int main()
{
  int i,n = 10000000;
  int *arr = (int*) malloc(n*sizeof(int));

  for(i=0; i < n; i++)
    arr[i] = rand()%n;

  #pragma omp parallel num_threads(2)
  {
    #pragma omp single
    quickSort(arr, 0, n-1);
  }
  //printf("Sorted array: \n");
  //printArray(arr, n);
  return 0;
}