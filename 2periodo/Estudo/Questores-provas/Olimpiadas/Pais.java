class Pais {
    public String nome;
    public int ouro;
    public int prata;
    public int bronze;

    Pais() {
        this(null, 0, 0, 0);
    }

    Pais(String nome) {
        this(nome, 0, 0, 0);
    }

    Pais(String nome, int ouro, int prata, int bronze) {
        this.nome = nome;
        this.ouro = ouro;
        this.prata = prata;
        this.bronze = bronze;
    }

    public static boolean procurar(String nome, Pais lista[]) {
        boolean resp = false;
        for (int i = 0; i < lista.length && lista[i] != null; i++) {
            if (lista[i].nome.equals(nome)) {
                resp = true;
                i = lista.length;
            }
        }
        return resp;
    }

    public static int pos(String name, Pais lista[]) {
        int i = 0;
        while (lista[i] != null && !lista[i].nome.equals(name)) {
            i++;
        }
        return i;
    }

    public static void ordenar(Pais lista[]) {
        for (int i = 1; i < lista.length && lista[i] != null; i++) {
            Pais tmp = lista[i];
            int j = i - 1;
            while ((j >= 0) && (lista[j].ouro)<(tmp.ouro)) {
                lista[j + 1] = lista[j];
                j--;
            }
            while ((j >= 0) && (lista[j].ouro)==(tmp.ouro) && (lista[j].prata)<(tmp.prata)) {
                lista[j + 1] = lista[j];
                j--;
            }
            while ((j >= 0) && (lista[j].ouro)==(tmp.ouro) && (lista[j].prata)==(tmp.prata) && (lista[j].bronze)<(tmp.bronze)) {
                lista[j + 1] = lista[j];
                j--;
            }
            lista[j + 1] = tmp;
        }
    }

    public static void main(String[] args) {
        Pais lista[] = new Pais[300];
        int n = 0;
        String modalidade;
        while ((modalidade = MyIO.readLine()) != null) {
            for (int i = 0; i < 3; i++) {
                String nome = MyIO.readLine();
                if (!procurar(nome, lista)) {
                    lista[n] = new Pais(modalidade);
                    n++;
                }
                int posicao = pos(nome, lista);
                if (i == 0) {
                    lista[posicao].ouro++;
                } else if (i == 1) {
                    lista[posicao].prata++;
                } else {
                    lista[posicao].bronze++;
                }
            }
        }
        ordenar(lista);
        for (int i = 0; i < n; i++) {
            MyIO.println(lista[i].nome + " " + lista[i].ouro + " " + lista[i].prata + " " + lista[i].bronze);
        }
    }
}