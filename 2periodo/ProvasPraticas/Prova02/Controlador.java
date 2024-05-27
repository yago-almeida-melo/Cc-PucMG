import java.util.Scanner;

class Fila {
    public Aviao avioes[];
    public int tam;

    Fila() {
        this.avioes = new Aviao[100]; // Initialize with a large enough size
        this.tam = 0;
    }

    void inserir(Aviao x) {
        if (tam < avioes.length) {
            avioes[tam] = x;
            tam++;
        }
    }
}

class Aviao {
    public String id;

    public Aviao() {
        this.id = "";
    }

    public Aviao(String id) {
        this.id = id;
    }
}

public class Controlador {
    /*
     * Verifica se o input é um ponto cardeal (-1.-2.-3.-4)
     */
    public static boolean verify(String x) {
        return x.equals("-1") || x.equals("-2") || x.equals("-3") || x.equals("-4");
    }

    /*
     * Função para mostrar intercaladameente os avioes
     */
    public static void mostrar(Fila a, Fila b, Fila c, Fila d) {
        int maxTam = Math.max(Math.max(a.tam, b.tam), Math.max(c.tam, d.tam));
        for (int i = 0; i < maxTam; i++) {
            if (a.avioes[i] != null) {
                System.out.print(a.avioes[i].id + " ");
            }
            if (b.avioes[i] != null) {
                System.out.print(b.avioes[i].id + " ");
            }
            if (c.avioes[i] != null) {
                System.out.print(c.avioes[i].id + " ");
            }
            if (d.avioes[i] != null) {
                System.out.print(d.avioes[i].id + " ");
            }
        }
    }

    public static void main(String[] args) {
        Fila avioesLeste = new Fila();
        Fila avioesNorte = new Fila();
        Fila avioesSul = new Fila();
        Fila avioesOeste = new Fila();
        Scanner sc = new Scanner(System.in);
        String ponto ="";
        String copiaPonto="";
        while (sc.hasNext() && !ponto.equals("0")) {
            ponto = sc.next();
            if(verify(ponto)) {
                copiaPonto = ponto;
                continue;
            } else{
                Aviao novo = new Aviao(ponto);
                switch(copiaPonto) {
                    case "-1":
                        avioesOeste.inserir(novo);
                        break;
                    case "-3":
                        avioesNorte.inserir(novo);
                        break;
                    case "-2":
                        avioesSul.inserir(novo);
                        break;
                    case "-4":
                        avioesLeste.inserir(novo);
                        break;
                    default:
                        break;
                }
            }
        }
        mostrar(avioesOeste, avioesNorte, avioesSul, avioesLeste);
    }
}
