#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

// Função para dilatar (pegar o máximo local em uma vizinhança)
vector<int> dilatar(const vector<int>& v, int tamanho = 6) {
    int n = v.size();
    vector<int> res(n);
    int raio = tamanho / 2;

    for (int i = 0; i < n; i++) {
        int maximo = v[i];
        for (int j = max(0, i - raio); j <= min(n - 1, i + raio); j++) {
            maximo = max(maximo, v[j]);
        }
        res[i] = maximo;
    }
    return res;
}

// Função principal — reconstrução por dilatação simulada
int encontrarPico(const vector<int>& sinal, int reducao = 1) {
    int n = sinal.size();
    vector<int> marcador(n);

    // marcador inicial (reduzido)
    for (int i = 0; i < n; i++)
        marcador[i] = max(sinal[i] - reducao, 0);

    bool mudou = true;
    while (mudou) {
        mudou = false;
        vector<int> dilatado = dilatar(marcador);
        for (int i = 0; i < n; i++) {
            int novo_valor = min(dilatado[i], sinal[i]);
            if (novo_valor != marcador[i]) {
                marcador[i] = novo_valor;
                mudou = true;
            }
        }
    }

    // encontra o valor máximo (pico principal)
    return *max_element(marcador.begin(), marcador.end());
}

int main() {
    vector<int> sinal = {2, 5, 4, 7, 6, 9, 8, 5, 3, 6, 4, 2};

    int pico = encontrarPico(sinal);
    cout << "Valor do maior pico: " << pico << endl;

    return 0;
}
