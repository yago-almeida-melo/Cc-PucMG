/*
*   806454 - Yago Almeida Melo
*   TP2-Q12: Bolha em C
*/

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <stdbool.h>
#include <time.h>

#define MAX_TAM 404

//Variaveis globais de tempo e de número de comparações
clock_t start;
clock_t end;
double tempo = 0;
int comparacoes = 0, movimentacoes = 0;

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

//Arrays de Personagens
Personagem personagens[MAX_TAM];
Personagem sub[MAX_TAM];
int tamSubArray = 0;

void initPersonagem(Personagem *p);
void clone(Personagem *p, Personagem *clone);
LocalDate convertData(char data[]);
void setPersonagem(Personagem *p, char *line);
void makeArray(int key);
void makeSubArray();
void mostrar();
void toString(Personagem *p);
bool isFim(char str[]);
void swap(Personagem *p1, Personagem *p2);
void shellsort();
void registroLog(int key);

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
    function: clone
    @params: Personagem* && Personagem* && int
    action: cria um clone de um Personagem
*/
void clone(Personagem *p, Personagem *clone){
    strcpy(clone->id, p->id);
    strcpy(clone->name, p->name);
    strcpy(clone->alternate_names, p->alternate_names);
    strcpy(clone->house, p->house);
    strcpy(clone->ancestry, p->ancestry);
    strcpy(clone->species, p->species);
    strcpy(clone->patronus, p->patronus);
    clone->hogwartsStaff = p->hogwartsStaff;
    clone->hogwartsStudent = p->hogwartsStudent;
    strcpy(clone->actorName, p->actorName);
    clone->alive = p->alive;
    strcpy(clone->alternate_actors, p->alternate_actors);
    clone->dateOfBirth.dia = p->dateOfBirth.dia;
    clone->dateOfBirth.mes = p->dateOfBirth.mes;
    clone->dateOfBirth.ano = p->dateOfBirth.ano;
    clone->yearOfBirth = p->yearOfBirth;
    strcpy(clone->eyeColour, p->eyeColour);
    strcpy(clone->gender, p->gender);
    strcpy(clone->hairColour, p->hairColour);
    clone->wizard = p->wizard;
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
    char str[300], date[11], year[5]; strcpy(str,line);
    int x=0, y=0;

    while(str[x]!=';'){ p->id[y] = str[x]; x++; y++;}    p->id[y]='\0'; x++; y=0;
    while(str[x]!=';'){ p->name[y] = str[x]; x++; y++;}  p->name[y]='\0'; x++; y=0;
    while(str[x]!=';'){ if(str[x]!='[' && str[x]!=']' && str[x]!=39){ p->alternate_names[y] = str[x]; y++;} x++;}  p->alternate_names[y]='\0'; x++; y=0;   
    while(str[x]!=';'){ p->house[y] = str[x]; x++; y++;} p->house[y]='\0'; x++; y=0;
    while(str[x]!=';'){ p->ancestry[y] = str[x]; x++; y++;}  p->ancestry[y]='\0'; x++; y=0;
    while(str[x]!=';'){ p->species[y] = str[x]; x++; y++;}   p->species[y]='\0'; x++; y=0;
    while(str[x]!=';'){ p->patronus[y] = str[x]; x++; y++;}  p->patronus[y]='\0'; x++; y=0;
    while(str[x]!=';'){ if(str[x]=='V'){ p->hogwartsStaff=true; x+=10;} else{ p->hogwartsStaff = false; x+=5;}} x++; y=0;
    while(str[x]!=';'){ if(str[x]=='V'){ p->hogwartsStudent = true; x+=10;} else{p->hogwartsStudent=false; x+=5;}} x++; y=0;
    while(str[x]!=';'){ p->actorName[y] = str[x]; x++; y++;}  p->actorName[y]='\0'; x++; y=0;
    while(str[x]!=';'){ if(str[x]=='V'){ p->alive=true; x+=10;} else{ p->alive=false; x+=5;}} x++; y=0;
    while(str[x]!=';'){ p->alternate_actors[y] = str[x]; x++; y++;}  p->alternate_actors[y]='\0'; x++; y=0;
    while(str[x]!=';'){ date[y] = str[x]; x++; y++;} LocalDate data=convertData(date); p->dateOfBirth = data; x++; y=0;
    while(str[x]!=';'){ year[y] = str[x];x++; y++;} p->yearOfBirth = atoi(year) ;x++; y=0;
    while(str[x]!=';'){ p->eyeColour[y] = str[x]; x++; y++;}  p->eyeColour[y]='\0'; x++; y=0;
    while(str[x]!=';'){ p->gender[y] = str[x]; x++; y++;}  p->gender[y]='\0'; x++; y=0;
    while(str[x]!=';'){ p->hairColour[y] = str[x]; x++; y++;}  p->hairColour[y]='\0'; x++; y=0;
    if(str[x]=='V'){ p->wizard=true; x+=9;} else{ p->wizard=false; x+=4;}
}

