class Is{
	public static boolean onlyVowels(String linha){
		boolean soVogais = true; 					//variavel para verificar se a string toda eh de vogais
		char[] vogais = {'a','e','i','o','u','A','E','I','O','U'};
		for(int i=0;i<linha.length();i++){ 				//for para comparar cada caracter da string
			boolean ehVogal=false; 					// variavel que confere se o caracter em questao eh vogal
			for(int j=0;j<vogais.length;j++){ 			//for para comparar se o caracter i eh uma vogal minuscula ou maiuscula
				if(vogais[j] == linha.charAt(i)){ 		//se for vogal, interrompe o for e ja pula pro prox caracter 
					ehVogal = true;
					j = vogais.length;
				}
			}
			if(!ehVogal){ 			//se nao for vogal, interrompe o for exterior e string nao eh so de vogais
				soVogais = false;
				i = linha.length();
			}
		}
		return soVogais;
	}

	public static boolean onlyConsonants(String linha){
		boolean soConsoantes = true; 									// variavel para ver se str eh somente consoantes
                char[] vogais = {'a','e','i','o','u','A','E','I','O','U'};
                for(int i=0;i<linha.length();i++){ 										//for para comparar cada caracter da str
                        boolean ehConsoante=true; 											//variavel que confere se o caracter em questao eh consoante
			if((linha.charAt(i) >= 'a' && linha.charAt(i) <= 'z') || (linha.charAt(i) >= 'A' && linha.charAt(i) <= 'Z')) { 	//confere se o caracter eh um caracter especial
                        	for(int j=0;j<vogais.length;j++){							//for para passar por cada vogal,if para verificar se caracter eh uma vogal, se sim, 
					if(vogais[j] == linha.charAt(i)){                                               //str nao eh vogal
                                        	ehConsoante = false;
                                        	j = vogais.length;   
                                	}
                        	}
                        
				if(!ehConsoante){                   //se um caracter nao for consoante, str nao sera toda consoante
                               		soConsoantes = false;
                               		i = linha.length();
                       		}
				
                	} else {                                  //se nao, ela sera somente consoantes
				soConsoantes = false;
				i = linha.length();
			}
		}
                return soConsoantes;
	}

	public static boolean isInteger(String linha){
		boolean ehInt = true;                  //variavel para verificar se eh integer
		for(int i=0;i<linha.length();i++){       //for para passar por cada caracter da str
			char c = linha.charAt(i);	//caracter na posicao i
			if(c < '0' || c > '9'){            //if para conferir se eh um int
			       	ehInt = false;
				i = linha.length();
			}
		}
		return ehInt;
	}

	public static boolean isReal(String linha){
		boolean ehReal = true;                 //variavel para verificar real
		int contador = 0;                      //contador int para contar o numero de ',' e '.'
		for(int i=0;i<linha.length();i++){     //for para percorrer str
			char c = linha.charAt(i);       //caracter na posicao i
			if(c == '.' || c == ','){       // se for . ou , contador eh incrementado
			       	contador++;
				if(contador>1){            //se o contador tiver mais de um '.' ou ',', nao eh real e interrompe o codigo 
					ehReal = false;  
					i = linha.length();
				}
			} else if(c < '0' || c > '9'){         //confere se eh um int em cada posicao
				ehReal = false;
				i = linha.length();
			}
		}
		return ehReal;	
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
                	if(onlyVowels(frase)) MyIO.print("SIM ");
                	else MyIO.print("NAO ");
                		//Analisa Consoantes
                	if(onlyConsonants(frase)) MyIO.print("SIM ");
                	else MyIO.print("NAO ");
                		//Analisa Inteiro
                	if(isInteger(frase)) MyIO.print("SIM ");
               		else MyIO.print("NAO ");
                		//Analisa Real
               	 	if(isReal(frase)) MyIO.println("SIM");
               		else MyIO.println("NAO");
				//le novamente  
			frase = MyIO.readLine();
			if(strIgual(frase,"FIM")) fim = true;

		}while(!fim);           //fim do-while
	} //fim da main
}  //fim da class Is


