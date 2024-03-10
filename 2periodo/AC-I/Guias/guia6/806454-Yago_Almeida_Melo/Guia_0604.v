/*
	806454 - Yago Almeida Melo 
	Guia_0604.v
*/

module expression(output s1, 
input X,
input Y, 
input W,
input Z);

    assign s1 = (X|Y|~W|Z) & (X|~Y|W|~Z) & (X|~Y|~W|~Z) & (~X|~Y|W|~Z);
endmodule

module simplified(output s2, 
input X,
input Y, 
input W,
input Z);

    assign s2 = ~Z|(W|~Y|X) & X|~W
endmodule

module Guia_0603;
// ------------------------- definir dados
	reg X, Y, W, Z;
	wire s1, s2;

    expression EX (s1, X,Y,W,Z);
    simplified SIM (s2,X,Y,W,Z);
	
// ------------------------- parte principal
initial
  begin : main
	$display("Guia_0604 - Yago Almeida Melo - 806454");
	$display("Test module");
    $display("   X    Y    W    Z    s1   s2");
// projetar testes do modulo
	$monitor("%4b %4b %4b %4b %4b %4b", X, Y, W, Z, s1, s2);
	X = 1'b0; Y = 1'b0; W = 1'b0; Z = 1'b0;
	#1
	X = 1'b0; Y = 1'b0; W = 1'b0; Z = 1'b1;
	#1
    X = 1'b0; Y = 1'b0; W = 1'b1; Z = 1'b0;
	#1
    X = 1'b0; Y = 1'b0; W = 1'b1; Z = 1'b1;
	#1
    X = 1'b0; Y = 1'b1; W = 1'b0; Z = 1'b0;
	#1
    X = 1'b0; Y = 1'b1; W = 1'b0; Z = 1'b1;
	#1
    X = 1'b0; Y = 1'b1; W = 1'b1; Z = 1'b0;
	#1
    X = 1'b0; Y = 1'b1; W = 1'b1; Z = 1'b1;
	#1
    X = 1'b1; Y = 1'b0; W = 1'b0; Z = 1'b0;
	#1
    X = 1'b1; Y = 1'b0; W = 1'b0; Z = 1'b1;
	#1
    X = 1'b1; Y = 1'b0; W = 1'b1; Z = 1'b0;
	#1
    X = 1'b1; Y = 1'b0; W = 1'b1; Z = 1'b1;
	#1
    X = 1'b1; Y = 1'b1; W = 1'b0; Z = 1'b0;
	#1
    X = 1'b1; Y = 1'b1; W = 1'b0; Z = 1'b1;
	#1
    X = 1'b1; Y = 1'b1; W = 1'b1; Z = 1'b0;
    #1
    X = 1'b1; Y = 1'b1; W = 1'b1; Z = 1'b1;
	
  end
endmodule // Guia_0604
