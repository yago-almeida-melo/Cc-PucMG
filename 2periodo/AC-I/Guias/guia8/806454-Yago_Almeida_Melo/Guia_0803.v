/*
	Guia_0803.v
	806454 - Yago Almeida Melo
*/


module Guia_0803(
    input [5:0] entradaA,
    input [5:0] entradaB,
    output igualdade
);

assign igualdade = (entradaA == entradaB) ? 1 : 0;

endmodule

module test_Guia_0803;

reg [5:0] entradaA;
reg [5:0] entradaB;
wire igualdade;

Guia_0803 UUT(
    .entradaA(entradaA),
    .entradaB(entradaB),
    .igualdade(igualdade)
);

initial begin
    $monitor("Entrada A: %b, Entrada B: %b, Igualdade: %b", entradaA, entradaB, igualdade);
    //test1
    entradaA = 6'b000000;
    entradaB = 6'b000000;
    #10;
    //test2
    entradaA = 6'b101010;
    entradaB = 6'b101010;
    #10;    
    //test3
    entradaA = 6'b111111;
    entradaB = 6'b000000;
    #10;
    
    $finish;
end

endmodule

