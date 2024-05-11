/*
    Guia_1302.v
    806454 - Yago Almeida Melo
*/

module jkff ( output q, output qnot,
    input j, input k,
    input clk, input preset, input clear );

    reg q, qnot;
    always @( posedge clk or posedge preset or posedge clear ) begin
        if ( clear )begin
            q <= 0; qnot <= 1; end
        else if ( preset ) begin 
            q <= 1; qnot <= 0; end
        else if ( j & ~k ) begin 
            q <= 1; qnot <= 0; end
        else if ( ~j & k ) begin 
            q <= 0; qnot <= 1; end
        else if ( j & k )begin 
            q <= ~q; qnot <= ~qnot; end
    end
endmodule // jkff

module contador_decadico_cres(output [5:0] count,
    input in, input clk,
    input clear);

    wire [5:0] inverted_count;
    wire out1, out2;
    nand n1(out1, ~count[3], count[2], ~count[1], count[0]);
    nand n2(out2, clear, out1);

    jkff jk1(.q(count[3]), .qnot(inverted_count[0]), .j(jk), .k(jk), .clk(inverted_count[1]), .preset(1'b0), .clear(out2));
    jkff jk2(.q(count[2]), .qnot(inverted_count[1]), .j(jk), .k(jk), .clk(inverted_count[2]), .preset(1'b0), .clear(out2));
    jkff jk3(.q(count[1]), .qnot(inverted_count[2]), .j(jk), .k(jk), .clk(inverted_count[3]), .preset(1'b0), .clear(out2));
    jkff jk4(.q(count[0]), .qnot(inverted_count[3]), .j(jk), .k(jk), .clk(clk), .preset(1'b0), .clear(out2));
endmodule

module teste_contador;
    reg clk = 0;
    wire [5:0] count;
    reg reset;
    reg jk = 1;
    

    contador_decadico_cres c1(count, jk, clk, preset);

    
    always #5 clk = ~clk;

    initial begin
        reset = 1;
        #10 
        reset = 0;
        #10;
        $monitor("Contador crescente decadico: %b", count);
        #300;
        $finish;
    end

endmodule
 