 /*
 *  806454 - Yago Almeida Melo
 *  TP1-Q02: Registro em C 
 */

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <stdbool.h>

typedef struct{
    char[][] list;
}Lista;

typedef struct{
    char[] id, name, house, ancestry, species, patronus, actorName, eyeColour, gender, hairColour;
    int yearOfBirth;
    bool hogwartsStaff, hogwartsStudent, alive, wizard;
    Lista alternate_names, alternate_actors;
}Personagem;

void initPersonagem(Personagem *p){
    p->id = "";
    p->name = "";
    p->alternate_names = NULL;
    p->house = NULL;
    p->ancestry = NULL;
    p->species = NULL;
    p->patronus = NULL;
    p->hogwartsStaff = false;
    p->hogwartsStudent = false;
    p->actorName = NULL;
    p->alive = false;
    p->alternate_actors = NULL;
    p->dateOfBirth = NULL;
    p->yearOfBirth = -1;
    p->eyeColour = NULL;
    p->gender = NULL;
    p->hairColour = NULL;
    p->wizard = false;
}

// Salva Personagem por id, a partir do arquivo csv
void arrayOfPersonagens(Personagem j[], int key) {
    char path[100];
    if(key == 0){
        strcpy(path, "/tmp/characters.csv");
    } else{
        strcpy(path, "/home/yago/Documents/Cc-PucMG/2periodo/TrabalhosPraticos/2024-1/TP2-Aeds2/characters.csv");
    }
    FILE *file = fopen(path, "r");
    if (file == NULL)
    {
        perror("Erro ao abrir o arquivo");
    }
    char linha[MAX_LINE_LENGTH] = {0};
    char tmp[MAX_LINE_LENGTH] = {0};
    char dif[2] = ",";
    bool encontrou = false;
    bool header = true;
    char nova[200];

    while (fgets(linha, sizeof(linha), file)) { // le cada linha do arquivo
        if (header) {
            header = false;
        }
        else {
            strcpy(tmp, linha);
            char *idAtual = strtok(tmp, dif);
            if (atoi(idAtual) == id) {
                tratamentoVirgula(linha, nova);
                setPersonagem(j, nova);
                encontrou = true;
            }
        }
    }
    fclose(file);
}
// Adiciona Personagens
void addPersonagens(Personagem p[])
{
    char id[37] = {0};
    fgets(id, sizeof(id), stdin);
    bool isFim = false;
    if (strcmp(id, "FIM\n") == 0)
        isFim = true;
    while (!isFim)
    {
        Personagem personagem;
        initPersonagem(&personagem);
        salvaPorId(&personagem, idNum);
        insert(p,personagem);
        fgets(id, sizeof(id), stdin);
        if (strcmp(id, "FIM\n") == 0)
            isFim = true;
    }
    fflush(stdin);
}

void insert(Personagem p[], Personagem x){

}

int main(){

    return 0;
}