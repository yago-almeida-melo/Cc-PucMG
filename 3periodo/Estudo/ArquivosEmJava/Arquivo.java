
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Arquivo {
    final int TAM_CABECALHO = 4;
    RandomAccessFile arquivo;
    String nomeArquivo;

    public Arquivo(String na) throws IOException {
        File d = new File("dados");
        if(!d.exists())
            d.mkdir();
        this.nomeArquivo = "dados/"+na;
        arquivo = new RandomAccessFile(this.nomeArquivo, "rw");
        if(arquivo.length()<TAM_CABECALHO) {
            // inicializa o arquivo, criando seu cabecalho
            arquivo.writeInt(0);
        }
    }

    public int create(Carros c) throws IOException {
        arquivo.seek(0);
        int proximoID = arquivo.readInt()+1;
        arquivo.seek(0);
        arquivo.writeInt(proximoID);
        c.setId(proximoID);
        arquivo.seek(arquivo.length());
        arquivo.writeInt(c.id);
        arquivo.writeUTF(c.modelo);
        arquivo.writeInt(c.ano);
        arquivo.writeFloat(c.preco);
        return c.id;
    }
    
    public Carros read(String modelo) throws IOException {
        Carros c;
        arquivo.seek(TAM_CABECALHO);
        while(arquivo.getFilePointer()<arquivo.length()) {
            c = new Carros();
            c.id = arquivo.readInt();
            c.modelo = arquivo.readUTF();
            c.ano = arquivo.readInt();
            c.preco = arquivo.readFloat();
            if(c.modelo.compareTo(modelo)==0){
                System.out.println("ACHOU");
                return c;
            } 
        }
        return null;
    }

    public void close() throws IOException {
        arquivo.close();
    }
}

