/*
	Guia_0804.v
	806454 - Yago Almeida Melo
*/

module oneBitCompare(
    input a,
    input b,
    output diff
); 
    
    xnor XNOR1(diff,a,b);
endmodule

module full_compare(
    input [5:0] a,b,
    output diff
);
    wire c1,c2,c3,c4,c5,c6;
    oneBitCompare OB1(a[5], b[5], c1);
    oneBitCompare OB2(a[4], b[4], c2);
    oneBitCompare OB3(a[3], b[3], c3);
    oneBitCompare OB4(a[2], b[2], c4);
    oneBitCompare OB5(a[1], b[1], c5);
    oneBitCompare OB6(a[0], b[0], c6);
    assign diff = c1 || c2 || c3 || c4 || c5 || c6;

endmodule

module Guia_0804;
    reg [5:0] a;
    reg [5:0] b;
    wire diff; 

    full_compare uut(.a(a), .b(b), .diff(diff));

    initial begin
        $monitor("%b - %b = %b", a, b, diff);
        a = 6'b001010; b = 6'b000100;
        #10;
        a = 6'b000001; b = 6'b000001;
        #10;
        a = 6'b000010; b = 6'b000010;
        #10;
        a = 6'b000100; b = 6'b000100;
        #10;
        a = 6'b001000; b = 6'b001000;
        #10;
        a = 6'b010000; b = 6'b000001;
        #10;
        a = 6'b100000; b = 6'b000001;
        #10;
        $finish();
    end
endmodule



