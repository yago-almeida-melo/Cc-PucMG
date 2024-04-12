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
    char id[50], name[50], house[50], ancestry[50], species[50], patronus[50], actorName[50], eyeColour[50], gender[50],
    hairColour[50];
    char *alternate_names, *alternate_actors;
    int yearOfBirth;
    bool hogwartsStaff, hogwartsStudent, alive, wizard;
    LocalDate dateOfBirth;
} Personagem;

void initPersonagem(Personagem *p) {
    strcpy(p->id, "");
    strcpy(p->name, "");
    p->alternate_names = (char*)malloc(100 * sizeof(char));
    strcpy(p->house, "");
    strcpy(p->ancestry, "");
    strcpy(p->species, "");
    strcpy(p->patronus, "");
    p->hogwartsStaff = false;
    p->hogwartsStudent = false;
    strcpy(p->actorName, "");
    p->alive = false;
    p->alternate_actors = (char*)malloc(100 * sizeof(char));
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

char* fixList(char line[]) {
    size_t len = strlen(line);
    if (len <= 2) {
        return "";
    }
    // Aloca memória para o array de caracteres corrigido
    char* resp = (char*)malloc(len-1);
    if (resp == NULL) {
        return NULL;
    }
    int i = 1;
    int j = 0;
    // Copia os caracteres corrigidos para o novo array de caracteres
    while (i < len - 1) {
        resp[j++] = line[i++];
    }
    // Adiciona o caractere nulo final para formar uma string válida
    resp[j] = '\0';
    return resp;
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

/*void setPersonagem(Personagem *p, char *linha) {
    char *token = strtok(linha, ";");
    int count = 0;
    while (token != NULL) {
        if (strcmp(token, "") == 0) {
            token = strtok(NULL, ";");
        } else {
            switch (count) {
                case 0:
                    strcpy(p->id, token);
                    break;
                case 1:
                    strcpy(p->name, token);
                    break;
                case 2:
                    p->alternate_names = fixList(token);
                    break;
                case 3:
                    strcpy(p->house, token);
                    break;
                case 4:
                    strcpy(p->ancestry,token);
                    break;
                case 5:
                    strcpy(p->species,token);
                    break;
                case 6:
                    strcpy(p->patronus,token);
                    break;
                case 7:
                    p->hogwartsStaff = (strcmp(token, "VERDADEIRO") == 0);
                    break;
                case 8:
                    p->hogwartsStudent = (strcmp(token, "VERDADEIRO") == 0);
                    break;
                case 9:
                    strcpy(p->actorName, token);
                    break;
                case 10: 
                    p->alive = (strcmp(token, "VERDADEIRO") == 0);
                    break;
                case 11:
                    p->alternate_actors = fixList(token);
                    break;
                case 12:
                    LocalDate date = convertData(token);
                    p->dateOfBirth = date;
                    break;
                case 13:
                    p->yearOfBirth = atoi(token);
                    break;
                case 14:
                    strcpy(p->eyeColour, token);
                    break;
                case 15:
                    strcpy(p->gender, token);
                    break;
                case 16:
                    strcpy(p->hairColour, token);
                    break;
                case 17: 
                    p->wizard = (strcmp(token, "VERDADEIRO") == 0);
                    break;
            }
            token = strtok(NULL, ";");
            count++;
        }
    }
}*/

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

void toString(Personagem *p) {
    printf("%s ## %s ## {%s} ## %s ## %s ## %s ## %s ## %s ## %s ## %s ## %s ## %d-%d-%d ## %d ## %s ## %s ## %s ## %s\n", p->id, p->name, p->alternate_names, p->house, 
    p->ancestry, p->species, p->patronus, p->hogwartsStaff ? "true" : "false", p->hogwartsStudent ? "true" : "false", p->actorName, p->alive ? "true" : "false", 
    p->dateOfBirth.dia, p->dateOfBirth.mes,p->dateOfBirth.ano, p->yearOfBirth, p->eyeColour, p->gender, p->hairColour, p->wizard ? "true" : "false");
}

int main() {
    Personagem p[MAX_TAM];
    arrayOfPersonagens(p, 0);
    for(int i=0;i<MAX_TAM;i++){
        toString(&p[i]);
    }
    return 0;
}
