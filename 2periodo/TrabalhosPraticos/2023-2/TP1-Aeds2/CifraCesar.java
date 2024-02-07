

class CifraCesar{
        public static String Cifra(String palavra){
		char[] charsCriptografadas = new char[palavra.length()]; //array de char para mensagem criptografada
		int tabelaAscii=0;
        	for (int i = 0; i < palavra.length(); i++) { //loop para passar por cada caracter da String
            		tabelaAscii = palavra.charAt(i)+3; // transforma o char em um int (tabela ASCII) com +3
			charsCriptografadas[i] = (char)tabelaAscii; //movimentacao para a mensagem criptografada
		}
			return new String(charsCriptografadas);
	}
    	public static void main(String[] args) {
		String input;
		boolean fim = false;
		input  = MyIO.readLine();
		if(input.equals("FIM")) fim = true; //flag 
		do{
        		String inputCripto = Cifra(input); // chama a funcao Cifra e retorna a String criptografada
        		MyIO.println(inputCripto); // imprime a String criptografada
			input  = MyIO.readLine();
                	if(input.equals("FIM")) fim = true; //flag
		}while(!fim); //do-while repete ate que o usuario digite 'FIM'

	}	
}        

