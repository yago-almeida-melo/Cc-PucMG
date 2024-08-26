import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Constructor;

/*
 * CRUD: Faz as operçaões de inserção, remoção, atualização e listagem de registros
 */
public class CRUD<T extends Registro> {
    Constructor<T> construtor;
    final int TAM_CABECALHO = 4;
    RandomAccessFile arquivo;
    String nomeArquivo;

    /*
     * Construtor de CRUD: Iniciliza o arquivo 
     */
    public CRUD(Constructor<T> c, String name) throws IOException{
        File f = new File(".//dados");
        if(!f.exists()){
            f.mkdir();
        }
        this.nomeArquivo = ".//dados//"+name;
        this.construtor = c;
        arquivo = new RandomAccessFile(this.nomeArquivo, "rw");
        if(arquivo.length()<TAM_CABECALHO){
            arquivo.writeInt(0);
        }
    }

    /*
     * Função: create
     * @params: <T>
     * Cria um registro no arquivo;
     */
    public int create(T obj) throws IOException {
        arquivo.seek(0);
        int ultimoID = arquivo.readInt();
        int novoID = ultimoID + 1;
        arquivo.seek(0);
        arquivo.writeInt(novoID);

        obj.setId(novoID);

        arquivo.seek(arquivo.length());
        byte[] b = obj.toByteArray();
        arquivo.writeByte(' '); // Lápide (registro ativo)
        arquivo.writeShort(b.length); // Tamanho do registro
        arquivo.write(b); // Conteúdo do registro

        return novoID;
    }

    /*
     * Função: read
     * @params: int
     * Faz a busca sequencial de um registro, a partir do id, e o retorna;
     */
    public T read(int id) throws Exception {
        arquivo.seek(TAM_CABECALHO);
        while (arquivo.getFilePointer() < arquivo.length()) {
            byte lapide = arquivo.readByte();
            int tamanho = arquivo.readShort();
            byte[] b = new byte[tamanho];
            arquivo.read(b);

            if (lapide == ' ') { // Registro ativo
                T obj = construtor.newInstance();
                obj.fromByteArray(b);
                if (obj.getId() == id) {
                    return obj;
                }
            }
        }
        return null;
    }

    /*
     * Função: update
     * @params: <T>
     * Atualiza um registro;
     */
    public boolean update(T novoObj) throws Exception {
        arquivo.seek(TAM_CABECALHO);
        while (arquivo.getFilePointer() < arquivo.length()) {
            long posicao = arquivo.getFilePointer();
            byte lapide = arquivo.readByte();
            int tamanho = arquivo.readShort();
            byte[] b = new byte[tamanho];
            arquivo.read(b);

            if (lapide == ' ') { // Registro ativo
                T obj = construtor.newInstance();
                obj.fromByteArray(b);
                if (obj.getId() == novoObj.getId()) {
                    byte[] novoRegistro = novoObj.toByteArray();
                    if (novoRegistro.length <= tamanho) {
                        arquivo.seek(posicao + 3); // Posiciona após lápide e tamanho
                        arquivo.write(novoRegistro);
                    } else {
                        // Marca o registro atual como excluído
                        arquivo.seek(posicao);
                        arquivo.writeByte('*');

                        // Escreve o novo registro no final do arquivo
                        arquivo.seek(arquivo.length());
                        arquivo.writeByte(' ');
                        arquivo.writeShort(novoRegistro.length);
                        arquivo.write(novoRegistro);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    /*
     * Função: delete
     * @params: int
     * Deleta um registro, a partir de seu id;
     */
    public boolean delete(int id) throws Exception {
        arquivo.seek(TAM_CABECALHO);
        while (arquivo.getFilePointer() < arquivo.length()) {
            long posicao = arquivo.getFilePointer();
            byte lapide = arquivo.readByte();
            int tamanho = arquivo.readShort();
            byte[] b = new byte[tamanho];
            arquivo.read(b);

            if (lapide == ' ') { // Registro ativo
                T obj = construtor.newInstance();
                obj.fromByteArray(b);
                if (obj.getId() == id) {
                    arquivo.seek(posicao);
                    arquivo.writeByte('*'); // Marca o registro como excluído
                    return true;
                }
            }
        }
        return false;
    }

    //Fecha o arquivo
    public void close() throws IOException {
        arquivo.close();
    }
}
