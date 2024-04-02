/*
	Guia_0805.v
	806454 - Yago Almeida Melo
*/

module complement_one (
input [4:0] a,
output [4:0] res
);
nor NOT1 (res[4], a[4]);
nor NOT2 (res[3], a[3]);
nor NOT3 (res[2], a[2]);
nor NOT4 (res[1], a[1]);
nor NOT5 (res[0], a[0]);
endmodule

// Meio somador
module halfAdder (
    input a, b,
    output sum, carry
);
    xor XOR1 ( sum, a, b );
    and AND1 ( carry, a, b );
endmodule

// Somador completo usando meio somador
module fullAdder(
    input a, b, cin,
    output sum, carry
);
    wire c, c1, s;
    halfAdder HA0(a, b, s, c);
    halfAdder HA1(cin, s, sum, c1);
    assign carry = c | c1 ;
endmodule

module complement_two(
    input [4:0] a,
    output [4:0] sum,
    output carry
);
    wire c1, c2, c3, c4;

    fullAdder FA0(a[0], 1'b1, 1'b0, sum[0], c1);
    fullAdder FA1(a[1], 1'b0, c1, sum[1], c2);
    fullAdder FA2(a[2], 1'b0, c2, sum[2], c3);
    fullAdder FA3(a[3], 1'b0, c3, sum[3], c4);
    fullAdder FA4(a[4], 1'b0, c4, sum[4], carry);
endmodule



module Guia_0805;
  reg [4:0] a;
  wire [4:0] a_comp, res;
  wire carry;

  complement_one uut1(a, res);
  complement_two uut2(res ,a_comp, carry);

  initial begin
    $monitor("a=%b, complemento de 2=%b", a, a_comp);
    a = 5'b00011; 
    #10;
    a = 5'b00001; 
    #10;
    a = 5'b00010; 
    #10;
    a = 5'b00100; 
    #10;
    a = 5'b01000; 
    #10;
    a = 5'b10000; 
    #10;
    $finish();
  end
endmodule

