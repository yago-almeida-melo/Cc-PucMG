import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;

public class Compilador{
    public static char X; // primeiro valor
    public static char Y; // segundo valor
    public static char W; // instrucao

// Funcao que separa o mnemonico entre 'W=' e ';'
public static String separaMnemonico(String data){
    String resp = "";
    int count = 2; // o mnemonico comeca apos o "W="
    char i = data.charAt(count); // inicia i com o primeiro char do mnemonico

    while( i != ';'){
        resp += i;
        i = data.charAt(++count);
    }

    return resp;
}// separaMnemonico()

// Traduz o mnemonico para o codigo correspondente
public static char traduzMnemonico(String mnemonico) throws Exception{
    return switch (mnemonico) {
        case "umL" -> '0';
        case "AonB" -> '1';
        case "copiaA" -> '2';
        case "nAxnB" -> '3';
        case "AeBn" -> '4';
        case "nA" -> '5';
        case "AenB" -> '6';
        case "nAonB" -> '7';
        case "AxB" -> '8';
        case "zeroL" -> '9';
        case "copiaB" -> 'A';
        case "AeB" -> 'B';
        case "nB" -> 'C';
        case "nAeBn" -> 'D';
        case "AoB" -> 'E';
        case "nAeB" -> 'F';
        default -> throw new Exception("Mnemonico desconhecido");
    };
}// traduzMnemonico()

// Traduz as instrucoes conforme aparecem no arquivo
public static String traduzInstrucao(String data) throws Exception {
    char letraInicio = data.charAt(0);
    String resp = null;

        // Atualiza X ou Y conforme sao lidos
        switch (letraInicio) {
            case 'X' -> X = data.charAt(data.length() - 2); // Valor de X
            case 'Y' -> Y = data.charAt(data.length() - 2); // Valor de Y
            case 'W' -> {
                // Quando encontra uma operacao, traduz o mnemonico e atualiza a resp
                String mnemonico = separaMnemonico(data);
                W = traduzMnemonico(mnemonico);
                
                // atualiza a resp com a instrucao
                resp = "" + X + Y + W;
            }
            default -> {
            }
        }
    return resp; // Nao retorna nada ate que uma operacao W seja encontrada
}// traduzInstrucao()

public static void main(String[] args){
    try {
        File input = new File("testeula.ula"); // abre o arquivo para leitura
        PrintWriter writer = new PrintWriter(new FileWriter("testeula.hex")); // abre o arquivo para escrita
        Scanner scanner = new Scanner(input); // inicializa o scanner

        String data = scanner.nextLine(); // pula o "inicio:"

        // executa ate encontrar "fim."
        while (!(data = scanner.nextLine().trim()).equals("fim.")) {
            String output = traduzInstrucao(data);
            if(output != null)
                writer.println(output); // escreve no arquivo
        }

        scanner.close();
        writer.close();

    } catch (Exception ex) {
        System.err.println("Erro" + ex);
    }
        
}// main()
}// class Compilador