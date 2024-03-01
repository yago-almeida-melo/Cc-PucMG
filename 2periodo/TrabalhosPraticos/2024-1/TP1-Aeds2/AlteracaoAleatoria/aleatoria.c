#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>

bool isFim(char ptr[]);
char * alteracaoAleatoria(char ptr[]);
char * alteracao(char ptr[], int i);


bool isFim(char ptr[]){
    return strlen(ptr) == 3 && ptr[0] == 'F' && ptr[1] == 'I' && ptr[2] == 'M';
}

char * alteracaoAleatoria(char ptr[]){
    return alteracao(ptr, 0);
}

char * alteracao(char ptr[], int i){
    char *resp = malloc((strlen(ptr)+1)*sizeof(char));
    char atual = ('a' + rand() % 26);
    char nova = ('a' + rand() % 26);
    if(i < strlen(ptr)-1){
        if(ptr[i] == atual){
            resp[i] = nova;
        } else{
            resp[i] = ptr[i];
        }
        char *recursiveResult = alteracao(ptr, i + 1);
        strcpy(resp + i + 1, recursiveResult); // Concatenate the results
        free(recursiveResult);
    } else {
        resp[i] = '\0'; // Terminate the string
    }
    return resp;
}    

int main(){
    char input[1000];
    srand(4);
    scanf("%[^\n]s", input); 
    while(!isFim(input)){
        char *resp = alteracaoAleatoria(input);
        printf("%s\n", resp);
        scanf("%[^\n]s", input);
    }
    free(input);
    return 0;
}