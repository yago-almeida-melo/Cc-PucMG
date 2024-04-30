package Bibliotecas.Java.Matriz;
import java.util.Scanner;

class Principal {
   static Scanner sc = new Scanner(System.in);
   public static void main(String[] args) {
      Matriz m1;

      m1 = new Matriz(3,3);

      m1.ler();
      m1.mostrar();
   }
}
