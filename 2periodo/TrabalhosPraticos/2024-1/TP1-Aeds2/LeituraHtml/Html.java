import java.io.*;
import java.net.*;

class Contador{
	public int[] a;		//conta a quantidade de vogais , consoantes, <br> e <table>
	public int[] e;
	public int[] i;
	public int[] o;
	public int[] u;
	public int consoantes;
	public int br;
	public int table;
	public String nome;

   Contador(String nome){ //construtor para armazenar difentes ocorrencias dos caracteres
		this.a = new int[5];             // indices
      this.e = new int[5];             //0 = letra normal
      this.i = new int[5];             //1 = com acento agudo
      this.o = new int[5];		         //2 = com crase
      this.u = new int[5];             //3 = com til       //4 = com acento circunflexo
      this.consoantes = 0;
      this.br = 0;
      this.table = 0;
      this.nome = nome;
	}

   public boolean isConsonant(char c){
      boolean consoante = true;
      return consoante;
   }
}
class Html {
    public static String getHtml(String endereco){
      URL url;
      InputStream is = null;
      BufferedReader br;
      String resp = "", line;

      try {
         url = new URL(endereco);
         is = url.openStream();  // throws an IOException
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
         // nothing to see here

      }

      return resp;
   }
    public static void main(String[] args){
        MyIO.print(getHtml("http://maratona.crc.pucminas.br/series/Black_Mirror.html"));
    }    
}
