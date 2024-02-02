/*
	Guia_0803.v
	806454 - Yago Almeida Melo
*/

module Guia_0805 (
    input wire [5:0] a,
    output reg [5:0] a_comp
    );

    always @* begin
      a_comp = ~a + 6'b000001;
    end
endmodule

