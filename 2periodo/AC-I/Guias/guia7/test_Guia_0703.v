/*
	test_Guia_0703.v
	806454 - Yago Almeida Melo	
*/

`include "Guia_0703.v"
   
module test_Guia_0703();
    reg a;
    reg b;
    reg chave1;
    reg chave2;
    wire y;
    parameter period = 50;

    Guia_0703 U1 (.a(a), .b(b), .chave1(chave1), .chave2(chave2), .y(y));

    initial begin
        a = 0;
        b = 0;
        chave1 = 0;
        chave2 = 0;

        #10 a = 1; b = 0; chave1 = 0; chave2 = 0; // AND
        #10 a = 1; b = 0; chave1 = 0; chave2 = 1; // NAND
        #10 a = 1; b = 0; chave1 = 1; chave2 = 0; // OR
        #10 a = 1; b = 0; chave1 = 1; chave2 = 1; // NOR
    end
endmodule

