/*
	Guia_0703.v
	806454 - Yago Almeida Melo
*/

module Guia_0703(
    input wire a,
    input wire b,
    input wire chave1,
    input wire chave2,
	output wire result
);
	wire out_or, out_nor, out_and,  out_nand;
	assign out_and = a & b;
	assign out_nand = ~(a & b);
	assign out_or = a | b;
	assign out_nor = ~(a | b);

assign result = chave1 ? (chave2 ? out_nor : out_nand) : (chave2 ? out_or : out_and);

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

