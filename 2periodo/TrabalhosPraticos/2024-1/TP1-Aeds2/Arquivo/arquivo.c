/*
    806454 - Yago Almeida Melo
    TP1/2024-1  Arquivo em C
*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>

void lerLinhaArquivo(FILE *file, int i);

/*
*   function: lerArquivo
*   @params: File
*   action: Ler todas as linha do final até o inicio e printar cada numero real
*/
void lerLinhaArquivo(FILE *file, int index){
    char buf[1000];
    if (file == NULL) {
        perror("Erro ao abrir o arquivo");
        exit(EXIT_FAILURE);
    }
    int linha=0;
    fseek(file, 0, SEEK_SET);  //Reposiciona o ponteiro para o inicio do arquivo
    while(fgets(buf, sizeof(buf), file) != NULL) { 
	    linha++;  //muda de linha
	    if (index == linha) { //se linha atual eh igual a linha do parametro, printa o numero
		    printf("%s", buf);		
        }
	}
} 

int main(){
    FILE *file = fopen("valores.txt", "w"); //abertura para escrita
    if (file == NULL) {
        perror("Erro ao abrir o arquivo");
        exit(EXIT_FAILURE);
    }
    int n = 0;
    float x = 0.0;
    scanf(" %i", &n);
    for(int i=0;i<n;i++){ //leitura dos numeros reais e inserção no arquivo
        scanf(" %f", &x);
        fprintf(file, "%g\n", x);
    }
    fclose(file);
    file = fopen("valores.txt", "r"); //abertura para leitura
    for(int j=n;j>0;j--){
        lerLinhaArquivo(file, j);
    }
    fclose(file);
    return 0;
}