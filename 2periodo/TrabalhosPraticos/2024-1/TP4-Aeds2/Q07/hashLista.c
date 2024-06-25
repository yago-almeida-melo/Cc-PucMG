/*
    806454 - Yago Almeida Melo                                                   
    TP4-Q07: Hash Indireta com Lista Flexivel em C
*/

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <stdbool.h>
#include <time.h>

#define MAX_TAM 404
#define tamTab 21
int comparacoes = 0;
double tempo = 0.0;
clock_t inicio, fim;

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

typedef struct Celula {
    Personagem elemento;
    struct Celula* prox;
} Celula;

void start();
Celula* novaCelula(Personagem elemento);
void inserir(Personagem p);
int hash(char *str);
void pesquisar(char* nome);

Personagem p[MAX_TAM];
Celula* array[tamTab];

void start() {
    for (int i = 0; i < tamTab; i++) {
        array[i] = NULL;
    }
}

Celula* novaCelula(Personagem elemento) {
    Celula* nova = (Celula*)malloc(sizeof(Celula));
    nova->elemento = elemento;
    nova->prox = NULL;
    return nova;
}

void inserir(Personagem p) {
    int pos = hash(p.name);
    Celula* nova = novaCelula(p);
    nova->prox = array[pos];
    array[pos] = nova;
}

int hash(char *str) {
    int resp = 0;
    for (int i = 0; str[i] != '\0'; i++) {
        resp += (int)str[i];
    }
    resp = resp % tamTab;
    return resp;
}

void pesquisar(char* nome) {
    int pos = hash(nome);
    Celula* tmp = array[pos];
    bool encontrado = false;
    while (tmp != NULL && !encontrado) {
        comparacoes++;
        if (strcmp(tmp->elemento.name, nome) == 0) {
            encontrado = true;
        }
        tmp = tmp->prox;
    }
    if (encontrado) {
        printf("%s (pos: %d) SIM\n", nome, pos);
    } else {
        printf("%s NAO\n", nome);
    }
}

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
    function: clonePersonagem
    @params: Personagem* destino, Personagem* origem
    action: copia os valores de um struct Personagem para outro
