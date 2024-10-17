package Main;

import java.util.Scanner;


public class Principal {
    protected static Scanner sc = new Scanner(System.in);
    protected static final String GREEN = "\u001B[32m";
    protected static final String RED = "\u001B[31m";
    protected static final String RESET = "\u001B[0m";
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
    } 

    protected static void opcoesMenu() {
        System.out.println("\nAEDs-III 1.0           ");
        System.out.println("-------------------------");
        System.out.println("> Início                 ");
        System.out.println("1 - Categorias           ");
        System.out.println("2 - Tarefas              ");
        System.out.println("0 - Sair                 ");
        System.out.print  ("Opção: "                 );
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
                System.out.println("Saindo...");
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

