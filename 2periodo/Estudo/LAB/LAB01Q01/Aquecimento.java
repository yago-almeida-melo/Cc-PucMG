import java.io.*;

public class Aquecimento {
    public static void main(String []args){
        String in = new String();
        in = MyIO.readLine();
        while(in.equals("FIM") == false){
            int qtd=0;
            for(int j=0;j<in.length();j++){
                if(in.charAt(j) >='A' && in.charAt(j) <= 'Z' ) qtd++;
            }    
            MyIO.println(qtd);
            in = MyIO.readLine();
        }
        
    }
}
