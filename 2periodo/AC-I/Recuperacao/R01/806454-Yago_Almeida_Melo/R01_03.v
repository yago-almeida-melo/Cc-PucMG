/*
    Recuperação - Q03 a & b
    806454 - Yago Almeida Melo
*/
//////////////////////////////////// Letra a //////////////////////////////////////////////
//nand ( nand(a,c), nand(b,d) )
module Q03a ( output s, input a, input b, input c, input d );
wire w1, w2, w3;
nand NAND_1 (w1, a, c);
nand NAND_2 (w2, b, d);
nand NAND_3 (w3, w1, w2);
assign s = w3;
endmodule 

//////////////////////////////////// Letra b //////////////////////////////////////////////
module mux_2to1 (
    output reg out,
    input sel,
    input a,
input b
);
    always @* begin
        case(sel)
            0: out = a;
            1: out = b;
            default: out = 1'b0; // Valor padrão para garantir que haja uma atribuição
        endcase
    end
endmodule

//mux ( mux(b,c,a), mux(not(a),not(b),c), not(c) )
module Q03b ( output s, input a, input b, input c );
    wire m1_out, m2_out, m3_out;
    mux_2to1 M1 (.out(m1_out), .sel(a), .a(b), .b(c));
    mux_2to1 M2 (.out(m2_out), .sel(c), .a(~a), .b(~a));
    mux_2to1 M3 (.out(m3_out), .sel(~c), .a(m1_out), .b(m2_out));
    assign s = m3_out;
endmodule // s = f (x,y)

module test;
    reg a, b, c, d;
    wire S, m3_out;

    Q03a Q03A (.s(S), .a(a), .b(b), .c(c), .d(d));
    Q03b MUX (.s(m3_out), .a(a), .b(b), .c(c));
    
    initial begin
        $display("a  b  c  d  S");
        #1 a=0; b=0; c=0; d=0;
        $display("%b  %b  %b  %b  %b", a, b, c, d, S);
        #1 a=0; b=0; c=0; d=1;
        $display("%b  %b  %b  %b  %b", a, b, c, d, S);
        #1 a=0; b=0; c=1; d=0;
        $display("%b  %b  %b  %b  %b", a, b, c, d, S);
        #1 a=0; b=0; c=1; d=1;
        $display("%b  %b  %b  %b  %b", a, b, c, d, S);
        #1 a=0; b=1; c=0; d=0;
        $display("%b  %b  %b  %b  %b", a, b, c, d, S);
        #1 a=0; b=1; c=0; d=1;
        $display("%b  %b  %b  %b  %b", a, b, c, d, S);
        #1 a=0; b=1; c=1; d=0;
        $display("%b  %b  %b  %b  %b", a, b, c, d, S);
        #1 a=0; b=1; c=1; d=1;
        $display("%b  %b  %b  %b  %b", a, b, c, d, S);
        #1 a=1; b=0; c=0; d=0;
        $display("%b  %b  %b  %b  %b", a, b, c, d, S);
        #1 a=1; b=0; c=0; d=1;
        $display("%b  %b  %b  %b  %b", a, b, c, d, S);
        #1 a=1; b=0; c=1; d=0;
        $display("%b  %b  %b  %b  %b", a, b, c, d, S);
        #1 a=1; b=0; c=1; d=1;
        $display("%b  %b  %b  %b  %b", a, b, c, d, S);
        #1 a=1; b=1; c=0; d=0;
        $display("%b  %b  %b  %b  %b", a, b, c, d, S);
        #1 a=1; b=1; c=0; d=1;
        $display("%b  %b  %b  %b  %b", a, b, c, d, S);
        #1 a=1; b=1; c=1; d=0;
        $display("%b  %b  %b  %b  %b", a, b, c, d, S);
        #1 a=1; b=1; c=1; d=1;
        $display("%b  %b  %b  %b  %b", a, b, c, d, S);
        //////////////////////////////////////////////
        $display("\n\na  b  c  m3_out");
        #1 a=0; b=0; c=0;
        $display("%b  %b  %b  %b", a, b, c, m3_out);
        #1 a=0; b=0; c=0;
        $display("%b  %b  %b  %b", a, b, c, m3_out);
        #1 a=0; b=0; c=1;
        $display("%b  %b  %b  %b", a, b, c, m3_out);
        #1 a=0; b=0; c=1;
        $display("%b  %b  %b  %b", a, b, c, m3_out);
        #1 a=0; b=1; c=0;
        $display("%b  %b  %b  %b", a, b, c, m3_out);
        #1 a=0; b=1; c=0;
        $display("%b  %b  %b  %b", a, b, c, m3_out);
        #1 a=0; b=1; c=1;
        $display("%b  %b  %b  %b", a, b, c, m3_out);
        #1 a=0; b=1; c=1;
        $display("%b  %b  %b  %b", a, b, c, m3_out);
        #1 a=1; b=0; c=0;
        $display("%b  %b  %b  %b", a, b, c, m3_out);
        #1 a=1; b=0; c=0;
        $display("%b  %b  %b  %b", a, b, c, m3_out);
        #1 a=1; b=0; c=1;
        $display("%b  %b  %b  %b", a, b, c, m3_out);
        #1 a=1; b=0; c=1;
        $display("%b  %b  %b  %b", a, b, c, m3_out);
        #1 a=1; b=1; c=0;
        $display("%b  %b  %b  %b", a, b, c, m3_out);
        #1 a=1; b=1; c=0;
        $display("%b  %b  %b  %b", a, b, c, m3_out);
        #1 a=1; b=1; c=1;
        $display("%b  %b  %b  %b", a, b, c, m3_out);
        #1 a=1; b=1; c=1;
        $display("%b  %b  %b  %b", a, b, c, m3_out);
    end
endmodule