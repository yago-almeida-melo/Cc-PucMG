import java.util.Random;

class Alteracaoaleatoria{
	public static String AlteraString(String linha, Random gerador){
		String novaLinha ="";
		int letra=0, novaLetra=0;                    //integers de chars (tabela ASCII)
		letra = ('a' + (Math.abs(gerador.nextInt()) % 26));   		//gera primeiro caracter, que sera substituido
		novaLetra = ('a' + (Math.abs(gerador.nextInt()) % 26));		//gera o segundo caracter que substituira o primeiro
		for(int i=0; i<linha.length();i++){                       //for que percorre a str
			if(linha.charAt(i) == (char)letra){               
				novaLinha += (char)novaLetra;        //se for a letra que sera substituida, ela eh trocada pela nova
			}else{                                                  
				novaLinha += linha.charAt(i);			//se nao for, continua os caracteres originais
			}
		}
		return novaLinha;
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
		boolean fim = false;        //flag
		String frase = MyIO.readLine();    //input
		Random gerador = new Random(); 	 //Instancia da class Random
		gerador.setSeed(4);		//semente
		if(strIgual(frase, "FIM")) fim = true;   //flag
		do{
			String novaFrase = AlteraString(frase, gerador);     //do-while para repetir as entradas e saidas
			MyIO.println(novaFrase); 
			frase = MyIO.readLine(); 
			if(strIgual(frase,"FIM")) fim = true; //flag
		}while(!fim);		//fim do-while
	}
}
