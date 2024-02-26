/*
*   806454 - Yago Almeida Melo
*   TP1 - Ciframento de Cesar em Java - 2024/1
*/

public class Cifra {
    /*
     * function: isFim
     * @params: String 
     * action: Recebe uma String e retorna verdadeiro se a String for igual a "FIM", senão retorna falso.
     */
    public static boolean isFim(String input){
        return input.charAt(0) == 'F' && input.charAt(1) == 'I' && input.charAt(2) == 'M';
    }
    /*
     * function: Ciframento
     * @params: String 
     * action: Recebe uma String e retorna outra String contendo cada um dos caracteres com um deslocamento pre determinado,
     * sendo ela aqui o 3.
     */
    public static String Ciframento(String x){
        String resp = new String();
        int ascii=0;
        for(int i=0;i<x.length();i++){
            ascii = x.charAt(i)+3;
            resp += (char)ascii;
        }
        return resp;
    }
    public static void main(String[] args){
        String input = MyIO.readLine();
        while(!isFim(input)){ // laço de repetição que executa o ciframento até que a entrada seja "FIM"
            MyIO.println(Ciframento(input));
            input = MyIO.readLine();
        }
    }    
}
