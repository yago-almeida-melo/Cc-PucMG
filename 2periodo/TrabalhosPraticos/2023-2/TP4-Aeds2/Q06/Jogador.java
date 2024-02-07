/*
 *      806454 - Yago Almeida Melo
 *      TP4 - Q06 / Arvore Trie
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

class No {
    public char elemento;
    public final int tamanho = 255; // MAX ASCII
    public No[] prox; // Array com 255 posicoes
    public boolean folha;
    
    /*
     * Construtores da classe.
     */
    public No() {
        this(' ');
    }

    public No(char elemento) {
        this.elemento = elemento;
        prox = new No[tamanho];
        for (int i = 0; i < tamanho; i++){
            prox[i] = null; 
        } 
        folha = false;
    }
}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////// CLASSE Trie   ///////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
class Trie {

    public static No raiz;
    public static int comparacoes = 0;
    public static double tempo = 0;

    /*
     * Construtor da classe.
     */
    public Trie() {
        raiz = new No();
    }

    // Metodos para inserir os jogadores
    public void inserir(String str) {
        inserir(str, raiz, 0);
    }

    private void inserir(String str, No no, int i) {
        if (no.prox[str.charAt(i)] == null) {
            no.prox[str.charAt(i)] = new No(str.charAt(i));
            if (i == str.length() - 1){
                no.prox[str.charAt(i)].folha = true;
            } else{
                inserir(str, no.prox[str.charAt(i)], i + 1);
            }
        } else if (no.prox[str.charAt(i)].folha == false && i < str.length() - 1){
            inserir(str, no.prox[str.charAt(i)], i + 1);
        }
    }

    // Metodos para mostrar os jogadores
    public void mostrar() {
        mostrar("", raiz);
    }

    public void mostrar(String s, No no) {
        if (no.folha == true)
            MyIO.println(s + no.elemento);
        else {
            for (int i = 0; i < no.prox.length; i++) {
                if (no.prox[i] != null) {
                    mostrar(s + no.elemento, no.prox[i]);
                }
            }
        }
    }

    // Metodos para pesquisar os jogadores
    public boolean pesquisar(String s) {
        return pesquisar(s, raiz, 0);
    }

    public boolean pesquisar(String s, No no, int i) {
        boolean resp = false;
        comparacoes++;
        if (no.prox[s.charAt(i)] == null) {
            resp = false;
        } else if (i == s.length() - 1) {
            resp = (no.prox[s.charAt(i)].folha == true);
        } else if (i < s.length() - 1) {
            resp = pesquisar(s, no.prox[s.charAt(i)], i + 1);
        }

        return resp;
    }

    /*
     * Metodo para registrar o log de execucao com movimentacoes
     */
    public void registroLog(long tempo) {
        File file = new File("/tmp/806454_treesort.txt");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file, false);
            fw.write("Matricula: 806454\t" + "Comparacoes: " + comparacoes + '\t' + "Tempo de execucao: " + tempo
                    + " milisegundos");
            fw.close();
        } catch (Exception e) {
            e.getMessage();
        }
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////// CLASSE JOGADOR
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// ///////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
class Jogador {
    /*
     * Atributos da classe Jogador
     */
    static int tamanho;
    private int id, altura, peso, anoNascimento;
    private String nome, universidade, cidadeNascimento, estadoNascimento;

    /*
     * Construtores da classe Jogador
     */
    Jogador() {
        this.id = -1;
        this.altura = -1;
        this.peso = -1;
        this.anoNascimento = -1;
        this.nome = null;
        this.universidade = null;
        this.cidadeNascimento = null;
        this.estadoNascimento = null;
    }

    Jogador(int id, int altura, int peso, int anoNascimento, String nome, String universidade,
            String cidadeNascimento,
            String estadoNascimento) {
        this.id = id;
        this.altura = altura;
        this.peso = peso;
        this.anoNascimento = anoNascimento;
        this.nome = nome;
        this.universidade = universidade;
        this.cidadeNascimento = cidadeNascimento;
        this.estadoNascimento = estadoNascimento;
    }

