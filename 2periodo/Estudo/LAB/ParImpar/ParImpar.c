#include <stdio.h>

int main(){
    int x=0;
    scanf("%i", &x);
    while(x!=0){
        if(x%2==0){
            printf("P\n");
        }else{
            printf("I\n");
        }
        scanf("%i", &x);
    }
}