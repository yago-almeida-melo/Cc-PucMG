/*
    806454 - Yago Almeida Melo
    TP4 - Q03 Arvore AVL em C
*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <math.h>
#include <time.h>

// atributos globais
#define MAX_LINE_LENGTH 200
#define MAX_ID 3921
int comparacoes = 0;
double tempo = 0.0;
clock_t start, end;

// Struct Jogador
typedef struct
{
    int id, altura, peso, anoNascimento;
    char nome[100], universidade[100], cidadeNascimento[100], estadoNascimento[100];
} Jogador;

// Struct NO
typedef struct No
{
    Jogador elemento;
    int nivel;
    struct No *esq;
    struct No *dir;
} No;

No *raiz = NULL;

// Funcoes Jogador
void initJogador(Jogador *j);
void addJogadores();
void tratamentoVirgula(char *linha, char *nova);
void salvaPorId(Jogador *j, int id);
void removeCaracteresEspeciais(char *str);
void setJogador(Jogador *j, char *linha);
char *imprimirJogador(Jogador j);
void swap(Jogador *a, Jogador *b);
// Funções da AVL
No *criaNo(Jogador elemento);
int max(int a, int b);
int altura(No *no);
int fatorBalanceamento(No *no);
No *rotacionarDir(No *no);
No *rotacionarEsq(No *no);
void inserindo(Jogador elemento);
No *inserir(No *no, Jogador elemento);
No *balancear(No *no);
bool buscaRec(No *no, char *str);
void busca(char *str);
void caminharCentral();
void caminharCentralRec(No *i);
void registroLog();

// Função para criar No
No *criaNo(Jogador elemento)
{
    No *novoNo = (No *)malloc(sizeof(No));
    novoNo->elemento = elemento;
    novoNo->nivel = 1;
    novoNo->esq = NULL;
    novoNo->dir = NULL;
    return novoNo;
}

// Função para calcular o maximo entre dois numeros
int max(int a, int b)
{
    return (a > b) ? a : b;
}

// Função para calcular a altura do no
int altura(No *no)
{
    if (no == NULL)
        return 0;
    return no->nivel;
}

// Função para calcular o fator de balanceamento do no
int fatorBalanceamento(No *no)
{
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
No *rotacionarEsq(No *no)
{
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
void inserindo(Jogador elemento)
{
    raiz = inserir(raiz, elemento);
}

No *inserir(No *no, Jogador elemento)
{
    if (no == NULL)
    {
        return criaNo(elemento);
    }
    int comparacao = strcmp(elemento.nome, no->elemento.nome);
    if (comparacao < 0)
    {
        no->esq = inserir(no->esq, elemento);
    }
    else if (comparacao > 0)
    {
        no->dir = inserir(no->dir, elemento);
    }
    else
    {
        // Atualiza os valores do jogador existente
        no->elemento = elemento;
        return no;
    }
    balancear(no);
}

// Função para balancear a arvore
No *balancear(No *no)
{
    no->nivel = 1 + max(altura(no->esq), altura(no->dir));
    int fator = fatorBalanceamento(no);
    if (fator > 1)
    {
        if (fatorBalanceamento(no->dir) < 0)
        {
            no->dir = rotacionarDir(no->dir);
        }
        return rotacionarEsq(no);
    }
    if (fator < -1)
    {
        if (fatorBalanceamento(no->esq) > 0)
        {
            no->esq = rotacionarEsq(no->esq);
        }
        return rotacionarDir(no);
    }
    return no;
}

// Função auxiliar para imprimir o caminho percorrido na pesquisa
void busca(char *nome) {
    printf("%s raiz", nome);
    comparacoes = 0; // Reinicializa o contador de comparações para cada busca
    if(buscaRec(raiz, nome)) {
        printf(" SIM\n");
    } else {
        printf(" NAO\n");
    }
}

// Função para realizar a pesquisa na árvore AVL
bool buscaRec(No *no, char *nome) {
    if (no == NULL){
        return false;
    }
    int comparacao = strcmp(nome, no->elemento.nome);
    comparacoes++; // Incrementa o contador de comparações

    if (comparacao == 0) {
        return true;
    }
    else if (comparacao > 0) {
        printf(" dir");
        return buscaRec(no->dir, nome);
    }
    else {
        printf(" esq");
        return buscaRec(no->esq, nome);
    }
}

/*
 *   Funções de Jogador
 */
