/*
	Guia_0102.v
	806454 - Yago Almeida Melo
*/

module Guia_0102;
	integer x = 0;
	reg [7:0] a = 8'b10011;
	reg [7:0] b = 8'b11101;
	reg [7:0] c = 8'b10110;
	reg [7:0] d = 8'b101101;
	reg [7:0] e = 8'b110011;
initial
	begin : main
		$display ( "Guia_0102 - Tests" );
		x = a;
		$display ( "a = %8b (2) = %2d (10)", a , x);
		x = b;
		$display ( "b = %8b (2) = %2d (10)", b , x);
		x = c;
		$display ( "c = %8b (2) = %2d (10)", c , x);
		x = d;
		$display ( "d = %8b (2) = %2d (10)", d , x);
		x = e;
		$display ( "e = %8b (2) = %2d (10)", e , x);
	end // main
endmodule 
