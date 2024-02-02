
/*
	Guia_0801.v
   	806454 - Yago Almeida Melo
*/ 

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

module sixBitAdder(
    input [5:0] a, b,
    output [5:0] sum,
    output carry
);
    wire c1, c2, c3, c4, c5;

    fullAdder FA0(a[0], b[0], 1'b0, sum[0], c1);
    fullAdder FA1(a[1], b[1], c1, sum[1], c2);
    fullAdder FA2(a[2], b[2], c2, sum[2], c3);
    fullAdder FA3(a[3], b[3], c3, sum[3], c4);
    fullAdder FA4(a[4], b[4], c4, sum[4], c5);
    fullAdder FA5(a[5], b[5], c5, sum[5], carry);
endmodule

module sixBitAdder_tb;
    reg [5:0] a, b;
    wire [5:0] sum;
    wire carry;

    sixBitAdder uut(a, b, sum, carry);

    initial begin
        a = 6'b000000; b = 6'b000000;
        #10;
        a = 6'b000001; b = 6'b000001;
        #10;
        a = 6'b000010; b = 6'b000010;
        #10;
        a = 6'b000100; b = 6'b000100;
        #10;
        a = 6'b001000; b = 6'b001000;
        #10;
        a = 6'b010000; b = 6'b010000;
        #10;
        a = 6'b100000; b = 6'b100000;
        #10;
        $finish();
    end

    initial begin
			$display("Guia_0801 - Ricardo Soares Cerqueira - 1445631\n");
        $monitor("a=%b, b=%b, sum=%b, carry=%b", a, b, sum, carry);
    end
endmodule
