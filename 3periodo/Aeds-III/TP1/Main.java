import java.io.File;

public class Main {
    public static void main(String[] args) {
        try {
            //Cria o arquivo produtos.db
            new File("dados/produtos.db").delete(); 
            CRUD<Produto> arqProdutos = new CRUD<>(Produto.class.getConstructor(), "produtos.db");
            //Produtos de exemplo
            Produto p1 = new Produto("Produto A", 10.0f);
            Produto p2 = new Produto("Produto B", 20.0f);
            Produto p3 = new Produto("Produto C", 30.0f);
            Produto p4 = new Produto("Produto D", 50.0f);

            //Insere os Produtos
            int id1 = arqProdutos.create(p1);
            int id2 = arqProdutos.create(p2);
            int id3 = arqProdutos.create(p3);
            int id4 = arqProdutos.create(p4);

            //Busca os tres produtos
            System.out.println(arqProdutos.read(id1));
            System.out.println(arqProdutos.read(id2));
            System.out.println(arqProdutos.read(id3));
            System.out.println(arqProdutos.read(id4));
            System.err.println("");

            //Alterando o preço do p2 e exibindo resultado
            p1.setPreco(100.0f);
            arqProdutos.update(p1);
            System.out.println(arqProdutos.read(id1));

            //Alterando o nome de p3(aumentando tamanho) e exibindo resultado
            p2.setNome("Produto XXXX");
            arqProdutos.update(p2);
            System.out.println(arqProdutos.read(id2));

            //Alterando o nome de p1(diminuindo tamanho) e exibindo resultado
            p3.setNome("P3");
            arqProdutos.update(p3);
            System.out.println(arqProdutos.read(id3)+"\n");

            //Deletando o produto 3 e exibindo resultado(null)
            boolean deletado = arqProdutos.delete(id3);
            if(deletado){
                System.err.println("O Produto "+id3+" foi deletado\n");
            }else{
                System.err.println("Error ao tentar deletar o Produto "+id3+"\n");
            }

            System.out.println(arqProdutos.read(id1));
            System.out.println(arqProdutos.read(id2));
            System.out.println(arqProdutos.read(id3)); // Deve retornar null
            System.out.println(arqProdutos.read(id4));

            arqProdutos.close(); //fecha arquivo
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
