/*
	Guia_0103.v
	806454 - Yago Almeida Melo
*/

module Guia_0103;
	integer x = 67; 
	reg [9:0] y = 0; 
initial
	begin : main
		y = x;
		$display ( "a = %2d (10) = %o%o%o%o (4)", x, y[7:6],y[5:4],y[3:2],y[1:0]);
		x = 58;
		y = x;
		$display ( "b = %2d (10) = %o (8)", x, y);
		x = 76;
		y = x;
		$display ( "c = %2d (10) = %x (16)", x, y);
		x = 157;
		y = x;
		$display ( "d = %3d (10) = %x (16)", x, y);
		x = 735;
		y = x;
		$display ( "e = %3d (10) = %x (16)", x, y);
	end 
endmodule 
