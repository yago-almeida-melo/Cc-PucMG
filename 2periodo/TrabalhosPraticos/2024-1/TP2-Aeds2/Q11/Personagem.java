 /*
 *  806454 - Yago Almeida Melo
 *  TP1-Q11: CountingSort em Java
 */

 import java.io.FileReader;
 import java.io.BufferedReader;
 import java.io.FileWriter;
 import java.time.Duration;
 import java.time.Instant;
 import java.io.IOException;
 import java.util.Scanner;
 import java.time.LocalDate;
 
 
 /*
  * Class Lista
  * Lista de Alternate_Name & Alternate_Actors
  */
 class Lista{
     public String[] list;
     Lista(){
         this.list = null;
     }
     Lista(String[] list){
         this.list = list;
     }
 
     /*
      * function: getList()
      * @params: null
      * @action: Imprime a Lista desejada
      */
     public String getList(){
         String resp="";
         resp+='{';
         for(int i=0;i<list.length;i++){
             resp += list[i];
             if(i != (list.length-1)) resp += ", ";
         } 
         resp+='}';
         return resp;
     }
 }
 
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
             writer.write("Matricula: 806454\tTempo de execucao: " + this.time + " segundos\tNumero de comparacoes: " + this.comparacoes +
             "\tNumero de movimentacoes: "+ this.movimentacoes);
             writer.close();
         } catch (IOException e) {
             System.out.println("Erro ao escrever o log no arquivo: " + e.getMessage());
         }
     }
 }
 
 class Personagem{
     /*
      * Atributos privados
      */
     private String id, name, house, ancestry, species, patronus, actorName, eyeColour, gender, hairColour;
     private int yearOfBirth;
     private boolean hogwartsStaff, hogwartsStudent, alive, wizard;
     public LocalDate dateOfBirth;
     Lista alternate_names, alternate_actors;
 
     public static Scanner sc = new Scanner(System.in);
 
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
     boolean hogwartsStaff, boolean hogwartsStudent, String actorName,boolean alive, String[] alternate_actors, String dateOfBirth,
     int yearOfBirth, String eyeColour, String gender, String hairColour, boolean wizard){
         this.id = id;
         this.name = name;
         this.alternate_names = new Lista(alternate_names) ;
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
     public void setId(String id){ this.id = id;}
     public String getId(){ return id;}
     public void setName(String name){ this.name = name;}
     public String getName(){ return name;}
     public void setAlternate_names(String[] alternate_names){ this.alternate_names = new Lista(alternate_names); }
     public String getAlternate_names(){ return alternate_names.getList();}
     public void setHouse(String house){ this.house = house;}
     public String getHouse(){ return house;}
     public void setAncestry(String ancestry){ this.ancestry = ancestry;}
     public String getAncestry(){ return ancestry;}
     public void setSpecies(String species){ this.species = species;}
     public String getSpecies(){ return species;}
     public void setPatronus(String patronus){ this.patronus = patronus;}
     public String getPatronus(){ return patronus;}
     public void setHogwartsStaff(String hogwartsStaff){ if(hogwartsStaff.equals("VERDADEIRO")){setHogwartsStaff(true);}else{setHogwartsStaff(false);} }
     public void setHogwartsStaff(boolean hogwartsStaff){ this.hogwartsStaff = hogwartsStaff;}
     public boolean getHogwartsStaff(){ return hogwartsStaff;}
     public void setHogwartsStudent(String hogwartsStudent){if(hogwartsStudent.equals("VERDADEIRO")){setHogwartsStudent(true);}else{setHogwartsStudent(false);} }
     public void setHogwartsStudent(boolean hogwartsStudent){ this.hogwartsStudent = hogwartsStudent;}
     public boolean getHogwartsStudent(){ return hogwartsStudent;}
     public void setActorName(String actorName){ this.actorName = actorName;}
     public String getActorName(){ return actorName;}
     public void setAlive(String alive){if(alive.equals("VERDADEIRO")){setAlive(true);}else{setAlive(false);} }
     public void setAlive(boolean alive){ this.alive = alive;}
     public boolean getAlive(){ return alive;}
     public void setAlternate_Actors(String[] alternate_actors){ this.alternate_actors = new Lista(alternate_actors); }
     public String getAlternate_Actors(){ return alternate_actors.getList(); }
     public void setDateOfBirth(String dateOfBirth){ setDateOfBirth(LocalDate.parse(formatter(dateOfBirth))); }
     public void setDateOfBirth(LocalDate dateOfBirth){ this.dateOfBirth = dateOfBirth;}
     public LocalDate getDateOfBirth(){ return dateOfBirth; }
     public void setYearOfBirth(int yearOfBirth){ this.yearOfBirth = yearOfBirth;}
     public int getYearOfBirth(){ return yearOfBirth;}
     public void setEyeColour(String eyeColour){ this.eyeColour = eyeColour;}
     public String getEyeOfColour(){ return eyeColour;}
     public void setGender(String gender){ this.gender = gender;}
     public String getGender(){ return gender;}
     public void setHairColour(String hairColour){ this.hairColour = hairColour;}
     public String getHairColour(){ return hairColour;}
     public void setWizard(String wizard){if(wizard.equals("VERDADEIRO")){setWizard(true);}else{setWizard(false);} }
     public void setWizard(boolean wizard){ this.wizard = wizard;}
     public boolean getWizard(){ return wizard;}
 
     /*
      * function: clone
      * @params: null
      * @action: Retorna um objeto Personagem, fazendo um clone
      */
     public Personagem clone() throws CloneNotSupportedException{
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
      * function: mostrar 
      * @params: Personagem[]
      * action: Imprime todos os personagens de um Array
      */
     public static void mostrar(Personagem personagens[]){
        for(int i=0;i<personagens.length && personagens[i]!=null;i++){
            imprimir(personagens[i]);
        }
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
         String resp = "["+getId()+" ## "+getName()+" ## "+getAlternate_names()+ " ## "+getHouse()+" ## "+getAncestry()+" ## "+getSpecies()+
         " ## "+getPatronus()+" ## "+getHogwartsStaff()+" ## "+getHogwartsStudent()+" ## "+getActorName()+" ## "+
         getAlive()+" ## "+getDateOfBirth()+" ## "+getYearOfBirth()+" ## "+getEyeOfColour()+" ## "+getGender()+" ## "+
         getHairColour()+" ## "+getWizard()+"]"; 
         return resp;
     }
     public static String toString(Personagem x) {
         String resp = "["+x.getId()+" ## "+x.getName()+" ## "+x.getAlternate_names()+ " ## "+x.getHouse()+" ## "+x.getAncestry()+" ## "+x.getSpecies()+
         " ## "+x.getPatronus()+" ## "+x.getHogwartsStaff()+" ## "+x.getHogwartsStudent()+" ## "+x.getActorName()+" ## "+x.getAlive()+" ## "+formatter(x.getDateOfBirth().toString())+
         " ## "+x.getYearOfBirth()+" ## "+x.getEyeOfColour()+" ## "+x.getGender()+" ## "+x.getHairColour()+" ## "+x.getWizard()+"]";
         return resp;
     }
 
     /*
      * function: booleanToString
      * @params: boolean 
      * @action: Retorna "VERDADEIRO" se o booleano for true e "FALSO" se for false
      */
     public static String booleanToString(boolean x){
         return x ? "VERDADEIRO" : "FALSO";
     }
 
     /* 
      * function: formatter
      * @params: String
      * @action: Retorna a String da data formatada
      */
     public static String formatter(String date){
         String[] x = date.split("-");
         if(x[1].length() == 1){
             x[1] = '0'+ x[1];
         }
         String resp = x[2]+"-"+x[1]+"-"+x[0];
         return resp;
     }
 
     /*
      * function: removePartOfString
      * @params: String & int & int
      * @action: Retorna a string do parametro sem a parte desejada, sendo os inteiros os index que serão retiradoss
      */
     public String removePartOfString(String x, int inicio, int fim){
         String part1 = x.substring(0, inicio);
         String part2 = x.substring(fim+1);
         return part1 + part2;
     }
 
     /*
      * function: subStringOfAlternate
      * @params: String
      * @action: Retorna um array de String com as duas listas
      */
     public String[] subSTringOfAlternate(String line){
         String[] resp = new String[2];
         for(int i=2;i>0;i--){
             int openBracket = line.indexOf("[");
             int closeBracket = line.indexOf("]");
             if(openBracket+1 != closeBracket){
                 resp[resp.length - i] = line.substring(openBracket, closeBracket+1);
             } else{
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
     public String removeLists(String x, String rem){
         x = x.replace(rem, "");
         return x;
     }
 
     /*
      * function: fixAlternates
      * @params: String
      * @action: Retorna um array de String da lista 
      */
     public String[] fixAlternates(String alternate){
         String[] resp = alternate.substring(1,alternate.length()-1).split("', '");
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
      * function: makeArray
      * @params: Personagem[] && int
      * @action: Faz a leitura de IDs e põe no Array de Personagem
      */
    public static int makeArray(Personagem[] p, int key) throws Exception {
         FileReader file;
         BufferedReader bf;
         String line;
         if(key == 0){
             file = new FileReader("/home/yago/Documents/Cc-PucMG/2periodo/TrabalhosPraticos/2024-1/TP2-Aeds2/characters.csv");
             bf = new BufferedReader(file);
         }
         else{
             file = new FileReader("/tmp/characters.csv");
             bf = new BufferedReader(file);
         }
         int i = 0;
         String id = sc.nextLine();
         while(!id.equals("FIM")){
             // Reset file pointer to the beginning of the file
             if(key == 0){
                 file = new FileReader("/home/yago/Documents/Cc-PucMG/2periodo/TrabalhosPraticos/2024-1/TP2-Aeds2/characters.csv");
                 bf = new BufferedReader(file);
             }
             else{
                 file = new FileReader("/tmp/characters.csv");
                 bf = new BufferedReader(file);
             }
             while ((line = bf.readLine()) != null) {
                 try{
                     String[] dados = line.split(";");
                     if(id.equals(dados[0])){
                         Personagem per = new Personagem();
                         per.setDados(line);
                         p[i] = per;
                         i++;
                         break; // Exit the loop after finding the character
                     }
                 } catch(Exception e){
                     e.printStackTrace();
                 }
             }
             id = sc.nextLine();
         }
         p = fixLength(p, i);
         bf.close();
         file.close();
         return i;
    }

     /*
      * function: fixLength
      * @params: Personagem[] && int
      * action: Retorna o Array de Personagem com o tamanho correto
      */
    public static Personagem[] fixLength(Personagem p[], int novoTamanho){
        Personagem[] newArray = new Personagem[novoTamanho];
        for (int i = 0;i<novoTamanho;i++) {
            newArray[i] = p[i];
        }
        return newArray;
    }
     
     /*
      * function: swap
      * @params: Personagem[] && int && int
      * action: Troca dois personagens
      */
    public static void swap(Personagem p[], int i, int j, Log log){
        Personagem tmp = p[i];
        p[i] = p[j];
        p[j] = tmp;
        log.incrementaMov(); log.incrementaMov(); log.incrementaMov();
    }

    /*
       * function: getMaior
       * @params: Personagem[] && int
       * action: Retorna o maior elemento do array.
       */
      public static int getMaior(Personagem array[], int tam) {
        int maior = array[0].getYearOfBirth();
 
         for (int i = 0; i < tam; i++) {
          if(maior < array[i].getYearOfBirth()){
             maior = array[i].getYearOfBirth();
          }
         }
        return maior;	
     }

     /*
      * function: counting
      * @params: Personagem[] && int && Log 
      * action: Ordena o Array de Personagem pelo atributo yearOfBirth, usando CountingSOrt
      */
      public static void counting(Personagem sub[], int tam, Log log) {
        //Array para contar o numero de ocorrencias de cada elemento
        int[] count = new int[getMaior(sub, tam) + 1];
        Personagem[] ordenado = new Personagem[tam];
        for (int i = 0; i < count.length; count[i] = 0, i++);
  
        for (int i = 0; i < tam; count[sub[i].getYearOfBirth()]++, i++);
  
        for(int i = 1; i < count.length; count[i] += count[i-1], i++);
  
        for(int i = tam-1; i >= 0; ordenado[count[sub[i].getYearOfBirth()]-1] = sub[i], count[sub[i].getYearOfBirth()]--, i--);
  
        for(int i = 0; i < tam; sub[i] = ordenado[i], log.incrementaMov(),i++);

        sort(sub, tam,log);
        mostrar(sub);
     }

     /*
      * function: sort
      * @params: Personagem[] && int && Log 
      * action: Ordena o Array de Personagem pelo atributo por nome se o yearOfBirth for igual, usando Insercao
      */
     public static void sort(Personagem array[], int tam, Log log){
        for(int i=0; i<tam;i++){
            int menor = i;
            for(int j=i+1;j<tam; j++){
                if(array[menor].getYearOfBirth() == array[j].getYearOfBirth() && array[menor].getName().compareTo(array[j].getName()) > 0){
                    menor = j;
                }
                log.incrementaComp();
            }
            swap(array, menor, i, log);
        }
     }
  
     //MAIN
    public static void main(String[] args) throws Exception{
        Personagem personagens[] = new Personagem[404];
        Log log = new Log("/tmp/806454_countingsort.txt");
        int tam = makeArray(personagens, 1);  // Segundo parametro: 0 para teste / 1 para enviar ao verde com /tmp/characters.csv
        Instant start = Instant.now();
        counting(personagens, tam, log);
        Instant end = Instant.now();
        long elapsedTime = Duration.between(start, end).toMillis();
        log.time = (float) elapsedTime / 1000; // Tempo em segundos
        log.registroLog();
    }
}