/*
*   806454 - Yago Almeida Melo
*   TP04 - Q09 / Hashing Indireto com Lista Encadeada
*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>

// atributos globais
#define MAX_LINE_LENGTH 200
#define MAX_ID 3921
#define TAMANHO 25
#define NULO -1
int comparacoes = 0;

typedef struct No {
    Jogador elemento;
    struct No *prox;
} No;

typedef struct Lista {
    No *inicio;
} Lista;

typedef struct Hash {
    Lista tabela[TAMANHO];
} Hash;

// Struct Jogador
typedef struct
{
    int id, altura, peso, anoNascimento;
    char nome[100], universidade[100], cidadeNascimento[100], estadoNascimento[100];
} Jogador;

int h(Jogador elemento) {
    return elemento.altura % TAMANHO;
}

void inicializarTabela(Hash hash) {
    for (int i = 0; i < TAMANHO; i++) {
        hash.tabela[i].inicio = NULL;
    }
}

int pesquisar(Hash *hash, Jogador elemento) {
    int pos = h(elemento);
    No *atual = hash->tabela[pos].inicio;

    while (atual != NULL) {
        if (strcmp(atual->elemento.nome, elemento.nome) == 0) {
            return 1; // Encontrou o elemento na lista
        }
        atual = atual->prox;
    }

    return 0; // Elemento não encontrado
}

void inserirInicio(Lista *lista, Jogador elemento) {
    No* aux = lista->inicio;
    while(aux == NULL){
        aux = aux->prox;
    }
    aux->prox = novoNo(elemento);
}

void inserir(Hash hash, Jogador elemento) {
    int pos = h(elemento);
    inserirInicio(&(hash.tabela[pos]), elemento);
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
void addJogadores(Hash hash)
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
            inserir(hash, jogador);
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
// Funcao para criar uma nova celula
No *novoNo(Jogador elemento) {
    No *nova = (No *)malloc(sizeof(No));
    nova->elemento = elemento;
    nova->prox = NULL;
    return nova;
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
    Hash hash;
    inicializarTabela(hash);
    addJogadores(hash);
    char nome[100];
    fgets(nome, sizeof(nome), stdin);
    while (strcmp(nome, "FIM\n") != 0) {
        pesquisar(&hash, nome);
        fgets(nome, sizeof(nome), stdin);
    }
    return 0;
}
