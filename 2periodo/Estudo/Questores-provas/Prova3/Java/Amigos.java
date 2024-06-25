import java.util.Scanner;


class Celula{
    public String elemento;
    public Celula prox;
    Celula(){
        this.elemento ="";
        this.prox = null;
    }
    Celula(String nome){
        this.elemento = nome;
        this.prox = null;
    }
}

class Amigos{
    static Scanner sc = new Scanner(System.in); 
    static Celula inicio = null;
    static Celula fim = null;

    public static void inserir(String nome){
        fim.prox = new Celula(nome);
        fim = fim.prox;
    }

    public static void inserirNovos(String nomes, String indicado){
        if(indicado.equals("nao")) {
            String[] s = nomes.split(" ");
            for(int i=0;i<s.length;i++){
                inserir(s[i]);
            } 
        } else{
            Celula tmp = inicio;
            while(!tmp.prox.elemento.equals(indicado)) { 
                tmp = tmp.prox;
            }
            Celula x = tmp.prox;
            tmp.prox = null;
            String[] s = nomes.split(" ");
            for(int i=0;i<s.length;i++){
                tmp.prox = new Celula(s[i]);
                tmp = tmp.prox;
            }
            tmp.prox = x;
            tmp = null; x = null; 
        }
    }

    static void mostrar(){
        for(Celula i=inicio.prox; i!=null; i=i.prox){
            System.out.print(i.elemento +" ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        inicio = fim = new Celula();
        String lista = sc.nextLine();
        String nova = sc.nextLine();
        String indicado = sc.nextLine();
        String[] s = lista.split(" ");
        for(int i=0;i<s.length;i++){
            inserir(s[i]);
        }
        inserirNovos(nova, indicado);
        mostrar();
    }
}
