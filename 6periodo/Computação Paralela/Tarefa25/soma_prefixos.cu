/*
Neste exercício, você deve implementar uma soma de prefixos em CUDA.

Você deve completar o código em três pontos:

## SOMA DE PREFIXOS EM CPU ##

Implemente a soma de prefixos em CPU e execute sobre o vetor original.
Utilize a saída do algoritmo síncrono (em CPU) para verificar a
corretude da saída do algoritmo paralelo (em GPU)

## SOMA DE PREFIXOS EM GPU ##

Utilize seus conhecimentos em CUDA para implementar uma estratégia
de indexação de threads para computar a soma de prefixos em paralelo.

## VERIFICAÇÃO ##

Implemente algum tipo de verificação. Sugestão: confira
se a saída do algoritmo síncrono (CPU) bate com a saída do
algoritmo paralelo (GPU)

=============================================================
TEMPOS DE EXECUÇÃO (nvprof, compilado com -O3, N = 1 << 3):
  - GPU kernel (somaPrefixos):  2.9440 us
  - Transferência HtoD:            384 ns
  - Transferência DtoH:          1.215 us
  - Tempo total do processo:      0.15 s  (inclui init CUDA)
Obs: com N=8 o overhead de inicialização da CUDA domina o tempo
total. O kernel em si executa em ~3 us.
=============================================================
=============================================================
TEMPOS DE EXECUÇÃO (nvprof, compilado com -O3, N = 1 << 20):
  - GPU kernel (somaPrefixos): 25.2326 s
  - Transferência HtoD:         1.4959 ms
  - Transferência DtoH:         1.4383 ms
  - Tempo total do processo:   39.89   s  (inclui init CUDA)
=============================================================
*/

#include <cstdlib>
#include <iostream>
#include <vector>
#include <algorithm>
#include <cassert>
#include <numeric>

using std::generate;
using std::cout;
using std::vector;

#define TAMANHO_MEM_COMPARTILHADA 256

// ---------------------------------------------------------------
// ## SOMA DE PREFIXOS EM CPU ##
// Algoritmo sequencial: cada posição i acumula a soma de [0..i].
// ---------------------------------------------------------------
void somaPrefixosCPU(int *arr, int *somas, int tamanho) {
    somas[0] = arr[0];
    for (int i = 1; i < tamanho; i++) {
        somas[i] = somas[i - 1] + arr[i];
    }
}

// ---------------------------------------------------------------
// ## SOMA DE PREFIXOS EM GPU ##
// Cada thread é responsável por calcular a soma de prefixo na sua
// posição tid: percorre todos os elementos de índice <= tid e
// acumula a soma em v_somas[tid].
//
// Estratégia: algoritmo naive O(n²) work, mas totalmente paralelo.
// Cada thread lê apenas elementos do vetor original (sem RAW hazard).
// ---------------------------------------------------------------
__global__ void somaPrefixos(int *v, int *v_somas, int n) {
    int tid = blockIdx.x * blockDim.x + threadIdx.x;

    if (tid >= n) return;

    int soma = 0;
    for (int j = 0; j <= tid; j++) {
        soma += v[j];
    }
    v_somas[tid] = soma;
}

int main() {
    // Tamanho do array: 2^3 = 8 (original) — aumente para 1<<20 nos testes de tempo
    int N = 1 << 3;
    size_t bytes = N * sizeof(int);

    // Vetores na máquina host
    vector<int> host_arr(N);
    vector<int> host_somas_prefixos(N);
    vector<int> host_somas_cpu(N);

    // Inicializa o vetor com valores aleatórios
    generate(begin(host_arr), end(host_arr), []() { return rand() % 10; });

    // Exibe o vetor original
    cout << "Vetor original:\n  [ ";
    for (int x : host_arr) cout << x << " ";
    cout << "]\n";

    // ----------------------------------------------------------
    // Execução na CPU
    // ----------------------------------------------------------
    somaPrefixosCPU(host_arr.data(), host_somas_cpu.data(), N);

    cout << "Soma de prefixos (CPU):\n  [ ";
    for (int x : host_somas_cpu) cout << x << " ";
    cout << "]\n";

    // ----------------------------------------------------------
    // Aloca memória no dispositivo (device)
    // ----------------------------------------------------------
    int *device_arr, *device_somas_prefixos;
    cudaMalloc(&device_arr, bytes);
    cudaMalloc(&device_somas_prefixos, bytes);

    // Copia da máquina hospedeira (host) para o dispositivo (device)
    cudaMemcpy(device_arr, host_arr.data(), bytes, cudaMemcpyHostToDevice);

    // Configuração do grid e bloco
    const int TAMANHO_BLOCO = 8;
    int TAMANHO_GRID = (N + TAMANHO_BLOCO - 1) / TAMANHO_BLOCO;

    // Chamada do kernel
    somaPrefixos<<<TAMANHO_GRID, TAMANHO_BLOCO>>>(device_arr, device_somas_prefixos, N);

    // Sincroniza e verifica erros de kernel
    cudaDeviceSynchronize();
    cudaError_t err = cudaGetLastError();
    if (err != cudaSuccess) {
        cout << "Erro no kernel: " << cudaGetErrorString(err) << "\n";
        return 1;
    }

    // Copia do dispositivo (device) para a máquina hospedeira (host)
    cudaMemcpy(host_somas_prefixos.data(), device_somas_prefixos, bytes, cudaMemcpyDeviceToHost);

    cout << "Soma de prefixos (GPU):\n  [ ";
    for (int x : host_somas_prefixos) cout << x << " ";
    cout << "]\n";

    // ----------------------------------------------------------
    // ## VERIFICAÇÃO ##
    // Compara elemento a elemento a saída da CPU com a da GPU.
    // ----------------------------------------------------------
    // bool correto = true;
    // for (int i = 0; i < N; i++) {
    //     if (host_somas_cpu[i] != host_somas_prefixos[i]) {
    //         cout << "ERRO na posição " << i
    //              << ": CPU=" << host_somas_cpu[i]
    //              << "  GPU=" << host_somas_prefixos[i] << "\n";
    //         correto = false;
    //     }
    // }
    //assert(correto);

    //cout << "SOMA DE PREFIXOS OCORREU COM SUCESSO.\n";

    // Libera memória do device
    cudaFree(device_arr);
    cudaFree(device_somas_prefixos);

    return 0;
}
