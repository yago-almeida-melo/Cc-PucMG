/*
	Guia_0404.v
	806454 - Yago Almeida Melo
*/

module PoS_A (output S, input X, Y, Z);
    assign S = (X| Y|~Z) & (~X|Y| ~Z) & (~X|~Y|Z) & (~X|~Y|~Z);
endmodule

module PoS_B (output S, input X, Y, Z);
    assign S = (X| Y| Z) & (~X|Y| Z) & (~X|Y|~Z) & (~X|~Y|~Z);
endmodule

module PoS_C (output S, input X, Y, W, Z);
    assign S = (X|Y|W|Z) & (X|Y|W|~Z) & (X|~Y|W|~Z) & (X|~Y|~W|Z) & (~X|Y|W|Z) & (~X|Y|~W|~Z) & (~X|~Y|W|~Z);
endmodule

module PoS_D (output S, input X, Y, W. Z);
    assign S = (X|Y|W|~Z) & (X|Y|~W|~Z) & (X|~Y|W|Z) & (X|~Y|~W|Z) & (~X|Y|W|Z) & (~X|Y|W|~Z) & (~X|~Y|~W|Z);
endmodule

module PoS_E (output S, input X, Y, W, Z);
    assign S = (X|Y|W|Z) & (X|Y|W|~Z) & (X|Y|~W|Z) &(X|Y|~W|~Z) & (X|~Y|W|~Z) & (~X|Y|~W|Z) & (~X|Y|~W|~Z);
endmodule


module Guia_0404;
    reg X, Y, W, Z;
    wire pA, pB, pC, pD, pE;
    PoS_A POS_A (pA, Y, W, Z);
    PoS_B POS_B (pB, Y, W, Z);
    PoS_C POS_C (pC, X, Y, W, Z);
    PoS_D POS_D (pD, X, Y, W, Z);
    PoS_E POS_E (pE, X, Y, W, Z);
    
    initial begin
    $display("Exemplo - PoS");
    $display("-----------------------------");

    $display("Guia_0404 - Yago Almeida Melo\n");
    $display("\nA) f (x,y,z)= ∑ m ( 2, 5, 6, 7 )");
    $display("\nB) f (x,y,z)= ∑ m ( 1, 3, 6, 7 )");
    $display("\nC) f (x,y,w,z) = ∑ m ( 1, 2, 4, 6, 7, 12, 15 )");
    $display("\nD) f (x,y,w,z) = ∑ m ( 1, 2, 5, 8, 10, 12, 13 )");
    $display("\nE) f (x,y,w,z) = ∑ m ( 0, 2, 5, 7, 9, 13 )\n");
    // monitoramento
    $display(" x  y  z  w = sA sB sC sD sE");
    $monitor("%2b %2b %2b %2b = %2b %2b %2b %2b %2b", x, y, w, z, pA, pB, pC, pD, pE);
    // sinalizacao
    #1 X=0; Y=0; W=0; Z=0;
    #1 X=0; Y=0; W=0; Z=1;
    #1 X=0; Y=0; W=1; Z=0;
    #1 X=0; Y=0; W=1; Z=1;
    #1 X=0; Y=1; W=0; Z=0;
    #1 X=0; Y=1; W=0; Z=1;
    #1 X=0; Y=1; W=1; Z=0;
    #1 X=0; Y=1; W=1; Z=1;
    #1 X=1; Y=0; W=0; Z=0;
    #1 X=1; Y=0; W=0; Z=1;
    #1 X=1; Y=0; W=1; Z=0;
    #1 X=1; Y=0; W=1; Z=1;
    #1 X=1; Y=1; W=0; Z=0;
    #1 X=1; Y=1; W=0; Z=1;
    #1 X=1; Y=1; W=1; Z=0;
    #1 X=1; Y=1; W=1; Z=1;
    end
endmodule 

