/*
	Guia_0105.v
	806454 - Yago Almeida Melo
*/

module Guia_0105; 
	reg [7:0] d1 = 8'o116;
	reg [7:0] d2 = 8'o117;
	reg [7:0] d3 = 8'o111;
	reg [7:0] d4 = 8'o124;
	reg [7:0] d5 = 8'o105;
	reg [7:0] e1 = 8'h54;
	reg [7:0] e2 = 8'h61;
	reg [7:0] e3 = 8'h72;
	reg [7:0] e4 = 8'h64;
	reg [7:0] e5 = 8'h65;
 	reg [0:11][7:0] s = "PUC-M.G."; 
initial
	begin : main
		$display ("a = \"%8s\" = %h %h %h %h %h %h %h %h (16)", s, s[4],s[5],s[6],s[7],s[8],s[9],s[10],s[11]);
		s = "2024-01";
		$display ("b = \"%7s\" = %h %h %h %h %h %h %h (16)", s,s[5],s[6],s[7],s[8],s[9],s[10],s[11]);
		s = "Minas Gerais";
		$display ("c = \"%12s\" = %b %b %b %b %b %b %b %b %b %b %b %b (b)", s, s[0],s[1], s[2],s[3],s[4],s[5],s[6],s[7],s[8],s[9],s[10],s[11]);
		$display ("d = \"%o %o %o %o %o\" = %c%c%c%c%c", d1,d2,d3,d4,d5,d1,d2,d3,d4,d5);
		$display ("e = \"%h %h %h %h %h\" = %c%c%c%c%c", e1,e2,e3,e4,e5,e1,e2,e3,e4,e5);
	end 
endmodule 