*/
void clonePersonagem(Personagem *destino, Personagem *origem) {
    strcpy(destino->id, origem->id);
    strcpy(destino->name, origem->name);
    strcpy(destino->alternate_names, origem->alternate_names);
    strcpy(destino->house, origem->house);
    strcpy(destino->ancestry, origem->ancestry);
    strcpy(destino->species, origem->species);
    strcpy(destino->patronus, origem->patronus);
    destino->hogwartsStaff = origem->hogwartsStaff;
    destino->hogwartsStudent = origem->hogwartsStudent;
    strcpy(destino->actorName, origem->actorName);
    destino->alive = origem->alive;
    strcpy(destino->alternate_actors, origem->alternate_actors);
    destino->dateOfBirth = origem->dateOfBirth;
    destino->yearOfBirth = origem->yearOfBirth;
    strcpy(destino->eyeColour, origem->eyeColour);
    strcpy(destino->gender, origem->gender);
    strcpy(destino->hairColour, origem->hairColour);
    destino->wizard = origem->wizard;
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
    strcpy(str, line);
    int x = 0, y = 0;

    while (str[x] != ';'){p->id[y] = str[x];x++;y++;}p->id[y] = '\0';x++;y = 0;
    while (str[x] != ';'){p->name[y] = str[x];x++;y++;}p->name[y] = '\0';x++;y = 0;
    while (str[x] != ';'){if (str[x] != '[' && str[x] != ']' && str[x] != 39){p->alternate_names[y] = str[x];y++;}x++;}p->alternate_names[y] = '\0';x++;y = 0;
    while (str[x] != ';'){p->house[y] = str[x];x++;y++;}p->house[y] = '\0';x++;y = 0;
    while (str[x] != ';'){p->ancestry[y] = str[x];x++;y++;}p->ancestry[y] = '\0';x++;y = 0;
    while (str[x] != ';'){p->species[y] = str[x];x++;y++;}p->species[y] = '\0';x++;y = 0;
    while (str[x] != ';'){p->patronus[y] = str[x];x++;y++;}p->patronus[y] = '\0';x++;y = 0;
    while (str[x] != ';'){if (str[x] == 'V'){p->hogwartsStaff = true;x += 10;}else{p->hogwartsStaff = false;x += 5;}}x++;y = 0;
    while (str[x] != ';'){if (str[x] == 'V'){p->hogwartsStudent = true;x += 10;}else{p->hogwartsStudent = false;x += 5;}}x++;y = 0;
    while (str[x] != ';'){p->actorName[y] = str[x];x++;y++;}p->actorName[y] = '\0';x++;y = 0;
    while (str[x] != ';'){if (str[x] == 'V'){p->alive = true;x += 10;}else{p->alive = false;x += 5;}}x++;y = 0;
    while (str[x] != ';'){p->alternate_actors[y] = str[x];x++;y++;}p->alternate_actors[y] = '\0';x++;y = 0;
    while (str[x] != ';'){date[y] = str[x];x++;y++;}LocalDate data = convertData(date);p->dateOfBirth = data;x++;y = 0;
    while (str[x] != ';'){year[y] = str[x];x++;y++;}p->yearOfBirth = atoi(year);x++;y = 0;
    while (str[x] != ';'){p->eyeColour[y] = str[x];x++;y++;}p->eyeColour[y] = '\0';x++;y = 0;
    while (str[x] != ';'){p->gender[y] = str[x];x++;y++;}p->gender[y] = '\0';x++;y = 0;
    while (str[x] != ';'){p->hairColour[y] = str[x];x++;y++;}p->hairColour[y] = '\0';x++;y = 0;if (str[x] == 'V'){p->wizard = true;x += 10;}else{p->wizard = false;x += 5;}
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
    function: findID
    @params: Personagem[], char*
    action: procura o personagem pelo id e retorna ele;
*/
Personagem findID(Personagem array[], char *id) {
    for (int i = 0; i < MAX_TAM; i++) {
        if (strcmp(array[i].id, id) == 0) {
            return array[i];
        }
    }
    Personagem vazio;
    initPersonagem(&vazio);
    return vazio;
}

Personagem findName(Personagem array[], char* name) {
    for (int i = 0; i < MAX_TAM; i++) {
        if (strcmp(array[i].name, name) == 0) {
            return array[i];
        }
    }
    Personagem vazio;
    initPersonagem(&vazio);
    return vazio;
}

void inserirNaHash(Personagem array[]) {
    char id[100];
    scanf(" %[^\n\r]", id);
    while (strcmp(id, "FIM") != 0) {
        Personagem p = findID(array, id);
        inserir(p);
        scanf(" %[^\n\r]", id);
    }
}

void procurarNaHash() {
    char nome[100];
    scanf(" %[^\n\r]", nome);
    inicio = clock();
    while (strcmp(nome, "FIM") != 0) {
        pesquisar(nome);
        scanf(" %[^\n\r]", nome);
    }
    fim = clock();
    tempo = ((double)(fim - inicio)) / CLOCKS_PER_SEC;
}

/*
    function: registroLog
    @params: int
    action: Cria um arquivo contendo o tempo de execução e o numero de comparações do código
*/
void registroLog(int key) {
    FILE *arquivo;
    if (key == 0) {
        arquivo = fopen("806454_hashIndireta.txt", "w");
    } else {
        arquivo = fopen("/tmp/806454_hashIndireta.txt", "w");
    }
    if (arquivo == NULL) {
        perror("Erro ao abrir o arquivo.");
        exit(EXIT_FAILURE); 
    }
    fprintf(arquivo, "Matricula: 806454\tTempo de execucao: %.6f segundos\tComparacoes: %d", tempo, comparacoes);
    fclose(arquivo);
}

// MAIN
int main() {
    Personagem array[MAX_TAM];
    arrayOfPersonagens(array, 1); // 2* parâmetro: 0 == teste / 1 == verde
    start();
    inserirNaHash(array);
    procurarNaHash();
    registroLog(1);          // 0 == teste  /  1 == verde
    return 0;
}
