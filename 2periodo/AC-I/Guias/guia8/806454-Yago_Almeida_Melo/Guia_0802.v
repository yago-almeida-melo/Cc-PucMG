/*
    806454 - Yago Almeida Melo
    Guia_0802.v
*/



// Half-subtractor module
module half_subtractor(
    input A, 
    input B, 
    output diff, borrow
);

    wire not_A;
    not NOT1 (not_A, A);
    xor XOR1 (diff, A, B);
    and AND1 (borrow, not_A, B);

    wire not_a;
    not NOT_A (not_a, A);
    xor XOR1 (diff, A, B);
    and AND1 (borrow, not_a, B);
endmodule

module full_subtractor(
    input A, B, Bin,
    output diff, Bout 
);
    wire borrow1, borrow2, diff1;

    half_subtractor HS1(A, B, diff1, borrow1);

    half_subtractor HS2(Bin, diff1, diff, borrow2);

    assign Bout = borrow1 | borrow2;

    
endmodule

module sixBitSubtractor(
    input [5:0] a, b,
    output [5:0] diff,
    output Bout
);
    wire c1, c2, c3, c4, c5;

    full_subtractor FS0(a[5], b[5], 1'b0, diff[5], c1);
    full_subtractor FS1(a[4], b[4], c1, diff[4], c2);
    full_subtractor FS2(a[3], b[3], c2, diff[3], c3);
    full_subtractor FS3(a[2], b[2], c3, diff[2], c4);
    full_subtractor FS4(a[1], b[1], c4, diff[1], c5);
    full_subtractor FS5(a[0], b[0], c5, diff[0], Bout);
endmodule

module sixBitSubtractor_tb;
    reg [5:0] a;
    reg [5:0] b;
    wire [5:0] diff; 
    wire Bout;      

    sixBitSubtractor uut(a, b, diff, Bout);
    full_subtractor uut(.A(a), .B(b), .Cin(cin), .S(sum), .Cout(cout));

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


