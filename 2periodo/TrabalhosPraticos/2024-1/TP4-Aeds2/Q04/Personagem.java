
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Scanner;

class NoAN {

    public boolean cor;
    public Personagem elemento;
    public NoAN esq, dir;

    public NoAN() {
        this(null, false, null, null);
    }

    public NoAN(Personagem elemento) {
        this(elemento, false, null, null);
    }

    public NoAN(Personagem elemento, boolean cor) {
        this(elemento, cor, null, null);
    }

    public NoAN(Personagem elemento, boolean cor, NoAN esq, NoAN dir) {
        this.cor = cor;
        this.elemento = elemento;
        this.esq = esq;
        this.dir = dir;
    }
}

//Class Alvinegra
class Alvinegra {

    private NoAN raiz; // Raiz da arvore.

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
     * @return <code>true</code> se o elemento existir, <code>false</code> em
     * caso contrario.
     */
    public void pesquisar(String elemento, Log log) {
        System.out.print(elemento+" raiz ");
        boolean resp = pesquisar(elemento, raiz, log);
        if(resp){ System.out.print(" SIM\n"); } else{ System.out.print(" NAO\n"); }
    }

    /**
     * Metodo privado recursivo para pesquisar elemento.
     *
     * @param elemento Elemento que sera procurado.
     * @param i NoAN em analise.
     * @return <code>true</code> se o elemento existir, <code>false</code> em
     * caso contrario.
     */
    private boolean pesquisar(String elemento, NoAN i, Log log) {
        boolean resp;
        if (i == null) {
            log.incrementaComp();
            resp = false;
        } else if (elemento.equals(i.elemento.getName())) {
            log.incrementaComp();
            resp = true;
        } else if (elemento.compareTo(i.elemento.getName()) < 0) {
            log.incrementaComp();
            System.out.print(" esq");
            resp = pesquisar(elemento, i.esq, log);
        } else {
            System.out.print(" dir");
            resp = pesquisar(elemento, i.dir, log);
        }
        return resp;
    }

    /**
     * Metodo publico iterativo para exibir elementos.
     */
    public void caminharCentral() {
        System.out.print("[ ");
        caminharCentral(raiz);
        System.out.println("]");
    }

    /**
     * Metodo privado recursivo para exibir elementos.
     *
     * @param i NoAN em analise.
     */
    private void caminharCentral(NoAN i) {
        if (i != null) {
            caminharCentral(i.esq); // Elementos da esquerda.
            System.out.print(i.elemento + ((i.cor) ? "(p) " : "(b) ")); // Conteudo do no.
            caminharCentral(i.dir); // Elementos da direita.
        }
    }

    /**
     * Metodo publico iterativo para exibir elementos.
     */
    public void caminharPre() {
        System.out.print("[ ");
        caminharPre(raiz);
        System.out.println("]");
    }

    /**
     * Metodo privado recursivo para exibir elementos.
     *
     * @param i NoAN em analise.
     */
    private void caminharPre(NoAN i) {
        if (i != null) {
            System.out.print(i.elemento + ((i.cor) ? "(p) " : "(b) ")); // Conteudo do no.
            caminharPre(i.esq); // Elementos da esquerda.
            caminharPre(i.dir); // Elementos da direita.
        }
    }

    /**
     * Metodo publico iterativo para exibir elementos.
     */
    public void caminharPos() {
        System.out.print("[ ");
        caminharPos(raiz);
        System.out.println("]");
    }

    /**
     * Metodo privado recursivo para exibir elementos.
     *
     * @param i NoAN em analise.
     */
    private void caminharPos(NoAN i) {
        if (i != null) {
            caminharPos(i.esq); // Elementos da esquerda.
            caminharPos(i.dir); // Elementos da direita.
            System.out.print(i.elemento + ((i.cor) ? "(p) " : "(b) ")); // Conteudo do no.
        }
    }

