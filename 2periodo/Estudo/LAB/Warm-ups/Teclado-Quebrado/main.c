#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <stdbool.h>

#define MAX_LETTERS 100001

/*
*   806454 - Yago Almeida Melo
*   Warm-up 02 / Teclado Quebrado em C
*/

char* substring(char x[], int i){
    int j = 0;
    char resp[MAX_LETTERS];
    if (resp == NULL) {
        return NULL;
    }
    while(x[i]!='\0' && (x[i]!=']' || x[i]!='[')){ 
        resp[j++] = x[i++];
    }
    resp[j] = '\0';
    printf("\nSubstring: %s\n", resp); 
    return resp;
}


char* teclado(char in[]){
    char resp[MAX_LETTERS];
    char sub[] = "";
    if (resp == NULL) {
        return NULL;
    }
    for(int i = 0; i < strlen(in); i++){
        if(in[i] != '['){
            strncat(resp, &in[i], 1); 
        } else {
            strcat(sub ,substring(in, i + 1));
            if (sub != NULL) {
                printf("Inserino: %s", sub);
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
    char input[MAX_LETTERS];
    while(strcmp(scanf("%s", input), "FIM") != 0){
        printf("%s\n", teclado(input));
    }
    return 0;
}