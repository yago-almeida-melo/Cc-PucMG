/*
	Guia_0803.v
	806454 - Yago Almeida Melo
*/


module UnidadeLogica (
    input [4:0] a, // Entrada a de 5 bits
    input [4:0] b, // Entrada b de 5 bits
    output [4:0] s // Saída s de 5 bits
);

// Calcula o XNOR bit a bit entre a e b
wire [4:0] xnor_result;
assign xnor_result = a ~^ b; // ~^ é o operador XNOR 

// Calcula a função f para produzir a saída s
assign s[4] = xnor_result[4];
assign s[3] = xnor_result[3] & xnor_result[4];
assign s[2] = xnor_result[2] & xnor_result[3] & xnor_result[4];
assign s[1] = xnor_result[1] & xnor_result[2] & xnor_result[3] & xnor_result[4];
assign s[0] = xnor_result[0] & xnor_result[1] & xnor_result[2] & xnor_result[3] & xnor_result[4];

endmodule

