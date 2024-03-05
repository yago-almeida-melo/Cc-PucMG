/*
	806454 - Yago Almeida Melo 
	Guia_0501.v
*/

module test_Guia_0501;
// ------------------------- definir dados
	reg a, b;
	wire s;
	
	Guia_0501 uut (
    	.a(a),
    	.b(b),
    	.s(s)
	);
	
// ------------------------- parte principal
initial
  begin : main
	$display("Guia_0501 - Yago Almeida Melo - 806454");
	$display("Test module");
	$display("   a    b   out");
// projetar testes do modulo
	$monitor("%4b %4b %4b ", a, b, s);
	a = 1'b0; b = 1'b0;
	#1
	a = 1'b0; b = 1'b1;
	#1
	a = 1'b1; b = 1'b0;
	#1
	a = 1'b1; b = 1'b1;
  end
endmodule // test_Guia_0501
