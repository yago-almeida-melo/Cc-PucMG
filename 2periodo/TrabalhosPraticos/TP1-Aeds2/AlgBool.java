class AlgBool{
	
	public static String YagoSubString(String expressao, int inicio){
		String nova = new String();
		for(int i=inicio;i<expressao.length();i++){
			nova += expressao.charAt(i);
		}
		return nova;
	}

	public static void main(String[] args){
		String expressao = new String();
		String expressaoBool = new String();
		expressao  = MyIO.readLine();
		if(expressao.charAt(0) == '2'){
			expressaoBool = YagoSubString(expressao,3);
		} else {
		 	expressaoBool = YagoSubString(expressao,4);
		}
		MyIO.print(expressaoBool);
	}
}
