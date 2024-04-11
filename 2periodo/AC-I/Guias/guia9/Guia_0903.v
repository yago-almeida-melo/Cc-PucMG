/*
	Guia_0903.v
	806454 - Yago Almeida Melo	
	
*/
`include "clock.v"

module pulse ( signal, clock );
    input clock;
    output signal;      
    reg signal;
    always @ (posedge clock ) begin
        signal = 1'b1;
        #48 signal = 1'b0;
        #48 signal = 1'b1;
        #48 signal = 1'b0;
    end
endmodule // pulse

module Guia_0903;
    wire clock;
    clock clk ( clock );
    wire p;

    pulse pls( p, clock );

    initial begin
        $dumpfile ( "Guia_0903.vcd" );
        $dumpvars ( 1, clock, p); 
        $monitor("clock=%b / p = %b ", clock, p);
        #1000 $finish;
    end
endmodule

