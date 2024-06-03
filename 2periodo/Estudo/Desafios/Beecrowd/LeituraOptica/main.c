#include <stdio.h>
#include <string.h>
#include <stdbool.h>
#include <stdlib.h>
 
void verify(char a[], char b[], char c[], char d[], char e[]){
    int resp = 0;
    int aa = atoi(a); int bb = atoi(b); int cc = atoi(c); int dd = atoi(d); int ee = atoi(e);
    int arr[5]; int count=0;
    arr[0]=aa; arr[1]=bb; arr[2]=cc; arr[3]=dd; arr[4]=ee;
    for(int i=0;i<5;i++){
        if(arr[i]<=127){
            count++;
            resp = i;
        }
    }
    if(count!=1){ 
        printf("*\n");
    } else{
        if(resp==0){
            printf("A\n");
        } else if(resp==1){
            printf("B\n");
        } else if(resp==2){
            printf("C\n");
        } else if(resp==3){
            printf("D\n");
        } else{
            printf("E\n");
        }
    }
}

int main() {
    int x=0;
    scanf("%d", &x);
    while(x!=0){
        for(int i=0;i<x;i++){
            char in[50];
            scanf("%[^\n]", in);
            char a[4],b[4],c[4],d[4],e[4];
            int indexIn=0, index=0;
            while(in[indexIn]!=' '){a[index] = in[indexIn]; index++; indexIn++;} indexIn++;index=0;
            while(in[indexIn]!=' '){b[index] = in[indexIn]; index++; indexIn++;} indexIn++;index=0;
            while(in[indexIn]!=' '){c[index] = in[indexIn]; index++; indexIn++;} indexIn++;index=0;
            while(in[indexIn]!=' '){d[index] = in[indexIn]; index++; indexIn++;} indexIn++;index=0;
            while(in[indexIn]!=' '){e[index] = in[indexIn]; index++; indexIn++;}
            verify(a,b,c,d,e);
        }
        scanf("%d", &x);
    }
    return 0;
}