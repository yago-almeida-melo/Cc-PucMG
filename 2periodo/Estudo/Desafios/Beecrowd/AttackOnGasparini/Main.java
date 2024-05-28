import java.io.IOException;
import java.util.Scanner;
 
/**
 * IMPORTANT: 
 *      O nome da classe deve ser "Main" para que a sua solução execute
 *      Class name must be "Main" for your solution to execute
 *      El nombre de la clase debe ser "Main" para que su solución ejecutar
 */
public class Main {
 
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String inputLine = scanner.nextLine();
        String[] parts = inputLine.split(" ");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Entrada inválida. Certifique-se de digitar dois inteiros separados por um espaço.");
        }
        int qtdTitans = Integer.parseInt(parts[0]);
        int tamanho = Integer.parseInt(parts[1]);
        String titans = scanner.nextLine();
        String tams = scanner.nextLine();
        String[] parts1 = tams.split(" ");
        if (parts1.length != 3) {
            throw new IllegalArgumentException("Entrada inválida. Certifique-se de digitar dois inteiros separados por um espaço.");
        }
        int pp = Integer.parseInt(parts1[0]); 
        int mm = Integer.parseInt(parts1[1]); 
        int gg = Integer.parseInt(parts1[2]);
        int p=0,m=0,g=0;
        for(int i=0;i<qtdTitans;i++){
            if(titans.charAt(i)=='P') p++;
            if(titans.charAt(i)=='M') m++;
            if(titans.charAt(i)=='G') g++;
        }
        int total = (pp*p)+(mm*m)+(gg*g);
        int resp = (total + tamanho - 1) / tamanho;
        System.out.println(resp);
        scanner.close();
    }
}



