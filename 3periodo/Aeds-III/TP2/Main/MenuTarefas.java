package Main;

import Entidades.Tarefa;
import Enums.Prioridade;
import Enums.Status;
import File.ArquivoCategoria;
import File.ArquivoTarefa;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


public class MenuTarefas extends Principal{
    private static ArquivoTarefa arqTarefas;
    private static ArquivoCategoria arqCategorias;

    public MenuTarefas() throws Exception {
        arqTarefas = new ArquivoTarefa();
        arqCategorias = new ArquivoCategoria();
    } 

    public void menu() {
        try {
            int opcao = 0;
            do {
                opcoesMenu();
                opcao = leOpcao();
                executaOpcao(opcao);
            } while(opcao != 0);
        } catch (Exception e) {
            e.printStackTrace();
        } 
    } 

    protected static void opcoesMenu() {
        System.out.println("-------------------------"
        +"\nTarefas       "
        +"\n1 - Buscar               "
        +"\n2 - Incluir              "
        +"\n3 - Alterar              "
        +"\n4 - Excluir              "
        +"\n0 - Voltar               "
        +"\nOpção: ");
    } 

    protected static void executaOpcao(int opcao) {
        switch(opcao) {
            case 0:
                break;
            case 1:
                buscaTarefa();
                break;
            case 2:
                incluiTarefa();
                break;
            case 3:
                alteraTarefa();
                break;
            case 4:
                excluiTarefa();
                break;
            default:
                System.out.println("Opção inválida!");
                break;
        } 
    } 

    public static LocalDateTime formatarData(String dataStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime data = null;
        try {
            data = LocalDateTime.parse(dataStr, formatter);
        } catch(DateTimeParseException e) {
            System.out.println("\nFormato inválido. Por favor, use o formato dd/MM/yyyy.");
        }
        return data;
    } 

    private static void listaStatus() {
        System.out.println("\nEscolha um status:"
        +"\n1 - Pendente" 
        +"\n2 - Em Progresso" 
        +"\n3 - Concluída" 
        +"\nStatus: ");
    } 

    private static void listaPrioridades() {
        System.out.println("\nEscolha uma prioridade:");
        System.out.println("0 - Baixa                ");
        System.out.println("1 - Média                ");
        System.out.println("2 - Alta                 ");
        System.out.println("3 - Urgente              ");
        System.out.print  ("Opção: "                  );
    } 

    private static void listaCategorias() {
        System.out.println("\nCategorias:");
        /* FAZER UM METODO PARA LER DO 'arqCategoria' E QUE RETORNA TODAS AS CATEGORIAS EXISTENTES */

        /* 
        System.out.println("1 - Trabalho" );
        System.out.println("2 - Estudo"   );
        System.out.println("3 - Lazer"    );
        System.out.println("4 - Saúde"    );
        System.out.println("5 - Outros"   );
        System.out.print  ("Opção: "       ); 
        */
    } // end listar_categorias ()

    public static Tarefa leTarefa() {
        Tarefa tarefa = null;
        try {
            System.out.print("Nome: ");
            String nome = sc.nextLine();

            System.out.print("\nData de Criação: ");
            String dc1 = sc.nextLine();
            LocalDateTime dataCriacao = formatarData(dc1);

            listaStatus();
            byte statusB = (sc.nextByte());
            Status status = Status.fromByte(statusB);

            listaPrioridades();
            byte prioridadeB = sc.nextByte();
            Prioridade prioridade = Prioridade.fromByte(prioridadeB);

            listaCategorias();
            int idCategoria = Integer.parseInt(sc.nextLine());

            tarefa = new Tarefa(nome, dataCriacao, status, prioridade, idCategoria);
        } catch (Exception e) {
            System.out.println("\nErro na leitura!");
        } 
        return tarefa;
    } 

    public static void incluiTarefa() {
        System.out.println("\nIncluir Tarefa:");
        try{
            Tarefa novaTarefa = leTarefa();
            if (novaTarefa != null) {
                System.out.println("\nConfirma inclusao? (S/N)");
                char resp = sc.nextLine().charAt(0);
                if(resp == 'S' || resp == 's') {
                    try {
                        arqTarefas.create(novaTarefa);
                        System.out.println("Tarefa criada!");
                    } catch(Exception e) {
                        System.out.println("Erro do sistema. Não foi possível criar a tarefa!");
                    } 
                } else {
                    System.out.println("Inclusão cancelada!");
                } 
            }
        } catch(Exception e) {
            System.out.println("Erro ao incluir!");
        }
    } 

    public static boolean buscaTarefa() {
        boolean result = false;
        System.out.println("\nBuscar Tarefa:");
        return result;
    } 

    public static boolean alteraTarefa() {
        boolean result = false;
        System.out.println("\nAlterar Tarefa:");
        return result;
    } 

    public static boolean excluiTarefa() {
        boolean result = false;
        System.out.println("\nExcluir Tarefa:");
        return result;
    } 

} 
