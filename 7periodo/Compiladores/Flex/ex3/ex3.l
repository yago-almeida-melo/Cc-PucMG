%{
#include <stdio.h>
%}
%option noyywrap
%%
"010"        { printf("ALPHA BETA "); }   
"01"         { printf("ALPHA "); }        
"10"         { printf("BETA "); }         
"11"         { printf("OMEGA "); }        
1+           { printf("GAMMA "); }        
0+           { printf("DELTA "); }        
[ \t\n]+     { /* ignora */ }
.            { printf("ERRO "); }
%%
 
int main(){
    yylex();
    return 0;
}
//DELTA OMEGA ALPHA BETA GAMMA ALPHA BETA DELTA GAMMA
//= 000110110111010000111

//DELTA OMEGA ALPHA BETA GAMMA ALPHA BETA DELTA BETA 
//ALPHA BETA DELTA GAMMA
// = 00011011011101000010010000111