/*
	test_Guia_0805.v
	806454 - Yago Almeida Melo
*/

module test_Guia_0805;
    reg [5:0] a;
    wire [5:0] a_comp;

    Guia_0805 uut (
       .a(a),
       .a_comp(a_comp)
    );

    initial begin
      a = 6'b000000;
      repeat (64) begin
        #10;
        $display ("Testando %b a_comp=%b", a, a_comp);
        if (a_comp != (~a + 1)) begin
            $display ("ERROR!");
            $finish;
        end
        a = a + 1;
      end
      $display ("PASSOU!");
      $finish;
    end
endmodule

