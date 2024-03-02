// ---------------------
// TRUTH TABLE
// Nome: Yago Almeida Melo
// Matricula: 806454
// ---------------------
// ---------------------
// -- expressions
// ---------------------
module Guia_0401A (output s,
input x, y, z);
assign s = x & ~( y | ~z );
endmodule // fxy

module Guia_0401B (output s,
input x, y, z);
assign s = ~(x | ~y) & z;
endmodule // fxy

module Guia_0401C (output s,
input x, y, z);
assign s = ~(~x & y) & ~z;
endmodule // fxy

module Guia_0401D (output s,
input x, y, z);
assign s = ~(~x & ~y) & z;
endmodule // fxy

module Guia_0401E (output s,
input x, y, z);
assign s = (x | y) & ~(~y | z);
endmodule // fxy

// ---------------------
// -- test_module
// ---------------------
module Guia_0401;
reg x, y, z;
wire sA, sB, sC, sD, sE;
// instancias
Guia_0401A G0401A (sA, x, y, z);
Guia_0401B G0401B (sB, x, y, z);
Guia_0401C G0401C (sC, x, y, z);
Guia_0401D G0401D (sD, x, y, z);
Guia_0401E G0401E (sE, x, y, z);
// valores iniciais
initial begin: start
x=1'bx; y=1'bx; // indefinidos
end
// parte principal
initial begin: main
// identificacao
$display("Guia_0401 - Yago Almeida Melo\n");
$display("\nA) x . ( y+z' )'");
$display("\nB) ( x +y' )' . z");
$display("\nC) ( x' . y )' . z'");
$display("\nD) ( x' . y' )' . z");
$display("\nE) ( x + y ) . ( y' + z)'\n");
// monitoramento
$display(" x  y  z = sA sB sC sD sE");
$monitor("%2b %2b %2b = %2b %2b %2b %2b %2b", x, y, z, sA, sB, sC, sD, sE);
// sinalizacao
#1 x=0; y=0; z=0;
#1 x=0; y=0; z=1;
#1 x=0; y=1; z=0;
#1 x=0; y=1; z=1;
#1 x=1; y=0; z=0;
#1 x=1; y=0; z=1;
#1 x=1; y=1; z=0;
#1 x=1; y=1; z=1;
end
endmodule 