/*
    function: makeArray
    @params: int
    action: Cria Array com todos os Personagens do csv
*/
void makeArray(int key) {
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
            initPersonagem(&personagens[i]);
            setPersonagem(&personagens[i], line);
            i++;
        }
    }
    fclose(file);
}

/*
    function: makeSubArray
    @params: null
    action: A partir das entradas dos ids, monta-se um sub-Array, só com os respectivos Personagens 
*/
void makeSubArray(){
    char id[40];
    scanf(" %s", id);
    while(!isFim(id)){
        int i = 0;
        while(i<MAX_TAM){
            if(strcmp(personagens[i].id, id)==0){
                clone(&personagens[i], &sub[tamSubArray]);
                tamSubArray++;
                break;
            }
            i++;
        }
        scanf(" %s", id);
    }
}

/*
    function: mostar
    @params: null
    action: Mostar todos os elementos do subArray
*/
void mostrar(){
    for(int i=0;i<tamSubArray;i++){
        toString(&sub[i]);
    }
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
    function: isFim
    @params: char[]
    action: Retorna true se char[] é igual a "FIM", e false se não for;
*/
bool isFim(char str[]){
    return str[0] == 'F' && str[1] == 'I' && str[2] == 'M';
}

/*
    function: swap
    @params: Personagem* && Personagem*
    action: Faz a troca de referencia de dois Personagens 
*/
void swap(Personagem *p1, Personagem *p2){
    Personagem tmp = *p1;
    *p1 = *p2;
    *p2 = tmp;
    movimentacoes += 3;
}

/*
    function: comparator
    @params: Personagem && Personagem
    action: Compara dois Personagens pelo atributo hairColour (desempate por nome) e retorna um booleano
*/
bool comparator(Personagem a, Personagem b){
    bool resp = false;  
    if(strcmp(a.hairColour, b.hairColour) > 0){       // a < b
        resp = true; 
        comparacoes++;  
    } else if(strcmp(a.hairColour, b.hairColour) < 0){  // a > b
        resp = false;
        comparacoes++;
    } else{                               // a==b   
        if(strcmp(a.name, b.name) > 0){    // a < b
            resp = true;
            comparacoes++;
        } else{                 //a > b
            resp = false;
            comparacoes++;
        }
    } 
    return resp;
}

/*
    function: bolha
    @params: null
    action: Ordena o Array de Personagem pelo metodo bolha, pelo atributo hairColour e desempate por name
*/
void bolha() {
    start = clock();
    int i, j;
    for (i = 0; i < (tamSubArray-1); i++) {
        for (j = 0; j < (tamSubArray-1); j++) {
            if(comparator(sub[j], sub[j + 1])) {
                swap(&sub[j], &sub[j + 1]);
            }
        }
    }
    end = clock();
    tempo = ((double)(end - start)) / CLOCKS_PER_SEC;
    mostrar(&sub);
}

/*
    function: registroLog
    @params: int
    action: Cria um arquivo contendo o tempo de execução e o numero de comparações do código
*/
void registroLog(int key){
    FILE *arquivo;
    if(key == 0){
        arquivo = fopen("806454_bolha.txt", "w");
    }else{
        arquivo = fopen("/tmp/806454_bolha.txt", "w");
    }
    if (arquivo == NULL) {
        perror("Erro ao abrir o arquivo.");
        exit(EXIT_FAILURE); 
    }
    fprintf(arquivo, "Matricula: 806454\tTempo de execucao: %.6f segundos\tComparacoes: %d\tMovimentacoes: %d", tempo, comparacoes, movimentacoes);
    fclose(arquivo);
}

//MAIN
int main() {
    makeArray(1);          // 0 == teste   / 1 == verde
    makeSubArray();
    bolha();
    registroLog(1);          // 0 == teste  /  1 == verde
    return 0;
}
