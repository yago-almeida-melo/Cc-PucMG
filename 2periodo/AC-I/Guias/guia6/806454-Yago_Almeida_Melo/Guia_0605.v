/*
	806454 - Yago Almeida Melo 
	Guia_0605.v
*/

module expression(output s1, 
input x,
input y, 
input w,
input z);

    assign s1 = ~(~x|~y) & ~(x& y) | ~((y&z) | ~x);
endmodule

module simplified(output s2, 
input x,
input y, 
input w,
input z);

    assign s2 = (x&~z) | (x&~y);

endmodule

module Guia_0605;
// ------------------------- definir dados
	reg x, y, w, z;
	wire s1, s2;

    expression EX (s1, x,y,w,z);
    simplified SIM (s2,x,y,w,z);
	
// ------------------------- parte principal
initial
  begin : main
	$display("Guia_0605 - Yago Almeida Melo - 806454");
	$display("Test module");
    $display("   x    y    w    z    s1   s2");
// projetar testes do modulo
	$monitor("%4b %4b %4b %4b %4b %4b", x, y, w, z, s1, s2);
	x = 1'b0; y = 1'b0; w = 1'b0; z = 1'b0;
	#1
	x = 1'b0; y = 1'b0; w = 1'b0; z = 1'b1;
	#1
    x = 1'b0; y = 1'b0; w = 1'b1; z = 1'b0;
	#1
    x = 1'b0; y = 1'b0; w = 1'b1; z = 1'b1;
	#1
    x = 1'b0; y = 1'b1; w = 1'b0; z = 1'b0;
	#1
    x = 1'b0; y = 1'b1; w = 1'b0; z = 1'b1;
	#1
    x = 1'b0; y = 1'b1; w = 1'b1; z = 1'b0;
	#1
    x = 1'b0; y = 1'b1; w = 1'b1; z = 1'b1;
	#1
    x = 1'b1; y = 1'b0; w = 1'b0; z = 1'b0;
	#1
    x = 1'b1; y = 1'b0; w = 1'b0; z = 1'b1;
	#1
    x = 1'b1; y = 1'b0; w = 1'b1; z = 1'b0;
	#1
    x = 1'b1; y = 1'b0; w = 1'b1; z = 1'b1;
	#1
    x = 1'b1; y = 1'b1; w = 1'b0; z = 1'b0;
	#1
    x = 1'b1; y = 1'b1; w = 1'b0; z = 1'b1;
	#1
    x = 1'b1; y = 1'b1; w = 1'b1; z = 1'b0;
    #1
    x = 1'b1; y = 1'b1; w = 1'b1; z = 1'b1;
	
  end
endmodule // test_Guia_0501
