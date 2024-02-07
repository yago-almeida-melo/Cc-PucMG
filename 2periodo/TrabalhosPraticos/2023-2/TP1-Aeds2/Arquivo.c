#include <stdio.h>
#include <stdlib.h>

int main() {
	int n;
	scanf("%i", &n);    //le a qtde de numeros para input
	FILE *arq = fopen("arquivo.txt", "w");  //abre arquivo para escrever
	float numero;
	for (int i=0; i<n; i++) {  //laco para ler numeros
		scanf( "%f", &numero); 
		fprintf(arq, "%g\n", numero); //print no arquivo
	}

	fclose(arq); //fecha o arquivo

 	for (int i = n; i> 0; i--) { //laco que le os numeros de tras para frente
		leLinhaArq( "arquivo.txt", i); //chama a funcao para ler os numeros
	}
	return 0;
}

void leLinhaArq(char *file, int linha) {  //funcao que le a linha do arquivo
	char buf[1000];
	FILE *arq = fopen(file, "r"); //abre o arquivo para leitura
	int linhaA = 0;    //define a posicao, ou qual linha
	while (fgets(buf, sizeof(buf), arq) != NULL) { 
		linhaA++;  //muda de linha
		if (linhaA == linha) { //se linha atual eh igual a linha do parametro, printa o numero
			printf("%s", buf);
			break;
		}
	}
	fclose(arq);  //fecha o arquivo
}
