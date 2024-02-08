/*
 *      Guia_0202.java  /  2024-1
 *      806454 - Yago Almeida Melo
 */

class Guia_0202 {
    public static void double2bin(String x){
        String decimal = "";
        int dec=0;
        String[] tok = x.split(",",3);
        if (x.charAt(0) != '0') {
            dec = Integer.parseInt(tok[0]);
            while(dec>=0){
                int rest = (dec %= 2);
                decimal += rest;
                dec /= 2;
            }
        }
        int parteDec = Integer.parseInt(decimal);
        int count = 7;
        double y = 0.0;
        String fracao = "";        
        int frac = Integer.parseInt(tok[1]);
        while(frac>0 && count >=0){
            if((frac*2) >= 1) fracao += "1";
            else fracao += "0";
            frac = (frac*2)-1;
        }
        frac = Integer.parseInt(fracao);
        frac += parteDec;
        System.out.println(x + "(10) = " + y + "(2)");
    }
    public static void main(String[] args){
        double2bin("0,125");
    }   
}
