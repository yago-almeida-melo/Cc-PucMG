package Main;

import java.util.Scanner;

public class Principal {
    protected static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            int opcao = 0;
            do {
                opcoesMenu();
                opcao = leOpcao();
                executaOpcao(opcao);
            } while(opcao != 0); 
        
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sc.close();
        }
    }// main()

    protected static void opcoesMenu() {
        System.out.println("\nAEDs-III");
        System.out.println("-------------------------");
        System.out.println("CRUD de Tarefas e Categorias");
        System.out.println("1 - Categorias");
        System.out.println("2 - Tarefas");
        System.out.println("0 - Fim");
        System.out.print  ("Opção: ");
    } 

    protected static int leOpcao() {
        int opcao = 0;
        try {
            opcao = Integer.valueOf(sc.nextLine());
        } catch(NumberFormatException e) {
            opcao = -1;
        } 
        return opcao;
    } 

    protected static void executaOpcao(int opcao) throws Exception {
        switch(opcao) {
            case 0:
                System.out.println("Fim do programa.");
                break;
            case 1:
                (new MenuCategorias()).menu();
                break;
            case 2:
                (new MenuTarefas()).menu();
                break;
            default:
                System.out.println("Opção inválida!");
                break;
        } 
    } 
} 

