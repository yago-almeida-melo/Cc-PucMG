#include <stdio.h>
#include <stdlib.h>

typedef struct{
    int hora, minuto, critico;
}Paciente;

void calculo(Paciente x[], int tam){
    int resp = 0;
    int horaAtual = 7;
    int minutoAtual = 30;
    for(int i = 1;i<tam;i++){
        int minPaciente = (x[i].hora * 60) + x[i].minuto;
        int minHoraAtual = (horaAtual * 60) + minutoAtual; 
        if((minHoraAtual-minPaciente) > x[i].critico){
            resp++;
        } 
        if(i%2==1){
            minutoAtual = 0;
            horaAtual++;
        } else{
            minutoAtual = 30;
        }
    }
    printf("%i\n", resp);
}

int main(){
    int casos;
    while (scanf(" %i", &casos)==1 && casos > 0 && casos < 25) {
        Paciente* x = (Paciente*)malloc(casos * sizeof(Paciente));
        for (int i = 0; i < casos; i++) {
            scanf("%i %i %i", &x[i].hora, &x[i].minuto, &x[i].critico);
        }
        calculo(x, casos);
        free(x);
    }
    return 0;
}