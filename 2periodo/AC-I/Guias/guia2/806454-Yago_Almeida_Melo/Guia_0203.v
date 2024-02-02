/*
	Guia_0203.v
	806454 - Yago Almeida Melo
*/
module Guia_0203;
// define data
 reg [7:0] intD = 8'b00000001;
 reg [7:0] intE = 8'b00001110;
// decimal
 reg [7:0] a = 8'b01001000 ; // binary
 reg [7:0] b = 8'b10010100 ; // binary
 reg [7:0] c = 8'b10101100 ; // binary
 reg [7:0] d = 8'b11000100 ; // binary
 reg [7:0] e = 8'b10010000 ; // binary
// actions
 initial
  begin : main
  $display ( "Guia_0203 - BInario com parte fracionaria para bases 4, 8 e 16 " );
  $display ( "a = 0.%8b (2) = 0.%o%o%o%o (4)" , a, a[7:6], a[5:4], a[3:2], a[1:0] );
  $display ( " " );
  $display ( "b = 0.%8b (2) = 0.%o%o%o (8)", b, b[7:5], b[4:2], b[1:0] );
  $display ( " " );
  $display ( "c = 0.%8b (2) = 0.%x%x(16)", c, c[7:4], c[3:0] );
  $display ( " " );
  $display ( "d = 1,110001 (2) = %o.%o%o%o (8) ", intD, d[7:5], d[4:2], d[1:0] );
  $display ( " " );
  $display ( "e = 1110,1001 (2) = %x.%x%x (16)", intE, e[7:4], e[3:0] );
 end // main
endmodule // Guia_0203
