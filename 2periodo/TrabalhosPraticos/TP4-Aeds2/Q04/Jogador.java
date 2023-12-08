/*
	806454 - Yago Almeida Melo
	TP4 - Q04 / Arvore Alvinegra
*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

class NoAN {
   public boolean cor;
   public Jogador elemento;
   public NoAN esq, dir;

   public NoAN() {
      this(null);
   }

   public NoAN(Jogador elemento) {
      this(elemento, false, null, null);
   }

   public NoAN(Jogador elemento, boolean cor) {
      this(elemento, cor, null, null);
   }

   public NoAN(Jogador elemento, boolean cor, NoAN esq, NoAN dir) {
      this.cor = cor;
      this.elemento = elemento;
      this.esq = esq;
      this.dir = dir;
   }
}

/**
 * Arvore binaria de pesquisa
 * 
 * @author Max do Val Machado
 */
class Alvinegra {
   private NoAN raiz; // Raiz da arvore.
   static int comparacoes = 0;
   static double tempo = 0;

   /**
    * Construtor da classe.
    */
   public Alvinegra() {
      raiz = null;
   }

   /**
    * Metodo publico iterativo para pesquisar elemento.
    * 
    * @param elemento Elemento que sera procurado.
    * @return <code>true</code> se o elemento existir,
    *         <code>false</code> em caso contrario.
    */
   public boolean pesquisar(Jogador elemento) {
      return pesquisar(elemento, raiz);
   }

   /**
    * Metodo privado recursivo para pesquisar elemento.
    * 
    * @param elemento Elemento que sera procurado.
    * @param i        NoAN em analise.
    * @return <code>true</code> se o elemento existir,
    *         <code>false</code> em caso contrario.
    */
   private boolean pesquisar(Jogador elemento, NoAN i) {
      boolean resp;
      if (i == null) {
         resp = false;
      } else if (elemento == i.elemento) {
         resp = true;
      } else if (elemento.getNome().compareTo(i.elemento.getNome()) < 0) {
         resp = pesquisar(elemento, i.esq);
      } else {
         resp = pesquisar(elemento, i.dir);
      }
      return resp;
   }

   /**
    * Metodo publico iterativo para inserir elemento.
    * 
    * @param elemento Elemento a ser inserido.
    * @throws Exception Se o elemento existir.
    */
   public void inserir(Jogador j) throws Exception {
      // Se a arvore estiver vazia
      if (raiz == null) {
         raiz = new NoAN(j);
         // Senao, se a arvore tiver um elemento
      } else if (raiz.esq == null && raiz.dir == null) {
         if (j.getNome().compareTo(raiz.elemento.getNome()) < 0) {
            raiz.esq = new NoAN(j);
         } else {
            raiz.dir = new NoAN(j);
         }
         // Senao, se a arvore tiver dois elementos (raiz e dir)
      } else if (raiz.esq == null) {
         if (j.getNome().compareTo(raiz.elemento.getNome()) < 0) {
            raiz.esq = new NoAN(j);
         } else if (j.getNome().compareTo(raiz.dir.elemento.getNome()) < 0) {
            raiz.esq = new NoAN(raiz.elemento);
            raiz.elemento = j;
         } else {
            raiz.esq = new NoAN(raiz.elemento);
            raiz.elemento = raiz.dir.elemento;
            raiz.dir.elemento = j;
         }
         raiz.esq.cor = raiz.dir.cor = false;
         // Senao, se a arvore tiver dois elementos (raiz e esq)
      } else if (raiz.dir == null) {
         if (j.getNome().compareTo(raiz.elemento.getNome()) > 0) {
            raiz.dir = new NoAN(j);
         } else if (j.getNome().compareTo(raiz.esq.elemento.getNome()) > 0) {
            raiz.dir = new NoAN(raiz.elemento);
            raiz.elemento = j;
         } else {
            raiz.dir = new NoAN(raiz.elemento);
            raiz.elemento = raiz.esq.elemento;
            raiz.esq.elemento = j;
         }
         raiz.esq.cor = raiz.dir.cor = false;
         // Senao, a arvore tem tres ou mais elementos
      } else {
         inserir(j, null, null, null, raiz);
      }
      raiz.cor = false;
   }

