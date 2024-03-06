/*
	806454 - Yago Almeida Melo 
	Guia_0507.v
*/
// -------------------------
// -------------------------
// G0507_gate
// m a b s
// 0 0 0 1
// 1 0 1 0
// 2 1 0 0     (a'^b')'
// 3 1 1 1
//
// -------------------------
module Guia_0507 ( output s,
input a,
input b );

// descrever por portas
nor (not_a, a, a);
nor (not_b, b, b);
nor (wire1, not_a, b);
nor (wire2, not_b, a);
nor (s, wire1, wire2);
endmodule //endmodule Guia_0507



