/*
	Guia_0301.v
	806454 - Yago Almeida Melo
*/
module Guia_0301;
// define data
 reg [5:0] a = 8'b1100 ; // binary
 reg [7:0] b = 8'b1110 ; // binary
 reg [5:0] c = 8'b100101 ; // binary
 reg [6:0] d = 8'b100101 ; // binary
 reg [7:0] e = 8'b110101 ; // binary
 reg [7:0] x = 0 ; // binary
 reg [6:0] y = 0 ; // binary 
 reg [5:0] z = 0 ; // binary 
// actions
 initial
  begin : main
   $display ( "Guia_0301 - Tests" );
   z = ~a+1;
   $display ( "a = %8b -> C1(a) = %8b -> C2(a) = %6b", a, ~a, z );
   x = ~b+1;
   $display ( "b = %8b -> C1(b) = %8b -> C2(b) = %8b", b, ~b, x );
   z = ~c+1;
   $display ( "c = %8b -> C1(c) = %8b -> C2(c) = %6b", c, ~c, z );
   y = ~d+1;
   $display ( "d = %8b -> C1(c) = %8b -> C2(c) = %7b", d, ~d, y );
   x = ~e+1;
   $display ( "e = %8b -> C1(c) = %8b -> C2(c) = %8b", e, ~e, x );
  end // main
 endmodule // Guia_0301
