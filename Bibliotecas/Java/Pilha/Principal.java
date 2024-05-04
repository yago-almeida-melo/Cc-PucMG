class Principal {
    public static void main(String[] args){
        Pilha x = new Pilha();
        x.inserir(3);
        x.inserir(6);
        x.inserir(10);
        x.inserir(7);
        x.inserir(9);
        int a = x.remover();
        //System.out.println(a);
        x.mostrar();
        System.out.println("");
        x.mostrarInsercao();
        //System.out.println(x.soma());
        //System.out.print(x.maior());
    }
}
