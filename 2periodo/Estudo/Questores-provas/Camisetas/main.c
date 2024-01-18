#include <stdio.h>
#include <string.h>

typedef struct
{
    char nome[20];
    char cor[9];
    char tamanho;
} Aluno;

void camisas(Aluno alunos[], int qtd);
void ordenar(Aluno alunos[], int n);
void escrever(Aluno alunos[], int n);

void camisas(Aluno alunos[], int qtd) {
    char nome[50], corTam[11];
    for (int i = 0; i < qtd; i++) {
        printf("Aluno[%i]: ", i);
        scanf(" ");
        fgets(nome, sizeof(nome), stdin);
        nome[strcspn(nome, "\n")] = '\0'; 
        strcpy(alunos[i].nome, nome);
        printf("Tamanho e cor (e.g., branco): ");
        fgets(corTam, sizeof(corTam), stdin);
        corTam[strcspn(corTam, "\n")] = '\0'; 
        char *tok = strtok(corTam, " ");
        if (corTam[0] == 'b') {
            strcpy(alunos[i].cor, tok);
            tok = strtok(NULL, " ");
            alunos[i].tamanho = tok;
        }
        else {
            strcpy(alunos[i].cor, tok);
            tok = strtok(NULL, "\0");
            alunos[i].tamanho = tok;
        }
    }
    ordenar(alunos, qtd);
}

void ordenar(Aluno alunos[], int n) {
    for (int i = 1; i < n; i++) {
        Aluno tmp = alunos[i];
        int j = i - 1;
        while ((j >= 0) && (strcmp(alunos[j].cor, tmp.cor) > 0)) {
            alunos[j + 1] = alunos[j];
            j--;
        }
        while ((j >= 0) && (strcmp(alunos[j].cor, tmp.cor) == 0) && (alunos[j].tamanho > tmp.tamanho)) {
            alunos[j + 1] = alunos[j];
            j--;
        }
        while ((j >= 0) && (strcmp(alunos[j].cor, tmp.cor) == 0) && (alunos[j].tamanho == tmp.tamanho) && (strcmp(alunos[j].nome, tmp.nome) > 0)) {
            alunos[j + 1] = alunos[j];
            j--;
        }
        alunos[j + 1] = tmp;
    }
    escrever(alunos, n);
}

void escrever(Aluno alunos[], int n)
{
    for (int i = 0; i < n; i++)
    {
        printf("%s %c %s\n", alunos[i].cor, alunos[i].tamanho, alunos[i].nome);
    }
}

int main()
{
    int qtd = 0;
    do
    {
        printf("Quantidade: ");
        scanf(" %i", &qtd);
        if (qtd != 0)
        {
            Aluno alunos[qtd];
            camisas(alunos, qtd);
            printf("\n");
        }
    } while (qtd != 0);
    return 0;
}
