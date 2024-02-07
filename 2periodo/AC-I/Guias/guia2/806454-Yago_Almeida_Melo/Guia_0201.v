/*
	Guia_0201.v
	806454 - Yago Almeida Melo
*/
module Guia_0201;
<<<<<<< HEAD
	reg [7:0] a = 8'b01101000; 
	reg [7:0] b = 8'b10011000; 
	reg [7:0] c = 8'b10101000; 
	reg [7:0] d = 8'b11001000; 
	reg [7:0] e = 8'b11011000; 
	real x = 0 ; 
	real power2 = 1.0; 
	integer y = 7 ; 
=======
// define data
	reg [7:0] a = 8'b00101000; 
	reg [7:0] b = 8'b01011000; 
	reg [7:0] c = 8'b10001000; 
	reg [7:0] d = 8'b11011000; 
	reg [7:0] e = 8'b10011000; 
	real x = 0 ; 
	real power2 = 1.0; 
	integer y = 7 ; 

>>>>>>> 3497b14dd6a145bbbc946b5cd42f977955730fb3
initial
begin : main
	$display ( "Guia_0201 - Binario com parte fracionaria para decimal\n" );
	while ( y >= 0 ) begin
		power2 = power2 / 2.0;
		if ( a[y] == 1 ) begin
			x = x + power2;
		end
		y=y-1;
	end 
	$display ("a) 0.%b(2) = %f(10)", a, x );
	x = 0;
	power2 = 1.0;
	y = 7;
	while ( y >= 0 ) begin
		power2 = power2 / 2.0;
		if ( b[y] == 1 ) begin 
			x = x + power2;
		end
		y=y-1;
	end 
	$display ("b) 0.%b(2) = %f(10)", b, x );
	x = 0;
	power2 = 1.0;
	y = 7;
	while ( y >= 0 ) begin
		power2 = power2 / 2.0;
		if ( c[y] == 1 ) begin
			x = x + power2;
		end
		y=y-1;
	end 
	$display ("c) 0.%b(2) = %f(10)", c, x );
	x = 0;
	power2 = 1.0;
	y = 7;
	while ( y >= 0 ) begin
		power2 = power2 / 2.0;
		if ( d[y] == 1 ) begin
			x = x + power2;
		end
		y=y-1;
	end 
	x=x+1;
	$display ("d) 1.%b(2) = %f(10)", d, x );
	x = 0;
	power2 = 1.0;
	y = 7;
	while ( y >= 0 ) begin
		power2 = power2 / 2.0;
		if ( e[y] == 1 ) begin
			x = x + power2;
		end
		y=y-1;
	end 
	x=x+3; 
	$display ("e) 11.%b(2) = %f(10)", e, x );

end 
endmodule 