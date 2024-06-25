#include <stdio.h>
#include <stdlib.h>

#define MAX_PILHAS 1000
#define MAX_CAIXAS 1000

typedef struct {
    int tamanho;
    int caixas[MAX_CAIXAS];
} Pilha;

int main() {
    int N, P;
    Pilha pilhas[MAX_PILHAS];

    while (scanf("%d %d", &N, &P) && (N != 0 || P != 0)) {
        int i, j, pos_pilha_1 = -1, pos_caixa_1 = -1;

        // Ler as pilhas
        for (i = 0; i < P; i++) {
            scanf("%d", &pilhas[i].tamanho);
            for (j = 0; j < pilhas[i].tamanho; j++) {
                scanf("%d", &pilhas[i].caixas[j]);
                if (pilhas[i].caixas[j] == 1) {
                    pos_pilha_1 = i;
                    pos_caixa_1 = j;
                }
            }
        }

        // Contar o número de caixas a serem removidas
        int removidas = 0;

        // Verificar todas as pilhas para caixas que precisam ser removidas
        for (i = 0; i < P; i++) {
            // Se a pilha atual é a mesma que contém a caixa 1
            if (i == pos_pilha_1) {
                // Contar caixas acima da caixa 1 na mesma pilha
                removidas += pilhas[i].tamanho - pos_caixa_1 - 1;
            } else {
                // Verificar se a caixa no topo tem pelo menos um lado livre
                // Se a pilha tem apenas uma caixa, ela está automaticamente acessível
                if (pilhas[i].tamanho > 0) {
                    int topo_caixa = pilhas[i].caixas[pilhas[i].tamanho - 1];
                    // Verificar a adjacência
                    if ((i > 0 && pilhas[i - 1].tamanho == 0) || (i < P - 1 && pilhas[i + 1].tamanho == 0)) {
                        removidas++;
                    } else if (i == 0 || i == P - 1) {
                        removidas++;
                    }
                }
            }
        }

        printf("%d\n", removidas);
    }

    return 0;
}
