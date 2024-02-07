/*
	Guia_0201.v
	806454 - Yago Almeida Melo
*/
module Guia_0201;
	reg [7:0] a = 8'b01101000; 
	reg [7:0] b = 8'b10011000; 
	reg [7:0] c = 8'b10101000; 
	reg [7:0] d = 8'b11001000; 
	reg [7:0] e = 8'b11011000; 
	real x = 0 ; 
	real power2 = 1.0; 
	integer y = 7 ; 
initial
begin : main
	$display ( "Guia_0201 - Binario com parte fracionaria para decimal\n" );
	
	//1b.)a)
	while ( y >= 0 ) begin
		power2 = power2 / 2.0;
		if ( a[y] == 1 ) begin
			x = x + power2;
		end
		y=y-1;
	end // end while
	$display ("a) 0.%b(2) = %f(10)", a, x );

	// Reset
	x = 0;
	power2 = 1.0;
	y = 7;

	//1b.)b)
	while ( y >= 0 ) begin
		power2 = power2 / 2.0;
		if ( b[y] == 1 ) begin 
			x = x + power2;
		end
		y=y-1;
	end // end while
	$display ("b) 0.%b(2) = %f(10)", b, x );

	// Reset
	x = 0;
	power2 = 1.0;
	y = 7;

	//1b.)c)
	while ( y >= 0 ) begin
		power2 = power2 / 2.0;
		if ( c[y] == 1 ) begin
			x = x + power2;
		end
		y=y-1;
	end // end while
	$display ("c) 0.%b(2) = %f(10)", c, x );

	// Reset
	x = 0;
	power2 = 1.0;
	y = 7;

	//1b.)d)
	while ( y >= 0 ) begin
		power2 = power2 / 2.0;
		if ( d[y] == 1 ) begin
			x = x + power2;
		end
		y=y-1;
	end // end while
	x=x+1;// parte inteira
	$display ("d) 1.%b(2) = %f(10)", d, x );

	// Reset
	x = 0;
	power2 = 1.0;
	y = 7;

	//1b.)e)
	while ( y >= 0 ) begin
		power2 = power2 / 2.0;
		if ( e[y] == 1 ) begin
			x = x + power2;
		end
		y=y-1;
	end // end while
	x=x+3; // parte inteira
	$display ("e) 11.%b(2) = %f(10)", e, x );

end // main
endmodule // Guia_0201
