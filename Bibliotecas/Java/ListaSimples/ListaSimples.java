public class ListaSimples {
    private Celula primeiro;
    private Celula ultimo;
    public static int qtd = 0;

    ListaSimples(){
        primeiro = new Celula();
        ultimo = primeiro;
    }

    public void inserirInicio(int x){
        Celula tmp = new Celula(x);
        tmp.prox = primeiro.prox;
		primeiro.prox = tmp;
		if (primeiro == ultimo) {                 
			ultimo = tmp;
		}
        tmp = null;
        qtd++;
    }

    public void inserirFim(int x){
        ultimo.prox = new Celula(x);
        ultimo = ultimo.prox;
        qtd++;
    }

    public void inserir(int x, int pos){ //p  1  3  4
        if(pos == 0){
            inserirInicio(x);
        } else if(pos == qtd){
            inserirFim(x);
        } else{
            Celula tmp = primeiro;
            for(int i=0;i<pos;i++, tmp = tmp.prox);
            Celula nova = new Celula(x);
            nova.prox = tmp.prox;
            tmp.prox = nova;
            tmp = null; nova = null;
        }
    }

    public void mostrar(){
        Celula tmp = primeiro.prox;
        System.out.print("[ ");
        for(;tmp!= null; tmp = tmp.prox){
            System.out.print(tmp.elemento+" ");
        }
        System.out.print("]");
    }
}
