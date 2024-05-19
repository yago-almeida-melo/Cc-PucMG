class  MatrizLista{
    CelulaMat inicio;
    public int linha, coluna;
    public MatrizLista() {
        this.inicio = null;
        this.coluna = 0;
        this.linha = 0;
    }

    public MatrizLista(int linha, int coluna) {
        this.linha = linha;
        this.coluna = coluna;
        ///////////////////////////////// Implementação ///////////////////////////////// 
        inicio = new CelulaMat();
        CelulaMat tmpLinha = inicio; CelulaMat tmpColuna = inicio; CelulaMat tmpLink = inicio;
        int i = 0, j = 0;
        while (j < coluna - 1) {
           tmpColuna.dir = new CelulaMat();
           tmpColuna.dir.esq = tmpColuna;
           tmpColuna = tmpColuna.dir;
           j++;
        }
        j=0;
        tmpColuna = inicio;
        /////////////////////// Apos a primeira linha ///////////////////////////
        while (i < linha - 1) {
           tmpLink = tmpLinha; // ponteiro que link sup e inf fica acima de tmpLinha e tmpColuna
           tmpLinha.inf = new CelulaMat();    //Cria nova CelulaMat na nova Linha
           tmpLinha.inf.sup = tmpLinha;     //Linka a nova CelulaMat com a linha de cima
           tmpLinha = tmpLinha.inf;         //Move o ponteiro de linha para a linha de baixo
           tmpColuna = tmpLinha;            //Ponteiro de coluna aponta para o de linha
           j = 0;
           while (j < coluna - 1) {
              // Cria as Celulas da Linha (encadeia somente a linha)
              tmpColuna.dir = new CelulaMat();
              tmpColuna.dir.esq = tmpColuna;
              tmpColuna = tmpColuna.dir;
              // Linka as Celulas de baixo com as de cima
              tmpLink = tmpLink.dir;
              tmpLink.inf = tmpColuna;         ///ERRORRRRR
              tmpLink.inf.sup = tmpLink;
              // Incrementação para controlar o numero de colunas
              j++;
           }
           i++;
        }

    }
}