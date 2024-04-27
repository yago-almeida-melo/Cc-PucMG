/*
    Recuperação 01 - Questão 5b
    806454 - Yago Almeida Melo
*/

module xor_table(
    input [3:0]a,
    input [3:0]b,
    output [3:0]out,
    output [3:0]out0,
    output [3:0]out_or
);

xor out1 (out[3], a[3], b[3]);        
xor out2 (out[2], a[2], b[2]);  
xor out3 (out[1], a[1], b[1]);  
xor out4 (out[0], a[0], b[0]);
assign out0 = ~out; 
assign out_or = out | out0; 

endmodule

module R01_05a;

reg [3:0] a, b;
wire [3:0] out1, out2, out3;

xor_table xor_inst(
    .a(a),
    .b(b),
    .out(out1),
    .out0(out2),
    .out_or(out3)
);

initial begin
    a = 4'b0101; b = 4'b0110;
    $display("a | b | XOR(a, b) | XOR(-a, -b)");
    $display("%b  %b   %b   %b  %b", a, b, out1, out2, out3);
    $finish;
end

endmodule
