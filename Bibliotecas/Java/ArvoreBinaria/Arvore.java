public class Arvore{
    public No raiz;
    public static int altura = 0;

    Arvore(){
        this.raiz = null;
    }

    public void inserir(int x){
        raiz = inserir(x, raiz);
    }
    No inserir(int x, No i){
        if(i==null){
            i = new No(x);
        } else if(x < i.elemento){
            i.esq = inserir(x, i.esq);
        } else{
            i.dir = inserir(x, i.dir);
        }
        return i;
    }

    public boolean pesquisar(int x){
        return pesquisar(x, raiz);
    }

    public boolean pesquisar(int x, No i){
        boolean resp = false;
        if(i.elemento == x){
            resp = true;
        } else if(i.elemento > x){
            resp = pesquisar(x, i.esq);
        } else{
            resp = pesquisar(x, i.dir);
        }
        return resp;
    }

    public void caminharCentral(No i){
        if(i!=null){
            caminharCentral(i.esq);
            System.out.print(i.elemento+" ");
            caminharCentral(i.dir);
        }
    }

    public void caminharPre(No i){
        if(i!=null){
            System.out.print(i.elemento+" ");
            caminharPre(i.esq);
            caminharPre(i.dir);
        }
    }

    public void caminharPos(No i){
        if(i!=null){
            caminharPos(i.esq);
            caminharPos(i.dir);
            System.out.print(i.elemento+" ");
        }
    }

    public void caminharDecrescente(No i){
        if(i!=null){
            caminharDecrescente(i.dir);
            System.out.print(i.elemento+" ");
            caminharDecrescente(i.esq);
        }
    }
}