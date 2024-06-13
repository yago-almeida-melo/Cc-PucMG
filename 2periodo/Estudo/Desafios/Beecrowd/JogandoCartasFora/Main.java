import java.io.IOException;
import java.util.Scanner;
 


public class Main {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        while(n!=0){
            int vet[] = new int[n];
            for(int x=0;x<n;x++){
                vet[x] = x+1;
            }
            int rem[] = new int[n-1];
            for(int a=0;a<n-1;a++){
                rem[a] = -1;
            }
            int removidos = 0;
            int i=n-1;
            while(removidos<n-1){
                for(int w=0;w<i+1;w++){
                    System.out.print(vet[w]);
                }
                System.out.print("\n");
                int retirado = vet[0];
                rem[removidos] = retirado;
                removidos++;
                for(int a=0;a<i;a++){
                    vet[a] = vet[a+1];
                }
                int mov = vet[0];
                for(int b=0;b<i-1;b++){
                    vet[b] = vet[b+1];
                }
                i--;
                vet[i] = mov;
            }
            System.out.print("\nDiscarded cards: ");
            for(int p = 0; p<removidos;p++){
                System.out.print(rem[p]+" ");
            }
            System.out.print("\nRemaining card: "+ vet[0]+"\n");
            n = sc.nextInt();
        }
    }
}
