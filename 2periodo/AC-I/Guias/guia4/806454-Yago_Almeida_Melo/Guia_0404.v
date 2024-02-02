/*
	Guia_0404.v
	806454 - Yago Almeida Melo
*/
module Guia_0403;
    reg X, Y, Z;
    wire S;
    assign S = (~X| Y|~Z) & (~X|~Y| Z) & (~X|~Y|~Z);
    
    initial begin
        $display("Exemplo - PoS");
        $display("-----------------------------");

        for (X = 0; X < 2; X = X + 1) begin
            for (Y = 0; Y < 2; Y = Y + 1) begin
                for (Z = 0; Z < 2; Z = Z + 1) begin
                    #1;
                    $monitor("%b %b %b | %b\n", X, Y, Z, S);
                end
            end
        end

        $finish;
    end
endmodule 

