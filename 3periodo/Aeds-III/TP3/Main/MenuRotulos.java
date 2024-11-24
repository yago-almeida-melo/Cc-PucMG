package Main;

import Entidades.*;
import File.*;
import java.util.ArrayList;
import java.util.Scanner;

public class MenuRotulos {

    public static ArquivoRotulo arqRotulo;
    private static Scanner sc = new Scanner(System.in);

    public void menu() throws Exception {
        arqRotulo = new ArquivoRotulo();
        int opcao;
        do {
            System.out.println("\nAEDsIII");
            System.out.println("-------");
            System.out.println("- Inicio Rotulos");
            System.out.println("1 - Incluir");
            System.out.println("2 - Alterar");
            System.out.println("3 - Excluir");
            System.out.println("4 - Listar");
            System.out.println("0 - Voltar");

            System.out.print("Opção: ");
            try {
                opcao = Integer.valueOf(sc.nextLine());  // Leitura da opção do usuário
            } catch (NumberFormatException e) {
                opcao = -1;  // Caso o valor não seja numérico, define a opção como inválida
            }

            // Switch para tratar cada opção do menu
            switch (opcao) {
                case 1:
                    incluirRotulo();  // Chama o método para incluir uma nova Rotulo
                    break;
                case 2:
                    alterarRotulo();  // Chama o método para alterar uma Rotulo existente
                    break;
                case 3:
                    excluirRotulo();  // Chama o método para excluir uma Rotulo
                    break;
                case 4:
                    listarRotulos();  // Chama o método para listar todas as Rotulos
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opcao invalida!");  // Mensagem para opção inválida
                    break;
            }

        } while (opcao != 0);  // Repete o menu até que o usuário escolha sair
    }

    // Método para buscar uma Rotulo pelo nome
    public void listarRotulos() {

        String nomeEtiqueta;
        try {
            System.out.println("Digite o nome da etiqueta que deseja listar as tarefas");
            System.out.println();
            arqRotulo.listar();

            nomeEtiqueta = sc.nextLine();
            ArrayList<Tarefa> t = arqRotulo.read(nomeEtiqueta);
            for(int i = 0; i < t.size(); i++) {
               t.get(i).toString();
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // Método para incluir uma nova Rotulo
    public void incluirRotulo() throws Exception {
        String nome;
        boolean dadosCompletos = false;

        System.out.println("\nInclusao de Rotulo");
        do {
            System.out.print("\nNome da Rotulo (min. de 5 letras): ");
            nome = sc.nextLine();
            if (nome.length() >= 5 || nome.length() == 0) {
                dadosCompletos = true;
            } else {
                System.err.println("O nome da Rotulo deve ter no mínimo 5 caracteres.");
            }
        } while (!dadosCompletos);  // Solicita o nome da Rotulo até que tenha pelo menos 5 caracteres

        if (nome.length() == 0) {
            return;  // Retorna se o nome não for informado
        }
        System.out.println("Confirma a inclusao da Rotulo? (S/N) ");
        char resp = sc.nextLine().charAt(0);
        if (resp == 'S' || resp == 's') {
            
                Rotulo c = new Rotulo(nome);  // Cria um novo Rotulo
                arqRotulo.create(c);  // Adiciona o Rotulo ao arquivo
                System.out.println("Rotulo criada com sucesso.");
            
        }
    }

    // Método para alterar uma Rotulo existente
    public void alterarRotulo() throws Exception {
        String nome;

        System.out.println("\nAlterar Rotulo");
        System.out.print("\nNome da Rotulo (min. de 5 letras): \n\n");
        arqRotulo.listar();

        nome = sc.nextLine();

        if (nome.length() == 0) {
            return;  // Retorna se o nome não for informado
        }
        System.out.println("Confirma a alteracao da Rotulo? (S/N) ");
        char resp = sc.nextLine().charAt(0);
        if (resp == 'S' || resp == 's') {
            try {
                System.out.println("Digite o novo nome da Rotulo: ");
                String novo = sc.nextLine();
                arqRotulo.update(nome, novo);
                System.out.println("Rotulo atualizada com sucesso.");
            } catch (Exception e) {
                System.err.println("Erro do sistema. Nao foi possivel criar a Rotulo!");
            }
        }
    }

    // Método para excluir uma Rotulo
    public void excluirRotulo() throws Exception {
        String nome;

        System.out.println("\nExcluir Rotulo: \n\n");
        ArrayList<Rotulo> rotulos = arqRotulo.listar();
        int id = sc.nextInt();
        while(id < 0 || id >= rotulos.size()){
            System.out.println("Digite um valor valido");
            id = sc.nextInt();
        }
        nome = rotulos.get(id-1).getNome();
        System.out.println("Confirma a exclusao da Rotulo? (S/N) ");
        char resp = sc.nextLine().charAt(0);
        if (resp == 'S' || resp == 's') {
            try {
                if (arqRotulo.delete(nome)) {  // Exclui a Rotulo do arquivo
                    System.out.println("Rotulo excluido com sucesso.");
                } else {
                    System.err.println("Erro do sistema. Nao foi possível excluir o Rotulo!");
                }
            } catch (Exception e) {
                System.err.println("Erro do sistema. Nao foi possível excluir o Rotulo!");
            }
        }
    }
}
