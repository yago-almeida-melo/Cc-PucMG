/*
*   806454 - Yago Almeida Melo
*   test_Guia_0703.v 
*/

module test_Guia_0703;

reg a, b, chave1, chave2;
wire result;

Guia_0703 UUT(
    .a(a),
    .b(b),
    .chave1(chave1),
    .chave2(chave2),
    .result(result)
);

initial begin
    $display("a   b   chave1   chave2   result\n");
    $monitor("%b   %b     %b        %b        %b", a,b,chave1,chave2,result);
    // Teste 1: (AND)
    a = 0; b = 0; chave1 = 0; chave2 = 0;
    #10;
    // Teste 2:  (OR)
    a = 0; b = 0; chave1 = 0; chave2 = 1;
    #10;
    // Teste 3: (NAND)
    a = 0; b = 0; chave1 = 1; chave2 = 0;
    #10;
    // Teste 4: (NOR)
    a = 0; b = 0; chave1 = 1; chave2 = 1;
    #10;
    // Teste 5: (AND)
    a = 0; b = 1; chave1 = 0; chave2 = 0;
    #10;
    // Teste 6: (OR)
    a = 0; b = 1; chave1 = 0; chave2 = 1;
    #10;
    // Teste 7: (NAND)
    a = 0; b = 1; chave1 = 1; chave2 = 0;
    #10;
    // Teste 8: (NOR)
    a = 0; b = 1; chave1 = 1; chave2 = 1;
    #10;
    // Teste 9: (AND)
    a = 1; b = 0; chave1 = 0; chave2 = 0;
    #10;
    // Teste 10: (OR)
    a = 1; b = 0; chave1 = 0; chave2 = 1;
    #10;
    // Teste 11: (NAND)
    a = 1; b = 0; chave1 = 1; chave2 = 0;
    #10;
    // Teste 12: (NOR)
    a = 1; b = 0; chave1 = 1; chave2 = 1;
    #10;
    // Teste 13: (AND)
    a = 1; b = 1; chave1 = 0; chave2 = 0;
    #10;
    // Teste 14: (OR)
    a = 1; b = 1; chave1 = 0; chave2 = 1;
    #10;
    // Teste 15: (NAND)
    a = 1; b = 1; chave1 = 1; chave2 = 0;
    #10;
    // Teste 16: (NOR)
    a = 1; b = 1; chave1 = 1; chave2 = 1;
    #10;
    
    
    $finish;
end

endmodule
