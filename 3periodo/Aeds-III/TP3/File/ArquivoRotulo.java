package File;

import Entidades.*;
import java.util.ArrayList;

public class ArquivoRotulo extends Arquivo<Rotulo> {
    ArvoreBMais<ParRotuloId> arvoreB;

    /* Criando o Arquivo de Rotulo */
    public ArquivoRotulo()throws Exception{
        super("Rotulos", Rotulo.class.getConstructor());
        try{
            arvoreB = new ArvoreBMais<>(ParRotuloId.class.getConstructor(),
            5,
            "BaseDeDados//ArvoresRotulos.db");
        }catch(Exception e){
            System.out.println(e.getMessage());
            throw new Exception();
        }
    }

    /* CRUD DE Rotulo */

    /* Método Publico para a criação de Rotulo. Retorna a Rotulo criada */
    public int create(String nomeRotulo)throws Exception{
        Rotulo Rotulo = new Rotulo(nomeRotulo);
        return this.create1(Rotulo);
    }
    
    /* Método Privado da criação de Rotulo. Retorna o ID da Rotulo */
    private int create1(Rotulo Rotulo) throws Exception{
        int id = super.create(Rotulo);
        Rotulo.setId(id);
        try{
            arvoreB.create(new ParRotuloId(Rotulo.getNome(),Rotulo.getId()));
        }catch(Exception e){
            System.out.println("Erro na criação de um novo Rotulo");
            System.out.println(e.getMessage());
        }
        return id;
    } 
    
    /* Método de leitura listando as Tarefas do Rotulo passado como parametro. Retorna as Tarefas */
    public ArrayList<Tarefa> read(String nomeRotulo)throws Exception{
        ArrayList<Tarefa> t = new ArrayList<>();
        ArquivoTarefa tarefas = new ArquivoTarefa();
        try{
            ArrayList<ParRotuloId> Rotulo = arvoreB.read(new ParRotuloId(nomeRotulo));
        
            /*Se a Rotulo estiver vazia, incapaz de fazer o método*/
            if(Rotulo.isEmpty()){
                throw new Exception("Rotulo inxistente");
            }
            t = tarefas.read(Rotulo.get(0));   
        }catch(Exception e){
            System.out.println("Erro na leitura do Arquivo");
            System.out.println(e.getMessage());
        }
        return t;
    }

    /* Método de atualização do nome de uma Rotulo. Retornando se foi feito com Sucesso ou Não. */
    public boolean update(String nomeRotulo, String novoRotulo)throws Exception{
        Rotulo eti = new Rotulo(novoRotulo);
        
        try{
            ArrayList<ParRotuloId> Rotulo = arvoreB.read(new ParRotuloId(nomeRotulo));
            /*Se a Rotulo estiver vazia, incapaz de fazer o método*/
            if(Rotulo.isEmpty()){
                throw new Exception("Rotulo Inexistente");
            }
            eti.setId(Rotulo.get(0).getId());
            
            if(super.update(eti)){
                System.out.println("Atualizo");
            }

            
            arvoreB.delete(Rotulo.get(0));
            arvoreB.create(new ParRotuloId(eti.getNome(), eti.getId()));
        }
        catch (Exception e){
            System.out.println("Erro no update do Arquivo");
            System.out.println(e.getMessage());
        }
        
        return true;
    }

    /* Método de atualização chamando o Método Update de Tarefa. Retorna um booleano. */
    /*public boolean updateTarefa(String nomeRotulo, String nomeTarefa, Tarefa updateTarefa)throws Exception{
        ArrayList<ParRotuloId> Rotulo = arvoreB.read(new ParRotuloId(nomeRotulo));
        ArquivoTarefas tarefas = new ArquivoTarefas();
        
        try{
            //Se a Rotulo estiver vazia, incapaz de fazer o método
            if(Rotulo.isEmpty()){
                throw new Exception("Rotulo Inexistente");
            }
            
        }
        catch(Exception e){
            System.out.println("Erro no updateTarefa");
            System.out.println(e.getMessage());
        }
        
        return tarefas.update(Rotulo.get(0), nomeTarefa, updateTarefa);
    }*/

    /* Método de Delete. Procura pelo nome. Retorna Booleano */
    /*public boolean deleteTarefa(String nomeRotulo, String nomeTarefa)throws Exception{
        ArrayList<ParRotuloId> Rotulo = arvoreB.read(new ParRotuloId(nomeRotulo));
        ArquivoTarefas tarefas = new ArquivoTarefas();
        
        try{

        Se a Rotulo estiver vazia, incapaz de fazer o método
            if(Rotulo.isEmpty()){
                throw new Exception("Rotulo Inexistente");
            }
        }
        catch(Exception e){
            System.out.println("Erro no Delete");
            System.out.println(e.getMessage());
        }
        
        return tarefas.delete(Rotulo.get(0), nomeTarefa);
    }*/

    /* Método de Deletar Rotulo. Procura pelo nome da Rotulo e a deleta. Retorna booleano */
    public boolean delete(String nomeRotulo) throws Exception{
        try{
            ArrayList<ParRotuloId> eti = arvoreB.read(new ParRotuloId(nomeRotulo));

            /*Se a Rotulo estiver vazia, incapaz de fazer o método*/
            if(eti.isEmpty()){
                throw new Exception("Rotulo Inesistente");
            }

            ArquivoTarefa tarefas = new ArquivoTarefa();
            ArrayList<Tarefa> t = tarefas.read(eti.get(0));
    
            if(!t.isEmpty())
                throw new Exception("Tarefas existentes dentro desta Rotulo");
            
            return super.delete(eti.get(0).getId()) ? arvoreB.delete(eti.get(0)) : false;
        }catch(Exception e){
            System.out.println("Erro em deletar");
            System.out.println(e.getMessage());
        }
        return false;
    }

    /* Listando as Rotulo */
    public ArrayList<Rotulo> listar() throws Exception{
        ArrayList<Rotulo> rotulos = new ArrayList<>();
        try{
             rotulos = super.list();

            if(rotulos.isEmpty())
                throw new Exception("Rotulos ainda não foram criados");
            
            for(int i = 0; i<rotulos.size(); i++){
                System.out.println("Indice: " + rotulos.get(i).getId() + " Nome do Rotulo: " + rotulos.get(i).getNome());
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return rotulos;
    }

}
