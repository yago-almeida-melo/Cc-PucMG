
public class Lista {
    CelulaLista inicio;
    CelulaLista fim;

    Lista(){
        this.inicio = null;
        this.fim = null;
    }

    Lista(int x){
        this.inicio = new CelulaLista();
        this.fim = this.inicio;
        CelulaLista pLista = inicio;
        inicio.topo = new CelulaPilha(1);
        CelulaPilha pPilha = inicio.topo;
        pPilha.prox = new CelulaPilha(1);
        pPilha.prox.prox = new CelulaPilha(1);
        
        pLista.prox = new CelulaLista();
        pLista = pLista.prox;
        pPilha = pLista.topo;
        pPilha = new CelulaPilha(2);
        pPilha.prox = new CelulaPilha(2);
        pPilha.prox.prox = new CelulaPilha(2);
        pPilha.prox.prox.prox = new CelulaPilha(2);

        pLista.prox = new CelulaLista();
        pLista = pLista.prox;
        this.fim = pLista;
        pPilha = pLista.topo;
        pPilha = new CelulaPilha(3);
    }

    public CelulaLista maiorPilha(){
        CelulaLista pLista = inicio;
        CelulaPilha pPilha = inicio.topo;
        CelulaLista resp = inicio;
        int qtd = 0;
        int maior = 0;
        while(pLista!=null){
            while(pPilha!=null){
                qtd++;
                pPilha = pPilha.prox;
            }
            if(qtd > maior){
                resp = pLista;
            }
            qtd = 0;
            pLista = pLista.prox;
        }
        return resp;
    }

    public static void main(String[] args){
        Lista l = new Lista(2);
        CelulaLista p = l.maiorPilha(); 
        System.out.println(p.topo.elemento);
    }
}
