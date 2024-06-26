/*
*  806454 - Yago Almeida Melo
*  TP3-Q11: Quicksort
*/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Scanner;


/*
  * Class : Log
  * Cria Arquivo contendo Matrícula, tempo de execução e número de comparações
  */
  class Log{
    public int comparacoes, movimentacoes;
    public float time;
    public String fileName;
    

    Log(String fileName){
        this.comparacoes = 0;

        this.fileName = fileName;
    }

    public void incrementaComp(){
        comparacoes++;
    }
    public void incrementaMov(){
       movimentacoes++;
    }

    public void registroLog(){
        try {
            FileWriter writer = new FileWriter(this.fileName);
            writer.write("Matrícula: 806454\tTempo de execução: " + this.time + " segundos\tNúmero de comparações: " + this.comparacoes +
            "\tNúmero de movimentações: "+ this.movimentacoes);
            writer.close();
        } catch (IOException e) {
            System.out.println("Erro ao escrever o log no arquivo: " + e.getMessage());
        }
    }
}

/*
 * Class Celula
 */
class Celula{
    Personagem elemento;
    Celula prox;
    Celula ant;
    Celula(){
        this(null);
    }
    Celula(Personagem p){
        this.prox = null;
        this.ant = null;
        this.elemento = p;
    }
}

/* 
 * Class PilhaPersonagem
 * Pilha propriamente dita de Personagem
 */
class ListaPersonagem {
    public Celula primeiro, ultimo;
    public int tamanho;
    public Scanner sc;

    ListaPersonagem(){
        Celula tmp = new Celula();
        primeiro = tmp;
        ultimo = primeiro;
        tamanho = 0;
        this.sc = new Scanner(System.in);
    }
    
    /*
     * function: inserir
     * @params: Personagem
     * action: insere o Personagem na Lista
     */
    void inserir(Personagem personagem) throws Exception {
        ultimo.prox = new Celula(personagem);
		ultimo.prox.ant = ultimo;
		ultimo = ultimo.prox;
        tamanho++;
    }

    /*
      * function: swap
      * @params: Personagem[] && int && int
      * action: Troca dois personagens
      */
    public void swap(Celula p1, Celula p2, Log log){
        Personagem tmp = p1.elemento;
        p1.elemento = p2.elemento;
        p2.elemento = tmp; 
        log.incrementaMov(); log.incrementaMov(); log.incrementaMov();
    }

     /* 
    * function: comparator
    * @params: Personagem && Personagem && Log
    * action: Compara dois Personagens pelo id e se for igual, desempate por nome
    */
    public static boolean comparator(Personagem a, Personagem b, Log log){
        boolean resp;
        //Retorna true se a é menor que b
            if(a.getHouse().compareTo(b.getHouse()) < 0){
                resp = true;
                log.incrementaComp();
            } else if(a.getHouse().compareTo(b.getHouse()) > 0){
                resp = false;
                log.incrementaComp();
                log.incrementaComp();
            } else{  
                log.incrementaComp();  
                log.incrementaComp();                                          
                if(a.getName().compareTo(b.getName()) < 0){
                    resp = true;
                } else{
                    resp = false;
                }
                log.incrementaComp();
            }
        
        return resp;
    }

    public boolean verify(Celula i, Celula procurada){
        boolean x = false;
        while(!x && i!=null){
            if(i == procurada) {
                x = true;
            }
            i = i.prox;
        }
        return x;
    }
    public void quicksort(Celula i, Celula j, Log log){
        if (i != j && i != j.prox) {
            Celula ii = i;
            Celula jj = j;
            Personagem pivo = i.elemento;
            while (verify(ii, jj)) {
                while (comparator(ii.elemento, pivo, log)) {
                    ii = ii.prox;
                }
                while (comparator(pivo, jj.elemento, log)) {
                    jj = jj.ant;
                }
                if (verify(ii, jj)) {
                    swap(ii, jj, log);
                    ii = ii.prox;
                    jj = jj.ant;
                }
            }
            quicksort(i, jj, log);
            quicksort(ii, j, log);
        }
    }
    public void quicksort(Log log){
        quicksort(primeiro.prox, ultimo, log);
    }

    /*
     * function: mostrar
     * @params: 
     * action: mostra Personagens na pilha, do topo para a base
     */
    void mostrar() {
        Celula i = primeiro.prox;
		while(i!=null) { 
            Personagem.imprimir(i.elemento);
            i = i.prox;
        }
    }
}

