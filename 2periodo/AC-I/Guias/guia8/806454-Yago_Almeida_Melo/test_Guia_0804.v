/*
	test_Guia_0804.v
	806454 - Yago Almeida Melo
*/

module test_Guia_0804;
    reg [5:0] a, b;
    wire a_neq_b;

    Guia_0804 uut (
       .a(a),
       .b(b),
       .a_neq_b(a_neq_b)
    );

    initial begin
      a = 6'b000000;
      repeat (64) begin
        b = 0;
        repeat (64) begin
          #10;
          $display ("Testando %b and %b a_neq_b=%b", a, b, a_neq_b);
          if (a!=b && a_neq_b!=1'b1) begin
            $display ("ERROR!");
            $finish;
          end
          if (a==b && a_neq_b!=1'b0) begin
            $display ("ERROR!");
            $finish;
          end
          b = b + 1;
        end
        a = a + 1;
      end
      $display ("PASSOU!");
      $finish;
    end
endmodule

