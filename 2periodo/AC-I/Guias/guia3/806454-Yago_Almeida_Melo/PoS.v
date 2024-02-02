/*
	Guia_0403_test.v
	806454 - Yago Almeida Melo
*/
module PoS (output s, input X, Y);
    assign S = ( X | Y ) // 0
    & ( ~X | ~Y ); // 3
endmodule




