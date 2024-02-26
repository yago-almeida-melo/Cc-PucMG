/*
*   806454 - Yago Almeida Melo
*   TP1 - Palindromo Recursivo em Java - 2024/1
*/

import java.util.*;

public class PalindromoRecu {
  /*
   * function: isFim
   * 
   * @params: (String)
   * Action: Recebe uma String e retorna verdadeiro se a String
   * é igual a "FIM", e falso se não for.
   */
  public static boolean isFim(String x) {
    return x.length() == 3 && x.charAt(0) == 'F' && x.charAt(1) == 'I' && x.charAt(2) == 'M';
  }

  /*
   * function: isPalindromo
   * 
   * @params: (String)
   * Action: Recebe uma String e retorna verdadeiro se a String
   * é um palíndromo, e falso se não for.
   */
  public static boolean isPalindromo(String x) {
    return isPalindromo(x, 0);
  }

  /*
   * function: isPalindromo
   * 
   * @params: (String, int)
   * Action: Recebe uma String e um inteiro, analisa a palavra recursivamente
   * se é ou não palindromo.
   */
  public static boolean isPalindromo(String input, int index) {
    boolean palindromo = true;
    if (index < input.length() / 2 && palindromo) {
      if (input.charAt(index) != input.charAt(input.length() - 1 - index)) {
        palindromo = false;
      } else {
        isPalindromo(input, index + 1);
      }
    }
    return palindromo;
  }

  // Main
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    String entrada = "";
    entrada = sc.nextLine();
    while (!isFim(entrada)) {
      if (isPalindromo(entrada)) {
        System.out.println("SIM");
      } else {
        System.out.println("NAO");
      }
      entrada = sc.nextLine();
    }
    sc.close();
  }
}
