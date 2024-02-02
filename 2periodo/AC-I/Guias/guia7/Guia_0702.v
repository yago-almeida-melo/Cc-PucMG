/*
	Guia_0702.v
	806454 - Yago Almeida Melo
*/


module Guia_0702 (
    input wire x,
    input wire y,
    input wire select, // Entrada de seleção (0 para OR, 1 para NOR)
    output wire z
);

assign z = (select == 1'b0) ? (x | y) : ~(x | y);

endmodule 



module test_Guia_0702;

    reg x, y, select;
    wire z;


    Guia_0702 u1 (
        .x(x),
        .y(y),
        .select(select),
        .z(z)
    );

    initial begin
        // Teste OR (select = 0)
        x = 1'b0; y = 1'b0; select = 1'b0;
        #1 $display("Teste OR: x = %b, y = %b, select = %b, z = %b", x, y, select, z);
        
        // Teste NOR (select = 1)
        x = 1'b1; y = 1'b0; select = 1'b1;
        #1 $display("Teste NOR: x = %b, y = %b, select = %b, z = %b", x, y, select, z);
        
        $finish;
    end

endmodule /

