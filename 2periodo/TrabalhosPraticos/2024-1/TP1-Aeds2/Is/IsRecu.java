/*
*   806454 - Yago Almeida Melo
*   TP1 - Is Recursivo em Java - 2024/1
*/

public class IsRecu {
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
     * @params: String e int
     * action: Recebe uma String e retorna verdadeiro se a String tiver somente vogais.
     */
    public static boolean onlyVowel(String str, int i){
        boolean vowels = true;
        if(i < str.length()){
            if(str.charAt(i) != 'a' && str.charAt(i) != 'e' && str.charAt(i) != 'i' && str.charAt(i) != 'o' && str.charAt(i) != 'u' 
            && str.charAt(i) != 'A' && str.charAt(i) != 'E' && str.charAt(i) != 'I' && str.charAt(i) != 'O' && str.charAt(i) != 'U'){
                vowels = false;
            } else{
                vowels = onlyVowel(str, i+1);
            }
        }
        return vowels;
    }
    /*
     * function: onlyConsonants
     * @params: String 
     * action: Recebe uma String e retorna verdadeiro se a String tiver somente consoantes.
     */
    public static boolean onlyConsonants(String str, int i){
        boolean consonants = true;
        if(i < str.length()){
            if(str.charAt(i) == 'a' || str.charAt(i) == 'e' || str.charAt(i) == 'i' || str.charAt(i) == 'o' || str.charAt(i) == 'u' 
            || str.charAt(i) == 'A' || str.charAt(i) == 'E' || str.charAt(i) == 'I' || str.charAt(i) == 'O' || str.charAt(i) == 'U' || (str.charAt(i)>='0' && str.charAt(i)<='9')){
                consonants = false;
            } else{
                consonants = onlyConsonants(str, i+1);
            }
        }
        return consonants;
    }
    /*
     * function: isInteger
     * @params: String 
     * action: Recebe uma String e retorna verdadeiro se a String for somente um numero inteiro.
     */
    public static boolean isInteger(String str, int i){
        boolean integer = true;
        if(i<str.length()){
            if(str.charAt(i)<'0' || str.charAt(i)>'9'){
                integer = false;
            } else{
                integer = isInteger(str, i+1);
            }
        }
        return integer;
    }
    /*
     * function: isReal
     * @params: String 
     * action: Recebe uma String e retorna verdadeiro se a String for somente um real.
     */
    public static boolean isReal(String str, int i, int virgulasEpontos){
        boolean real = true;
        if(i<str.length()){
            if(str.charAt(i)>='0' && str.charAt(i)<='9'){
                real = isReal(str, i+1, virgulasEpontos);
            } else if(str.charAt(i) == '.' || str.charAt(i) ==  ','){
                real = isReal(str, i+1, virgulasEpontos+1);
            } else{
                real = false;
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
            if(onlyVowel(input,0)) MyIO.print("SIM ");
            else MyIO.print("NAO ");
            if(onlyConsonants(input,0)) MyIO.print("SIM ");
            else MyIO.print("NAO ");
            if(isInteger(input,0)) MyIO.print("SIM ");
            else MyIO.print("NAO ");
            if(isReal(input,0,0)) MyIO.print("SIM\n");
            else MyIO.print("NAO\n");
            input = MyIO.readLine(); 
        }
    }
}
