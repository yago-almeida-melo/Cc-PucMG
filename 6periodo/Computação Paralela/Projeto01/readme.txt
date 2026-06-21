================================================================================
  K-MEANS CLUSTERING - VERSÕES SEQUENCIAL E PARALELAS (OpenMP + CUDA)
================================================================================

GRUPO: André Luís Silva, Ricardo Soares, Yago Almeida
DATA : 17/06/2026

────────────────────────────────────────────────────────────────────────────────
1. DESCRIÇÃO DA APLICAÇÃO
────────────────────────────────────────────────────────────────────────────────

O K-Means é um algoritmo de aprendizado de máquina não supervisionado que
agrupa N pontos em k clusters. O algoritmo funciona em três passos iterativos:

  1. Atribui cada ponto ao centroide mais próximo.
  2. Recalcula o centroide de cada cluster como a média de todos os pontos
     atribuídos a ele.
  3. Repete os passos 1 e 2 até que menos de 0,01% dos pontos mudem de
     cluster em uma iteração (critério de convergência utilizado).

Os dados de entrada gerados sinteticamente são 10 milhões de pontos 2D
distribuídos aleatoriamente dentro de um círculo de raio 20 centrado na
origem. O número de clusters utilizado nos experimentos é k=11.

────────────────────────────────────────────────────────────────────────────────
2. ARQUIVOS DO PROJETO
────────────────────────────────────────────────────────────────────────────────

  kmeans_sequential.c    - Versão sequencial original (com medição de tempo)
  kmeans_openmp_cpu.c    - Versão paralela OpenMP para CPU multicore
  kmeans_openmp_gpu.c    - Versão paralela OpenMP Target para GPU (offloading)
  kmeans_cuda.cu         - Versão paralela CUDA para GPU Nvidia
  readme.txt             - Este arquivo

────────────────────────────────────────────────────────────────────────────────
3. COMPILAÇÃO
────────────────────────────────────────────────────────────────────────────────

Requisitos:
  - GCC ≥ 7  (para versões sequencial e OpenMP CPU)
  - GCC ≥ 10 com suporte offload nvptx  (para OpenMP GPU)
    ou Clang/LLVM com plugin NVPTX
  - CUDA Toolkit ≥ 10  (para versão CUDA, nvcc)
  - GPU Nvidia com compute capability ≥ 6.0 (para atomicAdd em double)

─── Versão Sequencial ───────────────────────────────────────────────────────
  gcc -O2 -o kmeans_seq kmeans_sequential.c -lm

─── Versão OpenMP CPU ───────────────────────────────────────────────────────
  gcc -O2 -fopenmp -o kmeans_omp_cpu kmeans_openmp_cpu.c -lm

─── Versão OpenMP GPU (GCC com nvptx) ───────────────────────────────────────
  gcc -O2 -fopenmp -foffload=nvptx-none \
      -o kmeans_omp_gpu kmeans_openmp_gpu.c -lm

  ou com Clang:
  clang -O2 -fopenmp -fopenmp-targets=nvptx64-nvidia-cuda \
        -o kmeans_omp_gpu kmeans_openmp_gpu.c -lm

─── Versão CUDA ─────────────────────────────────────────────────────────────
  nvcc -O2 -arch=sm_75 -o kmeans_cuda kmeans_cuda.cu -lm

  Ajuste -arch conforme a geração da GPU:
    sm_60  → GTX 10xx / Tesla P-series
    sm_70  → V100 / Titan V
    sm_75  → RTX 20xx / Quadro RTX
    sm_80  → A100 / RTX 30xx
    sm_89  → RTX 40xx / L40S

────────────────────────────────────────────────────────────────────────────────
4. EXECUÇÃO
────────────────────────────────────────────────────────────────────────────────

─── Sequencial ──────────────────────────────────────────────────────────────
  ./kmeans_seq

─── OpenMP CPU (definir número de threads via variável de ambiente) ──────────
  OMP_NUM_THREADS=1  ./kmeans_omp_cpu
  OMP_NUM_THREADS=2  ./kmeans_omp_cpu
  OMP_NUM_THREADS=4  ./kmeans_omp_cpu
  OMP_NUM_THREADS=8  ./kmeans_omp_cpu
  OMP_NUM_THREADS=16 ./kmeans_omp_cpu
  OMP_NUM_THREADS=32 ./kmeans_omp_cpu

─── OpenMP GPU ──────────────────────────────────────────────────────────────
  ./kmeans_omp_gpu

─── CUDA ────────────────────────────────────────────────────────────────────
  ./kmeans_cuda

────────────────────────────────────────────────────────────────────────────────
5. TEMPOS DE EXECUÇÃO E SPEEDUP
────────────────────────────────────────────────────────────────────────────────

Parâmetros do experimento:
  N = 10.000.000 pontos  |  k = 11 clusters  |  raio = 20.0

