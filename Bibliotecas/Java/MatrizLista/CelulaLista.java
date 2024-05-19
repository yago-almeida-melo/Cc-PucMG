class CelulaLista {
    CelulaLista prox;
    public int elemento;

    CelulaLista(){
        this(null, 0);
    }
    CelulaLista(CelulaLista prox, int elemento){
        this.prox = prox;
        this.elemento = elemento;
    }
    
}
