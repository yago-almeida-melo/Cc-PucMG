/*
	806454 - Yago Almeida Melo 
	Guia_0508.v
*/
// -------------------------
// -------------------------
// G0508_gate
// m a b s
// 0 0 0 1
// 1 0 1 0
// 2 1 0 0     (a'&b')'
// 3 1 1 1
//
// -------------------------
module Guia_0508 ( output s,
input a,
input b );

// descrever por portas
assign s = ~(~a&~b);
endmodule //endmodule Guia_0508



