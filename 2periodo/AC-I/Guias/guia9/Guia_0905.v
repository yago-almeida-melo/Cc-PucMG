/*
*	Guia_0905.v
* 	806454 - Yago Almeida Melo
*/





module Guia_0905 (
    input wire clk,       					// Sinal de clock de entrada do módulo Guia_09001
    output wire pulse    					// Saída do pulso
);

reg [1:0] count;         					// Contador de 2 bits para controlar a marcação

always @(posedge clk) begin
    if (count == 2'b01) begin
        pulse <= 1'b1;   					// Saída de pulso ativo
    end else begin		
        pulse <= 1'b0;   					// Saída de pulso desativado
    end
    count <= count + 1'b1; 					// Incrementar o contador
end

endmodule





module test_Guia_0905;

    reg clk;
    wire pulse;

    Guia_0905 UUT (
        .clk(clk),
        .pulse(pulse)
    );

    always begin
        #1 clk = ~clk;
    end

    initial begin
        clk = 0;
        $dumpfile("waveform.vcd");
        $dumpvars(0, test_Guia_0905);
        $display("Iniciando simulação...");
        $monitor("Tempo=%t pulse=%b", $time, pulse);

        #10;
        $finish;
    end

endmodule

