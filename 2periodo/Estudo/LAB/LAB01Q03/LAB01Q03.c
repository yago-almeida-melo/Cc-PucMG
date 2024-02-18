/*
    806454 - Yago Almeida Melo 
    LAB01Q03 - Verificar quantas letras Maiusculas em C
*/

#include <stdio.h>
#include <string.h>

void maiusculas(char input[]){
    int x=0;
    for(int i=0;i<strlen(input);i++){
        if(input[i]>='A' && input[i]<='Z') x++;
    }
    printf("%i\n", x);
}

int main(){
    char ptr[100];
    fgets(ptr, sizeof(ptr), stdin);
    while(strcmp(ptr,"FIM\n")!=0){
        maiusculas(ptr);
        fgets(ptr, sizeof(ptr), stdin);
    }
    return 0;
}