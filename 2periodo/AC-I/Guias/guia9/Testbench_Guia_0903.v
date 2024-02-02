/*
	Testbench_Guia_0903.v
	806454 - Yago Almeida Melo
*/

module Testbench_Guia_0903;

reg clk;
reg reset;
wire pulse;

// Instanciar o módulo Guia_0903
Guia_0903 pulse_gen (
    .clk(clk),
    .reset(reset),
    .pulse(pulse)
);

// Gerar um sinal de clock
always begin
    #5 clk = ~clk;
end

// Teste simples
initial begin
    clk = 0;
    reset = 0;
    
    // Adicione comandos de teste conforme necessário
    
    // Exemplo: Ativar o reset por um curto período
    #50 reset = 1;
    #10 reset = 0;
    
    // Adicione mais comandos de teste conforme necessário
    
    $finish; // Terminar a simulação
end

endmodule