    /**
     * Metodo publico iterativo para inserir elemento.
     *
     * @param elemento Elemento a ser inserido.
     * @throws Exception Se o elemento existir.
     */
    public void inserir(Personagem elemento) throws Exception {
        // Se a arvore estiver vazia
        if (raiz == null) {
            raiz = new NoAN(elemento);
            // Senao, se a arvore tiver um elemento
        } else if (raiz.esq == null && raiz.dir == null) {
            if (elemento.getName().compareTo(raiz.elemento.getName()) < 0) {
                raiz.esq = new NoAN(elemento);
            } else {
                raiz.dir = new NoAN(elemento);
            }
            // Senao, se a arvore tiver dois elementos (raiz e dir)
        } else if (raiz.esq == null) {
            if (elemento.getName().compareTo(raiz.elemento.getName()) < 0) {
                raiz.esq = new NoAN(elemento);
            } else if (elemento.getName().compareTo(raiz.elemento.getName()) < 0) {
                raiz.esq = new NoAN(raiz.elemento);
                raiz.elemento = elemento;
            } else {
                raiz.esq = new NoAN(raiz.elemento);
                raiz.elemento = raiz.dir.elemento;
                raiz.dir.elemento = elemento;
            }
            raiz.esq.cor = raiz.dir.cor = false;
            // Senao, se a arvore tiver dois elementos (raiz e esq)
        } else if (raiz.dir == null) {
            if (elemento.getName().compareTo(raiz.elemento.getName()) > 0) {
                raiz.dir = new NoAN(elemento);
            } else if (elemento.getName().compareTo(raiz.elemento.getName()) > 0) {
                raiz.dir = new NoAN(raiz.elemento);
                raiz.elemento = elemento;
            } else {
                raiz.dir = new NoAN(raiz.elemento);
                raiz.elemento = raiz.esq.elemento;
                raiz.esq.elemento = elemento;
            }
            raiz.esq.cor = raiz.dir.cor = false;

            // Senao, a arvore tem tres ou mais elementos
        } else {
            inserir(elemento, null, null, null, raiz);
        }
        raiz.cor = false;
    }

