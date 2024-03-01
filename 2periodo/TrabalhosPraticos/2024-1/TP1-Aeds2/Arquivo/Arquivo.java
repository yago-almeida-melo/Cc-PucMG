import java.io.*;
import java.util.*;

public class Arquivo {
     // Abre o arquivo para escrita
    

     
    public static void leitura(int qtd, RandomAccessFile file){

    }
    public static void main(String[] args){
        int qtd = 0;
        qtd = MyIO.readInt();
        try{
            RandomAccessFile arquivo = new RandomAccessFile("valores.txt", "rw"); // Abertura do arquivo
            // Leitura e gravação dos valores
            for (int i = 0; i < qtd; i++) {
                double valor = MyIO.readDouble();
                arquivo.writeDouble(valor);
            }
            arquivo.close();
            arquivo = new RandomAccessFile("valores.txt", "r");
            leitura(qtd, arquivo);
        } catch (IOException e){
            e.printStackTrace();
        }
        
    }
}
