/*
*	Guia_0904.v
*	806454 - Yago Almeida Melo
*/

module Guia_0904 (
    input wire clk,       					// Sinal de clock de entrada do módulo Guia_0900
    output wire pulse    					// Saída do pulso
);

reg [1:0] count;         					// Contador de 2 bits para dividir a frequência

always @(posedge clk) begin
    if (count == 2'b11) begin
        pulse <= 1'b1;   					// Saída de pulso ativo
        count <= 2'b00;  					// Reiniciar o contador
    end else begin
        pulse <= 1'b0;   					// Saída de pulso desativado
        count <= count + 1'b1; 					// Incrementar o contador
    end
end

endmodule



module test_Guia_0904;

    reg clk;
    wire pulse;

    Guia_0904 UUT (
        .clk(clk),
        .pulse(pulse)
    );

    // Geração de clock
    always begin
        #5 clk = ~clk;
    end

    initial begin
        clk = 0;
        $dumpfile("waveform.vcd");
        $dumpvars(0, test_Guia_0904);
        $display("Iniciando simulação...");
        $monitor("Tempo=%t pulse=%b", $time, pulse);

        #100 $finish;
    end

endmodule


