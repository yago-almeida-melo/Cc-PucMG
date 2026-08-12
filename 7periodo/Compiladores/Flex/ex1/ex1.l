%{
#include <stdio.h>
%}
%option noyywrap

%%
"if"          { printf("morango "); }
[a-f]+        { printf("banana "); }
[a-z]+        { printf("abacaxi "); }
"=="          { printf("uva "); }
"="           { printf("limao "); }
[ \t\n]+      { /* ignora espaços */ }
.             { printf("erro "); }
%%

int main()
{
    yylex();
    return 0;
}
