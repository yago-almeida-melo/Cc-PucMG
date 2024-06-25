#include <stdio.h>
#include <string.h>

int hash(char x[], int k){
    int resp=0;
    for(int i=0;i<strlen(x);i++){
        resp += (int)(x[i]-65) + k + i;
    }
    return resp;
}

int main(){
    int n = 0;
    int L = 0;
    char in[50];
    scanf("%d", &n);
    for(int i=0;i<n;i++){
        scanf("%d", &L);
        int resp=0;
        for(int a=0;a<L;a++){
            scanf("%s", in);
            resp += hash(in, a);
        }
        printf("%d\n", resp);
    }
    return 0;
}