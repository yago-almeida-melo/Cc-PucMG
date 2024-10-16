package File;

import Entidades.Tarefa;

public class ArquivoTarefa extends Arquivo<Tarefa> {
    Arquivo<Tarefa> arqTarefa;
    HashExtensivel<ParNomeId> indiceIndiretoNome;

    public ArquivoTarefa() throws Exception {
        super(Tarefa.class.getConstructor(), "tarefas");
        indiceIndiretoNome = new HashExtensivel<>(
            ParNomeId.class.getConstructor(), 
            4, 
            ".\\dados\\indiceNome.hash_d.db", 
            ".\\dados\\indiceNome.hash_c.db"
        );
    }

    @Override
    public int create(Tarefa c) throws Exception {
        int id = super.create(c);
        indiceIndiretoNome.create(new ParNomeId(c.getNome(), id));
        return id;
    }

    public Tarefa read(int id) throws Exception {
        ParNomeId pci = indiceIndiretoNome.read(ParNomeId.hash(id));
        if(pci == null)
            return null;
        return read(pci.getId());
    }
    
    public boolean delete(int id) throws Exception {
        ParNomeId pci = indiceIndiretoNome.read(ParNomeId.hash(id));
        if(pci != null) 
            if(delete(pci.getId())) 
                return indiceIndiretoNome.delete(ParNomeId.hash(id));
        return false;
    }

    @Override
    public boolean update(Tarefa novoTarefa) throws Exception {
        Tarefa TarefaVelho = read(novoTarefa.getId());
        if(super.update(novoTarefa)) {
            if(novoTarefa.getId() == TarefaVelho.getId()) {
                indiceIndiretoNome.delete(ParNomeId.hash(TarefaVelho.getId()));
                indiceIndiretoNome.create(new ParNomeId(novoTarefa.getNome(), novoTarefa.getId()));
            }
            return true;
        }
        return false;
    } 
}
