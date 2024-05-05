/*
    Guia_1201
    806454 - Yago Almeida Melo
*/

module jkff ( output q, output qnot,
    input j, input k,
    input clk, input preset, input clear );

    reg q, qnot;
    always @( posedge clk or posedge preset or posedge clear ) begin
        if ( clear ) begin
            q <= 0; qnot <= 1;
        end
        else if ( preset ) begin
            q <= 1;     
            qnot <= 0; 
        end
        else if ( j & ~k ) begin 
            q <= 1;
            qnot <= 0; 
        end
        else if ( ~j & k ) begin 
            q <= 0; 
            qnot <= 1; 
        end
        else if ( j & k ) begin 
            q <= ~q; 
            qnot <= ~qnot; 
        end
    end
endmodule // jkff

module memoria_RAM_1x4(
    input wire clk,         
    input wire reset,       
    input wire endereco,    
    input wire dado_in,     
    output reg [3:0] dado_out 
);

wire [3:0] q;
wire [3:0] qnot;
wire j, k;

generate
    genvar i;
    for (i = 0; i < 4; i = i + 1) begin : jk_loop
        jkff jk_inst (
            .q(q[i]),
            .qnot(qnot[i]),
            .j(j),
            .k(k),
            .clk(clk),
            .preset(1'b0),
            .clear(1'b0)
        );
    end
endgenerate

always @(posedge clk) begin
    if (reset) begin
        dado_out <= 4'b0;
    end else if (endereco) begin
        dado_out <= dado_in;
    end
end

always @(negedge clk) begin
    if (!reset && !endereco) begin
        dado_out <= dado_out;
    end
end

endmodule

module test_brench;
parameter PERIOD = 10; 

// Sinais
reg clk = 0;       
reg reset = 1;      
reg endereco = 0;   
reg dado_in = 4'b0; 

memoria_RAM_1x4 memoria_ram (
    .clk(clk),
    .reset(reset),
    .endereco(endereco),
    .dado_in(dado_in),
    .dado_out()
);

always #((PERIOD/2)) clk = ~clk;

initial begin
    $display("Inicializando teste...");
    $display("Escrevendo na memória...");
    dado_in = 4'b1010; 
    endereco = 1;      
    #20;               

    $display("Lendo da memória...");
    endereco = 0;      
    #20;               

    $display("Teste finalizado.");
    $finish;
end
endmodule