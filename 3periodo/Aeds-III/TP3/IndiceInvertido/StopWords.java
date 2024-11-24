package IndiceInvertido;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;

public class StopWords {
  // Lista de StopWords
  public ArrayList<String> stopWords = new ArrayList<String>();
  public ListaInvertida lista;

  public String[] stopWordsCheck(String titulo) {
    String chaves[] = titulo.split(" ");
    for (int i = 0; i < chaves.length; i++) {
      chaves[i] = chaves[i].toLowerCase();
    }
    for (int i = 0; i < chaves.length; i++) {
      for (int j = 0; j < stopWords.size(); j++) {
        if (chaves[i].equals(stopWords.get(j))) {
          chaves[i] = "";
        }
      }
    }
    return chaves;
  }

  public void wordsCounter(ArrayList<ElementoLista> elementos, String[] chaves, int idChave) {
    int qtdChaves = 0;
    for (int i = 0; i < chaves.length; i++) {
      if (!chaves[i].equals("") && !chaves[i].equals(" ")) {
        qtdChaves++;
      }
    }
    for (int i = 0; i < chaves.length; i++) {
      float frequencia = 1;
      if (!chaves[i].equals("") && !chaves[i].equals(" ")) {
        for (int j = 0; j < chaves.length; j++) {
          if (chaves[i].equals(chaves[j]) && i != j) {
            frequencia++;
            chaves[j] = "";
          }
        }
      }
      elementos.add(new ElementoLista(idChave, frequencia / qtdChaves));
    }
    // return chaves;
  }

  // Construtor
  public StopWords() {
    File arquivo;
    try {
      arquivo = new File("IndiceInvertido//stopwords.txt");
      Scanner scanner = new Scanner(arquivo);
      while (scanner.hasNext()) {
        String linha = scanner.nextLine();
        linha = linha.toLowerCase();
        // Como cada linha do arquivo possui um " " no final ao invés de um um "\n" ou
        // "\0", é necessário retirar o último caractere de cada linha
        linha = linha.substring(0, linha.length() - 1);
        stopWords.add(linha);
      }
      scanner.close();
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Erro ao abrir o arquivo de StopWords");
    }
    try {
      File d = new File("BaseDeDados");
      if (!d.exists())
        d.mkdir();
      lista = new ListaInvertida(
          4,
          "BaseDeDados//dicionario.listainv.db",
          "BaseDeDados//blocos.listainv.db");
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Erro ao criar a lista invertida");
    }
  }

  // Inserção na lista invertida
  public void inserir(String titulo, int id) {
    String[] chaves;
    chaves = stopWordsCheck(titulo);
    ArrayList<ElementoLista> elementos = new ArrayList<ElementoLista>();
    wordsCounter(elementos, chaves, id);
    for (int i = 0; i < chaves.length; i++) {
      if (chaves[i] != "" && chaves[i] != " ") {
        try {
          lista.create(chaves[i], elementos.get(i));
        } catch (Exception e) {
          e.printStackTrace();
          System.out.println("Erro ao inserir na lista invertida");
        }
      }
    }
    try {
      lista.incrementaEntidades();
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Erro ao incrementar entidades");
    }
  }

  // Método principal apenas para testes
  public void referencia(String[] args) {
    // Inicialização de Variáveis
    StopWords stopWords = new StopWords();
    Scanner console = new Scanner(System.in);
    boolean novaFrase = true;

    try {
      File d = new File("BaseDeDados");
      if (!d.exists())
        d.mkdir();
      lista = new ListaInvertida(
        4,
      "BaseDeDados//dicionario.listainv.db", 
      "BaseDeDados//blocos.listainv.db");

      // Adicionando chaves na lista invertida
      while (novaFrase) {
        System.out.println("Digite o título da tarefa: ");
        String titulo = console.nextLine();
        String[] chaves;
        chaves = stopWords.stopWordsCheck(titulo);
        ArrayList<ElementoLista> elementos = new ArrayList<ElementoLista>();
        stopWords.wordsCounter(elementos, chaves, 0);
        for (int i = 0; i < chaves.length; i++) {
          if (chaves[i] != "" && chaves[i] != " ") {
            lista.create(chaves[i], elementos.get(i));
          }
        }
        System.out.println("Deseja adicionar outra tarefa? (S/N)");
        String resposta = console.nextLine();
        if (resposta.equals("N") || resposta.equals("n")) {
          novaFrase = false;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    // Menu de opções
    try {
      File d = new File("BaseDeDados");
      if (!d.exists())
        d.mkdir();
      lista = new ListaInvertida(
        4, 
        "BaseDeDados//dicionario.listainv.db", 
        "BaseDeDados//blocos.listainv.db");

      int opcao;
      do {
        System.out.println("\n\n-------------------------------");
        System.out.println("              MENU");
        System.out.println("-------------------------------");
        System.out.println("1 - Inserir");
        System.out.println("2 - Buscar");
        System.out.println("3 - Excluir");
        System.out.println("4 - Imprimir");
        System.out.println("5 - Incrementar entidades");
        System.out.println("6 - Decrementar entidades");
        System.out.println("0 - Sair");
        try {
          opcao = Integer.valueOf(console.nextLine());
        } catch (NumberFormatException e) {
          opcao = -1;
        }

        switch (opcao) {
          case 1: {
            System.out.println("\nINCLUSÃO");
            System.out.print("Termo: ");
            String chave = console.nextLine();
            System.out.print("ID: ");
            int id = Integer.valueOf(console.nextLine());
            System.out.print("Frequência: ");
            float frequencia = Float.valueOf(console.nextLine());
            lista.create(chave, new ElementoLista(id, frequencia));
            lista.print();
          }
            break;
          case 2: {
            System.out.println("\nBUSCA");
            System.out.print("Chave: ");
            String chave = console.nextLine();
            ElementoLista[] elementos = lista.read(chave);
            System.out.print("Elementos: ");
            for (int i = 0; i < elementos.length; i++)
              System.out.print(elementos[i] + " ");
          }
            break;
          case 3: {
            System.out.println("\nEXCLUSÃO");
            System.out.print("Chave: ");
            String chave = console.nextLine();
            System.out.print("ID: ");
            int id = Integer.valueOf(console.nextLine());
            lista.delete(chave, id);
            lista.print();
          }
            break;
          case 4: {
            lista.print();
          }
            break;
          case 5: {
            lista.incrementaEntidades();
            System.out.println("Entidades: " + lista.numeroEntidades());
          }
            break;
          case 6: {
            lista.decrementaEntidades();
            System.out.println("Entidades: " + lista.numeroEntidades());
          }
            break;
          case 0:
            break;
          default:
            System.out.println("Opção inválida");
        }
      } while (opcao != 0);

    } catch (Exception e) {
      e.printStackTrace();
    }
    console.close();
  }
}