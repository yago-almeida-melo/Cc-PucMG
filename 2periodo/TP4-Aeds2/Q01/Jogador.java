package Q01;
/*
	806454 - Yago Almeida Melo
	TP4 - Q01 / Arvore Binaria
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
////////////////////// CLASSE ARVORE BINARIA
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// ///////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

class ArvoreBinaria {
	private No raiz; // Raiz da arvore.
	static int comparacoes = 0, movimentacoes = 0;

	/**
	 * Construtor da classe.
	 */
	public ArvoreBinaria() {
		raiz = null;
	}

	/**
	 * Metodo publico iterativo para pesquisar elemento.
	 * 
	 * @param x Elemento que sera procurado.
	 * @return <code>true</code> se o elemento existir,
	 *         <code>false</code> em caso contrario.
	 */
	public boolean pesquisar(String x) {
		return pesquisar(x, raiz);
	}

	/**
	 * Metodo privado recursivo para pesquisar elemento.
	 * 
	 * @param x Elemento que sera procurado.
	 * @param i No em analise.
	 * @return <code>true</code> se o elemento existir,
	 *         <code>false</code> em caso contrario.
	 */
	private boolean pesquisar(String x, No i) {
		boolean resp;
		if (i == null) {
			resp = false;
			MyIO.print(" NAO\n");

		} else if (comparacao(x, i.elemento.getNome()) == 0) {
			resp = true;
			MyIO.print(" SIM\n");

		} else if (comparacao(x, i.elemento.getNome()) < 0) {
			MyIO.print(" esq");
			resp = pesquisar(x, i.esq);

		} else {
			MyIO.print(" dir");
			resp = pesquisar(x, i.dir);
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

	/**
	 * Metodo publico iterativo para inserir elemento.
	 * 
	 * @param x Elemento a ser inserido.
	 * @throws Exception Se o elemento existir.
	 */
	public void inserir(Jogador x) throws Exception {
		raiz = inserir(x, raiz);
	}

	/**
	 * Metodo privado recursivo para inserir elemento.
	 * 
	 * @param x Elemento a ser inserido.
	 * @param i No em analise.
	 * @return No em analise, alterado ou nao.
	 * @throws Exception Se o elemento existir.
	 */
	private No inserir(Jogador x, No i) throws Exception {
		if (i == null) {
			i = new No(x);

		} else if (comparacao(x.getNome(), i.elemento.getNome()) < 0) {
			i.esq = inserir(x, i.esq);

		} else if (comparacao(x.getNome(), i.elemento.getNome()) > 0) {
			i.dir = inserir(x, i.dir);

		} else {
			throw new Exception("Erro ao inserir!");
		}

		return i;
	}

	/*
	 * Metodo para registrar o log de execucao com movimentacoes
	 */
	public static void registroLog(long tempo) {
		File file = new File("/tmp/806454_arvoreBinaria.txt");
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file, false);
			fw.write("Matricula: 806454\t" + "Comparacoes: " + comparacoes + '\t' + "Tempo de execucao: " + tempo + " milisegundos");
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
	public static void addJogadores(ArvoreBinaria arvore) {
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
		ArvoreBinaria arvore = new ArvoreBinaria();
		long tempo = 0;
		addJogadores(arvore);
		String nome = MyIO.readLine();
		boolean fim = false;
		while (!fim) {
			try {
				MyIO.print(nome + " raiz");
				long start = System.nanoTime();
				arvore.pesquisar(nome);
				long end = System.nanoTime();
				tempo = (end - start) / 10000;
			} catch (Exception e) {
				e.getMessage();
			}
			nome = MyIO.readLine();
			if (nome.equals("FIM"))
				fim = true;
		}
		arvore.registroLog(tempo);
	}
}