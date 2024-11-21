package Entidades;

import Interface.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.Normalizer;
import java.util.regex.Pattern;

public class Rotulo implements Registro {

    public int id;
    public String rotulo;

    // Construtor padrão
    public Rotulo() {
        this(-1, "");
    }

    // Construtor com apenas o rotulo
    public Rotulo(String n) {
        this(-1, n);
    }

    // Construtor com id e rotulo
    public Rotulo(int i, String n) {
        this.id = i;
        this.rotulo = n;
    }

    // Setter para id
    public void setId(int id) {
        this.id = id;
    }

    // Getter para id
    public int getId() {
        return id;
    }

    // Getter para rotulo
    public String getNome() {
        return rotulo;
    }

    // Setter para rotulo
    public void setNome(String rotulo) {
        this.rotulo = rotulo;
    }

    // Representação em string da Rotulo
    public String toString() {
        return "\nID..: " + this.id + "\nNome: " + this.rotulo;
    }

    // Converte o objeto para um array de bytes
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(this.id);
        dos.writeUTF(this.rotulo);

        return baos.toByteArray();
    }

    // Popula o objeto a partir de um array de bytes
    public void fromByteArray(byte[] b) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);

        this.id = dis.readInt();
        this.rotulo = dis.readUTF();
    }

    // Normaliza uma string removendo acentos e convertendo para minúsculas
    private static String strnormalize(String str) {
        String normalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalizedString).replaceAll("").toLowerCase();
    }
}

