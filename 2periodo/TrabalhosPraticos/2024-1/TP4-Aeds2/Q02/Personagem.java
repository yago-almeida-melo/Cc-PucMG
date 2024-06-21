
/*
 *  806454 - Yago Almeida Melo
 *  TP4-Q02: Arvore de Arvore em Java
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Scanner;

class No {
    public No2 outro;
    public int elemento; // Conteudo do no.
    public No esq, dir;  // Filhos da esq e dir.
    /**
     * Construtor da classe.
     *
     * @param elemento Conteudo do no.
     */
    public No(int elemento) {
        this(elemento, null, null, null);
    }

    /**
     * Construtor da classe.
     *
     * @param elemento Conteudo do no.
     * @param esq No da esquerda.
     * @param dir No da direita.
     */
    public No(int elemento, No2 outro, No esq, No dir) {
        this.elemento = elemento;
        this.outro = outro;
        this.esq = esq;
        this.dir = dir;
    }
}

class No2 {
    public Personagem elemento; // Conteudo do no.
    public No2 esq, dir;  // Filhos da esq e dir.

    /**
     * Construtor da classe.
     *
     * @param elemento Conteudo do no.
     */
    public No2(Personagem elemento) {
        this(elemento, null, null);
    }

    /**
     * Construtor da classe.
     *
     * @param elemento Conteudo do no.
     * @param esq No da esquerda.
     * @param dir No da direita.
     */
    public No2(Personagem elemento, No2 esq, No2 dir) {
        this.elemento = elemento;
        this.esq = esq;
        this.dir = dir;
    }
}

class ArvoreArvore {

    private No raiz; // Raiz da arvore.

    /**
     * Construtor da classe.
     */
    public ArvoreArvore() throws Exception {
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

    /*
     * Inserir na Primeira Arvore
     */
    public void inserir(int x) throws Exception {
        inserir(x, raiz);
    }

    public void inserir(int x, No i) throws Exception {
        if (i == null) {
            i = new No(x);
        } else if (x < i.elemento) {
            inserir(x, i.esq);
        } else if (x > i.elemento) {
            inserir(x, i.dir);
        } else {
            System.err.println("ERROR AO INSERIR NUM");
        }
    }

    /*
     * Inserir na Segunda Arvore
     */
    public void inserir(Personagem s) throws Exception {
        inserir(s, raiz);
    }

    public void inserir(Personagem s, No i) throws Exception {
        if (i == null) {
            throw new Exception("Erro ao inserir!");
        } else if ((s.getYearOfBirth()%15) < i.elemento) {
            inserir(s, i.esq);
        } else if ((s.getYearOfBirth()%15) > i.elemento) {
            inserir(s, i.dir);
        } else {
            i.outro = inserir2(s, i.outro);
        }
    }

    public No2 inserir2(Personagem s, No2 i) throws Exception {
        if (i == null) {
            i = new No2(s);
        } else if (s.getName().compareTo(i.elemento.getName()) < 0) {
            i.esq = inserir2(s, i.esq);
        } else if (s.getName().compareTo(i.elemento.getName()) > 0) {
            i.dir = inserir2(s, i.dir);
        } else {
            throw new Exception("Erro ao inserir: elemento existente!");
        }
        return i;
    }
    /**
     * Metodo publico iterativo para pesquisar elemento.
     *
     * @param elemento Elemento que sera procurado.
     * @return <code>true</code> se o elemento existir, <code>false</code> em
     * caso contrario.
     */
    public boolean pesquisar(String elemento, Log log) {
        System.out.print(elemento+" => raiz ");
        boolean resp = pesquisar(raiz, elemento, log);
        return resp;
    }

    private boolean pesquisar(No no, String x, Log log) {
        boolean resp = false;
        if (no!=null) {
            resp = pesquisarSegundaArvore(no.outro, x, log);    
            if(!resp){
                System.out.print(" ESQ");
                resp = pesquisar(no.esq, x, log);
            }
            if(!resp){
                System.out.print(" DIR");
                resp = pesquisar(no.dir, x, log);
            }
        }
        return resp;
    }

    private boolean pesquisarSegundaArvore(No2 no, String x, Log log) {
        boolean resp = false;
        if(no==null) { 
            resp = false; 
            log.incrementaComp();
        }
        else if(no.elemento.getName().compareTo(x) > 0){ 
            System.out.print("->esq"); 
            log.incrementaComp();
            resp = pesquisarSegundaArvore(no.esq, x, log);
        }
        else if(no.elemento.getName().compareTo(x) < 0){ 
            System.out.print("->dir"); 
            log.incrementaComp();
            resp = pesquisarSegundaArvore(no.dir, x, log);
        }
        else resp = true;
        return resp;
    }
}

//------------------------------------------------------------------------------------------------------------------
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

    public static void inserirNaArvore(ArvoreArvore arvore, Personagem personagens[], Scanner sc) throws Exception {
        String id = sc.nextLine();
        while (!id.equals("FIM")) {
            Personagem novo;
            novo = procuraId(personagens, id);
            arvore.inserir(novo);
            id = sc.nextLine();
        }
    }

    public static void procuraNaArvore(ArvoreArvore arvore, Log log, Scanner sc) {
        String nome = sc.nextLine();
        while (!nome.equals("FIM")) {
            arvore.pesquisar(nome, log);
            nome = sc.nextLine();
        }
    }

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        ArvoreArvore arvore = new ArvoreArvore();
        Log log = new Log("/tmp/806454_arvoreArvore.txt");
        Personagem personagens[] = new Personagem[404];
        ler(personagens, 1);  // Segundo parametro: 0 para teste / 1 para enviar ao verde com /tmp/characters.csv
        inserirNaArvore(arvore, personagens, sc);
        Instant start = Instant.now();
        procuraNaArvore(arvore, log, sc);
        Instant end = Instant.now();
        long elapsedTime = Duration.between(start, end).toMillis();
        log.time = (float) elapsedTime / 1000; // Tempo em segundos
        log.registroLog();
        sc.close();
    }

}