/*
 * Class Lista
 * Lista de Alternate_Name & Alternate_Actors
 */
class Lista {
    public String[] list;

    Lista() {
        this(null);
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
            if (i != (list.length - 1))
                resp += ", ";
        }
        resp += '}';
        return resp;
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
    public int tamanho = 0;

    /*
     * Construtores da classe Personagem
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

    Personagem(String id, String name, String[] alternate_names, String house, String ancestry, String species,
            String patronus,
            boolean hogwartsStaff, boolean hogwartsStudent, String actorName, boolean alive, String[] alternate_actors,
            String dateOfBirth,
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
        String resp = "[" + getId() + " ## " + getName() + " ## " + getAlternate_names() + " ## " + getHouse() + " ## "
                + getAncestry() + " ## " + getSpecies() +
                " ## " + getPatronus() + " ## " + getHogwartsStaff() + " ## " + getHogwartsStudent() + " ## "
                + getActorName() + " ## " +
                getAlive() + " ## " + getDateOfBirth() + " ## " + getYearOfBirth() + " ## " + getEyeOfColour() + " ## "
                + getGender() + " ## " +
                getHairColour() + " ## " + getWizard() + "]";
        return resp;
    }

    public static String toString(Personagem x) {
        String resp = "[" + x.getId() + " ## " + x.getName() + " ## " + x.getAlternate_names() + " ## "
                + x.getHouse()
                + " ## " + x.getAncestry() + " ## " + x.getSpecies() +
                " ## " + x.getPatronus() + " ## " + x.getHogwartsStaff() + " ## " + x.getHogwartsStudent() + " ## "
                + x.getActorName() + " ## " + x.getAlive() + " ## " + formatter(x.getDateOfBirth().toString()) +
                " ## " + x.getYearOfBirth() + " ## " + x.getEyeOfColour() + " ## " + x.getGender() + " ## "
                + x.getHairColour() + " ## " + x.getWizard() + "]";
        return resp;
    }

    /*
     * function: formatter
     * 
     * @params: String
     * 
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
     * 
     * @params: String & int & int
     * 
     * @action: Retorna a string do parametro sem a parte desejada, sendo os
     * inteiros os index que serão retiradoss
     */
    public String removePartOfString(String x, int inicio, int fim) {
        String part1 = x.substring(0, inicio);
        String part2 = x.substring(fim + 1);
        return part1 + part2;
    }

    /*
     * function: subStringOfAlternate
     * 
     * @params: String
     * 
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
     * 
     * @params: String & String
     * 
     * @action: Retorna uma String, sem a parte 'rem'
     */
    public String removeLists(String x, String rem) {
        x = x.replace(rem, "");
        return x;
    }

    /*
     * function: fixAlternates
     * 
     * @params: String
     * 
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
     * 
     * @params: Personagem[]
     * 
     * @action: Faz a leitura de todos os Personagens e põe no Array de Personagem
     */
    public static void ler(Personagem[] p, int key) throws Exception {
        FileReader file;
        BufferedReader bf;
        String line;
        if (key == 0) {
            file = new FileReader(
                    "/home/yago/Documents/Cc-PucMG/2periodo/TrabalhosPraticos/2024-1/characters.csv");
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

    public static Personagem findPersonagem(Personagem p[], String id) {
        Personagem x = new Personagem();
        for (int i = 0; i < p.length; i++) {
            if (p[i].getId().equals(id)) {
                try {
                    x = p[i].clone();
                    i = p.length;
                } catch (CloneNotSupportedException e) {
                    e.getMessage();
                }
            }
        }
        return x;
    }

    public static void main(String[] args) throws Exception {
        Personagem personagens[] = new Personagem[404];
        Log log = new Log("/tmp/806454_quicksort.txt");
        ListaPersonagem lista = new ListaPersonagem();
        ler(personagens, 1); // Segundo parametro: 0 para teste | 1 para enviar ao verde
        String id = lista.sc.nextLine();
        while (!id.equals("FIM")) {
            Personagem novo = findPersonagem(personagens, id);
            lista.inserir(novo);
            id = lista.sc.nextLine();
        }
        Instant start = Instant.now();
        lista.quicksort(log);
        Instant end = Instant.now();
        long elapsedTime = Duration.between(start, end).toMillis();
        log.time = (float) elapsedTime / 1000; // Tempo em segundos
        log.registroLog();
        lista.mostrar();
        lista.sc.close();
    }
}