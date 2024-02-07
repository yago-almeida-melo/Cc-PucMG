import java.io.*;
import java.net.*;

class Contador{             //classe que contara os elementos
	public int[] a;		//conta a quantidade de vogais , consoantes, <br> e <table>
	public int[] e;
	public int[] i;
	public int[] o;
	public int[] u;
	public int consoantes;
	public int br;
	public int table;
	public String nome;     //nome da pagina
	
	Contador(String nome){ //construtor para armazenar difentes ocorrencias dos caracteres
		this.a = new int[5];        // indices
        	this.e = new int[5];             //0 = letra normal
        	this.i = new int[5];             //1 = com acento agudo
        	this.o = new int[5];		 //2 = com crase
        	this.u = new int[5];             //3 = com til       //4 = com acento circunflexo

        	this.consoantes = 0;
        	this.br = 0;
        	this.table = 0;
        	this.nome = nome;
	}
	
	public String toString(){  //metodo para retorna string que sera printada
		return "a(" + this.a[0] + ") " +
        "e(" + this.e[0] + ") " + 
        "i(" + this.i[0] + ") " + 
        "o(" + this.o[0] + ") " + 
        "u(" + this.u[0] + ") " + 
        "á(" + this.a[1] + ") " +
        "é(" + this.e[1] + ") " +
        "í(" + this.i[1] + ") " +
        "ó(" + this.o[1] + ") " +
        "ú(" + this.u[1] + ") " +
        "à(" + this.a[2] + ") " +
        "è(" + this.e[2] + ") " +
        "ì(" + this.i[2] + ") " +
        "ò(" + this.o[2] + ") " +
        "ù(" + this.u[2] + ") " +
        "ã(" + this.a[3] + ") " +
        "õ(" + this.o[3] + ") " +
        "â(" + this.a[4] + ") " +
        "ê(" + this.e[4] + ") " +
        "î(" + this.i[4] + ") " +
        "ô(" + this.o[4] + ") " +
        "û(" + this.u[4] + ") " +
        "consoante(" + this.consoantes + ") " +
        "<br>(" + this.br + ") " + 
        "<table>(" + this.table + ") " +
        this.nome;
	}
} //fim da class Contador

public class Html{ 
	public static String getHtml(String endereco){ //metodo para pegar html de pagina url
      		URL url;
      		InputStream is = null;
      		BufferedReader br;
      		String resp = new String();
      		String line = new String();

      		try {
         	url = new URL(endereco);
         	is = url.openStream();  
         	br = new BufferedReader(new InputStreamReader(is));

         	while ((line = br.readLine()) != null) {
            		resp += line + "\n";
         	}
      		} catch (MalformedURLException mue) {
         		mue.printStackTrace();
      		} catch (IOException ioe) {
         		ioe.printStackTrace();
      		} 

      		try {
         		is.close();
      		} catch (IOException ioe) {
         		

      	}

      		return resp;
	}
	
	public static boolean isUpper(char c){ //metodo para verificar se caracter eh maiusculo
		return 'A' <= c && c <= 'Z';
	}
	
	public static boolean isLower(char c){ //metodo para verificar se caracter eh minusculo
		return 'a' <= c && c <= 'z';
	}
	
	public static boolean isLetter(char c){ //metodo para verificar se caracter eh letra
        	return isUpper(c) || isLower(c);
    	}
    	
    	public static boolean isVowel(char c){ //metodo para verificar se caracter eh vogal
        	if(isUpper(c))
            		return c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U';
        	else if(isLower(c))
            		return c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u';
        	else
            		return false;
    	}
    	
    	public static boolean isConsonant(char c){ //metodo para verificar se caracter eh consoante
        	return isLower(c) && !isVowel(c);
    	}
    	
    	public static boolean isBr(String str, int i){ //metodo para verificar se string tem <br>
        	return str.charAt(i) == '<' &&
        	str.charAt(i) == 'b' &&
        	str.charAt(i) == 'r' &&
        	str.charAt(i) == '>';
    	}
    	
    	public static boolean isTable(String str, int i){ //metodo para verificar se string tem <table>
        	return str.charAt(i) == '<' &&
        	str.charAt(i + 1) == 't' &&
        	str.charAt(i + 2) == 'a' &&
        	str.charAt(i + 3) == 'b' &&
        	str.charAt(i + 4) == 'l' &&
        	str.charAt(i + 5) == 'e' &&
        	str.charAt(i + 6) == '>';
    	}
    	
    	public static void searchHtml(Contador contador, String html){ //metodo para percorrer html
        	for(int i = 0; i < html.length(); i++){
            		if(isTable(html, i)){  //contador de <table>
                		contador.table++;
                		i += 6;
            		}else if(isBr(html, i)){ //contador de <br>
                		contador.br++;
                		i += 3;
            		}else if(isConsonant(html.charAt(i))){ //contador de consoante
                		contador.consoantes++;
            		}else{                            
                		switch(html.charAt(i)){                   //contador de vogal e suas variantes
                    			case 'a': contador.a[0]++; break;
                    			case 'e': contador.e[0]++; break;
                    			case 'i': contador.i[0]++; break;
                    			case 'o': contador.o[0]++; break;
                    			case 'u': contador.u[0]++; break;
                    			case 225: contador.a[1]++; break;
                    			case 233: contador.e[1]++; break;
                    			case 237: contador.i[1]++; break;
                    			case 243: contador.o[1]++; break;
                    			case 250: contador.u[1]++; break;
                    			case 224: contador.a[2]++; break;
                    			case 232: contador.e[2]++; break;
                    			case 236: contador.i[2]++; break;
                    			case 242: contador.o[2]++; break;
                    			case 249: contador.u[2]++; break;
                    			case 227: contador.a[3]++; break;
                    			case 245: contador.o[3]++; break;
                    			case 226: contador.a[4]++; break;
                   			case 234: contador.e[4]++; break;
                    			case 238: contador.i[4]++; break;
                   			case 244: contador.o[4]++; break;
                    			case 251: contador.u[4]++; break;
                    			default: break;
                		}
           		}
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
        	String nome = new String();  //nome da pagina
        	String addr = new String();  //url
        	String html = new String();  //html da url
        	boolean flag = false;    //flag
        	nome = MyIO.readLine();
        	if(strIgual(nome, "FIM")) flag = true;
        	do{
            		
               		Contador contador = new Contador(nome);
                	addr = MyIO.readLine();
               		html = getHtml(addr);
               		searchHtml(contador, html);
               		MyIO.println(contador.toString());
               		nome = MyIO.readLine();
               		if(strIgual(nome, "FIM")) flag = true;
            	}while(!flag); //fim do-while
    	} //fim da main
}//fim da class Html
