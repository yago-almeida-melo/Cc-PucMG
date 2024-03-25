/*
 *  806454 - Yago Almeida Melo
 *  TP1 - Álgebra Booleana Recursiva em Java
 */

 class AlgebraRecu {

    /*
     * MÉTODOS DE STRING CRIADOS POR YAGO
     *
     *
     * function: replaceYago
     * @params: String & char & char
     * action: Troca todos os caracteres de uma String por outro caracter escolhido;
     */
    public static String replaceYago(String x,char target, char replace){
        String resp = "";
        for(int i=0;i<x.length();i++){
            if(x.charAt(i)==target){ resp += replace; } else { resp +=x.charAt(i); }
        }
        return resp;
    }

    /* 
     * function: replaceYago
     * @params: String & String & String
     * action: Troca todos as cadeias de caracter de uma String por outra cadeia de caracter escolhido;
     */
    public static String replaceYago(String x, String target, String replace){
        String resp = "";
        for(int i=0;i<x.length();i++){
            if(x.charAt(i) != target.charAt(0)){  
                resp += x.charAt(i);              
            } else{
                for(int j=i+1, k=1;k<target.length();k++,j++){
                    if(x.charAt(j) != target.charAt(k)){
                        k = target.length();
                        resp += x.charAt(i);
                    } else {
                        resp += replace;
                        k = target.length();
                        i += target.length()-1;
                    }
                }
            }
        }
        return resp;
    }

    /* 
     * function: substringYago
     * @params: String & int & int
     * action: retorna uma parte da String, sendo que os int são a posição inicial e final da String que será partida e, será a partição retornada;
     */
    public static String substringYago(String x, int start, int end){
        if(start < 0 || end >= x.length()+1){ throw new StringIndexOutOfBoundsException(); }
        String resp = "";
        for(int i = start;i<end;i++){
            resp += x.charAt(i);
        }
        return resp;
    }

    /* 
     * function: substringYago
     * @params: String & int
     * action: retorna uma parte da String, sendo que o int é a posição inicial da String que será partida;
     */
    public static String substringYago(String x, int start){
        if(start < 0 || start > x.length()-1){ throw new StringIndexOutOfBoundsException(); }
        String resp = "";
        for(int i = start;i<x.length();i++){
            resp += x.charAt(i);
        }
        return resp;
    }

    /* 
     * function: algebra
     * @params: String 
     * action: Altera os valores da expressão por 1 ou 0 e retorna o resultado, sendo ele 1 ou 0;
     */
    public static String algebra(String x){
        String resp="";
        if(x.charAt(0)=='2'){
            if(x.charAt(2) == '1') { x = x.replace('A', '1'); } else{ x = x.replace('A', '0'); }
            if(x.charAt(4) == '1') { x = x.replace('B', '1'); } else{ x = x.replace('B', '0'); }
            resp = alteracao(x.substring(6), x.length()-7);
        } else{
            if(x.charAt(2) == '1') { x = x.replace('A', '1'); } else{ x = x.replace('A', '0'); }
            if(x.charAt(4) == '1') { x = x.replace('B', '1'); } else{ x = x.replace('B', '0'); }
            if(x.charAt(6) == '1') { x = x.replace('C', '1'); } else{ x = x.replace('C', '0'); }
            resp = alteracao(x.substring(8), x.length()-9); 
        }
        return resp;  
    }

    /* 
     * function: alteracao
     * @params: String
     * action: Retorna a String com as operações feitas;
     */
    public static String alteracao(String in, int i){
        if(i>=0){
            if(in.charAt(i)=='('){
                int end = i;
                while(!(in.charAt(end)==')')){
                    end++;
                }
                switch (in.charAt(i-1)) {    //switch-case identifica qual é a operação antes do '('
                    case 't':
                        String sub = in.substring(i-3, end+1); 
                        String resp = NOT(sub); 
                        in = in.replace(sub, resp);
                        
                        i = in.length()-1;
                        break;
                    case 'd':
                        sub = in.substring(i-3, end+1); 
                        resp = AND(sub);
                        in = in.replace(sub, resp);
                        
                        i = in.length()-1;
                        break;
                    case 'r':
                        sub = in.substring(i-2, end+1); 
                        resp = OR(sub);
                        in = in.replace(sub, resp);
                        
                        i = in.length()-1;
                        break;
                    default:
                        MyIO.println("ERRO no switch!");
                        break;
                }
            
            }
            in = alteracao(in,i-1);
        }
            
        return in;
    }

    /* 
     * function: NOT
     * @params: String 
     * action: retorna o resultado da expressão booleana NOT, sendo ela "1" ou "0";
     */
    public static String NOT(String x){
        return x.charAt(4) == '1' ? "0" : "1";
    }

    /* 
     * function: AND
     * @params: String 
     * action: retorna o resultado da expressão booleana AND, sendo ela "1" ou "0";
     */
    public static String AND(String x){
        String resp="1";
        for(int i=0;i<x.length();i++){
            if(x.charAt(i)=='0'){
                resp = "0";
                i = x.length();
            }
        }
        return resp;
    }

    /*
     * function: OR
     * @params: String 
     * action: retorna o resultado da expressão booleana OR, sendo ela "1" ou "0";
     */
    public static String OR(String x){
        String resp="0";
        for(int i=0;i<x.length();i++){
            if(x.charAt(i)=='1'){
                resp = "1";
                i = x.length();
            }
        }
        return resp;
    }

    /*
     * function: isFim
     * @params: String 
     * action: Recebe uma String e retorna verdadeiro se a String for igual a "0", senão retorna falso.
     */
    public static boolean isFim(String x){
        return x.length()==1 && x.charAt(0)=='0';
    }

    public static void main(String[] args){
        String input = MyIO.readLine();
        while(!isFim(input)){
            String resp = algebra(input);
            MyIO.println(resp);
            input = MyIO.readLine();
        }
    }
}
