/*
	Guia_0804.v
	806454 - Yago Almeida Melo
*/

module Guia_0804 (
    input wire [5:0] a,
    input wire [5:0] b,
    output reg a_neq_b
    );

    always @* begin
      if (a != b) begin
        a_neq_b = 1;
      end
      else begin
        a_neq_b = 0;
      end
    end
endmodule

