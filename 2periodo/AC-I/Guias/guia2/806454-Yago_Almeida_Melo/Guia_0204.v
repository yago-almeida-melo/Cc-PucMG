/*
	Guia_0204.v
	806454 - Yago Almeida Melo
*/

module Guia_0204;
// data
reg [15:0] a = 8'o312; 
reg [15:0] b = 16'h3e7;
reg [15:0] c = 16'o751;
reg [15:0] d = 16'o231;
reg [15:0] e = 16'hb52;
// actions
initial
begin : main
$display ( "Guia_0204 - Bases 4,8,16 fracionarias paea bases 2 e 4" );
$display ( "a = 0.%o (4) = 0.%b%b%b (2)", a[8:0], a[8:6],a[5:3],a[2:0] );
$display ( "b = 0.%h (16) = 0.%o%o%o (4)", b[11:0], b[11:8],b[7:6],b[1:0] );
$display ( "c = 0.%o (8) = 0.%b%b%b (2)", c[8:0], c[12:7],c[7:4],c[3:0] );
$display ( "d = 7.%o (8) = 13.%o%o%o (4)", d[8:0], d[11:8],d[7:4],d[3:0] );
$display ( "e = A.%h (16) = 22.%o%o%o (4)", e[11:0], e[12:10],e[8:5],e[4:3] );
end // main
endmodule // Guia_0204
