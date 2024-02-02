/*
	Guia_0703.v
	806454 - Yago Almeida Melo
*/

module Guia_0703(
    input a,
    input b,
    input chave1,
    input chave2,
    output y
);

assign y = (chave1 == 1'b0) ? ((chave2 == 1'b0) ? (a & b) : ~(a & b)) : ((chave2 == 1'b0) ? (a | b) : ~(a | b));

/*if(chave1 == 1'b0){
	if(chave2==1'b0){
		AND	
	} else{
		NAND
	}
} else if(chave2 == 1"b0){
	OR
} else{
	NOR
}
*/
endmodule

