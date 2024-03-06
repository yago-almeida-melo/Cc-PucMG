/*
	806454 - Yago Almeida Melo 
	Guia_0503.v
*/
// -------------------------
// -------------------------
// G0503_gate
// m a b s
// 0 0 0 1
// 1 0 1 1
// 2 1 0 0     a'+b
// 3 1 1 1
//
// -------------------------
module Guia_0503 ( output s,
input a,
input b );

// descrever por portas
nor ( not_a, a, a );
nor ( nor_ab, not_a, b );
nor ( s, nor_ab, nor_ab );
endmodule //endmodule Guia_0503



