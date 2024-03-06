/*
	806454 - Yago Almeida Melo 
	Guia_0504.v
*/
// -------------------------
// -------------------------
// G0504_gate
// m a b s
// 0 0 0 1
// 1 0 1 1
// 2 1 0 0     (a.b')'
// 3 1 1 1
//
// -------------------------
module Guia_0504 ( output s,
input a,
input b );

// descrever por portas
nand ( not_b, b, b );
nand ( s, not_b, a );
endmodule //endmodule Guia_0504



