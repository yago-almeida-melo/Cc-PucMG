#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>

bool buscar(char nome[30], char pokemons[150][30]);
void inserir(char nome[30], char pokemons[150][30]);
void escrever(char pokemons[150][30]);

int qtd=0;

int main(){
    char pokemons[150][30];
    char nome[30];
    scanf("%s", nome); 
    while(strcmp(nome,"FIM") != 0){
        inserir(nome, pokemons);
        scanf("%s", nome);              
    }
    escrever(pokemons);  
    return 0;
}

bool buscar(char nome[30], char pokemons[150][30]){
    for(int i=0; i<qtd; i++){
        if(strcmp(nome, pokemons[i]) == 0){
            return true;
        }
    }
    return false;
}

void inserir(char nome[30], char pokemons[150][30]){
    if(qtd < 150 && !buscar(nome, pokemons)){
        strcpy(pokemons[qtd], nome);
        qtd++;
    }
}

void escrever(char pokemons[150][30]){
    for(int i=0; i<qtd; i++){
        printf("%s\n", pokemons[i]);
    }
    printf("Voce tem %i Pokemons!", qtd);
}