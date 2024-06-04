#include <stdio.h>
#include <string.h>
#include <stdbool.h>
 
void verify(int a, int b, int c, int d, int e){
    int resp = 0;
    int arr[5]; int count=0;
    arr[0]=a; arr[1]=b; arr[2]=c; arr[3]=d; arr[4]=e;
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
        int a,b,c,d,e;
        for(int i=0;i<x;i++){
            scanf("%d %d %d %d %d",&a,&b,&c,&d,&e);
            verify(a,b,c,d,e);
        }
        scanf("%d", &x);
    }
    return 0;
}