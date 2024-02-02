module SoP (output s,
input x, y);
// mintermos
assign s = ( ~x & y ) // 1
| ( x & ~y ); // 2
endmodule // SoP
