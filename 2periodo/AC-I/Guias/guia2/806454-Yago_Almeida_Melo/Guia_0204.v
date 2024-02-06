/*
	Guia_0204.v
	806454 - Yago Almeida Melo
*/

module Guia_0204;
// data
reg [15:0] a = 8'o213; 
reg [15:0] b = 16'h4a2;
reg [15:0] c = 16'o747;
reg [15:0] d = 16'o654;
reg [15:0] e = 16'ha51;
reg [15:0] bin;
// actions
initial
begin : main
$display ( "Guia_0204 - Bases 4,8,16 fracionarias paea bases 2 e 4" );
$display ( "a = 0.%o (4) = 0.%b%b%b (2)", a[7:0],a[7:6],a[4:3],a[1:0] );
$display ( "b = 0.%h (16) = 0.%o%o%o%o%o%o (4)", b[11:0], b[11:10],b[9:8],b[7:6],b[5:4],b[3:2],b[1:0] );
$display ( "c = 0.%o (8) = 0.%b%b%b (2)", c[8:0], c[8:5],c[4:2],c[1:0] );
$display ( "d = 7.%o (8) = 13.%o%o%o%o (4)", d[8:0], d[8:7],d[5],d[3],d[2:1] );
$display ( "e = f.%h (16) = 33.%o%o%o%o%o%o (4)", e[11:0], e[11:10],e[9:8],e[7:6],e[5:4],e[3:2],e[1:0] );
end // main
endmodule // Guia_0204