    /*
     * Gets e Sets da classe Jogador
     */
    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }

    public int getAltura() {
        return altura;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public int getPeso() {
        return peso;
    }

    public void setAnoNascimento(int anoNascimento) {
        this.anoNascimento = anoNascimento;
    }

    public int getAnoNascimento() {
        return anoNascimento;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setUniversidade(String universidade) {
        this.universidade = universidade;
    }

    public String getUniversidade() {
        return universidade;
    }

    public void setCidadeNascimento(String cidadeNascimento) {
        this.cidadeNascimento = cidadeNascimento;
    }

    public String getCidadeNascimento() {
        return cidadeNascimento;
    }

    public void setEstadoNascimento(String estadoNascimento) {
        this.estadoNascimento = estadoNascimento;
    }

    public String getEstadoNascimento() {
        return estadoNascimento;
    }

    /*
     * Metodo para setar os dados do arquivo csv
     */
    public void setDados(String linha) {
        String[] dados = linha.split(",");
        setId(Integer.parseInt(dados[0]));
        setNome(dados[1]);
        setAltura(Integer.parseInt(dados[2]));
        setPeso(Integer.parseInt(dados[3]));
        if (dados.length > 4 && !dados[4].isEmpty())
            setUniversidade(dados[4]);
        else
            setUniversidade("nao informado");
        setAnoNascimento(Integer.parseInt(dados[5]));
        if (dados.length > 6 && !dados[6].isEmpty())
            setCidadeNascimento(dados[6]);
        else
            setCidadeNascimento("nao informado");
        if (dados.length > 7 && !dados[7].isEmpty())
            setEstadoNascimento(dados[7]);
        else
            setEstadoNascimento("nao informado");
    }

    /*
     * Metodo para clonar um objeto da classe Jogador
     */
    public Jogador clone() throws CloneNotSupportedException {
        Jogador novo = new Jogador();
        novo.id = this.id;
        novo.altura = this.altura;
        novo.peso = this.peso;
        novo.anoNascimento = this.anoNascimento;
        novo.nome = this.nome;
        novo.universidade = this.universidade;
        novo.cidadeNascimento = this.cidadeNascimento;
        novo.estadoNascimento = this.estadoNascimento;
        return novo;
    }

    /*
     * Metodo para imprimir os dados da classe Jogador
     */
    public void Imprimir(int id) {
        System.out.println(toString());
    }

    /*
     * Metodo para transformar em string os dados da classe Jogador
     */
    public String toString() {
        String resp = getNome() + " ## " + getAltura() + " ## " + getPeso() + " ## "
                + getAnoNascimento() + " ## " + getUniversidade() + " ## " + getCidadeNascimento() + " ## "
                + getEstadoNascimento() + " ##";
        return resp;
    }

    /*
     * Metodo para add jogadores em um array
     */
    public static void addJogadores(Trie trie) {
        String id = MyIO.readLine();
        boolean fim = false;
        while (!fim) {
            try {
                Jogador jogador = new Jogador();
                jogador.procuraID(id);
                trie.inserir(jogador.getNome());
                tamanho++;
            } catch (Exception e) {
                e.getMessage();
            }
            id = MyIO.readLine();
            if (id.equals("FIM"))
                fim = true;
        }
    }

    /*
     * Metodo para procurar um jogador pelo id e salva
     */
    public void procuraID(String id) throws Exception {
        FileReader file = new FileReader("/tmp/players.csv");
        BufferedReader bf = new BufferedReader(file);
        Integer idInt = Integer.parseInt(id);
        String linha;
        while ((linha = bf.readLine()) != null) {
            String numId = linha.substring(0, linha.indexOf(","));
            try {
                if (Integer.parseInt(numId) == idInt) {
                    setDados(linha);
                    break;
                }
            } catch (NumberFormatException e) {
                e.getMessage();
            }
        }
        bf.close();
        file.close();
    }

    public static void main(String[] args) {
        Trie arvore = new Trie();
        addJogadores(arvore);
        addJogadores(arvore);
        String input = MyIO.readLine();
        while (!input.equals("FIM")) {
            long start = System.nanoTime();
            if (arvore.pesquisar(input)){
                MyIO.println(input + " SIM");
            } else{
                MyIO.println(input + " NAO");
            }
            long end = System.nanoTime();
            long tempo = (end - start) / 10000;
            arvore.registroLog(tempo);
            input = MyIO.readLine();
        }
    }
}
