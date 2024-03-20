/*
	Guia_0701.v
	806454 - Yago Almeida Melo
*/

module Guia_0701 (
  input wire a,
  input wire b,
  input wire select,
  output wire out1_and,
  output wire out2_nand,
  output wire selected_output
);

assign out1_and = a & b;
assign out2_nand = ~(a & b);
assign selected_output = select ? out2_nand : out1_and;

endmodule
