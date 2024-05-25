
import java.util.Scanner;

class Main{
    public static Scanner sc;

    public static boolean isLetra(char x){
        return (x >= 'a' && x <= 'z') || (x >= 'A' && x <= 'Z');
    }

    public static void cript3(String x){
        String resp = "";
        for(int i=0;i<(x.length()/2);i++){
            resp += (char)(x.charAt(i));
        }
        for(int i=(x.length()/2);i<x.length();i++){
            resp += (char)(x.charAt(i) - 1);
        }
        System.out.println(resp);
    }

    public static void cript2(String x){
        String resp = "";
        for(int i = x.length()-1;i>=0;i--){
            resp += x.charAt(i);
        }
        cript3(resp);
    }

    public static void cript(String x){
        String resp = "";
        for(int i=0;i<x.length();i++){
            if(isLetra(x.charAt(i))){
                resp += (char) (x.charAt(i)+3);
            } else{
                resp += x.charAt(i);
            }
        }
        cript2(resp);
    }

    public static void main(String[] args){ 
        sc = new Scanner(System.in);
        int casos = sc.nextInt();
        String x="";
        int i=0;
        sc.nextLine();
        while(i < casos){
            x = sc.nextLine();
            cript(x);
            i++;
        }
    }
}