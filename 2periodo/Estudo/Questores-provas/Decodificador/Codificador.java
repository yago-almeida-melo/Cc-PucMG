class Codificador {
    public static String codificar(String s){
        String numString = "";
        String repetida = "";
        String resp = "";
        int  i=0;
        numString += s.charAt(i);
        int num = Integer.parseInt(numString);
        i+=2;
        while(s.charAt(i)!=']'){
            repetida += s.charAt(i);
            i++;
        }
        for(int j = 0; j < num; j++){
            resp += repetida;
        }
        return resp;
    }

    public static void main(String[] args){
        String s = MyIO.readLine();
        String resp = "";
        int i = 0;
        while(i < s.length()){
            if(s.charAt(i) >= '0' && s.charAt(i) <= '9'){
                int b = i;
                while(s.charAt(b) != ']'){
                    b++;
                }
                String sub = s.substring(i, b+1);
                resp += codificar(sub);
                i = b+1;
            } else{
                resp += s.charAt(i);
                i++;
            }
        }
        MyIO.println(resp);
    }
}
