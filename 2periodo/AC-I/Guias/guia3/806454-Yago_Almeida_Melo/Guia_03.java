/**
 * Arquitetura de Computadores I - Guia_03.java
 * 806454 - Yago Almeida Melo
 */
class Guia_03 {
    /*
     * Contador de erros.
     */
    private static int errors = 0;

    /*
     * Testar se dois valores sao iguais.
     * 
     * @param x - primeiro valor
     * 
     * @param y - segundo valor
     */
    public static void test_equals(Object x, Object y) {
        if (("" + x).compareTo("" + y) != 0)
            errors = errors + 1;
    } // end test_equals ( )
    /*
     * Exibir o total de erros.
     * 
     * @return mensagem com o total de erros
     */

    public static String test_report() {
        return ("" + errors);
    } // end test_report ( )
    /*
     * Converter valor binario para o complemento de 1.
     * 
     * @return complemento de 1 equivalente
     * 
     * @param length - tamanho
     * 
     * @param value - valor binario
     */

    public static String C1(int length, String value) {
        StringBuilder x = new StringBuilder();
        int dif = length - value.length();
        for(int i=0;i<dif;i++){
            x.append('0'); 
        }
        for(int i=0;i<value.length();i++){
            if(value.charAt(i)=='1') x.append('0');
            else x.append('1');
        }
        return x.toString();
    } // end C1 ( )
    /*
     * Converter valor binario para o complemento de 2.
     * 
     * @return complemento de 2 equivalente
     * 
     * @param length - tamanho
     * 
     * @param value - valor binario
     */

    public static String C2(int length, String value) {
        String x = C1(length, value);
        StringBuilder str = new StringBuilder();
        int carry = 1;
        for (int i = x.length() - 1; i >= 0; i--) {
            char bit = x.charAt(i);
            int bitInt = Character.getNumericValue(bit);
            int resultado = bitInt + carry;
            carry = resultado / 2;
            str.insert(0, resultado % 2);
        }
        if (carry > 0) {
            str.insert(0, carry);
        }
        return str.toString();
    } 
    /*
     * Converter valor em certa base para binario em complemento de 1.
     * 
     * @return complemento de 1 equivalente
     * 
     * @param length - tamanho
     * 
     * @param value - valor em outra base
     * 
     * @param base - base desse valor
     */

    public static String C1(int length, String value, int base) {
        return ("0");
    } // end C1 ( )
    /*
     * Converter valor em certa base para binario em complemento de 2.
     * 
     * @return complemento de 2 equivalente
     * 
     * @param length - tamanho
     * 
     * @param value - valor em outra base
     * 
     * @param base - base desse valor
     */

    public static String C2(int length, String value, int base) {
        return ("0");
    } // end C2 ( )
    /*
     * Converter valor binario com sinal para decimal.
     * 
     * @return decimal equivalente
     * 
     * @param value - valor binario
     */

    public static String sbin2dec(String value) {
        String x = "";
        int dec=0, len = value.length();
        for(int i = len-1, j = 1;i>=0; i++ , j = j*2){
            if(value.charAt(i)=='1') dec += j;
        }
        x = String.valueOf(dec);
        return x;
    } // end sbin2dec ( )
    /*
     * Operar (subtrair) valores em certa base.
     * 
     * @return valor resultante da operacao
     * 
     * @param value1 - primeiro valor na base dada
     * 
     * @param op
     * - operacao ("-")
     * 
     * @param value2 - segundo valor na base dada
     * 
     * @param base - base para a conversao
     */

    public static String eval(String value1, String op, String value2, int base) {
        return ("0");
    } // end eval ( )
    /*
     * Operar valores em certas bases.
     * 
     * @return valor resultante da operacao, se valida
     * 
     * @param value1 - primeiro valor
     * 
     * @param base1 - primeira base
     * 
     * @param op
     * - operacao
     * 
     * @param value2 - segundo valor
     * 
     * @param base2 - segunda base
     */

    public static String evalB1B2(String value1, int base1, String op, String value2, int base2) {
        return ("0");
    } // end dbinEval ( )

    public static void main(String[] args){
        System.out.println ( "Guia_03 - Java Tests " );
        System.out.println ( " 806454 - Yago Almeida Melo" );
        System.out.println ( );
        test_equals ( C1( 6,"1100" ), "0" );
        test_equals ( C1( 8,"1110" ), "0" );
        test_equals ( C2( 6, "100101" ), "0" );
        test_equals ( C2( 7, "100101" ), "0" );
        test_equals ( C2( 8, "110101" ), "0" );
        System.out.println( "1. errorTotalReportMsg = "+test_report ( ) );
        test_equals ( C1( 6, "132", 4 ), "0" );
        test_equals ( C1( 8, "4B", 16 ), "0" );
        test_equals ( C2( 6, "213", 4 ), "0" );
        test_equals ( C2( 7, "154", 8 ), "0" );
        test_equals ( C2( 8, "B8", 16 ), "0" );
        System.out.println( "2. errorTotalReportMsg = "+test_report ( ) );
        test_equals ( sbin2dec ("10111" ), 0 );
        test_equals ( sbin2dec ("110001" ), 0 );
        test_equals ( sbin2dec ("100101" ), 0 );
        test_equals ( sbin2dec ("1011101" ), 0 );
        test_equals ( sbin2dec ("1010011" ), 0 );
        System.out.println( "3. errorTotalReportMsg = "+test_report ( ) );
        test_equals ( eval("11101", "-", "1011", 2 ), "0" );
        test_equals ( eval( "101.0101", "-", "10.11", 2 ), "0" );
        test_equals ( eval("321", "-", "213", 4 ), "0" );
        test_equals ( eval("461", "-", "247", 8 ), "0" );
        test_equals ( eval("7C4", "-", "B1D", 16 ), "0" );
        System.out.println( "4. errorTotalReportMsg = "+test_report ( ) );
        test_equals ( evalB1B2 ( "110011", 2, "-","1101", 2 ), "0" );
        test_equals ( evalB1B2 ( "101,1101", 2, "-","3,3", 8 ), "0" );
        test_equals ( evalB1B2 ("231" , 4, "-","E", 16 ), "0" );        
        test_equals ( evalB1B2 ("D4" , 16, "-", "1011101", 2 ), "0" );
        test_equals ( evalB1B2 ("67" , 16, "-","3B", 16 ), "0" );
        System.out.println( "5. errorTotalReportMsg = "+test_report ( ) );
        System.out.print ( "\n\nApertar ENTER para terminar." );
        System.console ( ).readLine ( );
    }
}
