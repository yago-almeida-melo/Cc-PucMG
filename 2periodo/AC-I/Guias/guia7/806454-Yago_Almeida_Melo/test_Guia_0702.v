/*
*   806454 - Yago Almeida Melo
*   test_Guia_0702.v 
*/

module test_Guia_0702;

reg a, b, select;
wire out_or, out_nor, selected_output;

Guia_0702 UUT(
    .a(a),
    .b(b),
    .select(select),
    .out_or(out_or),
    .out_nor(out_nor),
    .selected_output(selected_output)
);

initial begin
    $display("a   b   select   selected_output\n");
    $monitor("%b   %b      %b           %b", a,b,select,selected_output);
    // Teste 1: select = 0 (OR)
    a = 0; b = 0; select = 0;
    #10;
    // Teste 2: select = 1 (NOR)
    a = 0; b = 0; select = 1;
    #10;
    // Teste 3: select = 0 (OR)
    a = 0; b = 1; select = 0;
    #10;
    // Teste 4: select = 1 (NOR)
    a = 0; b = 1; select = 1;
    #10;
    // Teste 5: select = 0 (OR)
    a = 1; b = 0; select = 0;
    #10;
    // Teste 6: select = 1 (NOR)
    a = 1; b = 0; select = 1;
    #10;
    // Teste 7: select = 0 (OR)
    a = 1; b = 1; select = 0;
    #10;
    // Teste 8: select = 1 (NOR)
    a = 1; b = 1; select = 1;
    #10;
    
    
    $finish;
end

endmodule
