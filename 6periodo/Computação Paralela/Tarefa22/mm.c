/*
 * Sequencial (-O3):                            9.2304 s
 * Paralela Multicore (-O3 -fopenmp):           1.9326 s
 * GPU - distribute:                          170.5616 s   
 * GPU - distribute parallel for:              15.8194 s   
 * GPU - distribute parallel for simd:         15.8205 s  
 * 
 * A versão GPU apresentou desempenho inferior à CPU devido a dois fatores: 
 * (1) a diretiva distribute isolada não cria threads dentro dos teams, 
 * resultando em paralelismo mínimo (2560 warps); 
 * (2) o custo de transferência de ~96 MB via PCIe entre CPU e GPU (MX350)
 * supera o ganho computacional para esta carga de trabalho. 
 * A MX350 também possui throughput FP64 reduzido (1/32 do FP32), 
 * penalizando operações com double. A versão multicore obteve o melhor speedup (~4.8×)
 * com 4 núcleos físicos. 
 *
 * ============================================================
 * warps_launched           : 2560
 * warp_execution_efficiency: 100%
 *
 * ============================================================
 * COMO COMPILAR
 * ============================================================
 * Sequencial:
 *   gcc8 -O3 -o mm mm.c
 *
 * Multicore (descomente o pragma MULTICORE abaixo):
 *   gcc8 -O3 -fopenmp -o mm mm.c
 *
 * GPU distribute (descomente o pragma GPU_DISTRIBUTE):
 *   gcc8 -O3 -fopenmp -foffload=nvptx-none -o mm mm.c
 *
 * GPU distribute parallel for (descomente GPU_DIST_PAR_FOR):
 *   gcc8 -O3 -fopenmp -foffload=nvptx-none -o mm mm.c
 *
 * GPU distribute parallel for simd (descomente GPU_DIST_PAR_FOR_SIMD):
 *   gcc8 -O3 -fopenmp -foffload=nvptx-none -o mm mm.c
 */

#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <linux/time.h>

/* ------------------------------------------------------------------
 * Utilitário: tempo em segundos
 * ------------------------------------------------------------------ */
static double now_sec(void) {
    struct timespec ts;
    clock_gettime(CLOCK_MONOTONIC, &ts);
    return ts.tv_sec + ts.tv_nsec * 1e-9;
}

/* ------------------------------------------------------------------
 * Versão SEQUENCIAL
 * ------------------------------------------------------------------ */
void mm_seq(double *a, double *b, double *c, int width) {
    for (int i = 0; i < width; i++) {
        for (int j = 0; j < width; j++) {
            double sum = 0;
            for (int k = 0; k < width; k++) {
                sum += a[i * width + k] * b[k * width + j];
            }
            c[i * width + j] = sum;
        }
    }
}

/* ------------------------------------------------------------------
 * Versão PARALELA MULTICORE (OpenMP)
 * ------------------------------------------------------------------ */
void mm_multicore(double *a, double *b, double *c, int width) {
    #pragma omp parallel for collapse(2) schedule(static)
    for (int i = 0; i < width; i++) {
        for (int j = 0; j < width; j++) {
            double sum = 0;
            for (int k = 0; k < width; k++) {
                sum += a[i * width + k] * b[k * width + j];
            }
            c[i * width + j] = sum;
        }
    }
}

/* ------------------------------------------------------------------
 * Versão GPU - "distribute"
 * ------------------------------------------------------------------ */
void mm_gpu_distribute(double *a, double *b, double *c, int width) {
    #pragma omp target teams distribute \
            map(to: a[0:width*width], b[0:width*width]) \
            map(tofrom: c[0:width*width])
    for (int i = 0; i < width; i++) {
        for (int j = 0; j < width; j++) {
            double sum = 0;
            for (int k = 0; k < width; k++) {
                sum += a[i * width + k] * b[k * width + j];
            }
            c[i * width + j] = sum;
        }
    }
}

/* ------------------------------------------------------------------
 * Versão GPU - "distribute parallel for"
 * ------------------------------------------------------------------ */
void mm_gpu_dist_par_for(double *a, double *b, double *c, int width) {
    #pragma omp target teams distribute parallel for collapse(2) \
            map(to: a[0:width*width], b[0:width*width]) \
            map(tofrom: c[0:width*width])
    for (int i = 0; i < width; i++) {
        for (int j = 0; j < width; j++) {
            double sum = 0;
            for (int k = 0; k < width; k++) {
                sum += a[i * width + k] * b[k * width + j];
            }
            c[i * width + j] = sum;
        }
    }
}

/* ------------------------------------------------------------------
 * Versão GPU - "distribute parallel for simd"
 * ------------------------------------------------------------------ */
void mm_gpu_dist_par_for_simd(double *a, double *b, double *c, int width) {
    #pragma omp target teams distribute parallel for simd collapse(2) \
            map(to: a[0:width*width], b[0:width*width]) \
            map(tofrom: c[0:width*width])
    for (int i = 0; i < width; i++) {
        for (int j = 0; j < width; j++) {
            double sum = 0;
            for (int k = 0; k < width; k++) {
                sum += a[i * width + k] * b[k * width + j];
            }
            c[i * width + j] = sum;
        }
    }
}

/* ------------------------------------------------------------------
 * main
 * ------------------------------------------------------------------ */
int main(void) {
    int width = 2000;

    double *a = (double *) malloc(width * width * sizeof(double));
    double *b = (double *) malloc(width * width * sizeof(double));
    double *c = (double *) malloc(width * width * sizeof(double));

    for (int i = 0; i < width; i++) {
        for (int j = 0; j < width; j++) {
            a[i * width + j] = i;
            b[i * width + j] = j;
            c[i * width + j] = 0;
        }
    }

    double t0, t1;

    /* --- Sequencial --- */
    t0 = now_sec();
    mm_seq(a, b, c, width);
    t1 = now_sec();
    printf("Sequencial:                       %.4f s\n", t1 - t0);

    for (int i = 0; i < width * width; i++) c[i] = 0;

    /* --- Multicore --- */
    t0 = now_sec();
    mm_multicore(a, b, c, width);
    t1 = now_sec();
    printf("Paralela Multicore:               %.4f s\n", t1 - t0);

    for (int i = 0; i < width * width; i++) c[i] = 0;

    /* --- GPU distribute --- */
    t0 = now_sec();
    mm_gpu_distribute(a, b, c, width);
    t1 = now_sec();
    printf("GPU distribute:                   %.4f s\n", t1 - t0);

    for (int i = 0; i < width * width; i++) c[i] = 0;

    /* --- GPU distribute parallel for --- */
    t0 = now_sec();
    mm_gpu_dist_par_for(a, b, c, width);
    t1 = now_sec();
    printf("GPU distribute parallel for:      %.4f s\n", t1 - t0);

    for (int i = 0; i < width * width; i++) c[i] = 0;

    /* --- GPU distribute parallel for simd --- */
    t0 = now_sec();
    mm_gpu_dist_par_for_simd(a, b, c, width);
    t1 = now_sec();
    printf("GPU distribute parallel for simd: %.4f s\n", t1 - t0);

    free(a);
    free(b);
    free(c);

    return 0;
}