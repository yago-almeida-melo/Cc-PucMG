class CifraCesarRecu{
	private static int chave = 3; // chave para alterar o caracter
	
        public static String Cifra(String palavra, int index){
        	String novaPalavra = new String();
		if(index<palavra.length()){              //if serve como laço de rep. que percorre a str				
			novaPalavra = (char)(palavra.charAt(index)+chave) + Cifra(palavra, index+1);    //recursao
		}
                return novaPalavra;    //retorna o char como str
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

        public static void main(String[] args) {
                String input = new String();      		//str que sera a entrada e a de saida 
                boolean fim = false;   		//flag ate digitar 'FIM'
                input  = MyIO.readLine(); 
                if(strIgual(input,"FIM")) fim = true;     //flag 
                do{
                        String inputCripto = Cifra(input, 0); // chama a funcao Cifra e retorna a String criptografada
                        MyIO.println(inputCripto); // imprime a String criptografada
                        input  = MyIO.readLine();  	 
                        if(strIgual(input,"FIM")) fim = true; 		//flag
                }while(!fim); 					//do-while repete ate que o usuario digite 'FIM'
        }
}

