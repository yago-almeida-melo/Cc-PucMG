public class Main {
    public static void main(String[] args){
        Arvore arvore = new Arvore();

        arvore.inserir(3);
        arvore.inserir(4);
        arvore.inserir(1);
        arvore.inserir(2);
        arvore.inserir(6);
        arvore.caminharDecrescente(arvore.raiz);
    }    
}
