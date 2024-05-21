public class Principal {
    public static void main(String[] args){
        ListaSimples lista = new ListaSimples();
        lista.inserirFim(2);
        lista.inserirFim(3);
        lista.inserirInicio(8);
        lista.inserirFim(6);
        lista.inserir(7, 2);
        lista.mostrar();
        lista.removerImpares();
        lista.mostrar();
    }
}
