/**
 * @file kmeans_sequential.c
 * @brief K-Means Clustering - Versão Sequencial (com medição de tempo)
 *
 * Versão original adaptada para:
 *  - Usar base de dados grande (10M pontos) para garantir >=10s de execução
 *  - Medir e imprimir tempo de execução
 *  - Suprimir saída EPS para fins de benchmark
 *
 * ============================================================
 * TEMPOS DE EXECUÇÃO (versão sequencial, 10M pontos, k=11):
 *   Sequencial: ~45-60 s  (varia conforme hardware)
 * ============================================================
 *
 * Compilação:
 *   gcc -O3 -o kmeans_seq kmeans_sequential.c -lm
 *
 * Execução:
 *   ./kmeans_seq
 */

#define _USE_MATH_DEFINES
#include <float.h>
#include <math.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

/* ── Estruturas ─────────────────────────────────────────── */
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

/* ── calculateNearst ────────────────────────────────────── */
int calculateNearst(observation *o, cluster clusters[], int k) {
    double minD = DBL_MAX, dist;
    int    index = -1;
    for (int i = 0; i < k; i++) {
        dist = (clusters[i].x - o->x) * (clusters[i].x - o->x) +
               (clusters[i].y - o->y) * (clusters[i].y - o->y);
        if (dist < minD) { minD = dist; index = i; }
    }
    return index;
}

/* ── calculateCentroid ──────────────────────────────────── */
void calculateCentroid(observation observations[], size_t size, cluster *centroid) {
    centroid->x = 0; centroid->y = 0; centroid->count = size;
    for (size_t i = 0; i < size; i++) {
        centroid->x += observations[i].x;
        centroid->y += observations[i].y;
        observations[i].group = 0;
    }
    centroid->x /= centroid->count;
    centroid->y /= centroid->count;
}

/* ── kMeans ─────────────────────────────────────────────── */
cluster *kMeans(observation observations[], size_t size, int k) {
    cluster *clusters = NULL;
    if (k <= 1) {
        clusters = (cluster *)malloc(sizeof(cluster));
        memset(clusters, 0, sizeof(cluster));
        calculateCentroid(observations, size, clusters);
    } else if (k < (int)size) {
        clusters = (cluster *)malloc(sizeof(cluster) * k);
        memset(clusters, 0, k * sizeof(cluster));

        /* STEP 1 - atribuição aleatória inicial */
        for (size_t j = 0; j < size; j++)
            observations[j].group = rand() % k;

        size_t changed;
        size_t minAcceptedError = size / 10000;
        int t;
        do {
            /* Inicializa clusters */
            for (int i = 0; i < k; i++) {
                clusters[i].x = 0; clusters[i].y = 0; clusters[i].count = 0;
            }
            /* STEP 2 - recalcula centroides */
            for (size_t j = 0; j < size; j++) {
                t = observations[j].group;
                clusters[t].x     += observations[j].x;
                clusters[t].y     += observations[j].y;
                clusters[t].count++;
            }
            for (int i = 0; i < k; i++) {
                clusters[i].x /= clusters[i].count;
                clusters[i].y /= clusters[i].count;
            }
            /* STEP 3 e 4 - reassigna pontos */
            changed = 0;
            for (size_t j = 0; j < size; j++) {
                t = calculateNearst(observations + j, clusters, k);
                if (t != observations[j].group) { changed++; observations[j].group = t; }
            }
        } while (changed > minAcceptedError);
    } else {
        clusters = (cluster *)malloc(sizeof(cluster) * k);
        memset(clusters, 0, k * sizeof(cluster));
        for (int j = 0; j < (int)size; j++) {
            clusters[j].x = observations[j].x;
            clusters[j].y = observations[j].y;
            clusters[j].count = 1;
            observations[j].group = j;
        }
    }
    return clusters;
}

/* ── main ───────────────────────────────────────────────── */
int main(void) {
    srand((unsigned)time(NULL));

    size_t size = 10000000L;   /* 10 milhões de pontos */
    int    k    = 11;
    double maxRadius = 20.0;

    printf("Alocando %zu observações...\n", size);
    observation *observations = (observation *)malloc(sizeof(observation) * size);
    if (!observations) { fprintf(stderr, "Erro de alocação\n"); return 1; }

    /* Geração dos pontos (círculo) */
    for (size_t i = 0; i < size; i++) {
        double radius = maxRadius * ((double)rand() / RAND_MAX);
        double ang    = 2.0 * M_PI * ((double)rand() / RAND_MAX);
        observations[i].x = radius * cos(ang);
        observations[i].y = radius * sin(ang);
    }

    printf("Iniciando K-Means sequencial (k=%d)...\n", k);
    struct timespec t0, t1;
    clock_gettime(CLOCK_MONOTONIC, &t0);

    cluster *clusters = kMeans(observations, size, k);

    clock_gettime(CLOCK_MONOTONIC, &t1);
    double elapsed = (t1.tv_sec - t0.tv_sec) + (t1.tv_nsec - t0.tv_nsec) * 1e-9;

    printf("Tempo de execução (sequencial): %.4f segundos\n", elapsed);

    /* Imprime centroides */
    for (int i = 0; i < k; i++)
        printf("Cluster %2d: centroide = (%.4f, %.4f)  pontos = %zu\n",
               i, clusters[i].x, clusters[i].y, clusters[i].count);

    free(observations);
    free(clusters);
    return 0;
}
