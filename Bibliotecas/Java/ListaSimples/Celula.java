public class Celula {
    public Celula prox;
    public int elemento;

    Celula(){
        this(0);
    }

    Celula(int x){
        this.prox = null;
        this.elemento = x;
    }
}  
