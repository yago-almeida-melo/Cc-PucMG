#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>

bool isLetra(char c){
    return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
}

void cript(char input[]){
    char* resp = "";
    for(int i=0;i<strlen(input);i++){
        if(isLetra(input[i])){
            resp += (char)(input[i]+3);
        }else{
            resp += input[i];
        }
    }
}

int main(){
    int casos = 0;
    scanf(" %i", &casos);
    char* input;
    for(int i=0;i<casos;i++){
        scanf(" %[^\n]", input);
        cript(input);
        printf("%s\n", input);
    }
    return 0;
}