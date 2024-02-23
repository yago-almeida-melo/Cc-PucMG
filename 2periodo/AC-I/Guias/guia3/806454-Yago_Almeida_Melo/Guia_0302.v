/*
	Guia_0302.v
	806454 - Yago Almeida Melo
*/

module Guia_0302;
// define data
 reg [7:0] a = 8'o132 ; // quaternary
 reg [7:0] b = 8'h4b ; // hexa
 reg [7:0] c = 8'o213; // quaternary 011011
 reg [8:0] d = 8'o154; // octal
 reg [7:0] e = 8'hb8; // hexa
 reg [7:0] x = 0 ; // binary
 reg [5:0] y = 0 ; // binary
 reg [9:0] z = 0 ; // binary
// actions
 initial
  begin : main
   $display ( "Guia_0302 - Complemento de" );
   y = ~a;
   $display ( "a = %o (4) = %b%b%b C1,6(a) = %b ", a, a[7:6],a[4:3],a[1:0], y );
   x = ~b;
   $display ( "b = %h (16) = %b%b C1,8(b) = %b ", b, b[7:4],b[3:0], x);
   y = ~c+1;
   $display ( "c = %o (4) = %b%b%b C2,6(c) = %b ", c , c[7:6],c[4:3],c[1:0], y);
   z = ~d+1;
   $display ( "d = %o (8) = %b%b%b C2,10(c) = %b ", d , d[8:6],d[5:3],d[2:0], z);
   x = ~e+1;
   $display ( "e = %h (16) = %b%b C2,8(c) = %b ", e , e[7:4],e[3:0], x);
   
  end // main
 endmodule // Guia_0302
