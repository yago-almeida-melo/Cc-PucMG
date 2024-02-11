/*
 *      Guia_02.java  /  2024-1
 *      806454 - Yago Almeida Melo
 */

public class Guia_02 {
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
     * Converter valor binario para decimal com parte fracionaria.
     * 
     * @return decimal equivalente
     * 
     * @param value - valor binario
     */

    public static double bin2double(String value) {
        int begin = value.indexOf(",");
        int dec = 0;
        if (value.charAt(0) != '0') {
            int power = 1;
            begin--;
            while (begin >= 0) {
                if (value.charAt(begin) == '1')
                    dec += power;
                power *= 2;
                begin--;
            }
        }
        begin = value.indexOf(",");
        double power2 = 1.0;
        double y = 0.0;
        for (int i = begin + 1; i < value.length(); i++) {
            power2 /= 2;
            if (value.charAt(i) == '1')
                y += power2;
        }
        y += dec;
        return y;
    }
    /*
     * Converter valor decimal para binario com parte fracionaria.
     * 
     * @return valor binario equivalente
     * 
     * @param value - decimal
     */

     public static String double2bin(double value) {
        // Check if the value is 0
        if (value == 0.0) {
            return "0";
        }
        int intValue = (int) value;
        StringBuilder binaryStringBuilder = new StringBuilder();
        if(intValue == 0) binaryStringBuilder.insert(0, 0);
        while (intValue > 0) {
            binaryStringBuilder.insert(0, intValue % 2);
            intValue /= 2;
        }
        binaryStringBuilder.append('.');
        double fractionalPart = value - (int) value;
        for (int i = 0; i < 5; i++) { 
            fractionalPart *= 2;
            int bit = (int) fractionalPart;
            binaryStringBuilder.append(bit);
            fractionalPart -= bit;
        }
        return binaryStringBuilder.toString();
    }
    

    /*
     * Converter valor binario com parte fracionaria para base indicada.
     * 
     * @return base para a conversao
     * 
     * @param value - valor binario
     */

     public static String dbin2base(String value, int base) {
        String[] parts = value.split("\\.");
        int intValue = Integer.parseInt(parts[0], 2);
        String integerPart = Integer.toString(intValue, base);
    
        if (parts.length > 1) {
            StringBuilder fractionalPart = new StringBuilder(".");
            String fractionalValue = "0." + parts[1];
    
            for (int i = 0; i < 16; i++) { // Limit the fractional part to 16 bits for simplicity
                // Multiply the fractional value by the base
                fractionalValue = doubleMultiply(fractionalValue, base);
    
                // Append the integer part of the result to the fractional part
                fractionalPart.append(Integer.toString((int) fractionalValue.charAt(0), base));
    
                // Update the fractional value to the decimal part
                fractionalValue = fractionalValue.substring(2);
    
                // Break if the fractional part becomes zero
                if (fractionalValue.equals("0")) {
                    break;
                }
            }
            System.out.println(integerPart+fractionalPart);
            return integerPart + fractionalPart.toString();
        }
    
        return integerPart;
    }
    
    // Helper method to multiply a binary fractional value by the base
    private static String doubleMultiply(String fractionalValue, int base) {
        StringBuilder result = new StringBuilder("0.");
        int carry = 0;
    
        for (int i = 2; i < fractionalValue.length(); i++) {
            int digit = Character.digit(fractionalValue.charAt(i), 2);
            int product = digit * base + carry;
            result.append(product / 2);
            carry = (product % 2) * 10;
        }
    
        return result.toString();
    }
    
    /*
     * Converter valor com parte fracionaria de uma base para outra base indicada.
     * 
     * @return valor equivalente na segunda base
     * 
     * @param value - valor na base1
     * 
     * @param base1 - primeira base
     * 
     * @param base2 - base para a conversao
     */

