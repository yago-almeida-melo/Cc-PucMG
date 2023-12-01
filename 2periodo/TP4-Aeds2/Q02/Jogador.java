/*
	806454 - Yago Almeida Melo
	TP4 - Q02 / Arvore de Arvore
*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

/*
 * Classe No
 */
class No {
    public int elemento; // Conteudo do no.
    public No esq; // No da esquerda.
    public No dir; // No da direita.
    public No2 raiz2;

    /**
     * Construtor da classe.
     * 
     * @param elemento Conteudo do no.
     */
    No(int elemento) {
        this.elemento = elemento;
        this.esq = this.dir = null;
        this.raiz2 = null;
    }

    /**
     * Construtor da classe.
     * 
     * @param elemento Conteudo do no.
     * @param esq      No da esquerda.
     * @param dir      No da direita.
     */
    No(int elemento, No esq, No dir) {
        this.elemento = elemento;
        this.esq = esq;
        this.dir = dir;
        this.raiz2 = null;
    }
}

///////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////// CLASSE NO2
/////////////////////////////////////////////////////////////////////////////////////////////////////// /////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////
class No2 {
    public Jogador elemento; // Conteudo do no.
    public No2 esq; // No da esquerda.
    public No2 dir; // No da direita.

    /**
     * Construtor da classe.
     * 
     * @param elemento Conteudo do no.
     */
    No2(Jogador elemento) {
        this.elemento = elemento;
        this.esq = this.dir = null;
    }

    /**
     * Construtor da classe.
     * 
     * @param elemento Conteudo do no.
     * @param esq      No2 da esquerda.
     * @param dir      No2 da direita.
     */
    No2(Jogador elemento, No2 esq, No2 dir) {
        this.elemento = elemento;
        this.esq = esq;
        this.dir = dir;
    }
}

///////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////// ARVORE DE ARVORE
/////////////////////////////////////////////////////////////////////////////////////////////// ///////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////
class ArvoreArvore {
    public No raiz; // Raiz da arvore.
    static int comparacoes = 0, movimentacoes = 0;

    /**
     * Construtor da classe.
     */
    public ArvoreArvore() {
        raiz = null;
        inserir(7);
        inserir(3);
        inserir(11);
        inserir(1);
        inserir(5);
        inserir(9);
        inserir(13);
        inserir(0);
        inserir(2);
        inserir(4);
        inserir(6);
        inserir(8);
        inserir(10);
        inserir(12);
        inserir(14);
    }

    public void inserir(int altMod) {
        raiz = inserir(altMod, raiz);
    }

    private No inserir(int x, No i) {
        if (i == null) {
            i = new No(x);
        } else if (x < i.elemento) {
            i.esq = inserir(x, i.esq);
        } else if (x > i.elemento) {
            i.dir = inserir(x, i.dir);
        } else {
            MyIO.println("Erro ao inserir Nó!");
        }
        return i;
    }

    public void inserir(Jogador s) {
        inserir(s, raiz.raiz2);
    }

    public void inserir(Jogador s, No i) {
        if (i == null) {
            MyIO.println("Erro ao inserir");
        } else if ((s.getAltura() % 15) < (i.elemento)) {
            comparacoes++;
            inserir(s, i.esq);
        } else if ((s.getAltura() % 15) > i.elemento) {
            comparacoes++;
            inserir(s, i.dir);
        } else {
            i.raiz2 = inserir(s, i.raiz2);
        }
    }

    private No2 inserir(Jogador s, No2 i) {
        if (i == null) {
            i = new No2(s);
        } else if (comparacao(s.getNome(), i.elemento.getNome()) < 0) {
            i.esq = inserir(s, i.esq);
        } else if (comparacao(s.getNome(), i.elemento.getNome()) > 0) {
            i.dir = inserir(s, i.dir);
        } else {
            MyIO.println("Erro ao inserir, elemento existente");
        }
        return i;
    }

    public boolean mostrar(String s) {
        MyIO.print(s + " raiz");
        return mostrar(raiz, s);
    }

    public boolean mostrar(No i, String s) {
        boolean resp = false;
        if (i != null) {
            if(!resp){
                resp = mostrar(i.raiz2, s);
                MyIO.print(" esq");
                resp = mostrar(i.esq, s);
                MyIO.print(" dir");
                resp = mostrar(i.dir, s);
            }
        }
        return resp;
    }

    public boolean mostrar(No2 i, String s) {
        boolean resp = false;
        if (i != null) {
            if (i.elemento.getNome().equals(s)) {
                resp = true;
            } else {
                MyIO.print(" ESQ");
                resp = mostrar(i.esq, s);
                if(!resp){
                    MyIO.print(" DIR");
                    resp = mostrar(i.dir, s);
                }
            }
        }
        return resp;
    }

    /*
     * Metodo para comparar dois elementos da lista
     */
    public static int comparacao(String pri, String seg) {
        int rsp = pri.compareTo(seg);
        comparacoes++;
        return rsp;
    }

    /*
     * Metodo para registrar o log de execucao com movimentacoes
     */
    public static void registroLog(long tempo) {
        File file = new File("806454_arvoreArvore.txt");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file, false);
            fw.write("Matricula: 806454\t" + "Comparacoes: " + comparacoes + '\t' +"Movimentações: "+ movimentacoes + '\t' + "Tempo de execucao: " + tempo
                    + " milisegundos");
            fw.close();
        } catch (Exception e) {
            e.getMessage();
        }
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////// CLASSE JOGADOR
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// //////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
class Jogador {
    /*
     * Atributos da classe Jogador
     */
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
     * Metodo para add jogadores 
     */
    public static void addJogadores(ArvoreArvore arvore) {
        String id = MyIO.readLine();
        boolean fim = false;
        while (!fim) {
            try {
                Jogador jogador = new Jogador();
                jogador.procuraID(id);
                arvore.inserir(jogador);
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
        FileReader file = new FileReader("players.csv");
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

    /*
     * Metodo para procurar um jogador pelo nome e salva
     */
    public void procuraNome(String nome) throws Exception {
        FileReader file = new FileReader("players.csv");
        BufferedReader bf = new BufferedReader(file);
        String linha;
        while ((linha = bf.readLine()) != null) {
            String[] linhaFinal = linha.split(", ", 8);
            try {
                if (linhaFinal[1].equals(nome)) {
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
        ArvoreArvore arvore = new ArvoreArvore();
        long tempo = 0;
        addJogadores(arvore);
        String nome = MyIO.readLine();
        boolean fim = false;
        while (!fim) {
            try {
                long start = System.nanoTime();
                if(!arvore.mostrar(nome)) MyIO.println(" NAO");
                else MyIO.println(" SIM");
                long end = System.nanoTime();
                tempo = (end - start) / 10000;
            } catch (Exception e) {
                e.getMessage();
            }
            nome = MyIO.readLine();
            if (nome.equals("FIM"))
                fim = true;
        }
        ArvoreArvore.registroLog(tempo);
    }
}
