/*
 *  806454 - Yago Almeida Melo
 *  Warm-up 01 - Coleção de Pokemon
 */

public class Pokemon {
    public static String pokemons[] = new String[151];
    static int N=0;

    public static void inserir(String pokemon){
        if(N<151 && verifica(pokemon)){
            pokemons[N] = pokemon;
            N++;
        }
    }

    public static boolean verifica(String pokemon){
        boolean resp = true;
        for(int i=0;i<pokemon.length() && resp;i++){
            if(pokemon.equals(pokemons[i])){ 
                resp = false;
            }
        }
        return resp;
    }
    public static void main(String[] args){
        int qtd=0;
        String nome="";
        qtd = MyIO.readInt();
        for(int i=0;i<qtd;i++){
            nome = MyIO.readLine();
            inserir(nome);
        }
        MyIO.println("Falta(m) "+(151-N)+" pomekon(s).");
    }
}
