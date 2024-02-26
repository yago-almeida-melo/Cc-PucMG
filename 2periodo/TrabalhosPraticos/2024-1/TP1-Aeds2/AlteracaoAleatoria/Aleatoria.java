import java.util.Random;
/*
*   806454 - Yago Almeida Melo
*   TP1 - Alteracao Aleatoria em Java - 2024/1
*/

public class Aleatoria {   
    static Random gerador = new Random();
    
     /*
     * function: isFim
     * @params: String 
     * action: Recebe uma String e retorna verdadeiro se a String for igual a "FIM", senão retorna falso.
     */
    public static boolean isFim(String input){
        return input.charAt(0) == 'F' && input.charAt(1) == 'I' && input.charAt(2) == 'M';
    }

    /*
     * function: AlteracaoAleatoria
     * @params: String
     * action: Sorteia uma letra de 'a' a 'z' para ser trocada com outra letra sorteada em uma String
     * e retorna esta String modificada. 
     */
    public static String AlteracaoAleatoria(String input){
        String resp = "";
        char sorteado1 = (char) ('a'+(Math.abs(gerador.nextInt()) % 26));
        char sorteado2 = (char) ('a'+(Math.abs(gerador.nextInt()) % 26));
        for(int i=0;i<input.length();i++){
            if(input.charAt(i)==sorteado1){
                resp += sorteado2;
            } else{
                resp += input.charAt(i);
            }
        }
        return resp;
    }
    public static void main(String[] args){
        gerador.setSeed(4);
        String input = MyIO.readLine(); 
        while(!isFim(input)){
            MyIO.println(AlteracaoAleatoria(input));
            input = MyIO.readLine(); 
        }
    }
}
