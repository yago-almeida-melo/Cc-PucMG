class CelulaMat {
    public int elemento;
    public CelulaMat inf, dir, sup, esq;
    CelulaLista primeiro, ultimo;

    CelulaMat(){
        this.elemento = 0;
        this.inf = null;
        this.dir = null;
        this.sup = null;
        this.esq = null;
        this.primeiro = this.ultimo = new CelulaLista();
    }

    CelulaMat(int elemento, CelulaMat inf, CelulaMat dir, CelulaMat sup, CelulaMat esq, CelulaLista primeiro, CelulaLista ultimo) {
        this.elemento = elemento;
        this.inf = inf;
        this.dir = dir;
        this.sup = sup;
        this.esq = esq;
        this.primeiro = this.ultimo = new CelulaLista();
    }
    
}
