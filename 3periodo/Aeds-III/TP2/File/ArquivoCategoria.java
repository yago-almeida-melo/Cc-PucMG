package File;
import Entidades.Categoria;

public class ArquivoCategoria extends File.Arquivo<Categoria> {

    Arquivo<Categoria> arqCategorias;

    public ArquivoCategoria() throws Exception {
        super("categorias", Categoria.class.getConstructor());
    }

}