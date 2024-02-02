/*
	Guia_0205.v
	806454 - Yago Almeida Melo
*/
module Guia_0205;
// define data
 reg [7:0] a = 8'b101010; // binary
 reg [7:0] a2 = 8'b11111; // binary
 reg [7:0] c;
// actions
initial
 begin : main
  $display ( "Guia_0205 - Operacoes com binarios fracionarios" );
  c = a + a2;
  $display ( "c = a+b = %b.%b", c[7:3],c[2:0] );
  c = a - a2;
  $display ( "c = a-b = %b.%b", c[7:3],c[2:0] );
  c = a * a2;
  $display ( "c = a*b = %b.%b", c[7:3],c[2:0] );
  c = a / a2;
  $display ( "c = a/b = %b.%b", c[7:3],c[2:0] );
  c = a % a2;
  $display ( "c = a%%b = %b.%b", c[7:3],c[2:0] );

 end // main
endmodule // Guia_0205   
