/*
	Guia_0704.v
	806454 - Yago Almeida Melo
*/ 

module xor_gate (
  input wire a,
  input wire b,
  output wire s
);
  assign s = (a & ~b) | (~a & b);
endmodule

module xnor_gate (
  input wire a,
  input wire b,
  output wire s
);
  assign s = ~(a ^ b);
endmodule

module or_gate (
  input wire a,
  input wire b,
  output wire s
);
  assign s = a | b;
endmodule

module nor_gate (
  input wire a,
  input wire b,
  output wire s
);
  assign s = ~(a | b);
endmodule

module mux_4to1 (
  input wire [1:0] sel,
  input wire a, // XOR result
  input wire b, // XNOR result
  input wire c, // OR result
  input wire d, // NOR result
  output wire s
);
  assign s = sel[1] ? (sel[0] ? d : c) : (sel[0] ? b : a);
endmodule

module LU (
  input wire a,
  input wire b,
  input wire [1:0] sel,
  output wire s
);
  wire xor_result;
  wire xnor_result;
  wire or_result;
  wire nor_result;

  xor_gate XOR1 (.a(a), .b(b), .s(xor_result));
  xnor_gate XNOR1 (.a(a), .b(b), .s(xnor_result));
  or_gate OR1 (.a(a), .b(b), .s(or_result));
  nor_gate NOR1 (.a(a), .b(b), .s(nor_result));

  mux_4to1 MUX1 (.sel(sel), .a(xor_result), .b(xnor_result), .c(or_result), .d(nor_result), .s(s));
endmodule

module test_LU;
  reg a;
  reg b;
  reg [1:0] sel;
  wire s;

  LU LU1 (.a(a), .b(b), .sel(sel), .s(s));

  initial begin
    $display("Guia_0704 - Teste do módulo LU");
    $display(" a b sel s");
    a = 1'b0; b = 1'b1; sel = 2'b00;
    #1 $monitor("%b %b %b %b", a, b, sel, s);
    #1 sel = 2'b01;
    #1 sel = 2'b10;
    #1 sel = 2'b11;
    #1 $finish;
  end
endmodule
