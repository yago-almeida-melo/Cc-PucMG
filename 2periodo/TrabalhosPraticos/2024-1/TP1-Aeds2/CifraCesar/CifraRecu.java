/*
 *	806454 - Yago Almeida Melo
 *	TP1 - Cifra de Cesar Recursiva em java / 2024-1
 */

class CifraRecu{
    /*
     * function: isFim
     * @params: String
     * action: Recebe uma String e retorna verdadeiro se a String for igual a "FIM", senão retorna falso.
     */
	public static boolean isFim(String in){
		return in.length() == 3 && in.charAt(0) == 'F' && in.charAt(1)== 'I' && in.charAt(2)== 'M';
	}
    /*
     * function: cifra
     * @params: String
     * action: Recebe uma String e chama a função recursiva para ciframento.
     */
	public static String cifra(String input){
		return cifraRecu(input, 0);
	}
    /*
     * function: Ciframento
     * @params: String
     * action: Recebe uma String e retorna outra String contendo cada um dos caracteres com um deslocamento pre determinado,
     * sendo ela aqui o 3, recursivamente.
     */
	public static String cifraRecu(String x, int index){
		String resp = new String();
		int ascii=0;
		if(index < x.length()){
			ascii = x.charAt(index)+3;
			resp += (char)ascii;
			MyIO.print(resp);
			resp = cifraRecu(x, index+1);
		}
		return resp;
	}
	//MAIN
	public static void main(String[] args){
		String in = "";
		in = MyIO.readLine();
		while(!isFim(in)){
			cifra(in);
			in = MyIO.readLine();
		}
	}
}
