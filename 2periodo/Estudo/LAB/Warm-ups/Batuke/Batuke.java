
import java.util.Scanner;

class Batuke {
    
    public static int total, percorridos;

    public static void locate(int[][] m, int[] pos, int tam){
        if(pos[0] >= 0 && pos[0] < tam && pos[1] >= 0 && pos[1] < tam){
            percorridos++;
            System.out.print("percorridos: "+ percorridos+'\n');

            System.out.print(m[pos[0]][pos[1]]+" ");
        }
        total++;
    }

    public static void direita(int[][] m, int pos[], int mov, int tam){
        for (int i=0;i<mov;i++) {
            locate(m, pos, tam);
            pos[1] = pos[1]+1; 
        }
    }
    public static void cima(int[][] m, int pos[], int mov, int tam){
        for (int i=0;i<mov;i++) {
            locate(m, pos, tam);
            pos[0] = pos[0]-1;   
        }
    }
    public static void baixo(int[][] m, int pos[], int mov, int tam){
        for (int i=0;i<mov;i++) {
            locate(m, pos, tam);
            pos[0] = pos[0]+1;   
        }
    }
    public static void esquerda(int[][] m, int pos[], int mov, int tam){
        for (int i=0;i<mov;i++) {
            locate(m, pos, tam);
            pos[1] = pos[1]-1;   
        }
    }

    public static void main(String[] args){
        total = 0; percorridos = 0;
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        int m[][] = new int[N][N];
        int count = 1;
        for(int i = 0;i<N;i++){
            for(int j=0;j<N;j++){
                m[i][j] = count;
                count++;
            }
        }
        int[] pos = new int[2];
        pos[0] = sc.nextInt();
        pos[1] = sc.nextInt();
        int mov = 1;
        while(mov < N*2 && percorridos < 16){
            if(mov>1) mov++;
            direita(m, pos, mov, N);   
            baixo(m, pos, mov, N);
            mov++;
            esquerda(m, pos, mov, N);
            cima(m, pos, mov, N);
        }
        System.out.println("\n"+total);
        sc.close();
    }    
}