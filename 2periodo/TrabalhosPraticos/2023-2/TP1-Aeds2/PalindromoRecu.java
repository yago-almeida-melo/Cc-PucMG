class PalindromoRecu{
	public static boolean verificaPalindromo(String str, int i){
		if(i >= str.length() / 2) { // Caso Base, se atingir ate a metade da string
           		return true;
        	}
        	if(str.charAt(i) != str.charAt(str.length() - i - 1)) {  //compara os caracteres e retorna falso se nao for igual
            		return false;
        	}
        	return verificaPalindromo(str, i + 1); //chama a funcao novamente alterando o valor de i
	}

	public static boolean strIgual(String s1, String s2) {         //método para verificar se duas str nos parametros sao iguais
		boolean igual = true;
		if(s1.length() != s2.length()) igual = false; // Verifica se strings tem o mesmo tamanho
		int i = 0;
		while(igual && i < s1.length()) {  // Se tamanho for igual, compara cada caracter
			if (s1.charAt(i) != s2.charAt(i)) igual = false;
			i++;
		}//fim while
		return igual;
	}  //fim strIgual
	
	public static void main(String[] args){
		String str = "";
        	boolean fim = false; //flag
		str = MyIO.readLine();
                if (strIgual(str,"FIM")) fim = true;
        	do {							//do-while para repetir entradas e saídas até que digite "FIM" 
            		if (verificaPalindromo(str,0)) MyIO.println("SIM");
            		else	MyIO.println("NAO");
			str = MyIO.readLine();
                        if (strIgual(str,"FIM")) fim = true;
        	} while (!fim);
	}
}
