/*
	Guia_0802.v
	806454 - Yago Almeida Melo
*/


module Subtractor (
    input [5:0] A, // Entrada do operando A (6 bits)
    input [5:0] B, // Entrada do operando B (6 bits)
    output [5:0] Difference // Saída da diferença (6 bits)
);

// Sinais intermediários
wire [5:0] Not_B;
wire [5:0] B_plus_1;

// Calcula o complemento de dois de B
assign Not_B = ~B;
assign B_plus_1 = Not_B + 1;

// Realiza a soma A + (-B)
assign Difference = A + B_plus_1;

endmodule
