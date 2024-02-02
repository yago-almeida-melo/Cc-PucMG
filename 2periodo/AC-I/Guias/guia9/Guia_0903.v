/*
	Guia_0903.v
	806454 - Yago Almeida Melo	
	
*/

module Guia_0903(
    input wire clk,        // Sinal de clock de entrada
    input wire reset,      // Sinal de reset (opcional)
    output wire pulse      // Pulso de saída com frequência um terço do gerador Guia_0900.v
);

reg [31:0] count;          // Registrador de contagem para temporização

parameter PERIOD = 30;    // Período de 30 unidades de tempo (um terço do gerador Guia_0900.v)

always @(posedge clk or posedge reset) begin
    if (reset) begin
        count <= 32'b0;   // Reiniciar a contagem em caso de reset
    end else begin
        if (count < PERIOD - 1) begin
            count <= count + 1; // Contagem para determinar o pulso
        end else begin
            count <= 0;       // Reiniciar a contagem ao atingir o período
        end
    end
end

assign pulse = (count == (PERIOD - 1) / 2); // Gerar o pulso no meio do período

endmodule

