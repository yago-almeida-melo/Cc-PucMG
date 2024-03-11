/*
 *    806454 - Yago Almeida Melo
 *    TP1 - Leitura HTML em java
 */

import java.io.*;
import java.net.*;

class Contador{
	public int[] a;		
	public int[] e;
	public int[] i;
	public int[] o;
	public int[] u;
	public int consoantes;
	public int br;
	public int table;
	public String nome;

   /*
    * Construtor de Contador
    */
   Contador(String nome){ 
		this.a = new int[5];             // indices
      this.e = new int[5];             //0 = letra normal (a)
      this.i = new int[5];             //1 = com acento agudo (á)
      this.o = new int[5];		         //2 = com crase (à)
      this.u = new int[5];             //3 = com til (ã)      
      this.consoantes = 0;             //4 = com acento circunflexo (â)
      this.br = 0;
      this.table = 0;
      this.nome = nome;
	}

    /*
    * function: toString  
    * @params: void
    * action: Retorna String contendo as quantidades de cada informação pedida no enunciado
    */
   public String toString(){  
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

   
} //FIM de Contador
class Html {

    /*
    * function: getHtml
    * @params: String
    * action: Retorna o contéudo HTML de um url em String
    */
    public static String getHtml(String endereco){
      String resp="";
      try{
         URL url = new URL(endereco);
         BufferedReader bf = new BufferedReader(new InputStreamReader(url.openStream()));
         while((endereco = bf.readLine()) != null) resp += endereco;
      } catch(MalformedURLException mue){
         mue.printStackTrace();
      } catch(IOException e){
         e.printStackTrace();
      }
      return resp;
   }

    /*
    * function: isUpper 
    * @params: char
    * action: Verifica se caracter é uma letra maiúscula
    */
   public static boolean isUpper(char c){ 
		return 'A' <= c && c <= 'Z';
	}
	
    /*
    * function: isLower
    * @params: char
    * action: Verifica se caracter é uma letra minúscula
    */
	public static boolean isLower(char c){ 
		return 'a' <= c && c <= 'z';
	}
	
    /*
    * function: isLetter 
    * @params: char
    * action: Verifica se caracter é uma letra do alfabeto
    */
	public static boolean isLetter(char c){ 
      return isUpper(c) || isLower(c);
   }
   
    /*
    * function:  isVowel
    * @params: char
    * action: Verifica se caracter é uma vogal
    */
   public static boolean isVowel(char c){
      if(isUpper(c))
   		return c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U';
      else if(isLower(c))
         return c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u';
      else
   		return false;
    	}
   
    /*
    * function: isConsonant 
    * @params: char
    * action: Verifica se caracter é uma consoante minúscula.
    */
   public static boolean isConsonant(char c){
      return isLower(c) && !isVowel(c);
   }
   
    /*
    * function: isBr 
    * @params: String & int
    * action: Verifica se String é igual a "<br>"
    */
   public static boolean isBr(String str, int i){ 
      return str.charAt(i) == '<' &&
      str.charAt(i+1) == 'b' &&
      str.charAt(i+2) == 'r' &&
      str.charAt(i+3) == '>';
   }

    /*
    * function: isTable
    * @params: String & int
    * action: Verifica se String é igual a "<table>"
    */
   public static boolean isTable(String str, int i){ 
      return str.charAt(i) == '<' &&
      str.charAt(i + 1) == 't' &&
      str.charAt(i + 2) == 'a' &&
      str.charAt(i + 3) == 'b' &&
      str.charAt(i + 4) == 'l' &&
      str.charAt(i + 5) == 'e' &&
      str.charAt(i + 6) == '>';
   }

   /*
    * function: contar
    * @params: Contador & String
    * action: conta os elementos pedidos da Questão. Ex: Vogais, consoantes, <tables>...
    */
   public static void contar(Contador contador,String html){
      for(int i=0; i<html.length();i++){
         if(isTable(html, i)){  
            contador.table++;
            i += 6;
         }else if(isBr(html, i)){ 
            contador.br++;
            i += 3;
         }else if(isConsonant(html.charAt(i))){ 
            contador.consoantes++;
         }else{                            
            switch(html.charAt(i)){                   
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

   /*
    * function: isFim 
    * @params: String
    * action: Verifica se String do parametro é igual a "FIM"
    */
   public static boolean isFim(String in){ 
      return in.length() == 3 && in.charAt(0)=='F' && in.charAt(1)=='I' && in.charAt(2)=='M';
   }

   public static void main(String[] args){
      String nome = MyIO.readLine();
      while(!isFim(nome)){   // leitura de dados até que se digite FIM
         Contador x = new Contador(nome); 
         String url = MyIO.readLine();
         String content = getHtml(url);
         contar(x, content);
         MyIO.println(x.toString());
         nome = MyIO.readLine();
      }
   }    
}
