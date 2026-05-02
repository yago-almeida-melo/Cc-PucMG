#include <stdio.h>

int v[100];
int idx[100];

int main() {
    int i;

    /*
     * MOTIVO 1 - Dependencia de dados entre iteracoes:
     *   v[i] depende de v[i-1], criando uma cadeia de dependencias
     *   que impede o compilador de executar iteracoes em paralelo.
     *
     * devec.c:20:17: note: not vectorized, possible dependence between
     *   data-refs v[_1] and v[i_17]
     */
    for (i = 1; i < 50; i++) {
        v[i] = v[i - 1] + 1;
    }

    /*
     * MOTIVO 2 - Fluxo de controle nao uniforme (break dentro do laco):
     *   O break cria uma segunda saida do laco alem da condicao i < 100,
     *   violando o requisito de "apenas uma entrada e uma saida".
     *
     * devec.c:35:5: note: not vectorized: control flow in loop.
     */
    for (i = 0; i < 100; i++) {
        v[i] = v[i] * 2;
        if (v[i] > 500000)
            break;
    }

    /*
     * MOTIVO 3 - Acesso indireto com possivel aliasing (gather):
     *   O indice de acesso a v vem de outro array (idx), entao o compilador
     *   nao consegue garantir que v[idx[i]] e v[i] nao se sobrepoem
     *   na memoria, impossibilitando a vetorizacao.
     *
     * devec.c:50:17: note: possible alias involving gather/scatter
     *   between v[_1] and v[i_11]
     */
    for (i = 0; i < 100; i++) {
        v[i] = v[idx[i]] + 1;
    }

    return 0;
}