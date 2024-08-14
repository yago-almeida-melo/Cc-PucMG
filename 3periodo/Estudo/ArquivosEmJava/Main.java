//import java.io.DataInputStream;
//import java.io.DataOutputStream;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
import java.io.RandomAccessFile;

public class Main {
    public static void main(String[] args) {
        Carros c1 = new Carros(1,"Supra", 1990, 1500000);
        Carros c2 = new Carros(2,"Civic", 2024, 500000);
        
        //System.out.println(c1);
        //System.out.println(c2);
        
        RandomAccessFile arq;
        //FileOutputStream arq;
        //DataOutputStream dos;
        //FileInputStream arq2;
        //DataInputStream dis;

        byte[] ba;


        try {
            ///////// ESCRITA //////////
            arq = new RandomAccessFile("dados/carros.db", "rw");

            //arq = new FileOutputStream("dados/carros.db");
            //dos = new DataOutputStream(arq);

            long p1 = arq.getFilePointer();
            ba = c1.toByteArray();
            arq.writeInt(ba.length);
            arq.write(ba);

            long p2 = arq.getFilePointer();
            ba = c2.toByteArray();
            arq.writeInt(ba.length);
            arq.write(ba);
            

            /*dos = new DataOutputStream(arq);

            dos.writeInt(c1.id);
            dos.writeUTF(c1.modelo);
            dos.writeInt(c1.ano);
            dos.writeFloat(c1.preco);

            dos.writeInt(c2.id);
            dos.writeUTF(c2.modelo);
            dos.writeInt(c2.ano);
            dos.writeFloat(c2.preco);

            dos.close();*/
            //arq.flush();
            

            ///////////// LEITURA ////////////

            Carros c3 = new Carros();
            Carros c4 = new Carros();

            int tam;

            

            //arq2 = new FileInputStream("dados/carros.db");
            //dis = new DataInputStream(arq2);

            arq.seek(p1);
            tam = arq.readInt();
            ba = new byte[tam];
            arq.read(ba);
            c3.fromByteArray(ba);

            arq.seek(p2);
            tam = arq.readInt();
            ba = new byte[tam];
            arq.read(ba);
            c4.fromByteArray(ba);

            System.out.println(c3);
            System.out.println(c4);
            arq.close();
            /*dis = new DataInputStream(arq2);
            

            c3.id = dis.readInt();
            c3.modelo = dis.readUTF();
            c3.ano = dis.readInt();
            c3.preco = dis.readFloat();

            c4.id = dis.readInt();
            c4.modelo = dis.readUTF();
            c4.ano = dis.readInt();
            c4.preco = dis.readFloat();*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
