package File;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ParNomeId implements RegistroHashExtensivel<ParNomeId> {
    private int id;
    private String nome;
    private final short TAMANHO = 54;

    public ParNomeId() {
        this.id = -1;
        this.nome = "";
    }

    public ParNomeId(String nome, int id) throws Exception {
        this.id = id;
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;                                 
    }

 
    @Override
    public int hashCode() {
        return hash(this.id);
    }

    public short size() {
        return this.TAMANHO;
    }

    public String toString() {
        return "("+this.id + ";" + this.nome+")";
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeUTF(this.nome);
        for(int i = this.nome.length();i<TAMANHO-4;i++){
            dos.writeByte(' ');
        }
        dos.writeInt(this.id);
        return baos.toByteArray();
    }

    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        this.nome = dis.readUTF().trim();
        this.id = dis.readInt();
    }

    public static int hash(int id) throws IllegalArgumentException {
        // Aplicar uma função de hash usando um número primo grande
        int hashValue = (int) (id % (int)(1e9 + 7));
        // Retornar um valor positivo
        return Math.abs(hashValue);
    }
}
