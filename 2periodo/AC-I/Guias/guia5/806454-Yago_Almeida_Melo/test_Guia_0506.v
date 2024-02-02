/*
	test_Guia_0506.v
	806454 - Yago Almeida Melo
*/

module test_Guia_0506;

reg a, b;
wire out;

Guia_0506 uut (
    .a(a),
    .b(b),
    .out(out)
);

initial begin
    // Teste 1: a = 0, b = 0 (XOR resultante = 0, Negado = 1)
    a = 0;
    b = 0;
    #10;
    if (out !== 1'b1) $display("Teste 1 falhou");

    // Teste 2: a = 0, b = 1 (XOR resultante = 1, Negado = 0)
    a = 0;
    b = 1;
    #10;
    if (out !== 1'b0) $display("Teste 2 falhou");

    // Teste 3: a = 1, b = 0 (XOR resultante = 1, Negado = 0)
    a = 1;
    b = 0;
    #10;
    if (out !== 1'b0) $display("Teste 3 falhou");

    // Teste 4: a = 1, b = 1 (XOR resultante = 0, Negado = 1)
    a = 1;
    b = 1;
    #10;
    if (out !== 1'b1) $display("Teste 4 falhou");

    $finish;
 end
endmodule

