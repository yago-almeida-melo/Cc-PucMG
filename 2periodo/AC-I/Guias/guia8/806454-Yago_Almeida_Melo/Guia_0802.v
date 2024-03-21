/*
    806454 - Yago Almeida Melo
    Guia_0802.v
*/



// Half-subtractor module
module half_subtractor(
    input A, // Operand A
    input B, // Operand B
    output diff, // Difference
    output borrow // Borrow-out
);
    assign diff = A ^ B;
    assign borrow = ~A & B;
endmodule

// Full-subtractor module
module full_subtractor(
    input wire A, // Operand A
    input wire B, // Operand B
    input wire Cin, // Carry-in
    output wire S, // Sum
    output wire Cout // Carry-out
);
    wire diff1, borrow1, diff2, borrow2;

    // First half-subtractor
    half_subtractor HS1(.A(A), .B(B), .diff(diff1), .borrow(borrow1));

    // Second half-subtractor
    half_subtractor HS2(.A(diff1), .B(Cin), .diff(diff2), .borrow(borrow2));

    // Output sum
    assign S = diff2;

    // Output carry-out
    assign Cout = borrow1 | borrow2;
endmodule

module Guia_0802;
    reg [5:0] a;
    reg [5:0] b;
    reg cin;    
    wire [5:0] sum; 
    wire cout;      

    full_subtractor uut(a[0], b[0], cin, sum[0], cout);

    initial begin
        $monitor("a=%b, b=%b, sum=%b, carry=%b", a, b, sum, cout);
        a = 6'b000011; b = 6'b000011;
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
endmodule


