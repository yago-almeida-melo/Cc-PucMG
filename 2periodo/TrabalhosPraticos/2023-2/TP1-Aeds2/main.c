#include <stdio.h>
#include <stdbool.h>


bool verificaPalindromo(char string[]) {
    int tamanho = strlen(string);
    bool ehPalindromo = true; 
    
    for (int i = 0, j = tamanho - 1; i < j; i++, j--) { //for para percorrer a string
        if (string[i] != string[j]) { //compara os caracteres
            ehPalindromo = false;  
            i = j; // Para sair do loop
        }
    }
    return ehPalindromo; //retornar o valor true ou false
}

int main() { // controle de fluxo do programa
    char string[100]; //declaração da string
    bool fim = false; 

    do {                                                            
        fgets(string, sizeof(string), stdin); //leitura da string
        string[strcspn(string, "\n")] = '\0';  // Remove o caractere '\n' da entrada

        if (strcmp(string, "FIM") == 0) { //comparação da string com a palavra FIM
            fim = true; //se for igual, o programa acaba
        } else if (verificaPalindromo(string)) {
            printf("SIM\n"); //se for palindromo, imprime SIM
        } else {
            printf("NAO\n"); //se não for palindromo, imprime NAO
        }
    } while (!fim); //enquanto não for o fim do programa

    return 0;
}
