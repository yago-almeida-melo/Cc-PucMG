/*
 *      Guia_0201.java   /   2024-1
 *      806454 - Yago Almeida Melo
 */
class Guia_0201 {
    public static void bin2double(String x) {
        int begin = x.indexOf(",");
        int dec = 0;
        if (x.charAt(0) != '0') {
            int power = 1;
            begin--;
            while(begin >= 0){
                if(x.charAt(begin) == '1') dec += power;
                power*=2;
                begin--;
            }
        }
        begin = x.indexOf(",");
        double power2 = 1.0;
        double y = 0.0;
        for (int i = begin + 1; i < x.length(); i++) {
            power2 /= 2;
            if (x.charAt(i) == '1')
                y += power2;
        }
        y += dec;
        System.out.println(x + "(2) = " + y + "(10)");
    }

    public static void main(String[] args) {
        bin2double("11,1101");
    }
}
