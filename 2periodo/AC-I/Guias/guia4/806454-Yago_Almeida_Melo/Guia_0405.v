/*
	Guia_0405.v
	806454 - Yago Almeida Melo
*/

module PoS_A (output S, input X, Y);
    assign S = (X | Y) & (~X | Y);
endmodule

module SoP_A (output S, input x, y);
    assign S = (~x & y) | (x & y);
endmodule

module PoS_B (output S, input X, Y);
    assign S = (~X | Y);
endmodule

module SoP_B (output S, input x, y);
    assign S = (~x & ~y) | (~x & y) | (x & y);
endmodule

module PoS_C (output S, input X, Y, Z);
    assign S = (X| ~Y| Z) & (~X|Y|~Z);
endmodule

module SoP_C (output S, input x, y, z);
    assign S = (~x&~y&~z) | (~x&~y&z) | (~x&y&z) | (x&~y&~z) | (x&y&~z) | (x&y&z);
endmodule

module PoS_D (output S, input X, Y, Z);
    assign S = (X| Y| ~Z) & (X|~Y|~Z) & (~X|~Y|~Z);
endmodule

module SoP_D (output S, input x, y, z);
    assign S = (~x&~y&~z) | (~x&y&~z) | (x&~y&~z) | (x&~y&z) | (x&y&~z);
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

    $display("Guia_0403 - Yago Almeida Melo\n");
    $display("\nA) f (x,y,z)= ∑ m ( 2, 5, 6, 7 )");
    $display("\nB) f (x,y,z)= ∑ m ( 1, 3, 6, 7 )");
    $display("\nC) f (x,y,w,z) = ∑ m ( 1, 2, 4, 6, 7, 12, 15 )");
    $display("\nD) f (x,y,w,z) = ∑ m ( 1, 2, 5, 8, 10, 12, 13 )");
    $display("\nE) f (x,y,w,z) = ∑ m ( 0, 2, 5, 7, 9, 13 )\n");
    // monitoramento
    $display(" x  y  z  w = sA sB sC sD sE");
    $monitor("%2b %2b %2b %2b = %2b %2b %2b %2b %2b", x, y, w, z, pA, pB, pC, pD, pE);
    // sinalizacao
    #1 x=0; y=0; w=0; z=0;
    #1 x=0; y=0; w=0; z=1;
    #1 x=0; y=0; w=1; z=0;
    #1 x=0; y=0; w=1; z=1;
    #1 x=0; y=1; w=0; z=0;
    #1 x=0; y=1; w=0; z=1;
    #1 x=0; y=1; w=1; z=0;
    #1 x=0; y=1; w=1; z=1;
    #1 x=1; y=0; w=0; z=0;
    #1 x=1; y=0; w=0; z=1;
    #1 x=1; y=0; w=1; z=0;
    #1 x=1; y=0; w=1; z=1;
    #1 x=1; y=1; w=0; z=0;
    #1 x=1; y=1; w=0; z=1;
    #1 x=1; y=1; w=1; z=0;
    #1 x=1; y=1; w=1; z=1;
endmodule 

