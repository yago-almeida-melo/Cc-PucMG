/*
	Guia_0704.v
	806454 - Yago Almeida Melo
*/

module Guia_0704(
    input wire a,
    input wire b,
    input [1:0] chave,

	  output wire result
);
	wire out_or, out_nor, out_xor,  out_xnor;
	assign out_or = a | b;
	assign out_nor = ~(a | b);
	assign out_xor = a ^ b;
	assign out_xnor = ~(a ^ b);

assign result = chave[1] ? (chave[0] ? out_xnor : out_xor) : (chave[0] ? out_nor : out_or);

/*
if(chave[0] == 1'b0){
	if(chave[1] == 1'b0){
		OR	
	} else{
		NOR
	}
} else if(chave[1] == 1"b0){
	XOR
} else{
	XNOR
}
*/
endmodule

