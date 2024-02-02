/*
	Guia_0105.v
	806454 - Yago Almeida Melo
*/

module Guia_0105; 
	reg [15:0] y; 
 	reg [0:11][7:0] s = "PUC-M.G."; // char array[3] (3x8 bits - little Endian)
initial
	begin : main
		$display ("a = \"%8s\" = %h %h %h %h %h %h %h %h (16)", s, s[4],s[5],s[6],s[7],s[8],s[9],s[10],s[11]);
		s = "2024-01";
		$display ("b = \"%7s\" = %h %h %h %h %h %h %h (16)", s,s[5],s[6],s[7],s[8],s[9],s[10],s[11]);
		s = "Minas Gerais";
		$display ("c = \"%12s\" = %b %b %b %b %b %b %b %b %b %b %b %b (b)", s, s[0],s[1], s[2],s[3],s[4],s[5],s[6],s[7],s[8],s[9],s[10],y[14]);
		y = 'o116117111124105;
		$display ("d = \"%16o\" = %s %s %s %s %s (s)", y, y[15:12],y[11:9],y[8:6],y[5:3],y[2:0]);
	end 
endmodule 