// Inicializa o jogador
void initJogador(Jogador *j)
{
    j->id = -1;
    strcpy(j->nome, "nao informado");
    j->altura = -1;
    j->peso = -1;
    strcpy(j->universidade, "nao informado");
    j->anoNascimento = -1;
    strcpy(j->cidadeNascimento, "nao informado");
    strcpy(j->estadoNascimento, "nao informado");
}

// Adiciona jogadores
void addJogadores()
{
    char id[5] = {0};
    fgets(id, sizeof(id), stdin);
    bool isFim = false;
    if (strcmp(id, "FIM\n") == 0)
        isFim = true;
    while (!isFim)
    {
        int idNum = atoi(id);
        if (idNum >= 0 && idNum <= MAX_ID)
        {
            Jogador jogador;
            initJogador(&jogador);
            salvaPorId(&jogador, idNum);
            inserindo(jogador);
        }
        fgets(id, sizeof(id), stdin);
        if (strcmp(id, "FIM\n") == 0)
            isFim = true;
    }
    fflush(stdin);
}

// Metodo que trata virgulas duplas e substitui por "nao informado"
void tratamentoVirgula(char *linha, char *nova)
{
    char *i = linha;
    char *j = nova;
    while (*i != '\0')
    { // passa caractere por caractere
        *j = *i;
        if (*i == ',' && *(i + 1) == ',')
        { // se encontrar ,, substitui por "nao informado,"
            j++;
            *j++ = 'n';
            *j++ = 'a';
            *j++ = 'o';
            *j++ = ' ';
            *j++ = 'i';
            *j++ = 'n';
            *j++ = 'f';
            *j++ = 'o';
            *j++ = 'r';
            *j++ = 'm';
            *j++ = 'a';
            *j++ = 'd';
            *j++ = 'o';
            *j = ',';
            i++;
        }
        i++;
        j++;
    }
    *j = '\0';
}

// Salva jogador por id, a partir do arquivo csv
void salvaPorId(Jogador *j, int id)
{
    char path[] = "/tmp/players.csv";
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

    while (fgets(linha, sizeof(linha), file) && !encontrou)
    { // le cada linha do arquivo
        if (header)
        {
            header = false;
        }
        else
        {
            strcpy(tmp, linha);
            char *idAtual = strtok(tmp, dif);
            if (atoi(idAtual) == id)
            {
                tratamentoVirgula(linha, nova);
                setJogador(j, nova);
                encontrou = true;
            }
        }
    }

    fclose(file);
}

// Remove caracteres especiais
void removeCaracteresEspeciais(char *str)
{
    int i, j = 0;
    char tmp[strlen(str) + 1];

    for (i = 0; str[i] != '\0'; i++)
    {
        if (str[i] != '"' && str[i] != ',' && str[i] != '\n')
        {
            tmp[j++] = str[i];
        }
    }
    tmp[j] = '\0';
    strcpy(str, tmp);
}

// Preenche o jogador com os dados da linha
void setJogador(Jogador *j, char *linha)
{
    char *token = strtok(linha, ",");
    int count = 0;
    while (token != NULL)
    {
        removeCaracteresEspeciais(token);
        if (strcmp(token, "") == 0)
        {
            token = strtok(NULL, ",");
        }
        else
        {
            switch (count)
            {
            case 0:
                j->id = atoi(token);
                break;
            case 1:
                strcpy(j->nome, token);
                break;
            case 2:
                j->altura = atoi(token);
                break;
            case 3:
                j->peso = atoi(token);
                break;
            case 4:
                strcpy(j->universidade, token);
                break;
            case 5:
                j->anoNascimento = atoi(token);
                break;
            case 6:
                strcpy(j->cidadeNascimento, token);
                break;
            case 7:
                strcpy(j->estadoNascimento, token);
                break;
            }
            token = strtok(NULL, ",");
            count++;
        }
    }
}

void registroLog(double tempo)
{
    FILE *file;
    file = fopen("/tmp/806454_avl.txt", "w");
    if (file == NULL)
    {
        perror("Erro ao abrir o arquivo.\n");
        return;
    }
    fprintf(file, "Matricula: 806454\tComparacoes: %d\tTempo de execucao: %ld milisegundos", comparacoes, tempo);
    fclose(file);
}

int main() {
    addJogadores();
    char str[100];
    fgets(str, sizeof(str), stdin);
    str[strcspn(str, "\n")] = '\0';
    while (strcmp(str, "FIM") != 0) {
        start = clock();
        busca(str);
        end = clock();
        tempo = ((double)end - start) / CLOCKS_PER_SEC;
        fgets(str, sizeof(str), stdin);
        str[strcspn(str, "\n")] = '\0';
    }
    registroLog(tempo);
}