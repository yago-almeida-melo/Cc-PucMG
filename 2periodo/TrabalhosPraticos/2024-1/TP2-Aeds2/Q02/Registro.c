/*
*   806454 - Yago Almeida Melo
*   TP2-Q02: Registro em C
*/

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <stdbool.h>

#define MAX_TAM 405

typedef struct {
    int dia, mes, ano;
} LocalDate;

typedef struct {
    char id[50], name[50], alternate_names[200], house[50], ancestry[50], species[50], patronus[50], actorName[50], alternate_actors[200],
    eyeColour[50], gender[50], hairColour[50];
    int yearOfBirth;
    bool hogwartsStaff, hogwartsStudent, alive, wizard;
    LocalDate dateOfBirth;
} Personagem;

void initPersonagem(Personagem *p) {
    strcpy(p->id, "");
    strcpy(p->name, "");
    strcpy(p->alternate_names, "");
    strcpy(p->house, "");
    strcpy(p->ancestry, "");
    strcpy(p->species, "");
    strcpy(p->patronus, "");
    p->hogwartsStaff = false;
    p->hogwartsStudent = false;
    strcpy(p->actorName, "");
    p->alive = false;
    strcpy(p->alternate_actors, "");
    p->dateOfBirth.dia = -1;
    p->dateOfBirth.mes = -1;
    p->dateOfBirth.ano = -1;
    p->yearOfBirth = -1;
    strcpy(p->eyeColour, "");
    strcpy(p->gender, "");
    strcpy(p->hairColour, "");
    p->wizard = false;
}

LocalDate convertData(char data[]) {
    LocalDate localDate;
    sscanf(data, "%d-%d-%d", &localDate.dia, &localDate.mes, &localDate.ano);
    return localDate;
}


void setPersonagem(Personagem *p, char *line){
    char input[300], date[11], year[5]; strcpy(input,line);
    int x=0, y=0;

    while(input[x]!=';'){ p->id[y] = input[x]; x++; y++;}    p->id[y]='\0'; x++; y=0;
    while(input[x]!=';'){ p->name[y] = input[x]; x++; y++;}  p->name[y]='\0'; x++; y=0;
    while(input[x]!=';'){ if(input[x]!='[' && input[x]!=']' && input[x]!=39){ p->alternate_names[y] = input[x]; y++;} x++;}  p->alternate_names[y]='\0'; x++; y=0;   
    while(input[x]!=';'){ p->house[y] = input[x]; x++; y++;} p->house[y]='\0'; x++; y=0;
    while(input[x]!=';'){ p->ancestry[y] = input[x]; x++; y++;}  p->ancestry[y]='\0'; x++; y=0;
    while(input[x]!=';'){ p->species[y] = input[x]; x++; y++;}   p->species[y]='\0'; x++; y=0;
    while(input[x]!=';'){ p->patronus[y] = input[x]; x++; y++;}  p->patronus[y]='\0'; x++; y=0;
    while(input[x]!=';'){ if(input[x]=='F'){ p->hogwartsStaff=false; x+=5;} else{ p->hogwartsStaff = true; x+=10;}} x++; y=0;
    while(input[x]!=';'){ if(input[x]=='F'){ p->hogwartsStudent = false; x+=5;} else{p->hogwartsStudent=true; x+=10;}} x++; y=0;
    while(input[x]!=';'){ p->actorName[y] = input[x]; x++; y++;}  p->actorName[y]='\0'; x++; y=0;
    while(input[x]!=';'){ if(input[x]=='V'){ p->alive=true; x+=10;} else{ p->alive=false; x+=5;}} x++; y=0;
    while(input[x]!=';'){ p->alternate_actors[y] = input[x]; x++; y++;}  p->alternate_actors[y]='\0'; x++; y=0;
    while(input[x]!=';'){ date[y] = input[x]; x++; y++;} LocalDate data=convertData(date); p->dateOfBirth = data; x++; y=0;
    while(input[x]!=';'){ year[y] = input[x];x++; y++;} p->yearOfBirth = atoi(year) ;x++; y=0;
    while(input[x]!=';'){ p->eyeColour[y] = input[x]; x++; y++;}  p->eyeColour[y]='\0'; x++; y=0;
    while(input[x]!=';'){ p->gender[y] = input[x]; x++; y++;}  p->gender[y]='\0'; x++; y=0;
    while(input[x]!=';'){ p->hairColour[y] = input[x]; x++; y++;}  p->hairColour[y]='\0'; x++; y=0;
    while(input[x]!=';'){ if(input[x]=='V'){ p->wizard=true; x+=10;} else{ p->wizard=false; x+=5;}}
}

void arrayOfPersonagens(Personagem p[], int key) {
    char path[100];
    if (key == 1) {
        strcpy(path, "/tmp/characters.csv");
    } else {
        strcpy(path, "/home/yago/Documents/Cc-PucMG/2periodo/TrabalhosPraticos/2024-1/TP2-Aeds2/characters.csv");
    }
    FILE *file = fopen(path, "r");
    if (file == NULL) {
        perror("Erro ao abrir o arquivo");
        return;
    }
    bool header = true;
    char line[300];
    int i = 0;
    while (fgets(line, sizeof(line), file)) {
        if (header) {
            header = false;
        } else {
            initPersonagem(&p[i]);
            setPersonagem(&p[i], line);
            i++;
        }
    }
    fclose(file);
}

char* dayAndMonthFix(int x){
    char *resp = (char*)malloc(3 * sizeof(char)); 
    if(x < 10){
        sprintf(resp, "0%d", x); 
    } else {
        sprintf(resp, "%d", x);
    }
    return resp;
}

void toString(Personagem *p) {
    printf("%s ## %s ## {%s} ## %s ## %s ## %s ## %s ## %s ## %s ## %s ## %s ## %s-%s-%d ## %d ## %s ## %s ## %s ## %s\n", p->id, p->name, p->alternate_names, p->house, 
    p->ancestry, p->species, p->patronus, p->hogwartsStaff ? "true" : "false", p->hogwartsStudent ? "false" : "false", p->actorName, p->alive ? "false" : "false", 
    dayAndMonthFix(p->dateOfBirth.dia), dayAndMonthFix(p->dateOfBirth.mes),p->dateOfBirth.ano, p->yearOfBirth, p->eyeColour, p->gender, p->hairColour, p->wizard ? "false" : "false");
}

void findId(Personagem p[]){
    char strid[50];
    fgets(strid ,sizeof(strid) , stdin);
    strid[strcspn(strid, "\n")] = '\0';
    while(strcmp(strid, "FIM") != 0){
        for(int i=0;i<MAX_TAM;i++){
            if(strcmp(p[i].id, strid)==0){
                toString(&p[i]);
                i = MAX_TAM;
            }
        }
        fgets(strid ,sizeof(strid) , stdin);
        strid[strcspn(strid, "\n")] = '\0';
    }
}

int main() {
    Personagem p[MAX_TAM];
    arrayOfPersonagens(p, 1);
    findId(p);
    return 0;
}
