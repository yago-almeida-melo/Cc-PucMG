/*
	Guia_0504.v
	806454 - Yago Almeida Melo
*/

module Guia_0504 (
    input a,
    input b,
    output out
);

wire and_result, not_and_result;

// Porta NAND para calcular a operação (~a & b)
nand (and_result, ~a, b);

// Porta NAND para calcular a negação de (~a & b)
nand (not_and_result, and_result, and_result);

// Porta NAND para calcular a expressão ~(~a & b)
nand (out, not_and_result, not_and_result);

endmodule

