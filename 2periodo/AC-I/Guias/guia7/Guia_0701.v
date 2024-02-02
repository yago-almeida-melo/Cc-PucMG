/*
	Guia_0701.v
	806454 - Yago Almeida Melo
*/

module Guia_0701 (
  input a, b, selection,
  output result
);
  wire and_result, nand_result;
  assign and_result = a & b;     // AND 
  assign nand_result = ~(a & b); // NAND 
  assign result = selection ? nand_result : and_result;
endmodule
