
class Pilha {
    public Celula topo;

    Pilha(){
        topo = null;
    }

    Pilha(int x){
        Celula nova = new Celula();
        topo = nova;
        int i=1;
        while(i<x){
            nova = new Celula(i);
            nova.prox = topo;
            topo = nova;
            nova = null;
            i++;
        }
    }
    
    public void inserir(int x){
        Celula tmp = new Celula(x);
        tmp.prox = topo;
        topo = tmp;
        tmp = null;
    }

    public int remover(){
        if(topo == null){
            System.err.println("ERRO, LISTA VAZIA");
        }
        Celula tmp = topo;
        int x = tmp.elemento;
        topo = topo.prox;
        tmp.prox = null;
        tmp = null;
        return x;
    }

    public void mostrar(){
        Celula tmp = topo;
        while(tmp!=null){
            System.out.print(tmp.elemento+" ");
            tmp = tmp.prox;
        }
    }

    public void mostrarInsercao(){
        mostrar2(topo);
    }

    public void mostrar2(Celula topo){
        if(topo!=null){
            mostrar2(topo.prox);
            System.out.print(topo.elemento+" ");
        }
    }

    public int soma(){
        return soma(topo);
    }

    public int soma(Celula topo){
        if(topo == null){
            return 0;
        } else{
            int soma = soma(topo.prox);
            return soma + topo.elemento;
        }
    }

    public int maior(){
        return maior(topo, 0);
    }

    public int maior(Celula tmp, int x){
        if(tmp == null){
            return x;
        } else{
            if(tmp.elemento > x){
                x = maior(tmp.prox, tmp.elemento);
            }
            x = maior(tmp.prox, x);
        }
        return x;
    }
}
