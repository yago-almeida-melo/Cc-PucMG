/*
 * 806454 - Yago Almeida Melo
 * TP1/2024-1:  - Arquivo em Java 
 */

import java.io.*;

public class Arquivo {
    /*
     * function: leitura
     * @params: int, int, RandomAccessFile
     * action: Recebe um arquivo e, recursivamente, lê este arquivo de trás para frente. OBS: Somente numeros reais!
     */
    public static void leitura(int index, int qtd, RandomAccessFile file) throws IOException{
        if(index<qtd){
            file.seek((qtd - index - 1) * Double.BYTES); //Muda de linha do arquivo
            double num = file.readDouble();
            int numInteiro = (int) num;
            if(num == numInteiro){
        	    MyIO.println(numInteiro);
            } else {
        	    MyIO.println(num);
            }
            leitura(index+1, qtd, file);
        } 
    }
    public static void main(String[] args){
        int n = 0;
        n = MyIO.readInt();
        try{
            RandomAccessFile arquivo = new RandomAccessFile("valores.txt", "rw"); // Abertura do arquivo para escrita
            // Leitura e gravação dos valores
            for (int i = 0; i < n; i++) {
                double valor = MyIO.readDouble();
                arquivo.writeDouble(valor);
            }
            arquivo.close();
            arquivo = new RandomAccessFile("valores.txt", "r"); // Abertura para leitura
            arquivo.seek(arquivo.length()- 8); // Move para o fim do arquivo
            leitura(0,n, arquivo);
        } catch (IOException e){
            e.printStackTrace();
        }
        
    }
}
