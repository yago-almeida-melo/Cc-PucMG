/*
    Guia_1301.v
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

module teste_contador;
    reg clk = 0;
    wire [5:0] count;
    wire [5:0] inverted_count;
    reg reset;
    reg jk = 1;
    wire [5:0] q;
    wire [5:0] qnot;

    jkff jk1(.q(q[0]), .qnot(qnot[0]), .j(jk), .k(jk), .clk(clk), .preset(1'b0), .clear(reset));
    jkff jk2(.q(q[1]), .qnot(qnot[1]), .j(jk), .k(jk), .clk(qnot[0]), .preset(1'b0), .clear(reset));
    jkff jk3(.q(q[2]), .qnot(qnot[2]), .j(jk), .k(jk), .clk(qnot[1]), .preset(1'b0), .clear(reset));
    jkff jk4(.q(q[3]), .qnot(qnot[3]), .j(jk), .k(jk), .clk(qnot[2]), .preset(1'b0), .clear(reset));
    jkff jk5(.q(q[4]), .qnot(qnot[4]), .j(jk), .k(jk), .clk(qnot[3]), .preset(1'b0), .clear(reset));
    jkff jk6(.q(q[5]), .qnot(qnot[5]), .j(jk), .k(jk), .clk(qnot[4]), .preset(1'b0), .clear(reset));

    always #5 clk = ~clk;

    initial begin
        reset = 1;
        #10 
        reset = 0;
        #10;
        $monitor("Contador decrescente: %b", qnot);
        #200;
        $finish;
    end

endmodule
 