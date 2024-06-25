#include <stdio.h>

int main() {
    int n = 0, m = 0;
    scanf("%d %d", &n, &m);
    while (n != 0 && m != 0) {
        int v[m];
        for (int i = 0; i < m; i++) {
            scanf("%d", &v[i]);
        }
        int max_val = n > m ? n : m;  // Assuming the range of values in v is within [0, n] or [0, m]
        int count[max_val + 1];
        for (int x = 0; x <= max_val; x++) {
            count[x] = 0;
        }
        for (int j = 0; j < m; j++) {
            count[v[j]]++;
        }
        int unique_count = 0;
        for (int a = 0; a <= max_val; a++) {
            if (count[a] > 1) unique_count++;
        }
        printf("%d\n", unique_count);
        scanf("%d %d", &n, &m);
    }
    return 0;
}
