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
            qtd++;
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

    public int removerFim(){
        int elemento = -1;
        if(primeiro == ultimo){
            System.out.println("ERRO LISTA VAZIA");
        } else{
            Celula i = primeiro;
            for(;i.prox!=ultimo;i = i.prox);
            Celula tmp = i.prox;
            elemento = tmp.elemento;
            i.prox = null;
            i = tmp = null;
            qtd--;
        }
        return elemento;
    }

    public int removerInicio(){
        int elemento = -1;
        if(primeiro==ultimo){
            System.out.println("LISTA VAZIA");
        } else{
            elemento = primeiro.prox.elemento;
            Celula tmp = primeiro.prox;
            primeiro.prox = tmp.prox;
            tmp.prox = null;
            tmp = null;
            qtd--;
        }
        return elemento;
    }

    public int remover(int pos){
        int resp=-1;
        if(primeiro==ultimo){
            System.out.println("EROOR");
        } else if(pos < 0 || pos >= qtd){
        
        } else if(pos==0){
            resp = removerInicio();
        } else if(pos == qtd-1){
            resp = removerFim();
        } else{
            Celula i = primeiro;
            for(int j=0;j<pos;j++,i=i.prox);
            Celula tmp = i.prox;
            resp = tmp.elemento;
            i.prox = tmp.prox;
            tmp.prox = null;
            i = tmp = null;
        }
        return resp;
    }

    public void removerImpares(){
        for(Celula i = primeiro;i.prox!=null;i=i.prox){
            while(i.prox.elemento%2==1){
                Celula tmp = i.prox;
                i.prox = tmp.prox;
                tmp.prox = null;
                tmp = null;
            }
        }
    }
}