   private void balancear(NoAN bisavo, NoAN avo, NoAN pai, NoAN i) {
      // Se o pai tambem e preto, reequilibrar a arvore, rotacionando o avo
      if (pai.cor == true) {
         // 4 tipos de reequilibrios e acoplamento
         if (pai.elemento.getNome().compareTo(avo.elemento.getNome()) > 0) { // rotacao a esquerda ou direita-esquerda
            if (i.elemento.getNome().compareTo(pai.elemento.getNome()) > 0) {
               avo = rotacaoEsq(avo);
            } else {
               avo = rotacaoDirEsq(avo);
            }
         } else { // rotacao a direita ou esquerda-direita
            if (i.elemento.getNome().compareTo(pai.elemento.getNome()) < 0) {
               avo = rotacaoDir(avo);
            } else {
               avo = rotacaoEsqDir(avo);
            }
         }
         if (bisavo == null) {
            raiz = avo;
         } else if (avo.elemento.getNome().compareTo(bisavo.elemento.getNome()) < 0) {
            bisavo.esq = avo;
         } else {
            bisavo.dir = avo;
         }
         // reestabelecer as cores apos a rotacao
         avo.cor = false;
         avo.esq.cor = avo.dir.cor = true;
      } // if(pai.cor == true)
   }

   /**
    * Metodo privado recursivo para inserir elemento.
    * 
    * @param elemento Elemento a ser inserido.
    * @param avo      NoAN em analise.
    * @param pai      NoAN em analise.
    * @param i        NoAN em analise.
    * @throws Exception Se o elemento existir.
    */
   private void inserir(Jogador elemento, NoAN bisavo, NoAN avo, NoAN pai, NoAN i) throws Exception {
      if (i == null) {
         if (elemento.getNome().compareTo(pai.elemento.getNome()) < 0) {
            i = pai.esq = new NoAN(elemento, true);
         } else {
            i = pai.dir = new NoAN(elemento, true);
         }
         if (pai.cor == true) {
            balancear(bisavo, avo, pai, i);
         }
      } else {
         // Achou um 4-no: eh preciso fragmeta-lo e reequilibrar a arvore
         if (i.esq != null && i.dir != null && i.esq.cor == true && i.dir.cor == true) {
            i.cor = true;
            i.esq.cor = i.dir.cor = false;
            if (i == raiz) {
               i.cor = false;
            } else if (pai.cor == true) {
               balancear(bisavo, avo, pai, i);
            }
         }
         if (elemento.getNome().compareTo(i.elemento.getNome()) < 0) {
            inserir(elemento, avo, pai, i, i.esq);
         } else if (elemento.getNome().compareTo(i.elemento.getNome()) > 0) {
            inserir(elemento, avo, pai, i, i.dir);
         } else {
            throw new Exception("Erro inserir (elemento repetido)!");
         }
      }
   }

   private NoAN rotacaoDir(NoAN no) {
      NoAN noEsq = no.esq;
      NoAN noEsqDir = noEsq.dir;
      noEsq.dir = no;
      no.esq = noEsqDir;
      return noEsq;
   }

   private NoAN rotacaoEsq(NoAN no) {
      NoAN noDir = no.dir;
      NoAN noDirEsq = noDir.esq;
      noDir.esq = no;
      no.dir = noDirEsq;
      return noDir;
   }

   private NoAN rotacaoDirEsq(NoAN no) {
      no.dir = rotacaoDir(no.dir);
      return rotacaoEsq(no);
   }

   private NoAN rotacaoEsqDir(NoAN no) {
      no.esq = rotacaoEsq(no.esq);
      return rotacaoDir(no);
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
	private boolean pesquisar(String x, NoAN i) {
		boolean resp;
		if (i == null) {
			resp = false;
         comparacoes++;
			MyIO.print(" NAO\n");

		} else if (x.compareTo(i.elemento.getNome())  == 0) {
			resp = true;
         comparacoes++;
			MyIO.print(" SIM\n");

		} else if (x.compareTo(i.elemento.getNome())  < 0) {
			MyIO.print(" esq");
         comparacoes++;
			resp = pesquisar(x, i.esq);
		} else {
			MyIO.print(" dir");
			resp = pesquisar(x, i.dir);
		}
		return resp;
	}


   /*
    * Metodo para registrar o log de execucao com movimentacoes
    */
   public static void registroLog(long tempo) {
      File file = new File("/tmp/806454_alvinegra.txt");
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
   public static void addJogadores(Alvinegra arvore) {
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
      Alvinegra arvore = new Alvinegra();
      addJogadores(arvore);
      String nome = MyIO.readLine();
      boolean fim = false;
		while (!fim) {
			try {
				MyIO.print(nome + " raiz");
				long start = System.nanoTime();
				arvore.pesquisar(nome);
				long end = System.nanoTime();
				long tempo = (end - start) / 10000;
            Alvinegra.registroLog(tempo);
			} catch (Exception e) {
				e.getMessage();
			}
			nome = MyIO.readLine();
			if (nome.equals("FIM"))
				fim = true;
		}
   }
}
