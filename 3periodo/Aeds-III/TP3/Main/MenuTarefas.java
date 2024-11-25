package Main;

import Entidades.*;
import Enums.Prioridade;
import Enums.Status;
import File.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MenuTarefas {

    ArquivoTarefa arqTarefa;  // Arquivo para manipulação das tarefas
    ArquivoCategoria arqCategoria;  // Arquivo para manipulação das categorias
    ArquivoRotulo arqRotulo;  // Arquivo para manipulação dos rótulos
    private static Scanner sc = new Scanner(System.in);  // Scanner para leitura de entrada do usuário

    // Construtor da classe que inicializa os arquivos de categorias e tarefas
    public MenuTarefas() throws Exception {
        arqTarefa = new ArquivoTarefa();
        arqCategoria = new ArquivoCategoria();
        arqRotulo = new ArquivoRotulo();
    }

    // Método principal do menu de tarefas
    public void menu() throws Exception {
        int opcao;
        do {
            System.out.println("\nAEDsIII");
            System.out.println("-------");
            System.out.println("> Inicio > Tarefas");
            System.out.println("1 - Buscar");
            System.out.println("2 - Incluir");
            System.out.println("3 - Alterar");
            System.out.println("4 - Excluir");
            System.out.println("5 - Listar por categoria");
            System.out.println("6 - Atualizar Rotulo");
            System.out.println("0 - Voltar");

            System.out.print("Opcao: ");
            try {
                opcao = Integer.valueOf(sc.nextLine());  // Leitura da opção do usuário
            } catch (NumberFormatException e) {
                opcao = -1;  // Caso o valor não seja numérico, define a opção como inválida
            }

            // Switch para tratar cada opção do menu
            switch (opcao) {
                case 1:
                    buscarTarefas();  // Chama o método para buscar uma tarefa
                    break;
                case 2:
                    incluirTarefa();  // Chama o método para incluir uma nova tarefa
                    break;
                case 3:
                    alterarTarefa();  // Chama o método para alterar uma tarefa existente
                    break;
                case 4:
                    excluirTarefa();  // Chama o método para excluir uma tarefa
                    break;
                case 5: // Chama o método para listar as tarefas por categoria
                    listarPorCategoria();
                    break;
                case 6:
                    atualizarRotulo();  // Chama o método para atualizar os rótulos de uma tarefa
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opcao invalida!");  // Mensagem para opção inválida
                    break;
            }

        } while (opcao != 0);  // Repete o menu até que o usuário escolha sair
    }

    /* Interface Deletando Tarefa */
    public void excluirTarefa() throws Exception {

        try {

            String termo;
            int numeroTarefa = -1;
            sc.nextLine();
            ArrayList<Tarefa> tarefas = null;
            while (tarefas == null || tarefas.size() == 0) {
                System.out.println("Digite o termo que deseja pesquisar no banco de tarefas: ");
                termo = sc.nextLine();
                tarefas = buscarTarefas(termo);
                if (tarefas == null || tarefas.size() == 0) {
                    System.out.println("Erro ao buscar tarefas, tente novamente com um termo diferente");
                }
            }
            while (numeroTarefa < 0 || numeroTarefa > tarefas.size()) {
                System.out.println("Digite o número da Tarefa que deseja deletar\nObs: digite 0 para cancelar (favor ignorar a mensagem de erro)");
                numeroTarefa = sc.nextInt();
                if (numeroTarefa < 0 || numeroTarefa > tarefas.size()) {
                    System.out.println("Tarefa não encontrada, tente novamente");
                }
            }
            if (arqTarefa.delete(tarefas.get(numeroTarefa - 1).getId())) {
                System.out.println("Tarefa deletada com sucesso");
            } else {
                System.out.println("Erro ao deletar a tarefa");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /* Interface Listando Tarefa */
    public ArrayList<Tarefa> buscarTarefas(String termo) throws Exception {
        ArrayList<Tarefa> tarefas = null;
        try {

            termo = termo.toLowerCase();
            tarefas = arqTarefa.listar(termo);
            for (int i = 0; i < tarefas.size(); i++) {
                System.out.println((i + 1) + "º Tarefa " + "[" + "Nome da Tarefa: " + tarefas.get(i).getNome() + "||" + "Data de Inicio: "
                        + tarefas.get(i).getDataCriacao() + "||" + "Data de Fim: " + tarefas.get(i).getDataConclusao() + "||"
                        + "Status: " + tarefas.get(i).getStatus() + "||" + "Prioridade: " + tarefas.get(i).getPrioridade() + "]");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return tarefas;
    }

    public void buscarTarefas() throws Exception {
        try {
            sc.nextLine();
            System.out.println("Digite o termo que deseja buscar no banco de tarefas: ");
            String titulo = sc.nextLine();
            titulo = titulo.toLowerCase();
            ArrayList<Tarefa> tarefas = arqTarefa.listar(titulo);
            for (int i = 0; i < tarefas.size(); i++) {
                System.out.println((i + 1) + "º Tarefa " + "[" + "Nome da Tarefa: " + tarefas.get(i).getNome() + "||" + "Data de Inicio: "
                        + tarefas.get(i).getDataCriacao() + "||" + "Data de Fim: " + tarefas.get(i).getDataConclusao() + "||"
                        + "Status: " + tarefas.get(i).getStatus() + "||" + "Prioridade: " + tarefas.get(i).getPrioridade() + "]");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /* Interface Atualizando Tarefa */
    public void alterarTarefa() throws Exception {
        String termo, input;
        int numeroTarefa = -1;
        Tarefa t = new Tarefa(), old;
        ArrayList<Tarefa> tarefas = null;
        try {
            // Evitando Buffer
            sc.nextLine();
            while (tarefas == null || tarefas.isEmpty()) {
                System.out.println("Digite o termo que deseja pesquisar no banco de tarefas: ");
                termo = sc.nextLine();
                tarefas = buscarTarefas(termo);
                if (tarefas == null || tarefas.isEmpty()) {
                    System.out.println("Erro ao buscar tarefas, tente novamente com um termo diferente");
                }
            }
            while (numeroTarefa < 0 || numeroTarefa > tarefas.size()) {
                System.out.println("Digite o número da Tarefa que deseja atualizar\nObs: digite 0 para cancelar (favor ignorar a mensagem de erro)");
                numeroTarefa = sc.nextInt();
                if (numeroTarefa < 0 || numeroTarefa > tarefas.size()) {
                    System.out.println("Tarefa não encontrada, tente novamente");
                }
            }
            old = tarefas.get(numeroTarefa - 1);
            System.out.println("Tarefa Selecionada: " + old.getNome());

            // Evitando Buffer
            sc.nextLine();
            System.out.println("Digite seu novo nome");
            t.setNome(sc.nextLine());

            // Evitando Buffer
            sc.nextLine();

            // Scanneando a Data de Inicio
            LocalDate data = null;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            while (data == null) {
                System.out.println("Digite a data de inicio (No formato dd/MM/yyyy)");
                input = sc.nextLine();
                try {
                    data = LocalDate.parse(input, formatter);
                } catch (Exception e) {
                    System.out.println("Data inválida, favor utilizar o formato (dd/MM/yyyy)");
                    data = null;
                }
                if (data != null) {
                    t.setDataCriacao(data);
                }
            }

            // Evitando Buffer
            sc.nextLine();

            data = null;
            // Scanneando a Data Final
            while (data == null) {
                System.out.println("Digite a data do Fim (No formato dd/MM/yyyy)");
                input = sc.nextLine();
                try {
                    data = LocalDate.parse(input, formatter);
                } catch (Exception e) {
                    System.out.println("Data inválida, favor utilizar o formato (dd/MM/yyyy)");
                    data = null;
                }
                if (data != null) {
                    t.setDataConclusao(data);
                }
            }

            // Scanneando o Status da Tarefa
            System.out.println("Digite os Status da tarefa (0 para não iniciado, 1 para em andamento e 2 para finalizado)");
            t.setStatus(Status.values()[sc.nextInt()]);

            // Scanneando a Prioridade da Tarefa
            System.out.println("Digite a prioridade da nvoa Tarefa (Um numero inteiro)");
            t.setPrioridade(Prioridade.values()[sc.nextInt()]);

            if (arqTarefa.update(t)) {
                System.out.println("Tarefa atualizada com sucesso");
            } else {
                System.out.println("Erro ao atualizar a tarefa");
            }

            t = null;

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /* Interface de Criação da Tarefa */
    public void incluirTarefa() throws Exception {
        String categoria;
        int idCategoria = -1;

        System.out.println("\nInclusao de tarefa");

        System.out.print("Nome da Tarefa: ");
        String nome = sc.nextLine();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Inserção do status
        listaStatus();
        byte statusB = Byte.parseByte(sc.nextLine());
        Status status = Status.fromByte(statusB);

        // Inserção da prioridade
        listaPrioridades();
        byte prioridadeB = Byte.parseByte(sc.nextLine());
        Prioridade prioridade = Prioridade.fromByte(prioridadeB);

        System.out.print("Data de Criação (dd/MM/yyyy) - 0 para data atual: ");
        String dc = sc.nextLine();
        LocalDate dataCriacao = LocalDate.now();
        if (!dc.equals("0")) { 
            dataCriacao = LocalDate.parse(dc, formatter);  // Define a data de criação conforme entrada do usuário
        }

        LocalDate dataConclusao = LocalDate.parse("01/01/1970", formatter);  // Data padrão para tarefas não concluídas
        if(status == Status.CONCLUIDO){
            System.out.print("Data de Conclusão (dd/MM/yyyy): ");
            String input = sc.nextLine();
            dataConclusao = LocalDate.parse(input, formatter);  // Define a data de conclusão caso a tarefa esteja concluída
        }

        // Inserção da categoria
        boolean catEscolhida = true;
        do {
            System.out.println("\nDigite o NOME DA CATEGORIA para a tarefa: ");
            try {
                catEscolhida = true;

                arqCategoria.listar();  // Lista todas as categorias
                System.out.print("\n > ");
                categoria = sc.nextLine();
                
                idCategoria = arqCategoria.read(categoria).getId();  // Busca o ID da categoria pelo nome
            } catch (Exception e) {
                System.err.println("\nCategoria inválida! Tente novamente");
                catEscolhida = false;
            }
        } while (catEscolhida == false);

        try {
            Tarefa novaTarefa = new Tarefa(idCategoria, nome, dataCriacao, dataConclusao, status, prioridade);  // Cria uma nova tarefa
            arqTarefa.create(novaTarefa);  // Adiciona a tarefa ao arquivo
            System.out.println("Tarefa criada com sucesso.");
        } catch (Exception e) {
            System.err.println("Erro do sistema. Não foi possível criar a tarefa!");
        }
    }

    // Interface de Adição de Rotulo
    public void atualizarRotulo() throws Exception {
        String termo;
        int numeroTarefa = -1;
        Tarefa old = new Tarefa();
        ArrayList<Tarefa> tarefas = null;
        try {
            // Evitando Buffer
            sc.nextLine();
            while (tarefas == null || tarefas.size() == 0) {
                System.out.println("Digite o termo que deseja pesquisar no banco de tarefas: ");
                termo = sc.nextLine();
                tarefas = buscarTarefas(termo);
                //System.out.println("Tarefas: " + tarefas.size());
                if (tarefas == null || tarefas.size() == 0) {
                    System.out.println("Erro ao buscar tarefas, tente novamente com um termo diferente");
                }
            }
            while (numeroTarefa < 0 || numeroTarefa > tarefas.size()) {
                System.out.println("Digite o número da Tarefa que deseja atualizar\nObs: digite 0 para cancelar (favor ignorar a mensagem de erro)");
                numeroTarefa = sc.nextInt();
                if (numeroTarefa < 0 || numeroTarefa > tarefas.size()) {
                    System.out.println("Tarefa não encontrada, tente novamente");
                } else if (numeroTarefa == 0) {
                    return;
                }
            }
            old = tarefas.get(numeroTarefa - 1);
            System.out.println("Tarefa Selecionada: " + old.getNome());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Definindo Rotulos
        int newRotulo = 1;
        ArrayList<Rotulo> Rotulos = new ArrayList<>();
        ArrayList<Integer> posRotulosLista = new ArrayList<>();
        System.out.println("Deseja remover Rotulos ? (1 para sim, 0 para não)");
        newRotulo = sc.nextInt();
        while (newRotulo == 1) {
            System.out.println("Digite o índice da Rotulo que deseja remover dessa tarefa");
            System.out.println();
            Rotulos = arqRotulo.listar();
            System.out.println();
            posRotulosLista.add(sc.nextInt() - 1);
            System.out.println("Deseja remover mais Rotulos? (1 para sim, 0 para não)");
            newRotulo = sc.nextInt();
        }
        ArrayList<Integer> removed = new ArrayList<>();
        for (int i = 0; i < posRotulosLista.size(); i++) {
            removed.add(Rotulos.get(posRotulosLista.get(i)).getId());
        }
        posRotulosLista.clear();
        System.out.println("Deseja adicionar Rotulos ? (1 para sim, 0 para não)");
        newRotulo = sc.nextInt();
        while (newRotulo == 1) {
            System.out.println("Digite o índice da Rotulo que deseja adicionar dessa tarefa");
            System.out.println();
            Rotulos = arqRotulo.listar();
            System.out.println();
            posRotulosLista.add(sc.nextInt() - 1);
            System.out.println("Deseja adicionar mais Rotulos? (1 para sim, 0 para não)");
            newRotulo = sc.nextInt();
        }
        ArrayList<Integer> added = new ArrayList<>();
        for (int i = 0; i < posRotulosLista.size(); i++) {
            added.add(Rotulos.get(posRotulosLista.get(i)).getId());
        }

        if (arqTarefa.updateRotulos(old, removed, added)) {
            System.out.println("Rotulos atualizadas com sucesso");
        } else {
            System.out.println("Erro ao atualizar as Rotulos");
        }
    }

    public void listarPorCategoria() {
        System.out.println("\n> Buscar Tarefa por Categoria:");
        try {
            // Obtém todas as categorias existentes
            List<Categoria> categorias = arqCategoria.readAll();

            // Verifica se há categorias cadastradas
            if (categorias.isEmpty()) {
                System.out.println("Não há categorias cadastradas!");
            } else {
                // Lista as categorias disponíveis
                arqCategoria.listar();
                System.out.print("> ");
                int idCategoria = Integer.parseInt(sc.nextLine());

                // Verifica se o ID da categoria inserido é válido
                if (idCategoria > 0) {
                    // Obtém todas as tarefas associadas à categoria escolhida
                    List<Tarefa> tarefas = arqTarefa.readAll(idCategoria);

                    // Verifica se há tarefas cadastradas para a categoria escolhida
                    if (tarefas.isEmpty()) {
                        System.out.println("Não há tarefas cadastradas!");
                    } else {
                        // Exibe a lista de tarefas da categoria
                        System.out.println("\nLista de tarefas:");
                        for (Tarefa tarefa : tarefas) {
                            System.out.println(tarefa);
                        }
                    }
                } else {
                    System.out.println("ID inválido!");
                }
            }
        } catch (Exception e) { // Trata qualquer exceção inesperada que possa ocorrer
            System.out.println("Erro no sistema. Não foi possível buscar tarefa!");
        }
    }

    // Método para listar os status disponíveis
    private static void listaStatus() {
        System.out.println("\nEscolha o status:"
                + "\n0 - Pendente"
                + "\n1 - Em Progresso"
                + "\n2 - Concluída"
                + "\nStatus: ");
    }

    // Método para listar as prioridades disponíveis
    private static void listaPrioridades() {
        System.out.println("\nEscolha a prioridade:"
                + "\n0 - Baixa"
                + "\n1 - Média"
                + "\n2 - Alta"
                + "\n3 - Urgente"
                + "\nOpção: ");
    }
}
