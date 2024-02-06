#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <stdbool.h>

char *alteraString(char *linha) {  //funcao que gera a letra aleatoria e cria a nova cadeia de caracter com a letra nova
    int i=0;
    srand(4);
    char letra = ('a' + rand() % 26);       //class random
    char novaLetra = ('a' + rand() % 26);   //class random
    for(i = 0; i<strlen(linha);i++){    //percorre ate o final de linha
        if(linha[i] == letra)  linha[i] = novaLetra;     //se os caracetres coincidirem, adciona o novo caracter
        i++;
    }
    return linha;
}

int main() {
   
    bool fim = false;    //flag
    char frase[1000];     //input de tamanho 1000
    fgets(frase, sizeof(frase), stdin);     //leitura     
    frase[strcspn(frase, "\n")] = 0;         
    if(strcmp(frase, "FIM") == 0) fim = true;  //compara o input com "FIM"
    do{
        char *novaFrase = alteraString(frase);
        printf("%s\n", novaFrase);
        fgets(frase, sizeof(frase), stdin);
        frase[strcspn(frase, "\n")] = 0;
        if(strcmp(frase,"FIM") == 0) fim = true;   //compara o input com "FIM"
    }while(!fim);  //fim do-while
    return 0;
} //fim da main

