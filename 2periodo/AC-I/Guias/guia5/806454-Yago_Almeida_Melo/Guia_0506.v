/*
	Guia_0506.v
	806454 - Yago Almeida melo
*/

module Guia_0506 (
    input a,
    input b,
    output out
);

wire nand1, nand2, nand3, nand4;

// Portas NAND para calcular (a AND b)
nand (nand1, a, b);

// Portas NAND para calcular (a NAND a) e (b NAND b)
nand (nand2, a, a);
nand (nand3, b, b);

// Porta NAND para calcular [(a AND b) NAND (a NAND a) NAND (b NAND b)]
nand (nand4, nand1, nand2, nand3);

// A saída é a negação da expressão anterior
nand (out, nand4, nand4);

endmodule

