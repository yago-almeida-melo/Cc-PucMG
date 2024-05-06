public class No {
    public int elemento;
    public No esq, dir;

    No(){
        this.elemento = 0;
        this.esq = null;
        this.dir = null;
    }
    No(int x){
        this.elemento = x;
        this.esq = esq;
        this.dir = dir;
    }
    No(int elemento, No esq, No dir){
        this.elemento = elemento;
        this.esq = esq;
        this.dir = dir;
    }
}
