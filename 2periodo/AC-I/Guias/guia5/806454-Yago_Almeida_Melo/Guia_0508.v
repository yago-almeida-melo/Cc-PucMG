/*
	Guia_0508.v
	806454 - Yago Almeida Melo
*/

module Guia_0508 (
    input a,
    input b,
    output out
);

wire not_b, and_result, nand_result;

// Porta NAND para calcular a negação de ~b
nand (not_b, b, b);

// Porta NAND para calcular a operação (a & ~b)
nand (and_result, a, not_b);

// Porta NAND para calcular a negação da operação (a & ~b)
nand (nand_result, and_result, and_result);

// A saída é a negação da expressão (a & ~b)
nand (out, nand_result, nand_result);

endmodule

