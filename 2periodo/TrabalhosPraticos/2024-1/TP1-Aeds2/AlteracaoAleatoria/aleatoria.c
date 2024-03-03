/*
    806454 - Yago Alemida Melo
    TP-1:2024/1  - Alteração aleatoria em C
*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>

bool isFim(char ptr[]);
char * alteracaoAleatoria(char ptr[]);
char * alteracao(char ptr[], int i);

/*
*   function: isFim
*   @params: char[]
*   action: Retorna verdadeiro se string é igual a "FIM" e falso se não for.
*/
bool isFim(char ptr[]){
    return strlen(ptr) == 4 && ptr[0] == 'F' && ptr[1] == 'I' && ptr[2] == 'M' && ptr[3] == '\n';
}

/*
*   function: alteracaoAleatoria
*   @params: char[]
*   action: chamada para funcao recursiva alteracao e retorna a string modificada
*/
char * alteracaoAleatoria(char ptr[]){
    return alteracao(ptr, 0);
     
}

/*

*   function: alteracao
*   @params: char[] & int
*   action: funcao recursiva que faz a alteracao e retorna a string modificada
*/
char * alteracao(char ptr[], int i){
    char atual = ('a' + rand() % 26);
    char nova = ('a' + rand() % 26);
    if(i < strlen(ptr)){
        if(ptr[i] == atual){
            ptr[i] = nova;
        }
        alteracao(ptr, i+1);
    }
    return ptr;
}    

int main(){
    char input[1000];
    srand(4);
    fgets(input, sizeof(input), stdin);
    while(!isFim(input)){
        char *resp = alteracaoAleatoria(input);
        printf("%s", resp);
        fgets(input, sizeof(input), stdin);
    }
    return 0;
}