Hardware de referência (preencher com o ambiente real utilizado):
  CPU: _______________________ (ex: Intel Xeon Gold 6154 @ 3.0 GHz, 18 cores)
  GPU: _______________________ (ex: Nvidia RTX 3080, compute capability 8.6)
  RAM: _______________________ (ex: 64 GB DDR4)

┌─────────────────────────────────┬──────────────┬───────────┐
│ Versão / Configuração           │ Tempo (s)    │ Speedup   │
├─────────────────────────────────┼──────────────┼───────────┤
│ Sequencial                      │    ____.__   │   1.00x   │
│ OpenMP CPU  1 thread            │    ____.__   │   ___._x  │
│ OpenMP CPU  2 threads           │    ____.__   │   ___._x  │
│ OpenMP CPU  4 threads           │    ____.__   │   ___._x  │
│ OpenMP CPU  8 threads           │    ____.__   │   ___._x  │
│ OpenMP CPU 16 threads           │    ____.__   │   ___._x  │
│ OpenMP CPU 32 threads           │    ____.__   │   ___._x  │
│ OpenMP GPU  (offload)           │    ____.__   │   ___._x  │
│ CUDA GPU    (BLOCK_SIZE=256)    │    ____.__   │   ___._x  │
└─────────────────────────────────┴──────────────┴───────────┘

Instruções para preencher a tabela:
  Speedup = Tempo_Sequencial / Tempo_Versão_Paralela

Valores estimados para hardware típico (A100 / Xeon 32-core):
  Sequencial       :  ~52 s
  OpenMP 32 threads:   ~4 s   (speedup ~13x)
  OpenMP GPU       :   ~3 s   (speedup ~17x)
  CUDA             :  ~1.8 s  (speedup ~29x)

────────────────────────────────────────────────────────────────────────────────
6. MUDANÇAS REALIZADAS PARA A PARALELIZAÇÃO
────────────────────────────────────────────────────────────────────────────────

--- OpenMP CPU (kmeans_openmp_cpu.c) ---

  a) STEP 1 (inicialização aleatória):
       #pragma omp parallel for schedule(static)
     Distribui a atribuição aleatória entre as threads.

  b) STEP 2 (acumulação de centroides):
     Em vez de usar #pragma omp critical (lento), foi criado um array
     temporário local_x/local_y/local_count com uma linha por thread.
     Cada thread acumula em sua linha sem conflito, e ao fim faz-se a
     redução serial (O(threads×k), muito pequeno).

  c) STEP 3/4 (reassignação):
       #pragma omp parallel for reduction(+:changed)
     O contador de mudanças é reduzido automaticamente pelo OpenMP.

  d) Geração dos pontos:
       #pragma omp parallel for schedule(static)
     Paralelização da etapa de criação da base de dados.

--- OpenMP GPU (kmeans_openmp_gpu.c) ---

  a) Dados são copiados para o dispositivo com #pragma omp target data map(...)
     e permanecem lá durante todo o loop, minimizando transferências PCIe.

  b) O loop de acumulação usa:
       #pragma omp target teams distribute parallel for reduction(+:...)
       - target        : executa no dispositivo
       - teams         : cria múltiplas equipes (≈ blocos CUDA)
       - distribute    : divide iterações entre equipes
       - parallel for  : divide dentro de cada equipe (warps/threads)
       - reduction     : redução por cluster sem race conditions

  c) A reassignação usa o mesmo padrão com reduction(+:changed).

--- CUDA (kmeans_cuda.cu) ---

  Três kernels implementados:

  1. accumulateCentroids<<<N/256, 256>>>
     Cada thread acumula seu ponto nos centróides usando atomicAdd.

  2. updateCentroids<<<k/256, 256>>>
     Divide a soma acumulada pela contagem para obter o novo centroide.
     Kernel leve (apenas k threads).

  3. assignLabels<<<N/256, 256>>>
     Cada thread busca o centroide mais próximo para seu ponto e usa
     atomicAdd para contar mudanças de forma livre de race conditions.

  A medição de tempo usa cudaEvent (inclui apenas kernels, sem alocação).

────────────────────────────────────────────────────────────────────────────────
7. OBSERVAÇÕES
────────────────────────────────────────────────────────────────────────────────

  * A versão CUDA requer GPU com compute capability ≥ 6.0 para suportar
    atomicAdd em double. Em GPUs mais antigas, substitua double por float.

  * O suporte a OpenMP GPU offloading depende do compilador e da instalação.
    Com GCC padrão de distribuição Linux, o offload pode não estar disponível;
    nesse caso use o Clang com o plugin NVPTX ou compile o GCC com suporte a
    offload habilitado.

  * rand() não é thread-safe. Na geração paralela de pontos, os resultados
    exatos podem diferir entre execuções, mas a distribuição estatística é
    preservada. Para reprodutibilidade total, use rand_r() ou drand48_r().

  * O critério de parada (0.01% de pontos mudando) foi mantido igual ao
    código original para garantir resultados equivalentes entre as versões.

================================================================================
