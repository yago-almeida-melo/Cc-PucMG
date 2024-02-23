/*
	Guia_0304.v
	806454 - Yago Almeida Melo
*/
module subtraction_and_display;
 reg [4:0] binary_a, binary_b;
 reg [3:0] frac_binary_a, frac_binary_b;
 reg [8:0] base4_a, base4_b;
 reg [8:0] base8_a, base8_b;
 reg [11:0] base16_a, base16_b;
 //resultados
 reg [4:0] binary_result;
 reg [7:0] frac_binary_result;
 reg [5:0] base4_result;
 reg [8:0] base8_result;
 reg [11:0] base16_result;
//actions
 initial 
  begin
   $display ( "Guia_0304 - Subtracao" );
   // Binary subtraction
   binary_a = 5'b11101;
   binary_b = 5'b01011;
   binary_result = binary_a - binary_b;
   $display("a.) %b - %b = %b", binary_a, binary_b, binary_result);
   // Fractional binary 
   binary_a = 3'b101;
   binary_b = 3'b010;
   frac_binary_a = 4'b0101;
   frac_binary_b = 4'b1100;
   binary_result = binary_a - binary_b;
   frac_binary_result = frac_binary_a - frac_binary_b;
   $display("b.) %b,%b - %b,%b = %b,%b", binary_a, frac_binary_a, binary_b, frac_binary_b, binary_result, frac_binary_result);
   // Base 4 
   base4_a = 8'o321;
   base4_b = 8'o213;
   base4_result = base4_a - base4_b;
   $display("c.) %o - %o = %o%o", base4_a, base4_b, base4_result[5:4], base4_result[3:2]);
   // Base 8
   base8_a = 9'o461;
   base8_b = 9'o247;
   base8_result = base8_a - base8_b;
   $display("d.) %o - %o = %o", base8_a, base8_b, base8_result);
   // Base 16 
   base16_a = 12'h7c4;
   base16_b = 12'hb1d;
   base16_result = base16_a - base16_b;
   $display("e.) %h - %h = %h", base16_a, base16_b, base16_result);
  end
 endmodule

