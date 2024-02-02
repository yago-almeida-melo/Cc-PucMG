/*
	Guia_0502.v
	806454 - Yago Almeida MELO
*/

module Guia_0502 (
    input a,
    input b,
    output out
);

wire not_b;

// Porta NAND para calcular a negação de b
nand (not_b, b, b);

// Porta NAND para calcular a expressão (a | ~b)
nand (out, a, not_b);

endmodule

