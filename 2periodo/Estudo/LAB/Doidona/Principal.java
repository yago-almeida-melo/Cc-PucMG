class Hash{
    Hash outro;
    public int tabela[];
    public int m;
    Hash(){
        this(13);
    }
    Hash(int m){
        this.outro = null;
        this.m = m;
        this.tabela = new int[this.m];
        for (int i = 0; i < m; i++) {
            tabela[i] = -1;
        }
    }
}

public class Principal {
    public static void main(String[] args) {
        Hash x = new Hash();
        Hash y = new Hash();
        for(int i = 1;i<x.m;i++){
            x.tabela[i-1] = i;
        }
        y.tabela[0] = 1000;
        x.outro = y;
        System.out.print(x.outro.tabela[0]);
    }
}
