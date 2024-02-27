#include <stdio.h>
#include <stdbool.h>
#include <string.h>

#define MAXLEN 1000 // Constante para definir tamanho maximo do input

bool isFim(char input[]);
bool isPalindromoRecu(char ptr[], int index);
bool isPalindromo(char input[]);

/*
* function: isFim
* @params: char []
* action: Recebe um array de char e retorna verdadeiro for igual a "FIM", senão retorna falso.
*/
bool isFim(char input[]){
	return strlen(input)==3 && input[0] == 'F' && input[1] == 'I' && input[2] == 'M';
}
/*
* function: isPalindromo
* @params: char []
* action: Recebe um array de char e retorna verdadeiro se for palindromo, senão retorna falso.
*/
bool isPalindromo(char input[]){
	return isPalindromoRecu(input, 0);
}
/*
* function: isPalindromoRecu
* @params: char [], int 
* action: Recebe um array de char e analisa se é palindromo, recursivamente, senão retorna falso.
*/
bool isPalindromoRecu(char ptr[], int index){
	bool palindromo = true;
	if(index < strlen(ptr) && palindromo){
		if(ptr[index] != ptr[strlen(ptr)-index-1]){
			palindromo = false;
		} else{
			palindromo = isPalindromoRecu(ptr, index+1);
		}
	}
	return palindromo;
}
//MAIN
int main(){
	char str[MAXLEN];
	fgets(str,sizeof(str),stdin);
	str[strcspn(str,"\n")] = '\0';  //Retira o \n e substitui por \0
	while(!isFim(str)){
		if(isPalindromo(str)) puts("SIM");
		else puts("NAO");
		fgets(str,sizeof(str),stdin);
		str[strcspn(str,"\n")] = '\0';
	}
	return 0;
}
