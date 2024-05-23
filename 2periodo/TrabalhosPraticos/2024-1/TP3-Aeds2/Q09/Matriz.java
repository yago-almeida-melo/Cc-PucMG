/*
 *  806454 - Yago Almeida Melo
 *  TP3-Q09: Matriz Flexivel Java
 */

import java.util.Scanner;

   class Celula {
      public int elemento;
      public Celula inf, sup, esq, dir;
 
      public Celula() {
         this(0);
      }
 
      public Celula(int elemento) {
         this(elemento, null, null, null, null);
      }
 
      public Celula(int elemento, Celula inf, Celula sup, Celula esq, Celula dir) {
         this.elemento = elemento;
         this.inf = inf;
         this.sup = sup;
         this.esq = esq;
         this.dir = dir;
      }
   }



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

   public Matriz soma(Matriz m) {
      Matriz resp = null;
      if (this.linha == m.linha && this.coluna == m.coluna) {
         resp = new Matriz(this.linha, this.coluna);
         Celula aLinha, bLinha, cLinha, a, b, c;
         for (aLinha = this.inicio, bLinha = m.inicio, cLinha = resp.inicio; 
            aLinha != null && bLinha != null && cLinha != null; 
            aLinha = aLinha.inf, bLinha = bLinha.inf, cLinha = cLinha.inf) {
            for (a = aLinha, b = bLinha, c = cLinha; 
               a != null && b != null && c != null; 
               a = a.dir, b = b.dir, c = c.dir) {
               c.elemento = a.elemento + b.elemento;
            }
         }
      } else {
         throw new IllegalArgumentException("As matrizes devem ter as mesmas dimensões para serem somadas.");
      }
      return resp;
   }
  
   
   public Matriz multiplicacao(Matriz m) {
      if (this.coluna != m.linha) {
         throw new IllegalArgumentException("Número de colunas da primeira matriz deve ser igual ao número de linhas da segunda matriz.");
      }
      Matriz resp = new Matriz(this.linha, m.coluna);
      Celula aLinha, a, bColuna, b, cLinha, c;
      for (aLinha = this.inicio, cLinha = resp.inicio; aLinha != null; aLinha = aLinha.inf, cLinha = cLinha.inf) {
         for (bColuna = m.inicio, c = cLinha; bColuna != null; bColuna = bColuna.dir, c = c.dir) {
            int soma = 0;
            for (a = aLinha, b = bColuna; a != null && b != null; a = a.dir, b = b.inf) {
               soma += a.elemento * b.elemento;
            }
            c.elemento = soma;
         }
      }
      return resp;
   }
  
   public boolean isQuadrada(){
      return (this.linha == this.coluna);
   }
   
   public void mostrarDiagonalPrincipal() {
      if (isQuadrada()) {
         Celula tmp = inicio;
         for (int i = 0; i < linha; i++) {
            System.out.print(tmp.elemento + " ");
            if (tmp.dir != null && tmp.inf != null) {
               tmp = tmp.dir.inf;
            }
         }
         System.out.println();
      }
  }
  
   public void mostrarDiagonalSecundaria() {
      if (isQuadrada()) {
         Celula tmp = inicio;
         while (tmp.dir != null) {
           tmp = tmp.dir;
         }
         for (int i = 0; i < linha; i++) {
            System.out.print(tmp.elemento + " ");
            if (tmp.esq != null && tmp.inf != null) {
               tmp = tmp.esq.inf;
            }
         }
         System.out.println();
      }
   }
  
   
   public static void main(String[] args){
      int casos = 0;
      casos = sc.nextInt();
      for(int i=0;i<casos;i++){
         int linhas1 = sc.nextInt();
         int colunas1 = sc.nextInt();
         Matriz m1 = new Matriz(linhas1, colunas1);
         m1.ler();
         int linhas2 = sc.nextInt();
         int colunas2 = sc.nextInt();
         Matriz m2 = new Matriz(linhas2, colunas2);
         m2.ler();
         m1.mostrarDiagonalPrincipal();
         m1.mostrarDiagonalSecundaria();
         Matriz soma = m1.soma(m2);
         soma.mostrar();
         Matriz multiplicacao = m1.multiplicacao(m2);
         multiplicacao.mostrar();
      }
   }
}