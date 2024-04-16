/*
*   806454 - Yago Almeida Melo
*   TP2-Q02: Registro em C
*/

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <stdbool.h>

#define MAX_TAM 405

/*
*   Strcut da data de nascimento
*/
typedef struct {
    int dia, mes, ano;
} LocalDate;

/*
*   Struct do Personagem
*/
typedef struct {
    char id[50], name[50], alternate_names[200], house[50], ancestry[50], species[50], patronus[50], actorName[50], alternate_actors[200],
    eyeColour[50], gender[50], hairColour[50];
    int yearOfBirth;
    bool hogwartsStaff, hogwartsStudent, alive, wizard;
    LocalDate dateOfBirth;
} Personagem;


/*
    function: initPersonagem
    @params: Personagem*
    action: inicializa o personagem
*/
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

/*
    function: convertData
    @params: char[]
    action: retorna um LocalDate de acordo com o parametro char[]
*/
LocalDate convertData(char data[]) {
    LocalDate localDate;
    sscanf(data, "%d-%02d-%04d", &localDate.dia, &localDate.mes, &localDate.ano);
    return localDate;
}

/*
    function: setPersonagem
    @params: Personagem* && char*
    action: seta um personagem de acordo com a linha char*
*/
void setPersonagem(Personagem *p, char *line){
    char str[300], date[11], year[5];
    strcpy(str,line);
    int x=0, y=0;

    while(str[x]!=';'){ p->id[y] = str[x]; x++; y++;}    p->id[y]='\0'; x++; y=0;
    while(str[x]!=';'){ p->name[y] = str[x]; x++; y++;}  p->name[y]='\0'; x++; y=0;
    while(str[x]!=';'){ if(str[x]!='[' && str[x]!=']' && str[x]!=39){ p->alternate_names[y] = str[x]; y++;} x++;}  p->alternate_names[y]='\0'; x++; y=0;   
    while(str[x]!=';'){ p->house[y] = str[x]; x++; y++;} p->house[y]='\0'; x++; y=0;
    while(str[x]!=';'){ p->ancestry[y] = str[x]; x++; y++;}  p->ancestry[y]='\0'; x++; y=0;
    while(str[x]!=';'){ p->species[y] = str[x]; x++; y++;}   p->species[y]='\0'; x++; y=0;
    while(str[x]!=';'){ p->patronus[y] = str[x]; x++; y++;}  p->patronus[y]='\0'; x++; y=0;
    while(str[x]!=';'){ if(str[x]=='V'){ p->hogwartsStaff = true; x+=10;} else{ p->hogwartsStaff = false; x+=5;}} x++;y=0;
    while(str[x]!=';'){ if(str[x]=='V'){ p->hogwartsStudent = true; x+=10;} else{p->hogwartsStudent = false; x+=5;}} x++;y=0;
    while(str[x]!=';'){ p->actorName[y] = str[x]; x++; y++;}  p->actorName[y]='\0'; x++; y=0;
    while(str[x]!=';'){ if(str[x]=='V'){ p->alive=true; x+=10;} else{ p->alive=false; x+=5;}} x++; y=0;
    while(str[x]!=';'){ p->alternate_actors[y] = str[x]; x++; y++;}  p->alternate_actors[y]='\0'; x++; y=0;
    while(str[x]!=';'){ date[y] = str[x]; x++; y++;} LocalDate data=convertData(date); p->dateOfBirth = data; x++; y=0;
    while(str[x]!=';'){ year[y] = str[x];x++; y++;} p->yearOfBirth = atoi(year) ;x++; y=0;
    while(str[x]!=';'){ p->eyeColour[y] = str[x]; x++; y++;}  p->eyeColour[y]='\0'; x++; y=0;
    while(str[x]!=';'){ p->gender[y] = str[x]; x++; y++;}  p->gender[y]='\0'; x++; y=0;
    while(str[x]!=';'){ p->hairColour[y] = str[x]; x++; y++;}  p->hairColour[y]='\0'; x++; y=0;
    if(str[x]=='V'){ p->wizard=true; x+=10;} else{ p->wizard=false; x+=5;}
}

/*
    function: arrayOfPersonagens
    @params: Personagem[] && int
    action: Guarda todos os Personagens do csv em um array
*/
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

/*
    function: toString
    @params: Personagem*
    action: Imprime na tela o Personagem do parâmetro
*/
void toString(Personagem *p) {
    printf("[%s ## %s ## {%s} ## %s ## %s ## %s ## %s ## %s ## %s ## %s ## %s ## %02d-%02d-%d ## %d ## %s ## %s ## %s ## %s]\n", p->id, p->name, p->alternate_names, p->house, 
    p->ancestry, p->species, p->patronus, p->hogwartsStaff ? "true" : "false", p->hogwartsStudent ? "true" : "false", p->actorName, p->alive ? "true" : "false", 
    p->dateOfBirth.dia, p->dateOfBirth.mes, p->dateOfBirth.ano, p->yearOfBirth, p->eyeColour, p->gender, p->hairColour, p->wizard ? "true" : "false");
}

/*
    function: findId
    @params: Personagem[]
    action: pede o input que será o id, procura no array de Personagem e imprime este personagem na tela
*/
void findId(Personagem p[]){
    char strid[50];
    scanf("%s", strid);
    while(strcmp(strid, "FIM") != 0){
        for(int i=0;i<MAX_TAM;i++){
            if(strcmp(p[i].id, strid)==0){
                toString(&p[i]);
                i = MAX_TAM;
            }
        }
        scanf("%s", strid);
    }
}

//MAIN
int main() {
    Personagem p[MAX_TAM];
    arrayOfPersonagens(p, 0);
    findId(p);
    return 0;
}
