/*
*   806454 - Yago Almeida Melo
*   TP1 - Palindromo em C
*/

#include <stdio.h>
#include <stdbool.h>
#include <stdlib.h>
#include <string.h>

bool isFim(char x[])
{
    return x[0] == 'F' && x[1] == 'I' && x[2] == 'M';
}

bool analisaPalindromo(char string[])
{
    bool ehPalindromo = true;
    int len = strlen(string);
    for (int i = 0, j = len - 2; i < len / 2; i++, j--)
    {
        if (string[i] != string[j])
        {
            ehPalindromo = false;
            i = len;
        }
    }
    return ehPalindromo;
}

int main(void)
{
    char str[100];
    fgets(str, sizeof(str), stdin);
    while (!isFim(str))
    {
        if (analisaPalindromo(str))
            printf("SIM\n");
        else
            printf("NAO\n");
        fgets(str, sizeof(str), stdin);
    }
    return 0;
}