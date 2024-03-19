#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <stdbool.h>

#define MAX_LETTERS 100000

/*
*   806454 - Yago Almeida Melo
*   Warm-up 02 / Teclado Quebrado em C
*/

char* substring(char x[], int i){
    int j = 0;
    char* resp = (char*)malloc(MAX_LETTERS * sizeof(char)); 
    if (resp == NULL) {
        return NULL;
    }
    while(x[i]!='\0' && x[i]!=']'){ 
        resp[j++] = x[i++];
    }
    resp[j] = '\0'; 
}


char* teclado(char in[]){
    char* resp = (char*)malloc(MAX_LETTERS * sizeof(char)); 
    char* sub = (char*)malloc(MAX_LETTERS * sizeof(char));
    if (resp == NULL) {
        return NULL;
    }
    resp[0] = '\0';
    for(int i = 0; i < strlen(in); i++){
        if(in[i] != '['){
            strncat(resp, &in[i], 1); 
        } else {
            sub = substring(in, i + 1);
            if (sub != NULL) {
                strcat(sub, resp); 
            }
            while(in[i] != ']' && in[i] != '\0') {
                i++; 
            }
        }
    }
    return sub;
}



int main(){
    /*char input[MAX_LETTERS];
    while(scanf("%s", input) != EOF){
        printf("%s\n", teclado(input));
    }*/
    char in[MAX_LETTERS] = "opa[e]io[u]";
    printf("\n%s\n", teclado(in));
    return 0;
}