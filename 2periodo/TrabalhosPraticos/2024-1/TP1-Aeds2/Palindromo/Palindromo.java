/*
*   806454 - Yago Almeida Melo
*   Palindromo em Java - 2024/1
*/

class Palindromo{
    /*
    *   function: isFim
    *   @params: (String)
    *   Action: Recebe uma String e retorna verdadeiro se a String
    *   é igual a "FIM", e falso se não for.
    */
    public static boolean isFim(String input){
        return input.charAt(0) == 'F' && input.charAt(1) == 'I' && input.charAt(2) == 'M';
    }
    /*
    *   function: isPalindromo
    *   @params: (String)
    *   Action: Recebe uma String e retorna verdadeiro se a String
    *   é um palíndromo, e falso se não for.
    */
    public static boolean isPalindromo(String ptr){
        boolean palindromo = true;
        int len = ptr.length();
        for(int i=0,j = len-1 ;i < (len/2);i++, j--){
            if(ptr.charAt(i) != ptr.charAt(j)){
                palindromo = false;
            }
        }
        return palindromo;
    }
    //Main
    public static void main(String[] args){
        String input = "";
        input = MyIO.readLine();
        while(!isFim(input)){   //Repetição até String == "FIM"
            if(isPalindromo(input)) MyIO.println("SIM");
            else MyIO.println("NAO");
            input = MyIO.readLine();
        }
    }
}