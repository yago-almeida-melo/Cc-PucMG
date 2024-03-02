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
    assign S = (X|Y|~W|Z) & (X|~Y|~W|Z) & (X|~Y|~W|~Z) &(~X|Y|~W|~Z) & (~X|~Y|W|Z) & (~X|~Y|W|~Z);
endmodule

module SoP_E (output S, input x, y, w, z);
    assign S = (~x&~y&~w&~z) | (~x&~y&~w&z) | (~x&~y&w&z) | (~x&y&~w&~z) | (~x&y&~w&z) | (x&~y&~w&~z) | (x&~y&~w&z) | (x&~y&w&~z) | (x&y&w&~z) | (x&y&w&z);
endmodule

module Guia_0404;
    reg x, y, w, z, X, Y, W, Z;
    wire pA, pB, pC, pD, pE, sA, sB, sC, sD, sE;
    PoS_A POS_A (pA, w, z);
    SoP_A SOP_A (sA, W, Z);
    PoS_B POS_B (pB, w, z);
    SoP_B SOP_B (sB, W, Z);
    PoS_C POS_C (pC, Y ,W, Z);
    SoP_C SOP_C (sC, Y, w, z);
    PoS_D POS_D (pD, Y, W, Z);
    SoP_D SOP_D (sD, Y, W, Z);
    PoS_E POS_E (pE, X, Y, W, Z);
    SoP_E SOP_E (sE, X, Y, W, Z);
    
    initial begin
    $display("Exemplo - PoS & SoP");
    $display("-----------------------------");

    $display("Guia_0405 - Yago Almeida Melo\n");
    $display("\nA) f (x,y,z)= ∑ m ( 2, 5, 6, 7 )");
    $display("\nB) f (x,y,z)= ∑ m ( 1, 3, 6, 7 )");
    $display("\nC) f (x,y,w,z) = ∑ m ( 1, 2, 4, 6, 7, 12, 15 )");
    $display("\nD) f (x,y,w,z) = ∑ m ( 1, 2, 5, 8, 10, 12, 13 )");
    $display("\nE) f (x,y,w,z) = ∑ m ( 0, 2, 5, 7, 9, 13 )\n");
    // monitoramento
    $display(" x  y  z  w  X  Y  W  Z = pA sA pB sB pC sC pD sD pE sE");
    $monitor("%2b %2b %2b %2b %2b %2b %2b %2b = %2b %2b %2b %2b %2b %2b %2b %2b %2b %2b", x,y,w,z,X,Y,W,Z, pA, sA, pB, sB, pC, sC, pD, sD, pE, sE);
    // sinalizacao
    #1 x=0; y=0; w=0; z=0; X=0; Y=0; W=0; Z=0;
    #1 x=0; y=0; w=0; z=1; X=0; Y=0; W=0; Z=1;
    #1 x=0; y=0; w=1; z=0; X=0; Y=0; W=1; Z=0;
    #1 x=0; y=0; w=1; z=1; X=0; Y=0; W=1; Z=1;
    #1 x=0; y=1; w=0; z=0; X=0; Y=1; W=0; Z=0;
    #1 x=0; y=1; w=0; z=1; X=0; Y=1; W=0; Z=1;
    #1 x=0; y=1; w=1; z=0; X=0; Y=1; W=1; Z=0;
    #1 x=0; y=1; w=1; z=1; X=0; Y=1; W=1; Z=1;
    #1 x=1; y=0; w=0; z=0; X=1; Y=0; W=0; Z=0;
    #1 x=1; y=0; w=0; z=1; X=1; Y=0; W=0; Z=1;
    #1 x=1; y=0; w=1; z=0; X=1; Y=0; W=1; Z=0;
    #1 x=1; y=0; w=1; z=1; X=1; Y=0; W=1; Z=1;
    #1 x=1; y=1; w=0; z=0; X=1; Y=1; W=0; Z=0;
    #1 x=1; y=1; w=0; z=1; X=1; Y=1; W=0; Z=1;
    #1 x=1; y=1; w=1; z=0; X=1; Y=1; W=1; Z=0;
    #1 x=1; y=1; w=1; z=1; X=1; Y=1; W=1; Z=1;
    end
endmodule 

