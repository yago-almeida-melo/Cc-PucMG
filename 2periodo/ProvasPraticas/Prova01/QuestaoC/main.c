#include <stdio.h>
#include <string.h>

typedef struct Duendes {
    char nome[21];
    int idade;
} Duendes;

void ordenar(Duendes d[], int n) {
    for (int i = 0; i < n - 1; i++) {
        int menor = i;
        for (int j = i + 1; j < n; j++) {
            if (d[j].idade > d[menor].idade ) {
                menor = j;
            } else if ((d[i].idade == d[j].idade) && (strcmp(d[i].nome, d[j].nome) < 1)) {
                menor = j;
            }
        }
        if(menor != i){
            Duendes temp = d[i];
            d[i] = d[menor];
            d[menor] = temp;
        }
    }
}

int main(void) {
    int N;
    Duendes d[30];
    scanf("%d", &N);
    for (int i = 0; i < N; i++) {
        scanf("%s %d", d[i].nome, &d[i].idade);
    }
    ordenar(d, N);
    printf("Time 1\n");
    for(int i=0;i<5;i+=2){
        printf("%s %i\n", d[i].nome, d[i].idade);
    }
    printf("\nTime 2\n");
    for(int i=1;i<6;i+=2){
        printf("%s %i\n", d[i].nome, d[i].idade);
    }

    return 0;
}