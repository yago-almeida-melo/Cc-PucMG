%{
#include <stdio.h>
%}
%option noyywrap

%%
01|10       { printf("ALPHA "); }
0(10)*1     { printf("BETA "); }
1(01)*0     { printf("GAMMA "); }
[01]        { printf("DELTA "); }
%%

int main(){
    yylex();
    return 0;
}
// DELTA ALPHA BETA DELTA BETA GAMMA DELTA 
//= 11001010010110100

// ALPHA DELTA GAMMA DELTA GAMMA BETA DELTA ALPHA 
//= 0111010110100101110