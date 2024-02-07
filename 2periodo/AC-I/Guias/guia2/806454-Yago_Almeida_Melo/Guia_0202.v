/*
	Guia_0202.v
	806454 - Yago Almeida Melo
*/
module	Guia_0202;
	// define data
	real a = 0.125; // decimal
	real b = 0.625; // decimal
	real c = 0.750; // decimal
	real d = 0.03125; // decimal
	real e = 0.875; // decimal
	integer intB = 2;
	integer intC = 3;
	integer intD = 6;
	integer intE = 9;
	integer y = 7 ; // counter
	reg [7:0] binary2 = 0;
	reg [7:0] binary = 0 ; // binary
	reg [7:0] binaryInt = 0;// binary of int part
	// actions
	initial
 		begin : main
  			$display ( "Guia_0202 - Decimal com parte fracionaria para binario " );
  			while ( a > 0 && y >= 0 ) begin
   				if ( a*2 >= 1 ) begin
     					binary[y] = 1;
     					a = a*2.0 - 1.0;
    				end
   				else begin
     					binary[y] = 0;
     					a = a*2.0;
    				end 
   				y=y-1;
  			end 
  			$display ( "a = 0,125 (10) =  0.%8b (2)", binary );
  			$display ( " " );
			y = 7 ;
  			binary = 0; 
			binary2 = intB;
			while ( b > 0 && y >= 0 ) begin
   				if ( b*2 >= 1 ) begin
     					binary[y] = 1;
     					b = b*2.0 - 1.0;
    				end
   				else begin
     					binary[y] = 0;
     					b = b*2.0;
    				end 
   				y=y-1;
  			end
  			$display ( "b = 2,625 (10) =  %8b,%8b (2)",binary2, binary );
  			$display ( " " );
  			y = 7 ;
  			binary = 0 ;
			binary2 = intC;
			while ( c > 0 && y >= 0 ) begin
   				if ( c*2 >= 1 ) begin
     					binary[y] = 1;
     					c = c*2.0 - 1.0;
    				end
   				else begin
     					binary[y] = 0;
     					c = c*2.0;
    				end 
   				y=y-1;
  			end 
  			$display ( "c = 3,75 (10) =  %8b,%8b (2) ", binary2, binary );
  			$display ( " " );
  			y = 7 ;
  			binary = 0 ;
			binary2 = intD;
			while ( d > 0 && y >= 0 ) begin
   				if ( d*2 >= 1 ) begin
     					binary[y] = 1;
     					d = d*2.0 - 1.0;
    				end
   				else begin
     					binary[y] = 0;
    		 			d = d*2.0;
    				end 
   				y=y-1;
  			end 
  			$display ( "d = 6,03125 (10) =  %8b,%8b (2)", binary2, binary );
  			$display ( " " );
  			y = 7 ;
  			binary = 0 ; 
			binary2 = intE;
			while ( e > 0 && y >= 0 ) begin
   				if ( e*2 >= 1 ) begin
     					binary[y] = 1;
     					e = e*2.0 - 1.0;
    				end
   				else begin
   					binary[y] = 0;
     					e = e*2.0;
    				end 
   				y=y-1;
  			end 
  			$display ( "e = 9,875 (10) =  %8b,%8b (2)", binary2, binary );
  			$display ( " " );
	end 
endmodule // Guia_0202