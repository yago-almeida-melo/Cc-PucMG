/*
*   806454 - Yago Almeida Melo
*   test_Guia_0704.v 
*/

module test_Guia_0704;

reg a, b;
reg [1:0] chave;
wire result;

Guia_0704 UUT(
    .a(a),
    .b(b),
    .chave(chave),
    .result(result)
);

initial begin
    $display("a   b   chave     result\n");
    $monitor("%b   %b     %2b       %b", a,b,chave,result);
    // Teste 1: (OR)
    a = 0; b = 0; chave = 2'b00;
    #10;
    // Teste 2: (NOR)
    a = 0; b = 0; chave = 2'b01;
    #10;
    // Teste 3: (XOR)
    a = 0; b = 0; chave = 2'b10;
    #10;
    // Teste 4: (XNOR)
    a = 0; b = 0; chave = 2'b11;
    #10;
    // Teste 5: (OR)
    a = 0; b = 1; chave = 2'b00;
    #10;
    // Teste 6: (NOR)
    a = 0; b = 1; chave = 2'b01;
    #10;
    // Teste 7: (XOR)
    a = 0; b = 1; chave = 2'b10;
    #10;
    // Teste 8: (XNOR)
    a = 0; b = 1; chave = 2'b11;
    #10;
    // Teste 9: (OR)
    a = 1; b = 0; chave = 2'b00;
    #10;
    // Teste 1: (NOR)
    a = 1; b = 0; chave = 2'b01;
    #10;
    // Teste 1: (XOR)
    a = 1; b = 0; chave = 2'b10;
    #10;
    // Teste 1: (XNOR)
    a = 1; b = 0; chave = 2'b11;
    #10;
    // Teste 1: (OR)
    a = 1; b = 1; chave = 2'b00;
    #10;
    // Teste 1: (NOR)
    a = 1; b = 1; chave = 2'b01;
    #10;
    // Teste 1: (XOR)
    a = 1; b = 1; chave = 2'b10;
    #10;
    // Teste 1: (XNOR)
    a = 1; b = 1; chave = 2'b11;
    #10;
    $finish;
end

endmodule
