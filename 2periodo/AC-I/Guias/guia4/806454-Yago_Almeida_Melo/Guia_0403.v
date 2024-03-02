/*
	Guia_0403.v
	806454 - Yago Almeida Melo
*/
module SoP_A (output s, input x, y, z);
    assign s = (~x&y&~z) | (~x&y&~z) | (x&y&~z) | (x&y&z);
endmodule // SoP

module SoP_B (output s, input x, y, z);
    assign s = (~x&~y&z) | (~x&y&z) | (x&y&~z) | (x&y&z);
endmodule // SoP

module SoP_C (output s, input x, y, w, z);
    assign s = (~x&~y&~z&w) | (~x&~y&z&~w) | (~x&y&~w&~z) | (~x&y&w&~z) | (~x&y&w&z) | (x&y&~w&~z) | (x&y&w&z);
endmodule // SoP

module SoP_D (output s, input x, y, w, z);
    assign s = (~x&~y&~w&z) | (~x&~y&w&~z) | (~x&y&~w&z) | (x&~y&~w&~z) | (x&~y&w&~z) | (x&y&~w&~z) | (x&y&~w&z);
endmodule // SoP

module SoP_E (output s, input x, y, w, z);
    assign s = (~x&~y&~z&~w) | (~x&~y&z&~w) | (~x&y&~w&z) | (~x&y&w&z) | (x&~y&~w&z) | (x&y&~w&z);
endmodule // SoP

module Guia_0403;
    reg x, y, w, z;
    wire sA, sB, sC, sD, sE;
    // instancias
    SoP_A SOP_A (sA, x, y, z);
    SoP_B SOP_B (sB, x, y, z);
    SoP_C SOP_C (sC, x, y, w, z);
    SoP_D SOP_D (sD, x, y, w, z);
    SoP_E SOP_E (sE, x, y, w, z);
// valores iniciais
initial begin: start
    x=1'bx; y=1'bx; w=1'bx; z=1'bx;// indefinidos
end
// parte principal
initial begin: main
// identificacao
$display("Guia_0403 - Yago Almeida Melo\n");
$display("\nA) f (x,y,z)= ∑ m ( 2, 5, 6, 7 )");
$display("\nB) f (x,y,z)= ∑ m ( 1, 3, 6, 7 )");
$display("\nC) f (x,y,w,z) = ∑ m ( 1, 2, 4, 6, 7, 12, 15 )");
$display("\nD) f (x,y,w,z) = ∑ m ( 1, 2, 5, 8, 10, 12, 13 )");
$display("\nE) f (x,y,w,z) = ∑ m ( 0, 2, 5, 7, 9, 13 )\n");
// monitoramento
$display(" x  y  z  w = sA sB sC sD sE");
$monitor("%2b %2b %2b %2b = %2b %2b %2b %2b %2b", x, y, w, z, sA, sB, sC, sD, sE);
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
end
endmodule 
