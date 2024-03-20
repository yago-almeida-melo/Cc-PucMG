/*
*   806454 - Yago Almeida Melo
*   test_Guia_0705.v 
*/

module test_Guia_0705;

reg a, b, negate_b;
reg [2:0] select;
wire result;

Guia_0705 UUT(
    .a(a),
    .b(b),
    .select(select),
    .negate_b(negate_b),
    .result(result)
);

initial begin
    $display("a   b   negate_b   select     result\n");
    $monitor("%b   %b      %b         %3b         %b", a,b,negate_b,select,result);
    // Teste 1: (NOT)
    a = 0; b = 0; select = 3'b000; negate_b = 0;
    #10;
    // Teste 2: (AND)
    a = 0; b = 0; select = 3'b001; negate_b = 0;
    #10;
    // Teste 3: (NAND)
    a = 0; b = 0; select = 3'b010; negate_b = 0;
    #10;
    // Teste 4: (OR)
    a = 0; b = 0; select = 3'b011; negate_b = 0;
    #10;
    // Teste 5: (NOR)
    a = 0; b = 0; select = 3'b100; negate_b = 0;
    #10;
    // Teste 6: (XOR)
    a = 0; b = 0; select = 3'b101; negate_b = 0;
    #10;
    // Teste 7: (XNOR)
    a = 0; b = 0; select = 3'b110; negate_b = 0;
    #10;
    ////////////////////////////
    // Teste 8: (NOT)
    a = 0; b = 1; select = 3'b000; negate_b = 1;
    #10;
    // Teste 9: (AND)
    a = 0; b = 1; select = 3'b001; negate_b = 1;
    #10;
    // Teste 10: (NAND)
    a = 0; b = 1; select = 3'b010; negate_b = 1;
    #10;
    // Teste 11: (OR)
    a = 0; b = 1; select = 3'b011; negate_b = 1;
    #10;
    // Teste 12: (NOR)
    a = 0; b = 1; select = 3'b100; negate_b = 1;
    #10;
    // Teste 13: (XOR)
    a = 0; b = 1; select = 3'b101; negate_b = 1;
    #10;
    // Teste 14: (XNOR)
    a = 0; b = 1; select = 3'b110; negate_b = 1;
    #10;
    ////////////////////////////
    // Teste 15: (NOT)
    a = 1; b = 0; select = 3'b000; negate_b = 0;
    #10;
    // Teste 16: (AND)
    a = 1; b = 0; select = 3'b001; negate_b = 0;
    #10;
    // Teste 17: (NAND)
    a = 1; b = 0; select = 3'b010; negate_b = 0;
    #10;
    // Teste 18: (OR)
    a = 1; b = 0; select = 3'b011; negate_b = 0;
    #10;
    // Teste 19: (NOR)
    a = 1; b = 0; select = 3'b100; negate_b = 0;
    #10;
    // Teste 19: (XOR)
    a = 1; b = 0; select = 3'b101; negate_b = 0;
    #10;
    // Teste 20: (XNOR)
    a = 1; b = 0; select = 3'b110; negate_b = 0;
    #10;
    ////////////////////////////
    // Teste 21: (NOT)
    a = 1; b = 1; select = 3'b000; negate_b = 1;
    #10;
    // Teste 22: (AND)
    a = 1; b = 1; select = 3'b001; negate_b = 1;
    #10;
    // Teste 23: (NAND)
    a = 1; b = 1; select = 3'b010; negate_b = 1;
    #10;
    // Teste 24: (OR)
    a = 1; b = 1; select = 3'b011; negate_b = 1;
    #10;
    // Teste 25: (NOR)
    a = 1; b = 1; select = 3'b100; negate_b = 1;
    #10;
    // Teste 26: (XOR)
    a = 1; b = 1; select = 3'b101; negate_b = 1;
    #10;
    // Teste 27: (XNOR)
    a = 1; b = 1; select = 3'b110; negate_b = 1;
    #10;

    $finish;
end

endmodule
