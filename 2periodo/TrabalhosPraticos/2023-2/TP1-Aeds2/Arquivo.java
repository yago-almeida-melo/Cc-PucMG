
import java.io.*;

public class Arquivo {
    public static void main(String[] args) {
            // Leitura do número de valores a serem lidos
	 try {
            int n = MyIO.readInt();

            // Abre o arquivo para escrita
            RandomAccessFile arquivo = new RandomAccessFile("valores.txt", "rw");

            // Leitura e gravação dos valores
            for (int i = 0; i < n; i++) {
                double valor = MyIO.readDouble();
                arquivo.writeDouble(valor);
            }

            // Fecha o arquivo
            arquivo.close();

            // Reabre o arquivo para leitura
            arquivo = new RandomAccessFile("valores.txt", "r");

            // Move o ponteiro para o final do arquivo
            arquivo.seek(arquivo.length()-8);

            // Lê os valores de trás para frente e exibe na tela
            for (int i = 0; i < n; i++) {
        	arquivo.seek((n - i - 1) * Double.BYTES); //pula para o final do arquivo e vai ate o inicio do arquivo
        	double num = arquivo.readDouble();
        	int numInteiro = (int) num;
        	if(num == numInteiro){
        		MyIO.println(numInteiro);
        	} else {
        		MyIO.println(num);
        	}
        	
    	    }

            // Fecha o arquivo novamente
            arquivo.close();
	    } catch (IOException e) {  
           	e.printStackTrace();
            } catch (NumberFormatException e) {  //excecao quando nao eh inserido um  numero valido
            	System.err.println("Formato invalido para número real.");
            }
    }
}


