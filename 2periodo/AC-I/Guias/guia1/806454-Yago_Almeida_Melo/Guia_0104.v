/*
	Guia_0104.v
	806454 - Yago Almeida Melo
*/

module Guia_0104; 
	reg [7:0] x = 8'b10111; 
initial
	begin : main
		$display ( "a = %8b (2) = %o%o%o (4)", x,x[5:4], x[3:2], x[1:0]);
		x = 8'b11110;
		$display ( "b = %8b (2) = %o%o (8)", x, x[5:3], x[2:0] );
		x = 8'b100101;
		$display ( "c = %8b (2) = %x%x (16)", x, x[7:4], x[3:0] );
		x = 8'b101011;
		$display ( "d = %8b (2) = %o%o (8)", x, x[5:3], x[2:0] );
		x = 8'b101100;
		$display ( "e = %8b (2) = %o%o%o (4)", x, x[5:4], x[3:2], x[1:0] );
	end 
endmodule 
