/*
	Guia_0305.v
	806454 - Yago Almeida Melo
*/
module Guia_0305;
 // define data
 reg [7:0] s1 = 0;
 reg [7:0] s2 = 0;
 reg [7:0] frac = 0;
 reg [7:0] result = 0;
 // actions
 initial
  begin : main
   $display ( "Guia_0305 - Subtracao" );
   s1 = 6'b101011;
   s2 = 6'b001110;
   result = s1 - s2;
   $display("a) %b - %b = %b", s1 , s2, result);
   s1 = 7'b101_1011;
   s2 = 6'o2_4;
   result = s1 - s2;
   $display("b) %b - %o = %b,%b", s1 , s2, result[7:4], result[3:0]);
   s1 = 8'o312;
   s2 = 8'hd;
   result = s1 - s2;
   $display("c) %b - %h = %b", s1 , s2, result);
   s1 = 8'hc3;
   s2 = 8'b1011001;
   result = s1 - s2;
   $display("d) %h - %b = %b", s1 , s2, result);
   s1 = 51;
   s2 = 16'h2d;
   result = s1 - s2;
   $display("e) %d - %h = %b", s1 , s2, result);
  end // main
 endmodule // Guia_0305