    public static String dbase2base(String value, int base1, int base2) {
        String[] parts = value.split("\\.");
        int intValue = Integer.parseInt(parts[0], base1);
        String integerPart = Integer.toString(intValue, base2);
        if (parts.length > 1) {
            double fractionalValue = 0.0;
            for (int i = 0; i < parts[1].length(); i++) {
                char digit = parts[1].charAt(i);
                int digitValue = Character.digit(digit, base1);
                fractionalValue += digitValue / Math.pow(base1, i + 1);
            }
            StringBuilder fractionalPart = new StringBuilder(".");
            for (int i = 0; i < 16; i++) { 
                fractionalValue *= base2;
                fractionalPart.append(Integer.toString((int) fractionalValue, base2));
                fractionalValue -= (int) fractionalValue;
            }
            return integerPart + fractionalPart.toString();
        }
        return integerPart;
    }
    /*
     * Operar valores em binartest_
     * 
     * @return valor resultante da operacao, se valida
     * 
     * @param value1 - primeiro valor binario
     * 
     * @param op - operacao
     * 
     * @param value2 - segundo valor binario
     */

     public static String dbinEval(String value1, String op, String value2) {
        double decimalValue1 = bin2double(value1);
        double decimalValue2 = bin2double(value2);
        double result;
        switch (op) {
            case "+":
                result = decimalValue1 + decimalValue2;
                break;
            case "-":
                result = decimalValue1 - decimalValue2;
                break;
            case "*":
                result = decimalValue1 * decimalValue2;
                break;
            case "/":
                if (decimalValue2 == 0.0) {
                    return "Error: Division by zero";
                }
                result = decimalValue1 / decimalValue2;
                break;
            case "%":
                if (decimalValue2 == 0.0) {
                    return "Error: Modulus by zero";
                }
                result = decimalValue1 % decimalValue2;
                break;
            default:
                return "Error: Invalid operator";
        }
        return double2bin(result);
    }
    

    /*
     * Acao principal.
     */

     public static void main(String[] args) {
        test_equals(bin2double("0,00101"), 0.15625);
        test_equals(bin2double("0,01011"), 0.34375);
        test_equals(bin2double("0,10001"), 0.53125);
        test_equals(bin2double("1,11011"), 1.84375);
        test_equals(bin2double("11,10011"), 3.59375);
        System.out.println("1. errorTotalReport = " + test_report());
    
        test_equals(double2bin(0.125000), "0.00100");
        test_equals(double2bin(2.625000), "10.10100");
        test_equals(double2bin(3.750000), "11.11000");
        test_equals(double2bin(6.031250), "110.00001");
        test_equals(double2bin(9.875000), "1001.11100");
        System.out.println("2. errorTotalReport = " + test_report());
    
        test_equals(dbin2base("0.011010", 4), "0.122");
        test_equals(dbin2base("0.101101", 8), "0.55");
        test_equals(dbin2base("0.100111", 16), "0.9c");
        test_equals(dbin2base("1.111001", 8), "1.74");
        test_equals(dbin2base("1101.1101", 16), "d.d");
        System.out.println("3. errorTotalReport = " + test_report());
    
        test_equals(dbase2base("0.2131", 4, 2), "0.001011");
        test_equals(dbase2base("0.4A2", 4, 16), "0.666");
        test_equals(dbase2base("0.747", 8, 2), "0.111100111");
        test_equals(dbase2base("7.654", 8, 4), "13.312");
        test_equals(dbase2base("F.A51", 16, 4), "33.22110001");
        System.out.println("4. errorTotalReport = " + test_report());
    
        test_equals(dbinEval("101.01", "+", "11.011"), "1000.001");
        test_equals(dbinEval("1001.101", "-", "10.11"), "0110.011");
        test_equals(dbinEval("100,101", "*", "11.101"), "11111.00001");
        test_equals(dbinEval("10111.01", "/", "10.011"), "1000.11");
        test_equals(dbinEval("1101011", "%", "1011"), "110");
        System.out.println("5. errorTotalReport = " + test_report());
    } 
} // end class
