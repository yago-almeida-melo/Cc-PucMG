/*
	Guia_0901.v
	806454 - Yago Almeida Melo
*/


module PulseGen(
    input wire clk,
    output reg pulse
);
    always @(posedge clk) begin
        pulse <= 1'b1; 
        #3 pulse <= 1'b0;  
    end
endmodule
module TriggerGen(
    input wire clk,
    input wire trigger,
    output reg signal
);
    always @(posedge clk) begin
        if (trigger) begin
            signal <= 1'b1;  
            #60 signal <= 1'b0;  
        end else begin
            signal <= 1'b0;
        end
    end
endmodule
module top(
    input wire clk,
    input wire trigger,
    output wire pulse,
    output wire signal
);
    PulseGen PulseGenInstance(
        .clk(clk),
        .pulse(pulse)
    );

    TriggerGen TriggerGenInstance(
        .clk(clk),
        .trigger(trigger),
        .signal(signal)
    );
endmodule
module clock(
    output reg clk
);
    initial begin
        clk = 1'b0;
        forever #10 clk = ~clk;  // Gera um clock com período de 20 unidades de tempo
    end
endmodule
module Guia_0901;
    wire clock;
    reg p;
    wire p1,t1;

    
    clock clk(clock);

    
    PulseGen pulse1(
        .clk(clock),
        .pulse(p1)
    );

    
    TriggerGen trigger1(
        .clk(clock),
        .trigger(p),
        .signal(t1)
    );

    initial begin
        p = 1'b0;
    end

    initial begin
        $dumpfile ( "Guia_0901.vcd" );
        $dumpvars ( 1, clock, p1, p, t1 );
        #060 p = 1'b1;
        #120 p = 1'b0;
        #180 p = 1'b1;
        #240 p = 1'b0;
        #300 p = 1'b1;
        #360 p = 1'b0;
        #376 $finish;
    end
endmodule // Guia_0901
