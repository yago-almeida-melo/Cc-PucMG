import java.util.*;

class Palindromo {
	public static boolean verificaPalindromo(String string) {  //metodo para verificar se string é palindromo
        boolean ehPalindromo = true;
        for (int i = 0, j = string.length() - 1; i < j; i++, j--) { // for para comparar os caracteres até a metade da string
            if (string.charAt(i) != string.charAt(j)) {
                ehPalindromo = false;
                i = j;  //interrompe o for e string não é palindromo
            }
        }
        return ehPalindromo; //variavel retornará false ou true
    }

    public static void main(String[] args) { //controle de fluxo do código
        Scanner scanner = new Scanner(System.in); //Scanner para inputs
        String string;
        boolean fim = false; //flag 
        do {							//do-while para repetir entradas e saídas até que digite "FIM" 
            string = scanner.nextLine();
            if (string.equals("FIM"))
                fim = true;
            else if (verificaPalindromo(string))
                System.out.println("SIM");
            else
                System.out.println("NAO");
        } while (!fim);

        scanner.close(); //fecha scanner
    
    }
}
