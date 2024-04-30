public class Celula {
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