/*
	Guia_0403.v
	806454 - Yago Almeida Melo
*/
module Guia_0403;
    reg x, y, z;
    wire f;

    // Expressão em SoP: f(x, y, z) = x'yz + xy'z' + xyz
    assign f = (~x & y & ~z) | (x & ~y & z) | (x & y & ~z) |(x & y & z);
    
    initial begin
        $display("x y z | f(x, y, z) g(x, y, z)");
        $display("-----------------------------");

        $display("F = %b", f );
        /*for (x = 0; x < 2; x = x + 1) begin
            for (y = 0; y < 2; y = y + 1) begin
                for (z = 0; z < 2; z = z + 1) begin
                    #1;
                    $write("%b %b %b | %b\n", x, y, z, f);
                end
            end
        end*/

        $finish;
    end
endmodule 

