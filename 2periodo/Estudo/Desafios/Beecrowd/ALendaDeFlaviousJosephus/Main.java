import java.util.Scanner;

/**
 * IMPORTANT: 
 *      O nome da classe deve ser "Main" para que a sua solução execute
 *      Class name must be "Main" for your solution to execute
 *      El nombre de la clase debe ser "Main" para que su solución ejecutar
 */
public class Main {
    public static void problem(int caseNumber, int[] x, int n, int k) {
        int index = 0;
        int remaining = n;
        while (remaining > 1) {
            index = (index + k - 1) % remaining;
            remover(x, index, remaining);
            remaining--;
        }
        System.out.println("Case " + (caseNumber + 1) + ": " + x[0]);
    }

    public static void remover(int[] x, int index, int remaining) {
        for (int i = index; i < remaining - 1; i++) {
            x[i] = x[i + 1];
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int x = sc.nextInt();
        sc.nextLine();

        for (int i = 0; i < x; i++) {
            String in = sc.nextLine();
            String[] parts = in.split(" ");
            int n = Integer.parseInt(parts[0]);
            int k = Integer.parseInt(parts[1]);
            int[] vet = new int[n];

            for (int j = 0; j < n; j++) {
                vet[j] = j + 1;
            }
            problem(i, vet, n, k);
        }

        sc.close();
    }
}
