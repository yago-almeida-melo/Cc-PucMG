/*
	Guia_0705.v
	806454 - Yago Almeida Melo
*/


module Guia_0705 (
    input wire a,
    input wire b,
    input wire [2:0] select,
    input wire negate_b,
    output wire result
);

wire not_b, selected_gate;

assign not_b = negate_b ? ~b : b;

assign selected_gate = (select == 3'b000) ? (~a) :
                       (select == 3'b001) ? (a & not_b) :
                       (select == 3'b010) ? ~(a & not_b) :
                       (select == 3'b011) ? (a | not_b) :
                       (select == 3'b100) ? ~(a | not_b) :
                       (select == 3'b101) ? (a ^ not_b) :
                       (select == 3'b110) ? ~(a ^ not_b) :
                       1'b0;

assign result = selected_gate;

endmodule
