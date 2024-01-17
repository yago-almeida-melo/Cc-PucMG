#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct
{
    char nome[20];
    char cor[9];
    char tamanho;
} Aluno;

void camisas(Aluno alunos[], int qtd);
void ordenar(Aluno alunos[]);
void escrever(Aluno alunos[]);

void camisas(Aluno alunos[], int qtd) {
    char nome[50], corTam[11];
    for (int i = 0; i < qtd; i++) {
        fgets(nome, 50, stdin);
        strcpy(alunos[i].nome, nome);
        fgets(corTam, 11, stdin);
        char cor[9];
        if (strcmp(corTam[0], 'b') == 0){
            strncpy(cor, corTam + 0, 6);
            strcpy(alunos[i].cor, cor);
            strcpy(alunos[i].tamanho, corTam[7]);
        }
        else {
            strncpy(cor, corTam + 0, 8);
            strcpy(alunos[i].cor, cor);
            strcpy(alunos[i].tamanho, corTam[9]);
        }
    }
    ordenar(alunos);
}

void ordenar(Aluno alunos[]) {
    int n = sizeof(alunos);
    for (int i = 1; i < n; i++) {
        Aluno tmp = alunos[i];
        int j = i - 1;
        while ((j >= 0) && (strcmp(alunos[j].cor , tmp.cor) == 1)) {
            alunos[j + 1] = alunos[j];
            j--;
        }
        while ((j >= 0) && (strcmp(alunos[j].cor , tmp.cor) == 0) && (strcmp(alunos[j].tamanho , tmp.tamanho) == 1)) {
            alunos[j + 1] = alunos[j];
            j--;
        }
        while ((j >= 0) && (strcmp(alunos[j].cor , tmp.cor) == 0) && (strcmp(alunos[j].tamanho , tmp.tamanho) == 0) && (strcmp(alunos[j].nome , tmp.nome) == 1)) {
            alunos[j + 1] = alunos[j];
            j--;
        }
        alunos[j + 1] = tmp;
    }
    escrever(alunos);
}

void escrever(Aluno alunos[]) {
    for (int i = 0; i < sizeof(alunos); i++) {
        printf("%s %s %s", alunos[i].cor, alunos[i].tamanho, alunos[i].nome);
    }
}

int main() {
    int qtd = 0;
    do {
        scanf(" %i", &qtd);
        Aluno alunos[qtd];
        camisas(alunos, qtd);
        printf("\n");
    } while (qtd > 0);

    return 0;
}