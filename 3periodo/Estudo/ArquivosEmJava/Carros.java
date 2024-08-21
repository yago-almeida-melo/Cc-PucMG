import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.text.DecimalFormat;


public class Carros{
    protected int id;
    protected String modelo;
    protected int ano;
    protected float preco;

    DecimalFormat df = new DecimalFormat("#,##0.00");

    public Carros(){
        id = -1;
        modelo = "";
        ano = -1;
        preco = 0F;
    }

    public Carros(String m, int a, float p){
        modelo = m;
        ano = a;
        preco = p;
    }

    public void setId(int id){
        this.id = id;
    }

    public String toString(){
        return "\nID: "+id+"\nModelo: "+modelo+"\nAno: "+ano+"\nPreco: R$ "+df.format(preco);
    }

    public byte[] toByteArray() throws Exception{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(id);
        dos.writeUTF(modelo);
        dos.writeInt(ano);
        dos.writeFloat(preco);
        return baos.toByteArray();
    }

    public void fromByteArray(byte[] ba) throws Exception{
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        id = dis.readInt();
        modelo = dis.readUTF();
        ano = dis.readInt();
        preco = dis.readFloat();
    }
}