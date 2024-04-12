/*
	806454 - Yago Almeida Melo
	TP4 - Q05 / Arvore TreeSort
*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

class No {
    public Jogador elemento; // Conteudo do no.
    public No esq, dir; // Filhos da esq e dir.

    /**
     * Construtor da classe.
     * 
     * @param elemento Conteudo do no.
     */
    public No(Jogador elemento) {
        this(elemento, null, null);
    }

    /**
     * Construtor da classe.
     * 
     * @param elemento Conteudo do no.
     * @param esq      No da esquerda.
     * @param dir      No da direita.
     */
    public No(Jogador elemento, No esq, No dir) {
        this.elemento = elemento;
        this.esq = esq;
        this.dir = dir;
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////// CLASSE TreeSort
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// ///////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

class TreeSort {
    private No raiz; // Raiz da arvore.
    static int comparacoes = 0, movimentacoes = 0;
    private int n;

    // Construtor de TreeSort
    public TreeSort() {
        raiz = null;
        n = 0;
    }

    // Metodos para ordenar os jogadores
    public Jogador[] sort() {
        Jogador[] array = new Jogador[n];
        n = 0;
        sort(raiz, array);
        return array;
    }

    private void sort(No i, Jogador[] array) {
        if (i != null) {
            sort(i.esq, array);
            array[n++] = i.elemento;
            movimentacoes++;
            sort(i.dir, array);
        }
    }

    // Metodos para inserir um jogador na arvore
    public void inserir(Jogador x) {
        n++;
        raiz = inserir(x, raiz);
    }


    private No inserir(Jogador x, No i) {
        if (i == null) {
            i = new No(x);
        } else if (x.getNome().compareTo(i.elemento.getNome()) < 0) {
            i.esq = inserir(x, i.esq);
        } else if (x.getNome().compareTo(i.elemento.getNome()) >= 0) {
            i.dir = inserir(x, i.dir);
        }
        return i;
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
            fw.write("Matricula: 806454\t" + "Comparacoes: " + comparacoes + '\t' +"Movimentacoes: "+ movimentacoes +'\t'+ "Tempo de execucao: " + tempo
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
    public static void addJogadores(TreeSort arvore) {
        String id = MyIO.readLine();
        boolean fim = false;
        while (!fim) {
            try {
                Jogador jogador = new Jogador();
                jogador.procuraID(id);
                arvore.inserir(jogador);
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
        TreeSort arvore = new TreeSort();
        long tempo = 0;
        addJogadores(arvore);
        long start = System.nanoTime();
        Jogador[] jogadores = arvore.sort();
        long end = System.nanoTime();
        tempo = (end - start) / 10000;
        for (int i = 0; i < jogadores.length; i++) {
            MyIO.println(jogadores[i].getNome());
        }
        arvore.registroLog(tempo);
    }
}