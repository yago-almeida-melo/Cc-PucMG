package File;

import java.util.ArrayList;
import Entidades.Categoria;

public class ArquivoCategoria extends Arquivo<Categoria> {
    Arquivo<Categoria> arqTarefa;
    ArvoreBMais<ParNomeId> indiceIndiretoNome;

    public ArquivoCategoria() throws Exception {
        super("Categorias.db", Categoria.class.getConstructor());
        indiceIndiretoNome = new ArvoreBMais<>(ParNomeId.class.getConstructor(), 5, ".\\BasedeDados\\indiceIndiretoNome.btree.db" 
        );
    } 

    @Override
    public int create (Categoria obj) throws Exception {
        int id = super.create(obj);
        indiceIndiretoNome.create(new ParNomeId(obj.getNome(), id));
        return id;
    } 

    public Categoria read(String nome) throws Exception {
        ArrayList<ParNomeId> picn = indiceIndiretoNome.read(new ParNomeId(nome, -1));
        return super.read(picn.get(0).getIDCategoria());
    } 
    
    public boolean delete (int nome) throws Exception {
        boolean result = false;
        Categoria obj = super.read(nome);
        if(obj != null) {
            if(indiceIndiretoNome.delete(new ParNomeId(obj.getNome(), obj.getId()))) {
                result = super.delete(obj.getId());
            } 
        } 
        return result;
    }

    @Override
    public boolean update(Categoria novaCategoria) throws Exception {
        boolean result = false;
        Categoria categoriaAntiga = super.read( novaCategoria.getId() );
        if(super.update(novaCategoria)) {
            if(novaCategoria.getNome() != categoriaAntiga.getNome()) {
                if( indiceIndiretoNome.delete(new ParNomeId(categoriaAntiga.getNome(), categoriaAntiga.getId())) ) {
                    indiceIndiretoNome.create(new ParNomeId(novaCategoria.getNome(), novaCategoria.getId()));
                } 
                result = true;
            } 
        } 
        return result;
    } 

} 