import java.util.Scanner;

public class Matriz {
   private Celula inicio;
   private int linha, coluna;

   static Scanner sc = new Scanner(System.in);

   public Matriz() {
      this(3, 3);
   }

   public Matriz(int linha, int coluna) {
      this.linha = linha;
      this.coluna = coluna;
      ///////////////////////////////// Implementação ///////////////////////////////// 
      inicio = new Celula();
      Celula tmpLinha = inicio; Celula tmpColuna = inicio; Celula tmpLink = inicio;
      int i = 0, j = 0;
      while (j < coluna - 1) {
         tmpColuna.dir = new Celula();
         tmpColuna.dir.esq = tmpColuna;
         tmpColuna = tmpColuna.dir;
         j++;
      }
      j=0;
      tmpColuna = inicio;
      /////////////////////// Apos a primeira linha ///////////////////////////
      while (i < linha - 1) {
         tmpLink = tmpLinha; // ponteiro que link sup e inf fica acima de tmpLinha e tmpColuna
         tmpLinha.inf = new Celula();    //Cria nova Celula na nova Linha
         tmpLinha.inf.sup = tmpLinha;     //Linka a nova Celula com a linha de cima
         tmpLinha = tmpLinha.inf;         //Move o ponteiro de linha para a linha de baixo
         tmpColuna = tmpLinha;            //Ponteiro de coluna aponta para o de linha
         j = 0;
         while (j < coluna - 1) {
            // Cria as Celulas da Linha (encadeia somente a linha)
            tmpColuna.dir = new Celula();
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
      
      // Coleta de LIXO
      tmpColuna = null;
      tmpLinha = null;
      tmpLink = null;
   }

   public void setLinha(int linha){
      this.linha = linha;
   }
   public void setColuna(int coluna){
      this.coluna = coluna;
   }

   public int getLinha(){
      return this.linha;
   }
   public int getColuna(){
      return this.coluna;
   }

   public void ler() {
      Celula tmp, tmpL;
      for (tmpL = inicio; tmpL != null; tmpL = tmpL.inf) {
         for (tmp = tmpL; tmp != null; tmp = tmp.dir) {
            tmp.elemento = sc.nextInt();
         }
      }
   }

   public void mostrar() {
      Celula tmp, tmpL;
      for (tmpL = inicio; tmpL != null; tmpL = tmpL.inf) {
         for (tmp = tmpL; tmp != null; tmp = tmp.dir) {
            System.out.print(tmp.elemento + " ");
         }
         System.out.print("\n");
      }
   }

   /*
    * public Matriz soma (Matriz m) {
    * Matriz resp = null;
    * if(this.linha == m.linha && this.coluna == m.coluna){
    * resp = new Matriz(this.linha, this.coluna);
    * for(){
    * for(){
    * //sendo c (pont em resp), a (em this) e b (em m)
    * c.elemento = a.elemento + b.elemento;
    * }
    * }
    * //...
    * }
    * return resp;
    * }
    * 
    * public Matriz multiplicacao (Matriz m) {
    * Matriz resp = null;
    * if(){
    * //...
    * }
    * return resp;
    * }
    * 
    * public boolean isQuadrada(){
    * boolean (this.linha == this.coluna);
    * }
    * 
    * public void mostrarDiagonalPrincipal (){
    * if(isQuadrada() == true){
    * 
    * }
    * }
    * 
    * public void mostrarDiagonalSecundaria (){
    * if(isQuadrada() == true){
    * 
    * }
    * }
    */
}