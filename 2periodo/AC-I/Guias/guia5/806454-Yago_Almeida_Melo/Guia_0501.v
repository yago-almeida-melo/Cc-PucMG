/*
	Guia_0501.v
	806454 - Yago Almeida Melo
*/

module Guia_0501 (
    input a,
    input b,
    output out
);

	wire not_a, not_b;

	// Portas NOR para calcular as negações de a e b
	nor (not_a, a, a);
	nor (not_b, b, b);

	// Porta NOR para calcular a expressão (~a & ~b)
	nor (out, not_a, not_b);

endmodule
