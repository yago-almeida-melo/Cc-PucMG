/*
	Guia_0205.v
	806454 - Yago Almeida Melo
*/
module Guia_0205;
// define data
 reg [7:0] a; // binary
 reg [7:0] b;// binary
 reg [10:0] c;
// actions
initial
 begin : main
  $display ( "Guia_0205 - Operacoes com binarios fracionarios" );
  a = 8'b101_010;
  b = 8'b011_011;
  c = a + b;
  $display ( "a) 101.010 + 11.011 = %b.%b", c[7:3], c[2:0] );
  a = 8'b1001_101;
  b = 8'b0010_110;
  c = a - b;
  $display ( "b) 10001.101 - 10.110 = %b.%b", c[5:3],c[2:0] );
  a = 8'b100_101;
  b = 8'b011_101;
  c = a * b;
  $display ( "c) 100.101 * 11.101 = %b.%b", c[10:6],c[5:0] );
  a = 8'b10111_010;
  b = 8'b00010_011;
  c = a / b;
  $display ( "d) 10111.010 / 10.011 = %b", c);
  a = 8'b1101011;
  b = 8'b1011;
  c = a % b;
  $display ( "e) 1101011 %% 1011 = %b", c[3:0]);
 
 end // main
endmodule // Guia_0205  
