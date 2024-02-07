#include <stdio.h>
#include <stdbool.h>
#include <string.h>

bool verificaPalindromoRecu(char string[], int inicio, int final){
	bool ehPalindromo = true;
	if(inicio>=final){
		return ehPalindromo;
	} 
	if(string[inicio] != string[final]) {
		 return false;
	} 
		verificaPalindromoRecu(string,inicio+1,final-1);	
}

int main(){
	bool fim = false; 			//flag
	char string[100]; 			//string
	do{
		fgets(string, sizeof(string), stdin); //leitura da string
        	string[strcspn(string, "\n")] = '\0';  // Remove o caractere '\n' da entrada

        	if (strcmp(string, "FIM") == 0) { //comparação da string com a palavra FIM
            		fim = true; //se for igual, o programa acaba
       		} else {
			int tamanho = strlen(string)-1;
			if (verificaPalindromoRecu(string,0,tamanho)) {
            			printf("SIM\n"); //se for palindromo, imprime SIM
        		} else {
        	    		printf("NAO\n"); //se não for palindromo, imprime NAO
	        	}
		}	
	}while(!fim);
	return 0;
}	
