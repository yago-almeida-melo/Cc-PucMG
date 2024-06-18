/*
 *  806454 - Yago Almeida Melo
 *  TP4-Q03: Avl em C
 */

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <stdbool.h>
#include <time.h>

#define MAX_TAM 404
int comparacoes = 0;
double tempo = 0.0;
clock_t inicio, fim;

/*
 *   Strcut da data de nascimento
 */
typedef struct{
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

typedef struct No{
    Personagem elemento;
    struct No *esq, *dir;
    int nivel;
} No;

No *novoNo(Personagem p);
void start();
int max(int a, int b);
int altura(No *no);
int fatorBalanceamento(No *no);
No *rotacionarDir(No *no);
No *rotacionarEsq(No *no);
void inserir(Personagem elemento);
No *inserirRec( Personagem elemento, No *no);
No *balancear(No *no);
void pesquisar(char x[]);
bool pesquisarRec(No *i, char x[]);
void caminharPre();
void caminharPreRec(No* i);

No *novoNo(Personagem p){
    No *novo = (No *)malloc(sizeof(No));
    novo->elemento = p;
    novo->dir = NULL;
    novo->esq = NULL;
    novo->nivel = 1;
    return novo;
}

No* raiz;

void start(){
    raiz = NULL;
}

// Função para calcular o maximo entre dois numeros
int max(int a, int b) {
    return (a > b) ? a : b;
}

// Função para calcular a altura do no
int altura(No *no){
    if (no == NULL)
        return 0;
    return no->nivel;
}

// Função para calcular o fator de balanceamento do no
int fatorBalanceamento(No *no) {
    if (no == NULL)
        return 0;
    return altura(no->dir) - altura(no->esq);
}

// Função para rotacionar o no para a direita
No *rotacionarDir(No *no)
{
    No *noEsq = no->esq;
    No *noEsqDir = noEsq->dir;
    noEsq->dir = no;
    no->esq = noEsqDir;
    no->nivel = max(altura(no->esq), altura(no->dir)) + 1;
    noEsq->nivel = max(altura(noEsq->esq), altura(noEsq->dir)) + 1;
    return noEsq;
}

//  Funcao para rotacionar o no para a esquerda
No *rotacionarEsq(No *no) {
    No *noDir = no->dir;
    No *noDirEsq = noDir->esq;
    noDir->esq = no;
    no->dir = noDirEsq;
    no->nivel = max(altura(no->esq), altura(no->dir)) + 1;
    noDir->nivel = max(altura(noDir->esq), altura(noDir->dir)) + 1;

    return noDir;
}

/*
* Função para inserir um elemento na arvore AVL
*/ 
void inserir(Personagem elemento) {
    raiz = inserirRec(elemento, raiz);
}

No *inserirRec(Personagem elemento, No *no) {
    if (no == NULL){
        return novoNo(elemento);
    }
    int comparacao = strcmp(elemento.name, no->elemento.name);
    if (comparacao < 0){
        no->esq = inserirRec(elemento, no->esq);
    }
    else if (comparacao > 0) {
        no->dir = inserirRec(elemento, no->dir);
    }
    else{
        // Atualiza os valores do Personagem existente
        no->elemento = elemento;
        return no;
    }
    balancear(no);
}

// Função para balancear a arvore
No *balancear(No *no) {
    no->nivel = 1 + max(altura(no->esq), altura(no->dir));
    int fator = fatorBalanceamento(no);
    if (fator > 1){
        if (fatorBalanceamento(no->dir) < 0){
            no->dir = rotacionarDir(no->dir);
        }
        return rotacionarEsq(no);
    }
    if (fator < -1){
        if (fatorBalanceamento(no->esq) > 0){
            no->esq = rotacionarEsq(no->esq);
        }
        return rotacionarDir(no);
    }
    return no;
}

// Função auxiliar para imprimir o caminho percorrido na pesquisa
void pesquisar(char *nome) {
    printf("%s => raiz", nome);
    comparacoes = 0; // Reinicializa o contador de comparações para cada busca
    if(pesquisarRec(raiz, nome)) {
        printf(" SIM\n");
    } else {
        printf(" NAO\n");
    }
}

// Função para realizar a pesquisa na árvore AVL
bool pesquisarRec(No *no, char *nome) {
    if (no == NULL){
        return false;
    }
    int comparacao = strcmp(nome, no->elemento.name);
    comparacoes++; // Incrementa o contador de comparações

    if (comparacao == 0) {
        return true;
    }
    else if (comparacao > 0) {
        printf(" dir");
        return pesquisarRec(no->dir, nome);
    }
    else {
        printf(" esq");
        return pesquisarRec(no->esq, nome);
    }
}

Personagem p[MAX_TAM];

/*
    function: initPersonagem
    @params: Personagem*
    action: inicializa o personagem
*/
void initPersonagem(Personagem *p){
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
void clonePersonagem(Personagem *destino, Personagem *origem){
    strncpy(destino->id, origem->id, strlen(destino->id));
    strncpy(destino->name, origem->name, strlen(destino->name));
    strncpy(destino->alternate_names, origem->alternate_names, strlen(destino->alternate_names));
    strncpy(destino->house, origem->house, strlen(destino->house));
    strncpy(destino->ancestry, origem->ancestry, strlen(destino->ancestry));
    strncpy(destino->species, origem->species, strlen(destino->species));
    strncpy(destino->patronus, origem->patronus, strlen(destino->patronus));
    destino->hogwartsStaff = origem->hogwartsStaff;
    destino->hogwartsStudent = origem->hogwartsStudent;
    strncpy(destino->actorName, origem->actorName, strlen(destino->actorName));
    destino->alive = origem->alive;
    strncpy(destino->alternate_actors, origem->alternate_actors, strlen(destino->alternate_actors));
    destino->dateOfBirth = origem->dateOfBirth;
    destino->yearOfBirth = origem->yearOfBirth;
    strncpy(destino->eyeColour, origem->eyeColour, strlen(destino->eyeColour));
    strncpy(destino->gender, origem->gender, strlen(destino->gender));
    strncpy(destino->hairColour, origem->hairColour, strlen(destino->hairColour));
    destino->wizard = origem->wizard;
}

/*
    function: convertData
    @params: char[]
    action: retorna um LocalDate de acordo com o parametro char[]
*/
LocalDate convertData(char data[]){
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
void arrayOfPersonagens(Personagem p[], int key){
    char path[100];
    if (key == 1){
        strcpy(path, "/tmp/characters.csv");
    }
    else{
        strcpy(path, "/home/yago/Documents/Cc-PucMG/2periodo/TrabalhosPraticos/2024-1/TP2-Aeds2/characters.csv");
    }
    FILE *file = fopen(path, "r");
    if (file == NULL){
        perror("Erro ao abrir o arquivo");
        return;
    }
    bool header = true;
    char line[300];
    int i = 0;
    while (fgets(line, sizeof(line), file)){
        if (header){
            header = false;
        }
        else{
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
void toString(Personagem *p){
    printf("[%s ## %s ## {%s} ## %s ## %s ## %s ## %s ## %s ## %s ## %s ## %s ## %02d-%02d-%d ## %d ## %s ## %s ## %s ## %s]\n", p->id, p->name, p->alternate_names, p->house,
           p->ancestry, p->species, p->patronus, p->hogwartsStaff ? "true" : "false", p->hogwartsStudent ? "true" : "false", p->actorName, p->alive ? "true" : "false",
           p->dateOfBirth.dia, p->dateOfBirth.mes, p->dateOfBirth.ano, p->yearOfBirth, p->eyeColour, p->gender, p->hairColour, p->wizard ? "true" : "false");
}

/*
    function: findID
    @params: Personagem[], char*
    action: procura o personagem pelo id e retorna ele;
*/
Personagem findID(Personagem array[], char *id){
    Personagem personagem;
    if (id[strlen(id) + 2] == '\n'){
        id[strlen(id) + 2] = '\0';
    }
    for (int i = 0; i < MAX_TAM; i++){
        if (strcmp(array[i].id, id) == 0){
            personagem = array[i];
            i = MAX_TAM;
        }
    }
    return personagem;
}

void inserirNaArvore(Personagem array[]){
    char id[40];
    scanf("%s", id);
    while(strcmp(id, "FIM")!=0){
        Personagem x = findID(array, id);
        inserir(x);
        scanf("%s", id);
    }
}

void procurarNaArvore(){
    char nome[100];
    scanf(" %[^\n\r]", nome);
    inicio = clock();
    while(strcmp(nome, "FIM")!=0){
        pesquisar(nome);
        scanf(" %[^\n\r]", nome);
    }
    fim = clock();
    tempo = ((double)(inicio - fim)) / CLOCKS_PER_SEC;
}

/*
    function: registroLog
    @params: int
    action: Cria um arquivo contendo o tempo de execução e o numero de comparações do código
*/
void registroLog(int key){
    FILE *arquivo;
    if(key == 0){
        arquivo = fopen("806454_avl.txt", "w");
    }else{
        arquivo = fopen("/tmp/806454_avl.txt", "w");
    }
    if (arquivo == NULL) {
        perror("Erro ao abrir o arquivo.");
        exit(EXIT_FAILURE); 
    }
    fprintf(arquivo, "Matricula: 806454\tTempo de execucao: %.6f segundos\tComparacoes: %d", tempo, comparacoes);
    fclose(arquivo);
}

// MAIN
int main(){
    Personagem array[MAX_TAM];
    arrayOfPersonagens(array, 1); // 2* parâmetro: 0 == teste / 1 == verde
    start();
    inserirNaArvore(array);
    procurarNaArvore();
    registroLog(1);          // 0 == teste  /  1 == verde
    return 0;
}