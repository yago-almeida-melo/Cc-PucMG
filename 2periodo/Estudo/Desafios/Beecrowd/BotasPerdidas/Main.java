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
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        while(sc.hasNextInt()){
            sc.nextLine();
            int pares=0;
            String[] botas = new String[n];
            for(int i=0;i<n;i++){
                botas[i] = sc.nextLine();                
            }
            for(int j=0;j<n;j++){
                String parts[] = botas[j].split(" ");
                for(int k=j+1;k<n;k++){
                    String parts1[] = botas[k].split(" ");
                    if(parts[0].equals(parts1[0]) && !botas[j].equals(botas[k])){
                      pares++;  
                    } 
                }
            }
            System.out.println(pares);
            n = sc.nextInt();
        }
        sc.close();
    }
 
}
