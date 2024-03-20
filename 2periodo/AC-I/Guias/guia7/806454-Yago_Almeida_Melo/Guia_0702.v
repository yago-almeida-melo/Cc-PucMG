/*
	Guia_0702.v
	806454 - Yago Almeida Melo
*/


module Guia_0702 (
  input wire a,
  input wire b,
  input wire select,
  output wire out_or,
  output wire out_nor,
  output wire selected_output
);

assign out_or = (a | b);
assign out_nor = ~(a | b);
assign selected_output = select ? out_nor : out_or;

endmodule

