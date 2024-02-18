/*
    806454 - Yago Almeida Melo 
    LAB01Q04 - Verificar quantas letras Maiusculas em C RECURSIVO
*/

#include <stdio.h>
#include <string.h>

int maiusculas(char input[], int index, int qtd){
    if(index < strlen(input)){
        if(input[index]>='A' && input[index]<='Z'){
            qtd = 1 + maiusculas(input, index + 1, qtd);
        } else{
            qtd = maiusculas(input, index + 1, qtd);
        }
    }
    return qtd;
}

int main(){
    char ptr[100];
    fgets(ptr, sizeof(ptr), stdin);
    while(strcmp(ptr,"FIM\n")!= 0){
        printf("%i\n", maiusculas(ptr, 0, 0));
        fgets(ptr, sizeof(ptr), stdin);
    }
    return 0;
}