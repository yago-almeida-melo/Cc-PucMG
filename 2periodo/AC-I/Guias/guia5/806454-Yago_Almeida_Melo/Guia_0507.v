/*
	Guia_0507.v
	806454 - Yago Almeida Melo
*/

module Guia_0507 (
    input a,
    input b,
    output out
);

wire not_b, xor_result, nand_result;

// Porta NOR para calcular a negação de b
nor (not_b, b, b);

// Porta NOR para calcular a operação (a ^ ~b)
nor (xor_result, a, not_b);

// Porta NOR para calcular a negação da operação (a ^ ~b)
nor (nand_result, xor_result, xor_result);

// A saída é a negação da expressão (a ^ ~b)
nor (out, nand_result, nand_result);

endmodule

