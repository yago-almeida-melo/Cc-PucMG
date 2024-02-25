/*
*   806454 - Yago Almeida Melo
*   TP1 - Is em Java - 2024/1
*/

public class Is {
     /*
     * function: isFim
     * @params: String 
     * action: Recebe uma String e retorna verdadeiro se a String for igual a "FIM", senão retorna falso.
     */
    public static boolean isFim(String input){
        return input.charAt(0) == 'F' && input.charAt(1) == 'I' && input.charAt(2) == 'M';
    }
    /*
     * function: onlyVowel
     * @params: String 
     * action: Recebe uma String e retorna verdadeiro se a String tiver somente vogais.
     */
    public static boolean onlyVowel(String str){
        boolean vowels = true;
        for(int i=0;i<str.length();i++){
            if(str.charAt(i) != 'a' && str.charAt(i) != 'e' && str.charAt(i) != 'i' && str.charAt(i) != 'o' && str.charAt(i) != 'u' 
            && str.charAt(i) != 'A' && str.charAt(i) != 'E' && str.charAt(i) != 'I' && str.charAt(i) != 'O' && str.charAt(i) != 'U'){
                vowels = false;
                i = str.length();
            }
        }
        return vowels;
    }
    /*
     * function: onlyConsonants
     * @params: String 
     * action: Recebe uma String e retorna verdadeiro se a String tiver somente consoantes.
     */
    public static boolean onlyConsonants(String str){
        boolean consonants = true;
        for(int i=0;i<str.length();i++){
            if(str.charAt(i) == 'a' || str.charAt(i) == 'e' || str.charAt(i) == 'i' || str.charAt(i) == 'o' || str.charAt(i) == 'u' 
            || str.charAt(i) == 'A' || str.charAt(i) == 'E' || str.charAt(i) == 'I' || str.charAt(i) == 'O' || str.charAt(i) == 'U' || (str.charAt(i)>='0' && str.charAt(i)<='9')){
                consonants = false;
                i = str.length();
            }
        }
        return consonants;
    }
    /*
     * function: isInteger
     * @params: String 
     * action: Recebe uma String e retorna verdadeiro se a String for somente um numero inteiro.
     */
    public static boolean isInteger(String str){
        boolean integer = true;
        for(int i=0;i<str.length();i++){
            if(str.charAt(i)<'0' || str.charAt(i)>'9'){
                integer = false;
                i = str.length();
            }
        }
        return integer;
    }
    /*
     * function: isReal
     * @params: String 
     * action: Recebe uma String e retorna verdadeiro se a String for somente um real.
     */
    public static boolean isReal(String str){
        boolean real = true;
        int virgulasEpontos=0;
        for(int i=0;i<str.length();i++){
            if(str.charAt(i)>='0' && str.charAt(i)<='9'){
                continue;
            } else if(str.charAt(i) == '.' || str.charAt(i) == ','){
                virgulasEpontos++;
            } else{
                real = false;
                i=str.length();
            }
        }
        if(virgulasEpontos>1) real = false;
        return real;
    }
    public static void main(String[] args){
        String input = MyIO.readLine();
        /*
         * Laço de repeticão que irá imprimir na mesma linha se é ou não uma string só e vogal, consoante, um inteiro e um real.
         */
        while(!isFim(input)){
            if(onlyVowel(input)) MyIO.print("SIM ");
            else MyIO.print("NAO ");
            if(onlyConsonants(input)) MyIO.print("SIM ");
            else MyIO.print("NAO ");
            if(isInteger(input)) MyIO.print("SIM ");
            else MyIO.print("NAO ");
            if(isReal(input)) MyIO.print("SIM\n");
            else MyIO.print("NAO\n");
            input = MyIO.readLine(); 
        }
    }
}
