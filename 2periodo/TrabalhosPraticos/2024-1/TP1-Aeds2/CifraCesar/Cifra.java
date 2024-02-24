/*
*   806454 - Yago Almeida Melo
*   Ciframento de Cesar em Java - 2024/1
*/

public class Cifra {
    /*
     * function: Ciframento
     * @params: String 
     * action: Recebe uma String e retorna outra String contendo cada um dos caracteres com um deslocamento pre determinado,
     * sendo ela aqui o 3.
     */
    public static String Ciframento(String x){
        String resp = "";
        int ascii=0;
        for(int i=0;i<x.length();i++){
            ascii = x.charAt(i)+3;
            if(ascii>122 || (ascii>90 && ascii<94)){
                ascii -= 26;
            }
            resp += (char) ascii;
        }
        return resp;
    }
    public static void main(String[] args){
        String x = "";
        x = MyIO.readLine();
        while(!x.equals("FIM")){ // laço de repetição que executa o ciframento até que a entrada seja "FIM"
            System.out.println(Ciframento(x));
            x = MyIO.readLine();
        }
    }    
}
