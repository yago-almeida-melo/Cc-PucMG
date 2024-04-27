/*
    Recuperação - Q02
    806454 - Yago Almeida Melo
*/

module f ( output s, input x, input y );
wire w1, w2, w3, w4;
not NOT_1 (w1, y);
and AND_1 (w2, x, w1);
not NOT_2 (w3, w2);
or OR__1 (w4, y, x);
or OR__2 (s, w3, w4);
endmodule // s = f (x,y)

module test;
    reg x, y;
    wire s;
    f F1 (s, x, y);
    initial begin
        $display("x  y  s");
        $monitor("%b  %b  %b", x,y,s);
        #1 x=0; y=0;
        #1 x=0; y=1;
        #1 x=1; y=0;
        #1 x=1; y=1;
    end
endmodule