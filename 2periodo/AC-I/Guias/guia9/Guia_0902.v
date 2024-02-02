/*
	Guia_0902.v
	806454 - Yago Almeida Melo
*/

module Guia_0902(
    input wire clk,        // Sinal de clock para temporização
    input wire reset,      // Sinal de reset (opcional)
    output wire pulse_short, // Pulso de saída com período curto
    output wire pulse_long   // Pulso de saída com período longo
);

reg [31:0] count_short;     // Registrador de contagem para período curto
reg [31:0] count_long;      // Registrador de contagem para período longo

always @(posedge clk or posedge reset) begin
    if (reset) begin
        count_short <= 32'b0; // Reiniciar a contagem em caso de reset
        count_long <= 32'b0;  // Reiniciar a contagem em caso de reset
    end else begin
        if (count_short < 32'd5) begin // Período curto de 5 unidades de tempo
            count_short <= count_short + 1; // Contagem para determinar o pulso curto
        end
        if (count_long < 32'd20) begin // Período longo de 20 unidades de tempo
            count_long <= count_long + 1; // Contagem para determinar o pulso longo
        end
    end
end

assign pulse_short = (count_short == 32'd4); // Gerar o pulso curto
assign pulse_long = (count_long == 32'd19);   // Gerar o pulso longo

endmodule

// TESTE DE PULSO

module PulseGeneratorShort(
    input wire clk,        // Sinal de clock para temporização
    input wire reset,      // Sinal de reset (opcional)
    output wire pulse      // Pulso de saída com período curto
);

reg [31:0] count;          // Registrador de contagem para temporização

always @(posedge clk or posedge reset) begin
    if (reset) begin
        count <= 32'b0;   // Reiniciar a contagem em caso de reset
    end else begin
        if (count < 32'd5) begin // Período curto de 5 unidades de tempo
            count <= count + 1; // Contagem para determinar o pulso
        end
    end
end

assign pulse = (count == 32'd4); // Gerar o pulso

endmodule

module PulseGeneratorLong(
    input wire clk,        // Sinal de clock para temporização
    input wire reset,      // Sinal de reset (opcional)
    output wire pulse      // Pulso de saída com período longo
);

reg [31:0] count;          // Registrador de contagem para temporização

always @(posedge clk or posedge reset) begin
    if (reset) begin
        count <= 32'b0;   // Reiniciar a contagem em caso de reset
    end else begin
        if (count < 32'd20) begin // Período longo de 20 unidades de tempo
            count <= count + 1; // Contagem para determinar o pulso
        end
    end
end

assign pulse = (count == 32'd19); // Gerar o pulso

endmodule


