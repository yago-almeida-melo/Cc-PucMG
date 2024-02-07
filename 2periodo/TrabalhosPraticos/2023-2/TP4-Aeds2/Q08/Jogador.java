/*
	806454 - Yago Almeida Melo
	TP4 - Q08 / Hash com Rehash
*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////// CLASSE Hash
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// ///////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

class Hash {
    Jogador tabela[];
    int m;
    final int NULO = -1;
    static int comparacoes = 0;
    static double tempo = 0;

    /*
     * Construtores da classe Hash
     */
    public Hash() {
        this(21);
    }

    public Hash(int m) {
        this.m = m;
        this.tabela = new Jogador[this.m];
        for (int i = 0; i < m; i++) {
            tabela[i] = null;
        }
    }

    // Metodo de Hash
    public int h(Jogador elemento) {
        return elemento.getAltura() % m;
    }

    //metodo para o Rehash
    public int rehash(Jogador elemento) {
        return (elemento.getAltura() + 1) % m;

    }

    // Metodo de Inserir na Tabela Hash com Rehash
    public void inserir(Jogador elemento) throws Exception {
        int i = h(elemento);
        if (elemento.getId() == -1) {
            throw new Exception("Erro!");
        } else if (tabela[i] == null) {
            tabela[i] = elemento;
        } else if (tabela[i] != null) {
            i = rehash(elemento);
            if (tabela[i] == null) {
                tabela[i] = elemento;
            } else {
                throw new Exception("Erro! Rehash nao funcionou!");
            }
        } else {
            throw new Exception("Erro!");
        }
    }

    //Metodo para pesquisar na tabela Hash com Rehash
    public void pesquisar() {
        String elemento = MyIO.readLine();
        while (!elemento.equals("FIM")) {
            boolean resp = false;
            MyIO.print(elemento);
            double start = System.nanoTime();
            for (int i = 0; i < m && resp == false; i++) {
                if (tabela[i] != null && tabela[i].getNome().equals(elemento)) {
                    MyIO.print(" SIM\n");
                    resp = true;
                }
            }
            double end = System.nanoTime();
            tempo += end - start;
            if (resp == false) {
                MyIO.print(" NAO\n");
            }
            elemento = MyIO.readLine();
        }
    }

    /*
     * Metodo para registrar o log de execucao com movimentacoes
     */
    public static void registroLog() {
        File file = new File("806454_arvoreArvore.txt");
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
        String resp = getId() + " ## " + getNome() + " ## " + getAltura() + " ## " + getPeso() + " ## "
                + getAnoNascimento() + " ## " + getUniversidade() + " ## " + getCidadeNascimento() + " ## "
                + getEstadoNascimento() + " ##";
        return resp;
    }

    /*
     * Metodo para add jogadores em um array
     */
    public static void addJogadores(Hash hash) {
        String id = MyIO.readLine();
        boolean fim = false;
        while (!fim) {
            try {
                Jogador jogador = new Jogador();
                jogador.procuraID(id);
                hash.inserir(jogador);
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
        Hash hash = new Hash();
        addJogadores(hash);
        hash.pesquisar();
    }
}
