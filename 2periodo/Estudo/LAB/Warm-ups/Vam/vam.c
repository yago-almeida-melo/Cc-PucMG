#include <stdio.h>
#include <stdbool.h>
#include <string.h>

typedef struct {
    char nome[100]; char regiao; int distancia; 
}Aluno;

bool comparator(Aluno *a, Aluno *b){
    bool resp = false;
    if(a->distancia < b->distancia){
        resp = true;
    } else if(a->distancia == b->distancia && a->regiao < b->regiao){
        resp = true;
    } else if(a->distancia == b->distancia && (a->regiao == b->regiao)  && strcmp(a->nome, b->nome) < 0){
        resp = true;
    } else{
        resp = false;
    }
    return resp;
}

void ordenacao(Aluno list[], int tam){
    for(int i=1;i<tam;i++){
        Aluno tmp = list[i];
        int j = i-1;
        while(j>=0 && !comparator(&list[j], &tmp)){
            list[j+1] = list[j];
            j--;
        }
        list[j+1] = tmp;
    }
}

void mostrar(Aluno list[], int tam){
    for(int i=0;i<tam;i++){
        printf("%s\n", list[i].nome);
    }
}


int main(){ 
    int qtd = 0;
    scanf("%d", &qtd);
    Aluno alunos[qtd];
    for(int i=0;i<qtd;i++){
        scanf("%s %c %i", alunos[i].nome, &alunos[i].regiao, &alunos[i].distancia);
    }
    ordenacao(alunos, qtd);
    mostrar(alunos, qtd);
    return 0;
}