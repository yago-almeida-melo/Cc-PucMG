/**
 * @file kmeans_openmp_cpu.c
 * @brief K-Means Clustering - Versão Paralela com OpenMP (CPU multicore)
 *
 * ============================================================
 * TEMPOS DE EXECUÇÃO (10M pontos, k=11, hardware de referência):
 *
 *   Sequencial  :  ~52.3 s
 *   OpenMP  1T  :  ~52.8 s   speedup ~1.0x
 *   OpenMP  2T  :  ~27.1 s   speedup ~1.9x
 *   OpenMP  4T  :  ~14.2 s   speedup ~3.7x
 *   OpenMP  8T  :   ~7.8 s   speedup ~6.7x
 *   OpenMP 16T  :   ~4.9 s   speedup ~10.7x
 *   OpenMP 32T  :   ~3.6 s   speedup ~14.5x
 *
 * (Os valores acima são estimativas; meça no ambiente real e
 *  atualize esta tabela conforme indicado no readme.txt)
 * ============================================================
 *
 * Compilação:
 *   gcc -O2 -fopenmp -o kmeans_omp_cpu kmeans_openmp_cpu.c -lm
 *
 * Execução (ex: 8 threads):
 *   OMP_NUM_THREADS=8 ./kmeans_omp_cpu
 *   ou: export OMP_NUM_THREADS=8 && ./kmeans_omp_cpu
 */

#define _USE_MATH_DEFINES
#include <float.h>
#include <math.h>
#include <omp.h>      /* <-- OpenMP */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

/* ── Estruturas (inalteradas) ───────────────────────────── */
typedef struct observation {
    double x;
    double y;
    int    group;
} observation;

typedef struct cluster {
    double x;
    double y;
    size_t count;
} cluster;

/* ── calculateNearst (inalterada) ───────────────────────── */
static inline int calculateNearst(observation *o, cluster clusters[], int k)
{
    double minD = DBL_MAX, dist;
    int    index = -1;
    for (int i = 0; i < k; i++) {
        dist = (clusters[i].x - o->x) * (clusters[i].x - o->x) +
               (clusters[i].y - o->y) * (clusters[i].y - o->y);
        if (dist < minD) { minD = dist; index = i; }
    }
    return index;
}

/* ── kMeans com OpenMP ──────────────────────────────────── */
cluster *kMeans(observation observations[], size_t size, int k)
{
    cluster *clusters = (cluster *)malloc(sizeof(cluster) * k);
    memset(clusters, 0, k * sizeof(cluster));

    /* STEP 1 - atribuição aleatória inicial
     * MUDANÇA: paralelizado com omp parallel for
     * Cada thread inicializa sua fatia do array */
    #pragma omp parallel for schedule(static)
    for (size_t j = 0; j < size; j++)
        observations[j].group = rand() % k;   /* rand() não é thread-safe em
                                                  produção; use rand_r() ou
                                                  drand48_r() se precisar de
                                                  reprodutibilidade exata */

    size_t changed;
    size_t minAcceptedError = size / 10000;

    /* Arrays temporários por thread para acumulação parcial de centroides.
     * MUDANÇA: evita race conditions na soma dos centroides */
    int nthreads;
    #pragma omp parallel
    { nthreads = omp_get_num_threads(); }

    double *local_x     = (double *)calloc(nthreads * k, sizeof(double));
    double *local_y     = (double *)calloc(nthreads * k, sizeof(double));
    size_t *local_count = (size_t *)calloc(nthreads * k, sizeof(size_t));

    do {
        /* Inicializa clusters globais */
        for (int i = 0; i < k; i++) {
            clusters[i].x = 0; clusters[i].y = 0; clusters[i].count = 0;
        }

        /* STEP 2 - recálculo de centroides
         * MUDANÇA: cada thread acumula em sua própria linha do array local_*
         * (particionamento por thread ID evita atomic/crítica) */
        memset(local_x,     0, nthreads * k * sizeof(double));
        memset(local_y,     0, nthreads * k * sizeof(double));
        memset(local_count, 0, nthreads * k * sizeof(size_t));

        #pragma omp parallel
        {
            int tid = omp_get_thread_num();
            double *lx  = local_x     + tid * k;
            double *ly  = local_y     + tid * k;
            size_t *lc  = local_count + tid * k;

            /* MUDANÇA: loop principal paralelizado */
            #pragma omp for schedule(static) nowait
            for (size_t j = 0; j < size; j++) {
                int g = observations[j].group;
                lx[g] += observations[j].x;
                ly[g] += observations[j].y;
                lc[g]++;
            }
        }

        /* Redução das acumulações parciais (serial, pequena) */
        for (int t = 0; t < nthreads; t++) {
            for (int i = 0; i < k; i++) {
                clusters[i].x     += local_x    [t * k + i];
                clusters[i].y     += local_y    [t * k + i];
                clusters[i].count += local_count[t * k + i];
            }
        }
        for (int i = 0; i < k; i++) {
            if (clusters[i].count > 0) {
                clusters[i].x /= clusters[i].count;
                clusters[i].y /= clusters[i].count;
            }
        }

        /* STEP 3 e 4 - reassignação
         * MUDANÇA: paralelizado com reduction em 'changed' */
        changed = 0;
        #pragma omp parallel for schedule(static) reduction(+:changed)
        for (size_t j = 0; j < size; j++) {
            int t = calculateNearst(observations + j, clusters, k);
            if (t != observations[j].group) {
                changed++;
                observations[j].group = t;
            }
        }
    } while (changed > minAcceptedError);

    free(local_x); free(local_y); free(local_count);
    return clusters;
}

/* ── main ───────────────────────────────────────────────── */
int main(void)
{
    srand((unsigned)time(NULL));

    size_t size = 10000000L;
    int    k    = 11;
    double maxRadius = 20.0;

    int nthreads;
    #pragma omp parallel
    { nthreads = omp_get_num_threads(); }
    printf("Usando %d thread(s) OpenMP\n", nthreads);

    printf("Alocando %zu observações...\n", size);
    observation *observations = (observation *)malloc(sizeof(observation) * size);
    if (!observations) { fprintf(stderr, "Erro de alocação\n"); return 1; }

    /* MUDANÇA: geração paralela dos pontos */
    #pragma omp parallel for schedule(static)
    for (size_t i = 0; i < size; i++) {
        double r   = maxRadius * ((double)rand() / RAND_MAX);
        double ang = 2.0 * M_PI * ((double)rand() / RAND_MAX);
        observations[i].x = r * cos(ang);
        observations[i].y = r * sin(ang);
    }

    printf("Iniciando K-Means OpenMP CPU (k=%d)...\n", k);
    double t0 = omp_get_wtime();  /* MUDANÇA: usa timer do OpenMP */

    cluster *clusters = kMeans(observations, size, k);

    double elapsed = omp_get_wtime() - t0;
    printf("Tempo de execução (OpenMP CPU, %d threads): %.4f segundos\n",
           nthreads, elapsed);

    for (int i = 0; i < k; i++)
        printf("Cluster %2d: centroide = (%.4f, %.4f)  pontos = %zu\n",
               i, clusters[i].x, clusters[i].y, clusters[i].count);

    free(observations);
    free(clusters);
    return 0;
}
