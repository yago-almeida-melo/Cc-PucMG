import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.time.LocalDate;

public class Tarefa implements Registro {
    private int id;
    public String nome;
    public LocalDate dataCriacao, dataConclusao;
    public String status;
    public byte prioridade;

    public Tarefa() {
        this("",null,null,"",0);
    }

    public Tarefa(String nome, LocalDate dataCriacao, LocalDate dataConclusao, String status, byte prioridade) {
        this.nome = nome;
        this.dataCriacao = dataCriacao;
        this.dataConclusao = dataConclusao;
        this.status = status;
        this.prioridade = prioridade;
    }

    public Tarefa(int id, String nome, LocalDate dataCriacao, LocalDate dataConclusao, String status, byte prioridade) {
        this.id = id;
        this.nome = nome;
        this.dataCriacao = dataCriacao;
        this.dataConclusao = dataConclusao;
        this.status = status;
        this.prioridade = prioridade;
    }

    @Override
    public int getId() {
        return id;
    }
    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(id);
        dos.writeUTF(nome);
        dos.writeInt((int) dataCriacao.toEpochDay());
        dos.writeInt((int) dataConclusao.toEpochDay());
        dos.writeUTF(status);
        dos.write(prioridade);
        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        this.id = dis.readInt();
        this.nome = dis.readUTF();
        this.dataCriacao = LocalDate.ofEpochDay(dis.readInt());
        this.dataConclusao = LocalDate.ofEpochDay(dis.readInt());
        this.status = dis.readUTF();
        this.prioridade = dis.readByte();
    }

    @Override
    public String toString() {
        return "[ID=" + id + ", Nome=" + nome + ", Data de Criacao= " + "]";
    }
}
