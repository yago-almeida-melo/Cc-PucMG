 /*
 *  806454 - Yago Almeida Melo
 *  TP1-Q01: Classe em Java
 */

 import java.io.FileReader;
 import java.io.BufferedReader;
 import java.util.ArrayList;
 import java.time.LocalDate;


class Personagem{
    /*
     * Atributos privados
     */
    private String id, name, house, ancestry, species, patronus, hogwartsStudent, actorName, eyeColour, gender, hairColour;
    private int yearOfBirth;
    private boolean hogwartsStaff, alive, wizard;
    public LocalDate dateOfBirth;
    ArrayList<String> alternate_names;

    /*
     *  Construtores da classe Personagem 
     */
    Personagem(){
        this.id = null;
        this.name = null;
        this.alternate_names = null;
        this.house = null;
        this.ancestry = null;
        this.species = null;
        this.patronus = null;
        this.hogwartsStaff = false;
        this.hogwartsStudent = null;
        this.actorName = null;
        this.alive = false;
        this.dateOfBirth = null;
        this.yearOfBirth = -1;
        this.eyeColour = null;
        this.gender = null;
        this.hairColour = null;
        this.wizard = false;
    }

    Personagem(String id, String name, ArrayList<String> alternate_names, String house, String ancestry, String species, String patronus,
     boolean alive, LocalDate dateOfBirth, int yearOfBirth, String eyeColour, String gender, String hairColour, boolean wizard){
        this.id = id;
        this.name = name;
        this.alternate_names = (ArrayList<String>) alternate_names.clone();
        this.house = house;
        this.ancestry = ancestry;
        this.species = species;
        this.patronus = patronus;
        this.hogwartsStaff = hogwartsStaff;
        this.hogwartsStudent = hogwartsStudent;
        this.actorName = actorName;
        this.alive = alive;
        this.dateOfBirth = dateOfBirth;
        this.yearOfBirth = yearOfBirth;
        this.eyeColour = eyeColour;
        this.gender = gender;
        this.hairColour = hairColour;
        this.wizard = wizard;
    }

    public void setId(String id){ this.id = id;}
    public String getId(){ return id;}
    public void setName(String name){ this.name = name;}
    public String getName(){ return name;}
    public void setAlternate_names(ArrayList<String> alternate_names){ this.alternate_names = new ArrayList<String>(alternate_names.clone()); }
    public ArrayList<String> getAlternate_names(){ return alternate_names;}
    public void setHouse(String house){ this.house = house;}
    public String getHouse(){ return house;}
    public void setAncestry(String ancestry){ this.ancestry = ancestry;}
    public String getAncestry(){ return ancestry;}
    public void setSpecies(String species){ this.species = species;}
    public String getSpecies(){ return species;}
    public void setPatronus(String patronus){ this.patronus = patronus;}
    public String getPatronus(){ return patronus;}
    public void setHogwartsStaff(boolean hogwartsStaff){ this.hogwartsStaff = hogwartsStaff;}
    public boolean getHogwartsStaff(){ return hogwartsStaff;}
    public void setHogwartsStudent(boolean hogwartsStudent){ this.hogwartsStudent = hogwartsStudent;}
    public boolean getHogwartsStudent(){ return hogwartsStudent;}
    public void setActorName(boolean actorName){ this.actorName = actorName;}
    public boolean getActorName(){ return actorName;}
    public void setAlive(boolean alive){ this.alive = alive;}
    public boolean getAlive(){ return alive;}
    public void setDateOfBirth(LocalDate dateOfBirth){ this.dateOfBirth = dateOfBirth;}
    public LocalDate getDateOfBirth(){ return dateOfBirth;}
    public void setYearOfBirth(int yearOfBirth){ this.yearOfBirth = yearOfBirth;}
    public int getYearOfBirth(){ return yearOfBirth;}
    public void setEyeColour(String eyeColour){ this.eyeColour = eyeColour;}
    public String getEyeOfColour(){ return eyeColour;}
    public void setGender(String gender){ this.gender = gender;}
    public String getGender(){ return gender;}
    public void setHairColour(String hairColour){ this.hairColour = hairColour;}
    public String getHairColour(){ return hairColour;}
    public void setWizard(boolean wizard){ this.wizard = wizard;}
    public boolean getWizard(){ return wizard;}

    public Personagem clone() throws CloneNotSupportedException{
        Personagem novo = new Personagem();
        novo.id = this.id;
        novo.name = this.name;
        novo.alternate_names = (ArrayList<String>) alternate_names.clone();
        novo.house = this.house;
        novo.ancestry = this.ancestry;
        novo.species = this.species;
        novo.patronus = this.patronus;
        novo.hogwartsStaff = this.hogwartsStaff;
        novo.hogwartsStudent = this.hogwartsStudent;
        novo.actorName = this.actorName;
        novo.alive = this.alive;
        novo.dateOfBirth = this.dateOfBirth;
        novo.yearOfBirth = this.yearOfBirth;
        novo.eyeColour = this.eyeColour;
        novo.gender = this.gender;
        novo.hairColour = this.hairColour;
        novo.wizard = this.wizard;
    }

    /*
	 * Metodo para imprimir os dados da classe Personagem
	 */
	public void imprimir(String id) {
		System.out.println(toString());
	}

	/*
	 * Metodo para transformar em string os dados da classe Personagem
	 */
	public String toString() {
		String resp = getId() + " ## " + getName() + " ## " + getAlternate_names() + " ## " + getHouse() + " ## " + getAncestry() + " ## " + 
        getSpecies() + " ## " + getPatronus() + " ## " + getAlive() + " ## " + getDateOfBirth() + " ## " + getYearOfBirth() + " ## " + 
        getEyeOfColour() + " ## " + getGender() + " ## " + getHairColour() + " ## " + getWizard(); 
		return resp;
	}

    /*
	 * Metodo para setar os dados do arquivo csv
	 */
	public void setDados(String linha) {
		String[] dados = linha.split(",");
		setId(dados[0]);
		setName(dados[1]);
		setAlternate_names(dados[2]);
		setHouse(dados[3]);
		setAncestry(dados[4]);
		setSpecies(dados[5]);
		setPatronus(dados[6]);
		setHogwartsStaff(dados[7]);
        setHogwartsStudent(dados[8]);
        setActorName(dados[9]);
        setAlive(dados[10]);
        setDateOfBirth(dados[11]);
        setYearOfBirth(dados[12]);
        setEyeColour(dados[13]);
        setGender(dados[14]);
        setHairColour(dados[15]);
        setWizard(dados[16]);
	}

    /*
	 * Metodo para procurar um jogador pelo id e salva
	 */
	public void ler(String id) throws Exception {
		FileReader file = new FileReader("characters.csv");
		BufferedReader bf = new BufferedReader(file);
		String linha;
		while ((linha = bf.readLine()) != null) {
			String numId = linha.substring(0, linha.indexOf(","));
			try {
				if (numId.equals(id)) {
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

    public static void main(String[] args){

    }
}