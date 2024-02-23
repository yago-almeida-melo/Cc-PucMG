/*
	Guia_0303.v
	806454 - Yago Almeida
*/
module Guia_0303;
 // define data
 reg signed [4:0] a = 8'b10111; // binary
 reg signed [5:0] b = 8'b110001 ; // binary
 reg signed [5:0] c = 8'b100101 ; // binary
 reg signed [6:0] d = 8'b1011101 ; // binary
 reg signed [6:0] e = 8'b1010011 ; // binary
 reg signed [4:0] x = 0; // binary
 reg signed [5:0] y = 0; // binary
 reg signed [6:0] z = 0; // binary
 // actions
 initial
  begin : main
   $display ( "Guia_0303 - Tests" );
   x = ~a+1;
   y = ~(a-1);
   $display ( "a = %5b -> C1(a) = %8b -> C2(a) = %5b = %d = +%d", a, ~a, x, x, y );
   x = ~b+1;
   y = ~(b-1);
   $display ( "b = %6b -> C1(b) = %6b -> C2(b) = %6b = %d = +%d", b, ~b, x, x, y );
   x = ~c+1;
   y = ~(c-1);
   $display ( "c = %6b -> C1(c) = %6b -> C2(c) = +%6b ", c, ~c, x );
   x = ~d+1;
   y = ~(d-1);
   $display ( "d = %7b -> C1(d) = %7b -> C2(d) = +%7b ", d, ~d, y );
   z = ~e+1;
   y = ~(e-1);
   $display ( "e = %7b -> C1(e) = %7b -> C2(e) = %7b = +%h", e, ~e, z, y );
  end // main end 
 endmodule // Guia_0303

