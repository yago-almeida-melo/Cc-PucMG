class IsRecu {
	public static boolean soVogal(String str, int index) {
    		if (index == str.length()) {  //flag para parar recursao
        		return true;
    		}
    		char ch = str.charAt(index); //ch eh a posicao index da string str
		if(ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z'){  //avalia se for uma letra do alfabeto
    			if (ch == 'a' || ch == 'e' || ch == 'i' || ch == 'o' || ch == 'u' || ch == 'A' || ch == 'E' || ch == 'I' || ch == 'O' || ch == 'U') {
        			return soVogal(str, index + 1);   //avalia se for vogal e retorna a funcao se verdadeiro
    			} else {
        			return false; //nao eh vogal
    			}
		} else {
			return false; // nao eh letra do alfabeto
		}
	}

	public static boolean soConsoante(String str, int index) {
    		if (index == str.length()) { //flag para parar recursao
        		return true;
    		}
    		char ch = str.charAt(index);
		if(ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z'){ //avalia se for uma letra do alfabeto
    			if (ch == 'a' || ch == 'e' || ch == 'i' || ch == 'o' || ch == 'u' || ch == 'A' || ch == 'E' || ch == 'I' || ch == 'O' || ch == 'U') {
        			return false;  // retorna flaso se tiver uma vogal
    			} else {
        			return soConsoante(str, index + 1); //se nao, chama a funcao novamente
    			}
		} else {
			return false; //nao eh letra do alfabeto
		}
	}

	public static boolean ehInteger(String str, int index) {
    		if (index == str.length()) { //flag para parar recursao
        		return true;
    		}
    		char ch = str.charAt(index);
    		if (ch >= '0' && ch <= '9') { //avalia se caracter da posicao index eh um numero
        		return ehInteger(str, index + 1); //se seim, retorna funcao
    		} else {
        		return false; //se nao, retorna falso
    		}
	}

	public static boolean ehDouble(String str, int index, int contador) {
    	if (index == str.length()) { 
        	return contador <= 1; //verifica o numero de pontos ou virgulas
    	}
    	char ch = str.charAt(index);
    	if ((ch >= '0') && (ch <= '9')) { //verifica se eh um numero
        	return ehDouble(str, index + 1, contador); //se sim, retorna a funcao
    	} else if ((ch == ',' || ch == '.')) { // verifica se eh um ponto ou virgula
        	return ehDouble(str, index + 1, contador + 1); // se sim, chama a funcao novamente e incrementa o contador
    	} else {
        		return false; //se nao, retorna falso
    		}
	}
	
	public static boolean strIgual(String s1, String s2) {             //método para verificar se duas str nos parametros sao iguais
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
		boolean fim = false;
		String frase = MyIO.readLine();
		if(strIgual(frase,"FIM")) fim = true;          //flag
		do{
				//Analisa Vogais
                	if(soVogal(frase, 0)) MyIO.print("SIM ");
                	else MyIO.print("NAO ");
                		//Analisa Consoantes
                	if(soConsoante(frase, 0)) MyIO.print("SIM ");
                	else MyIO.print("NAO ");
                		//Analisa Inteiro
                	if(ehInteger(frase, 0)) MyIO.print("SIM ");
               		else MyIO.print("NAO ");
                		//Analisa Real
               	 	if(ehDouble(frase, 0, 0)) MyIO.println("SIM");
               		else MyIO.println("NAO");
				//le novamente  
			frase = MyIO.readLine();
			if(strIgual(frase,"FIM")) fim = true;

		}while(!fim);           //fim do-while
	}
}
