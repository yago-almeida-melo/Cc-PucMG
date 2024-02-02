/*
	Guia_0505.v
	806454 - Yago Almeida Melo
*/

module Guia_0505 (
    input a,
    input b,
    output out
);

wire not_a, not_b, and1, and2, or1;

// Portas NOR para calcular as negações de a e b
nor (not_a, a, a);
nor (not_b, b, b);

// Portas NOR para calcular as operações (a & b) e (~a & ~b)
nor (and1, a, b);
nor (and2, not_a, not_b);

// Porta NOR para calcular a disjunção exclusiva (XNOR)
nor (or1, and1, and2);

// Inverta o resultado para obter o XNOR
nor (out, or1, or1);

endmodule

