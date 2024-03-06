/*
	806454 - Yago Almeida Melo 
	Guia_0506.v
*/
// -------------------------
// -------------------------
// G0506_gate
// m a b s
// 0 0 0 1
// 1 0 1 1
// 2 1 0 0     (a^b)
// 3 1 1 1
//
// -------------------------
module Guia_0506 ( output s,
input a,
input b );

// descrever por portas
nand ( not_a, a, a );
nand ( not_b, b, b );
nand ( wire1, not_a, b );
nand ( wire2, not_b, a );
nand ( s, wire2, wire1 );
endmodule //endmodule Guia_0506



