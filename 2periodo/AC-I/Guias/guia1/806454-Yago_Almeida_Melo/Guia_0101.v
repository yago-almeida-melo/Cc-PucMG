/*
	Guia_0101.v
	806454 - Yago Almeida Melo
*/

module Guia_0101;
	integer a = 23; 
	reg [9:0] x = 0; 
	integer b = 57;  
	integer c = 732; 
	integer d = 321; 
	integer e = 364;  
initial
	begin : main
		$display ( "a = %2d (10)" , a );
		x = a;
		$display ( "a = %8b (2)", x );
		$display ( "b = %2d (10)" , b );
		x = b;
		$display ( "b = %8b (2)", x );
		$display ( "c = %2d (10)" , c );
		x = c;
		$display ( "c = %8b (2)", x );
		$display ( "d = %2d (10)" , d );
		x = d;
		$display ( "d = %8b (2)", x );
		$display ( "e = %2d (10)" , e );
		x = e;
		$display ( "e = %8b (2)", x );
	end 
endmodule 
