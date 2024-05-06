 #ifndef ARVORE_H
 #define ARVORE_H
 
#include <stdio.h>
#include <stdlib.h>
 
typedef struct No{
    int elemento;
    struct No *dir;
    struct No *esq;
}Celula;

No* new_No(int elemento){
    No *temp = (No*)malloc(sizeof(No));
    temp->elemento = elemento;
    temp->esq = NULL;
    temp->dir = NULL;
    return temp;
}

#endif