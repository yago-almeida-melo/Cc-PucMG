/*
*   806454 - Yago Almeida Melo
*   TP1 - Palindromo em C
*/

#include <stdio.h>
#include <stdbool.h>
#include <stdlib.h>
#include <string.h>

/*
* function: isFim
* @params: char []
* action: Recebe um array de char e retorna verdadeiro for igual a "FIM", senão retorna falso.
*/
bool isFim(char x[]) {
    return x[0] == 'F' && x[1] == 'I' && x[2] == 'M';
}
/*
     * function: analisaPalindromo
     * @params: char []
     * action: Recebe um array de char e retorna verdadeiro se for palindromo, senão retorna falso.
     */
bool analisaPalindromo(char string[]) {
    bool ehPalindromo = true;
    int len = strlen(string);
    for (int i = 0, j = len - 1; i < j; i++, j--) {
        if (string[i] != string[j]) {
            ehPalindromo = false;
            i = len;
        }
    }
    return ehPalindromo;
}

int main(void) {
    char str[100];
    fgets(str, sizeof(str), stdin);
    str[strcspn(str, "\n")] = '\0'; //Retira '\n' 
    while (!isFim(str)) {
        if (analisaPalindromo(str))
            printf("SIM\n");
        else
            printf("NAO\n");
        fgets(str, sizeof(str), stdin);
        str[strcspn(str, "\n")] = '\0';
    }
    return 0;
}