    private void balancear(NoAN bisavo, NoAN avo, NoAN pai, NoAN i) {
        // Se o pai tambem e preto, reequilibrar a arvore, rotacionando o avo
        if (pai.cor == true) {
            // 4 tipos de reequilibrios e acoplamento
            if (pai.elemento.getName().compareTo(avo.elemento.getName()) > 0) { // rotacao a esquerda ou direita-esquerda
                if (i.elemento.getName().compareTo(pai.elemento.getName()) > 0) {
                    avo = rotacaoEsq(avo);
                } else {
                    avo = rotacaoDirEsq(avo);
                }
            } else { // rotacao a direita ou esquerda-direita
                if (i.elemento.getName().compareTo(pai.elemento.getName()) < 0) {
                    avo = rotacaoDir(avo);
                } else {
                    avo = rotacaoEsqDir(avo);
                }
            }
            if (bisavo == null) {
                raiz = avo;
            } else if (avo.elemento.getName().compareTo(bisavo.elemento.getName()) < 0) {
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
     * @param avo NoAN em analise.
     * @param pai NoAN em analise.
     * @param i NoAN em analise.
     * @throws Exception Se o elemento existir.
     */
    private void inserir(Personagem elemento, NoAN bisavo, NoAN avo, NoAN pai, NoAN i) throws Exception {
        if (i == null) {
            if (elemento.getName().compareTo(pai.elemento.getName()) < 0) {
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
            if (elemento.getName().compareTo(i.elemento.getName()) < 0) {
                inserir(elemento, avo, pai, i, i.esq);
            } else if (elemento.getName().compareTo(i.elemento.getName()) > 0) {
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
}

/*
  * Class Lista
  * Lista de Alternate_Name & Alternate_Actors
 */
class Lista {

    public String[] list;

    Lista() {
        this.list = null;
    }

    Lista(String[] list) {
        this.list = list;
    }

    /*
      * function: getList()
      * @params: null
      * @action: Imprime a Lista desejada
     */
    public String getList() {
        String resp = "";
        resp += '{';
        for (int i = 0; i < list.length; i++) {
            resp += list[i];
            if (i != (list.length - 1)) {
                resp += ", ";
            }
        }
        resp += '}';
        return resp;
    }
}

/*
 * Class : Log
 * Cria Arquivo contendo Matrícula, tempo de execução e número de comparações
 */
class Log {

    public int comparacoes;
    public float time;
    public String fileName;

    Log(String fileName) {
        this.comparacoes = 0;

        this.fileName = fileName;
    }

    public void incrementaComp() {
        comparacoes++;
    }

    public void registroLog() {
        try {
            FileWriter writer = new FileWriter(this.fileName);
            writer.write("Matrícula: 806454\tTempo de execução: " + this.time + " segundos\tNúmero de comparações: " + this.comparacoes);
            writer.close();
        } catch (IOException e) {
            System.out.println("Erro ao escrever o log no arquivo: " + e.getMessage());
        }
    }
}

class Personagem {

    /*
      * Atributos privados
     */
    private String id, name, house, ancestry, species, patronus, actorName, eyeColour, gender, hairColour;
    private int yearOfBirth;
    private boolean hogwartsStaff, hogwartsStudent, alive, wizard;
    private LocalDate dateOfBirth;
    Lista alternate_names, alternate_actors;

    /*
      *  Construtores da classe Personagem 
     */
    Personagem() {
        this.id = null;
        this.name = null;
        this.alternate_names = null;
        this.house = null;
        this.ancestry = null;
        this.species = null;
        this.patronus = null;
        this.hogwartsStaff = false;
        this.hogwartsStudent = false;
        this.actorName = null;
        this.alive = false;
        this.alternate_actors = null;
        this.dateOfBirth = null;
        this.yearOfBirth = -1;
        this.eyeColour = null;
        this.gender = null;
        this.hairColour = null;
        this.wizard = false;
    }

    Personagem(String id, String name, String[] alternate_names, String house, String ancestry, String species, String patronus,
            boolean hogwartsStaff, boolean hogwartsStudent, String actorName, boolean alive, String[] alternate_actors, String dateOfBirth,
            int yearOfBirth, String eyeColour, String gender, String hairColour, boolean wizard) {
        this.id = id;
        this.name = name;
        this.alternate_names = new Lista(alternate_names);
        this.house = house;
        this.ancestry = ancestry;
        this.species = species;
        this.patronus = patronus;
        this.hogwartsStaff = hogwartsStaff;
        this.hogwartsStudent = hogwartsStudent;
        this.actorName = actorName;
        this.alive = alive;
        this.alternate_actors = new Lista(alternate_actors);
        this.dateOfBirth = LocalDate.parse(dateOfBirth);
        this.yearOfBirth = yearOfBirth;
        this.eyeColour = eyeColour;
        this.gender = gender;
        this.hairColour = hairColour;
        this.wizard = wizard;
    }

    /*
      * Sets e Gets
     */
    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setAlternate_names(String[] alternate_names) {
        this.alternate_names = new Lista(alternate_names);
    }

    public String getAlternate_names() {
        return alternate_names.getList();
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public String getHouse() {
        return house;
    }

    public void setAncestry(String ancestry) {
        this.ancestry = ancestry;
    }

    public String getAncestry() {
        return ancestry;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getSpecies() {
        return species;
    }

    public void setPatronus(String patronus) {
        this.patronus = patronus;
    }

    public String getPatronus() {
        return patronus;
    }

    public void setHogwartsStaff(String hogwartsStaff) {
        if (hogwartsStaff.equals("VERDADEIRO")) {
            setHogwartsStaff(true);
        } else {
            setHogwartsStaff(false);
        }
    }

    public void setHogwartsStaff(boolean hogwartsStaff) {
        this.hogwartsStaff = hogwartsStaff;
    }

    public boolean getHogwartsStaff() {
        return hogwartsStaff;
    }

    public void setHogwartsStudent(String hogwartsStudent) {
        if (hogwartsStudent.equals("VERDADEIRO")) {
            setHogwartsStudent(true);
        } else {
            setHogwartsStudent(false);
        }
    }

    public void setHogwartsStudent(boolean hogwartsStudent) {
        this.hogwartsStudent = hogwartsStudent;
    }

    public boolean getHogwartsStudent() {
        return hogwartsStudent;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    public String getActorName() {
        return actorName;
    }

    public void setAlive(String alive) {
        if (alive.equals("VERDADEIRO")) {
            setAlive(true);
        } else {
            setAlive(false);
        }
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public boolean getAlive() {
        return alive;
    }

    public void setAlternate_Actors(String[] alternate_actors) {
        this.alternate_actors = new Lista(alternate_actors);
    }

    public String getAlternate_Actors() {
        return alternate_actors.getList();
    }

    public void setDateOfBirth(String dateOfBirth) {
        setDateOfBirth(LocalDate.parse(formatter(dateOfBirth)));
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setYearOfBirth(int yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public int getYearOfBirth() {
        return yearOfBirth;
    }

    public void setEyeColour(String eyeColour) {
        this.eyeColour = eyeColour;
    }

    public String getEyeOfColour() {
        return eyeColour;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public void setHairColour(String hairColour) {
        this.hairColour = hairColour;
    }

    public String getHairColour() {
        return hairColour;
    }

    public void setWizard(String wizard) {
        if (wizard.equals("VERDADEIRO")) {
            setWizard(true);
        } else {
            setWizard(false);
        }
    }

    public void setWizard(boolean wizard) {
        this.wizard = wizard;
    }

    public boolean getWizard() {
        return wizard;
    }

    /*
      * function: clone
      * @params: null
      * @action: Retorna um objeto Personagem, fazendo um clone
     */
    public Personagem clone() throws CloneNotSupportedException {
        Personagem novo = new Personagem();
        novo.id = this.id;
        novo.name = this.name;
        novo.alternate_names = this.alternate_names;
        novo.house = this.house;
        novo.ancestry = this.ancestry;
        novo.species = this.species;
        novo.patronus = this.patronus;
        novo.hogwartsStaff = this.hogwartsStaff;
        novo.hogwartsStudent = this.hogwartsStudent;
        novo.actorName = this.actorName;
        novo.alive = this.alive;
        novo.alternate_actors = this.alternate_actors;
        novo.dateOfBirth = this.dateOfBirth;
        novo.yearOfBirth = this.yearOfBirth;
        novo.eyeColour = this.eyeColour;
        novo.gender = this.gender;
        novo.hairColour = this.hairColour;
        novo.wizard = this.wizard;
        return novo;
    }

    /*
      * Metodo para imprimir os dados da classe Personagem
     */
    public void imprimir(String id) {
        System.out.println(toString());
    }

    public static void imprimir(Personagem x) {
        System.out.println(toString(x));
    }

    /*
      * Metodo para transformar em string os dados da classe Personagem
     */
    public String toString() {
        String resp = "[" + getId() + " ## " + getName() + " ## " + getAlternate_names() + " ## " + getHouse() + " ## " + getAncestry() + " ## " + getSpecies()
                + " ## " + getPatronus() + " ## " + getHogwartsStaff() + " ## " + getHogwartsStudent() + " ## " + getActorName() + " ## "
                + getAlive() + " ## " + getDateOfBirth() + " ## " + getYearOfBirth() + " ## " + getEyeOfColour() + " ## " + getGender() + " ## "
                + getHairColour() + " ## " + getWizard() + "]";
        return resp;
    }

    public static String toString(Personagem x) {
        String resp = "[" + x.getId() + " ## " + x.getName() + " ## " + x.getAlternate_names() + " ## " + x.getHouse() + " ## " + x.getAncestry() + " ## " + x.getSpecies()
                + " ## " + x.getPatronus() + " ## " + x.getHogwartsStaff() + " ## " + x.getHogwartsStudent() + " ## " + x.getActorName() + " ## " + x.getAlive() + " ## " + formatter(x.getDateOfBirth().toString())
                + " ## " + x.getYearOfBirth() + " ## " + x.getEyeOfColour() + " ## " + x.getGender() + " ## " + x.getHairColour() + " ## " + x.getWizard() + "]";
        return resp;
    }

    /* 
      * function: formatter
      * @params: String
      * @action: Retorna a String da data formatada
     */
    public static String formatter(String date) {
        String[] x = date.split("-");
        if (x[1].length() == 1) {
            x[1] = '0' + x[1];
        }
        String resp = x[2] + "-" + x[1] + "-" + x[0];
        return resp;
    }

    /*
      * function: removePartOfString
      * @params: String & int & int
      * @action: Retorna a string do parametro sem a parte desejada, sendo os inteiros os index que serão retiradoss
     */
    public String removePartOfString(String x, int inicio, int fim) {
        String part1 = x.substring(0, inicio);
        String part2 = x.substring(fim + 1);
        return part1 + part2;
    }

    /*
      * function: subStringOfAlternate
      * @params: String
      * @action: Retorna um array de String com as duas listas
     */
    public String[] subSTringOfAlternate(String line) {
        String[] resp = new String[2];
        for (int i = 2; i > 0; i--) {
            int openBracket = line.indexOf("[");
            int closeBracket = line.indexOf("]");
            if (openBracket + 1 != closeBracket) {
                resp[resp.length - i] = line.substring(openBracket, closeBracket + 1);
            } else {
                resp[resp.length - i] = "{}";
            }
            line = removePartOfString(line, openBracket, closeBracket);
        }
        return resp;
    }

    /*
      * function: removeLists
      * @params: String & String
      * @action: Retorna uma String, sem a parte 'rem'
     */
    public String removeLists(String x, String rem) {
        x = x.replace(rem, "");
        return x;
    }

    /*
      * function: fixAlternates
      * @params: String
      * @action: Retorna um array de String da lista 
     */
    public String[] fixAlternates(String alternate) {
        String[] resp = alternate.substring(1, alternate.length() - 1).split("', '");
        return resp;
    }

    /*
      * Metodo para setar os dados do arquivo csv
     */
    public void setDados(String line) {
        line = line.replace("\"", "");
        line = line.replace("\'", "");
        String[] alternates = subSTringOfAlternate(line);
        line = removeLists(line, alternates[0]);
        line = removeLists(line, alternates[1]);
        String[] alternateName = fixAlternates(alternates[0]);
        String[] alternateActors = fixAlternates(alternates[1]);
        String[] dados = line.split(";");
        setId(dados[0]);
        setName(dados[1]);
        setAlternate_names(alternateName);
        setHouse(dados[3]);
        setAncestry(dados[4]);
        setSpecies(dados[5]);
        setPatronus(dados[6]);
        setHogwartsStaff((dados[7]));
        setHogwartsStudent((dados[8]));
        setActorName(dados[9]);
        setAlive((dados[10]));
        setAlternate_Actors(alternateActors);
        setDateOfBirth((dados[12]));
        setYearOfBirth(Integer.parseInt(dados[13]));
        setEyeColour(dados[14]);
        setGender(dados[15]);
        setHairColour(dados[16]);
        setWizard((dados[17]));
    }

    /*
      * function: ler
      * @params: Personagem[]
      * @action: Faz a leitura de todos os Personagens e põe no Array de Personagem
     */
    public static void ler(Personagem[] p, int key) throws Exception {
        FileReader file;
        BufferedReader bf;
        String line;
        if (key == 0) {
            file = new FileReader("/home/yago/Documents/Cc-PucMG/2periodo/TrabalhosPraticos/2024-1/TP2-Aeds2/characters.csv");
            bf = new BufferedReader(file);
        } else {
            file = new FileReader("/tmp/characters.csv");
            bf = new BufferedReader(file);
        }
        int i = 0;
        while ((line = bf.readLine()) != null) {
            try {
                Personagem per = new Personagem();
                per.setDados(line);
                p[i] = per;
                i++;
            } catch (Exception e) {
                e.getMessage();
            }
        }
        bf.close();
        file.close();
    }

    /*
      * function: procuraId
      * @params: Personagem[] && String
      * action: Procura o personagem no Array de acordo com seu id
     */
    public static Personagem procuraId(Personagem personagens[], String id) {
        Personagem resp = new Personagem();
        for (int i = 0; i < personagens.length; i++) {
            if ((personagens[i].getId()).equals(id)) {
                resp = personagens[i];
                i = personagens.length;
            }
        }
        return resp;
    }

    public static void inserirNaArvore(Alvinegra arvore, Personagem personagens[], Scanner sc) throws Exception{
        String id = sc.nextLine();
        while (!id.equals("FIM")){
            Personagem novo;
            novo = procuraId(personagens, id);
            arvore.inserir(novo);
            id = sc.nextLine();
        }
    }

    public static void procuraNaArvore(Alvinegra arvore, Log log, Scanner sc){
        String nome = sc.nextLine();
        while(!nome.equals("FIM")){ 
            arvore.pesquisar(nome, log);
            nome = sc.nextLine();
        }
    }

    public static void main(String[] args) throws Exception {
        Alvinegra alvinegra = new Alvinegra();
        Log log = new Log("806454_alvinegra.txt");
        Scanner sc = new Scanner(System.in);
        Personagem personagens[] = new Personagem[404];
        ler(personagens, 0);  // Segundo parametro: 0 para teste / 1 para enviar ao verde com /tmp/characters.csv
        inserirNaArvore(alvinegra, personagens, sc);
        Instant start = Instant.now();
        procuraNaArvore(alvinegra, log, sc);
        Instant end = Instant.now();
        long elapsedTime = Duration.between(start, end).toMillis();
        log.time = (float) elapsedTime / 1000; // Tempo em segundos
        log.registroLog();
        sc.close();
    }

}
