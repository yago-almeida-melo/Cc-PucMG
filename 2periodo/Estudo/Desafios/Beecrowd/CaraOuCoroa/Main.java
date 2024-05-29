
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        String in = "";
        int casos = 0;
        casos = sc.nextInt();
        while (casos > 0) {
            sc.nextLine();
            int mary = 0, john = 0;
            in = sc.nextLine();
            String[] parts = in.split(" ");
            for (int i = 0; i < parts.length; i++) {
                if (parts[i].equals("0")) {
                    mary++;
                } else {
                    john++;
                }
            }
            System.out.println("Mary won " + mary + " times and John won " + john + " times");
            casos = sc.nextInt();
        }

    }
}
 

