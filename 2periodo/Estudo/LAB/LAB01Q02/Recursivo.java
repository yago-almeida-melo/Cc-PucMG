class Recursivo {
    public static void main(String []args){
        String in = new String();
        in = MyIO.readLine();
        while(!(in.equals("FIM"))){
            int qtd=0;
            qtd = recursao(in, 0, 0);
            MyIO.println(qtd);
            in = MyIO.readLine();
        }
    }
    public static int recursao(String n, int len, int qtd){
        if(len<n.length()){
            if(n.charAt(len) >='A' && n.charAt(len) <= 'Z'){
                qtd = 1 + recursao(n, len+1, qtd);
            } else{
                qtd = recursao(n, len+1, qtd);
            }
        }
        return qtd;
    }
}

