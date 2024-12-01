package File;

import Entidades.*;
import IndiceInvertido.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ArquivoTarefa extends Arquivo<Tarefa> {

    ArvoreBMais<ParIdId> indice_indireto_id;  // Índice indireto para acessar tarefas por ID da categoria
    ArvoreBMais<ParIdRotuloCId> arvoreB;  // Índice invertido para acessar tarefas por palavra-chave
    StopWords stopWords;

    // Construtor da classe ArquivoTarefa
    public ArquivoTarefa() throws Exception {
        super("Tarefas", Tarefa.class.getConstructor());
        // Inicializa o índice indireto utilizando uma Árvore B+
        indice_indireto_id = new ArvoreBMais<>(
                ParIdId.class.getConstructor(),
                5,
                "BaseDeDados//indice_indireto_id.btree.db");
        arvoreB = new ArvoreBMais<>(
                ParIdRotuloCId.class.getConstructor(),
                5,
                "BaseDeDados//ArvoresTarefasRotulos.db");
        stopWords = new StopWords();
    }

    // Sobrescreve o método create para incluir a inserção no índice indireto
    @Override
    public int create(Tarefa c) throws Exception {
        int id = super.create(c);
        c.setId(id);
        indice_indireto_id.create(new ParIdId(c.getIdCategoria(), id));  // Atualiza o índice indireto
        ArrayList<Integer> idRotulos = c.getIdRotulos();
        for (int i = 0; i < idRotulos.size(); i++) {
            idRotulos.add(i);
            arvoreB.create(new ParIdRotuloCId(idRotulos.get(i), id));
        }
        c.setIdRotulos(idRotulos);
        stopWords.inserir(c.getNome(), id);
        return id;
    }

    // Método para ler todas as tarefas de uma categoria pelo ID da categoria
    public ArrayList<Tarefa> readAll(int id) throws Exception {
        // Busca no índice indireto a lista de pares com o ID da categoria fornecido
        ArrayList<ParIdId> p = indice_indireto_id.read(new ParIdId(id, -1));
        ArrayList<Tarefa> t = new ArrayList<>();
        Arquivo<Tarefa> arq = new Arquivo<>("Tarefas", Tarefa.class.getConstructor());

        if (p != null && !p.isEmpty()) {
            for (ParIdId p_aux : p) {
                Tarefa tarefa = (Tarefa) arq.read(p_aux.getId2());
                if (tarefa != null) {
                    t.add(tarefa);  // Adiciona a tarefa à lista
                }
            }
        }
        return t;  // Retorna a lista de tarefas
    }

    /* Método de Leitura. Lendo os ID's do ArrayList. Retorna as Tarefas */
    public ArrayList<Tarefa> read(ParNomeId parNomeId) throws Exception {

        ArrayList<Tarefa> t = new ArrayList<>();
        ArrayList<ParIdId> id = new ArrayList<>();
        id = indice_indireto_id.read(new ParIdId(parNomeId.getId()));
        for (int i = 0; i < id.size(); i++) {
            t.add(super.read(id.get(i).getId2()));
        }
        return t;
    }

    public ArrayList<Tarefa> read(ParRotuloId parRotuloId) throws Exception {

        ArrayList<Tarefa> t = new ArrayList<>();
        ArrayList<ParIdRotuloCId> id = new ArrayList<>();
        id = arvoreB.read(new ParIdRotuloCId(parRotuloId.getId()));
        for (int i = 0; i < id.size(); i++) {
            t.add(super.read(id.get(i).getId2()));
        }
        return t;
    }

    // Sobrescreve o método delete para excluir também do índice indireto
    @Override
    public boolean delete(int id) throws Exception {
        boolean result = false;
        Tarefa obj = super.read(id);
        if (obj != null) {
            // Exclui do índice indireto e marca o registro como excluído no arquivo
            if (indice_indireto_id.delete(new ParIdId(obj.getIdCategoria(), obj.getId()))) {
                result = super.delete(obj.getId());
            }
            String[] chaves = stopWords.stopWordsCheck(obj.getNome());
            ArrayList<Integer> idRotulos = obj.getIdRotulos();
            for(int i = 0; i < idRotulos.size(); i++) {
                arvoreB.delete(new ParIdRotuloCId(idRotulos.get(i), id));
            }
            for(int i = 0; i < chaves.length; i++) {
                stopWords.lista.delete(chaves[i], id);
            }
        }
        return result;
    }

    // Sobrescreve o método update para atualizar também o índice indireto
    @Override
    public boolean update(Tarefa novaTarefa) throws Exception {
        boolean result = false;
        Tarefa TarefaVelho = read(novaTarefa.getId());
        String chaves[] = stopWords.stopWordsCheck(TarefaVelho.getNome());
        for (int i=0;i<chaves.length;i++) {
            chaves[i] = chaves[i].toLowerCase();
            stopWords.lista.delete(chaves[i], TarefaVelho.getId());
        }
        stopWords.inserir(novaTarefa.getNome(), novaTarefa.getId());
        result = super.update(novaTarefa);
        // Atualiza o índice indireto, removendo o antigo e inserindo o novo
        indice_indireto_id.delete(new ParIdId(TarefaVelho.getIdCategoria(), TarefaVelho.getId()));
        indice_indireto_id.create(new ParIdId(novaTarefa.getIdCategoria(), novaTarefa.getId()));
        return result;
    }

     public boolean updateRotulos(Tarefa tarefa, ArrayList<Integer> removed, ArrayList<Integer> added){
        boolean result = false;
        //System.out.println("ID Tarefa: " + tarefa.getNome());
        //System.out.println("added.lenght: " + added.size());
        //System.out.println("removed.lenght: " + removed.size());
        try{
            ArrayList<Integer> idRotulos = tarefa.getIdRotulos();
            //System.out.println("Qtd Rotulos cadastradas: " + idRotulos.size());

            if(!idRotulos.isEmpty()){
                for(int i = 0; i < removed.size(); i++){
                    boolean existe = false;
                    for(int j = 0; j < idRotulos.size(); j++){
                        if(removed.get(i).equals(idRotulos.get(j))){
                            //System.out.println("Etiqueta removida: " + removed.get(i));
                            existe = true;
                        }
                        else if(j == idRotulos.size() - 1 && !existe){
                            System.out.println("Rotulo não encontrado");
                        }
                    }
                    if(existe){
                        arvoreB.delete(new ParIdRotuloCId(removed.get(i), tarefa.getId()));
                        idRotulos.remove(removed.get(i));
                    }
                }
            }
            else if(!removed.isEmpty() && idRotulos.isEmpty()){
                System.out.println("Não há Rotulos para serem removidos");
            }
            for(int i = 0; i < added.size(); i++){
                boolean existe = false;
                if(!idRotulos.isEmpty()){
                    for(int j = 0; j < idRotulos.size(); j++){
                        //System.out.println("added.get(" + i + "): " + added.get(i) + " - Etiqueta Cadastrada: " + idRotulos.get(j));
                        if(added.get(i).equals(idRotulos.get(j))){
                            System.out.println("Rotulo já existente");
                            existe = true;
                        }
                    }
                }
                if(!existe){
                    //System.out.println("Etiqueta adicionada: " + added.get(i));
                    idRotulos.add(added.get(i));
                    arvoreB.create(new ParIdRotuloCId(added.get(i), tarefa.getId()));
                }
            }
            boolean update = super.update(tarefa);
            tarefa.setIdRotulos(idRotulos);
            if(update){
                result = true;
            }
            else{
                result = false;
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return result;
    }

    public ArrayList<Tarefa> listar(String titulo) throws Exception{
        ArrayList<ElementoLista> elementos = new ArrayList<>();
        String [] chaves = stopWords.stopWordsCheck(titulo);
        for(int i = 0; i < chaves.length; i++){
            //System.out.println("Chave: " + chaves[i]);
            if(!chaves[i].equals("") && !chaves[i].equals(" ")){
                try{
                    ElementoLista [] elementoEncontrados;
                    elementoEncontrados = stopWords.lista.read(chaves[i]);
                    if(elementoEncontrados == null){
                        System.out.println("Nenhuma tarefa encontrada com esse termo");
                    } 
                    else {
                        for(int j = 0; j < elementoEncontrados.length; j++){
                            float frequencia = elementoEncontrados[j].getFrequencia();
                            float idf = stopWords.lista.numeroEntidades();
                            //System.out.println("Quantidade de entidade: " + idf);
                            //System.out.println("Quantidade de instâncias com essa palavra: " + elementoEncontrados.length);
                            idf /= elementoEncontrados.length;
                            //System.out.println("IDF: " + idf);

                            ElementoLista elementoAux = new ElementoLista(elementoEncontrados[j].getId(), frequencia * idf);
                            //System.out.println("Elemento encontrado: ID = " + elementoAux.getId() + ", Frequência TF-IDF = " + elementoAux.getFrequencia());
                            boolean existe = false;
                            for(int z = 0; z < elementos.size(); z++){
                                if(elementos.get(z).getId() == elementoAux.getId()){
                                    elementos.get(z).setFrequencia(elementos.get(z).getFrequencia() + elementoAux.getFrequencia());
                                    existe = true;
                                    z = elementos.size();
                                }
                            }
                            if(!existe){
                                elementos.add(elementoAux);
                            }
                        }
                    }
                }catch(Exception e){
                    System.out.println(e.getMessage());
                }
            }
        }

        // Ordenar a lista de elementos pela frequência TF-IDF
        Collections.sort(elementos, new Comparator<ElementoLista>() {
            @Override
            public int compare(ElementoLista e1, ElementoLista e2) {
                return Float.compare(e2.getFrequencia(), e1.getFrequencia());
            }
        });

        //System.out.println("Elementos ordenados pela frequência TF-IDF:");
        //for(ElementoLista elemento : elementos) {
            //System.out.println("ID = " + elemento.getId() + ", Frequência TF-IDF = " + elemento.getFrequencia());
        //}

        // Converter os elementos ordenados em tarefas
        ArrayList<Tarefa> tarefas = new ArrayList<>();
        for(ElementoLista elemento : elementos) {
            tarefas.add(super.read(elemento.getId()));
        }

        return tarefas;
    }
}
