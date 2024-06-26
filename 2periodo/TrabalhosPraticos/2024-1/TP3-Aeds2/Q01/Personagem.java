/*
*  806454 - Yago Almeida Melo
*  TP3-Q01: Lista Sequencial
*/

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.util.Scanner;

/* 
 * Class ListaPersonagem
 * Lista propriamente dita de Personagem
 */
class ListaPersonagem {
    public Personagem p[];
    public int tamanho = 0;
    public Scanner sc;

    ListaPersonagem(int tam) {
        this.p = new Personagem[tam];
        this.tamanho = 0;
        this.sc = new Scanner(System.in);
    }

    void inserirFim(Personagem personagem) throws Exception {
        if (tamanho >= p.length) {
            throw new Exception("Limite excedido!");
        }
        p[tamanho] = personagem;
        tamanho++;
    }

    void inserirInicio(Personagem personagem) throws Exception {
        if (tamanho >= p.length) {
            throw new Exception("Limite excedido!");
        }
        for (int i = tamanho - 1; i >= 0; i--) {
            p[i + 1] = p[i];
        }
        p[0] = personagem;
        tamanho++;
    }

    void inserir(Personagem personagem, int pos) throws Exception {
        if (pos < 0 || pos > p.length) {
            throw new Exception("Erro ao inserir!");
        }
        for (int i = tamanho - 1; i >= pos; i--) {
            p[i + 1] = p[i];
        }
        p[pos] = personagem;
        tamanho++;
    }

    Personagem removerInicio() throws Exception {
        if (tamanho == 0) {
            throw new Exception("Lista vazia!");
        }
        Personagem removido = p[0];
        for (int i = 0; i < tamanho; i++) {
            p[i] = p[i + 1];
        }
        tamanho--;
        return removido;
    }

    Personagem remover(int pos) throws Exception {
        if (tamanho == 0 || pos < 0 || pos >= tamanho) {
            throw new Exception("Erro ao remover!");
        }
        Personagem removido = p[pos];
        for (int i = pos; i < tamanho; i++) {
            p[i] = p[i + 1];
        }
        tamanho--;
        return removido;
    }

    Personagem removerFim() throws Exception {
        if (tamanho == 0) {
            throw new Exception("Erro ao remover! Lista vazia!!!");
        }
        Personagem removido = p[tamanho - 1];
        tamanho--;
        return removido;
    }

    Personagem findPersonagem(Personagem p[], String id) {
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

    public void metodos(Personagem p[], int x) {
        String m = "";
        Personagem novo;
        for (int i = 0; i < x+1; i++) {
            m = sc.nextLine();
            String[] str = m.split(" ");
            try {
                switch (str[0]) {
                    case "II":
                        novo = findPersonagem(p, str[1]);
                        inserirInicio(novo);
                        break;
                    case "IF":
                        novo = findPersonagem(p, str[1]);
                        inserirFim(novo);
                        break;
                    case "I*":
                        novo = findPersonagem(p, str[2]);
                        inserir(novo, Integer.parseInt(str[1]));
                        break;
                    case "RI":
                        novo = removerInicio();
                        System.out.println("(R) " + novo.getName());
                        break;
                    case "RF":
                        novo = removerFim();
                        System.out.println("(R) " + novo.getName());
                        break;
                    case "R*":
                        novo = remover(Integer.parseInt(str[1]));
                        System.out.println("(R) " + novo.getName());
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                e.getMessage();
            }
        }
        mostrar();
    }

    void mostrar() {
        for (int i = 0; i < tamanho; i++) {
            Personagem.imprimir(this.p[i], i);
        }
    }
}

/*
 * Class Lista
 * Lista de Alternate_Name & Alternate_Actors
 */
class Lista {
    public String[] list;

    Lista(String[] list) {
        this.list = list;
    }

    /*
     * function: getList()
     * 
     * @params: null
     * 
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
     * 
     * @params: null
     * 
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

    public static void imprimir(Personagem x, int id) {
        System.out.println(toString(x, id));
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

    public static String toString(Personagem x, int id) {
        String resp = "[" + id + " ## " + x.getId() + " ## " + x.getName() + " ## " + x.getAlternate_names() + " ## "
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

    public static void main(String[] args) throws Exception {
        Personagem personagens[] = new Personagem[404];
        ListaPersonagem list = new ListaPersonagem(404);
        ler(personagens, 1); // Segundo parametro: 0 para teste | 1 para enviar ao verde
        String id = list.sc.nextLine();
        while (!id.equals("FIM")) {
            Personagem novo = list.findPersonagem(personagens, id);
            list.inserirFim(novo);
            id = list.sc.nextLine();
        }
        int qtd = list.sc.nextInt();
        list.metodos(personagens, qtd);
        list.sc.close();
    }
}