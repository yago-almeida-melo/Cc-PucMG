/*
	Guia_0705.v
	806454 - Yago Almeida Melo
*/

module not_gate (
  input wire a,
  output wire s
);
  assign s = ~a;
endmodule

module and_gate (
  input wire a,
  input wire b,
  output wire s
);
  assign s = a & b;
endmodule

module nand_gate (
  input wire a,
  input wire b,
  output wire s
);
  assign s = ~(a & b);
endmodule

module xor_gate (
  input wire a,
  input wire b,
  output wire s
);
  assign s = a ^ b;
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

module mux_7to1 (
  input wire [2:0] sel,
  input wire a, // NOT result
  input wire b, // AND result
  input wire c, // NAND result
  input wire d, // XOR result
  input wire e, // XNOR result
  input wire f, // OR result
  input wire g, // NOR result
  output wire s
);
  assign s = sel == 3'b000 ? a :
             sel == 3'b001 ? b :
             sel == 3'b010 ? c :
             sel == 3'b011 ? d :
             sel == 3'b100 ? e :
             sel == 3'b101 ? f :
             sel == 3'b110 ? g :
             1'b0;
endmodule

module LU (
  input wire a,
  input wire b,
  input wire [2:0] sel,
  output wire s
);
  wire not_result;
  wire and_result;
  wire nand_result;
  wire xor_result;
  wire xnor_result;
  wire or_result;
  wire nor_result;

  not_gate NOT1 (.a(a), .s(not_result));
  and_gate AND1 (.a(a), .b(b), .s(and_result));
  nand_gate NAND1 (.a(a), .b(b), .s(nand_result));
  xor_gate XOR1 (.a(a), .b(b), .s(xor_result));
  xnor_gate XNOR1 (.a(a), .b(b), .s(xnor_result));
  or_gate OR1 (.a(a), .b(b), .s(or_result));
  nor_gate NOR1 (.a(a), .b(b), .s(nor_result));

  mux_7to1 MUX1 (.sel(sel), .a(not_result), .b(and_result), .c(nand_result), .d(xor_result), .e(xnor_result), .f(or_result), .g(nor_result), .s(s));
endmodule

module test_LU;
  reg a;
  reg b;
  reg [2:0] sel; // Corrigido para 3 bits
  wire s;

  LU LU1 (.a(a), .b(b), .sel(sel), .s(s));

  initial begin
		$display("Guia_0705 - Teste do módulo LU");
    $display(" a b sel s");
    a = 1'b0; b = 1'b1; sel = 3'b000; // Corrigido para 3 bits
    #1 $monitor("%b %b %b %b", a, b, sel, s);
    #1 sel = 3'b001; // Corrigido para 3 bits
    #1 sel = 3'b010; // Corrigido para 3 bits
    #1 sel = 3'b011; // Corrigido para 3 bits
    #1 sel = 3'b100; // Corrigido para 3 bits
    #1 sel = 3'b101; // Corrigido para 3 bits
    #1 sel = 3'b110; // Corrigido para 3 bits
    #1 $finish;
  end
endmodule
