/*
	806454 - Yago Almeida Melo 
	Guia_0501.v
*/
// -------------------------
// -------------------------
// G0501_gate
// m a b s
// 0 0 0 0
// 1 0 1 0
// 2 1 0 0 <- a.b'
// 3 1 1 1
//
// -------------------------
module Guia_0501 ( output s,
input a,
input b );
// definir dado local
wire not_b;
// descrever por portas
nor ( not_b, b, b );
nor ( s, not_b, a );
endmodule //endmodule Guia_0501 



