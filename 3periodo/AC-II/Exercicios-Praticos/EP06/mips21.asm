#	Programa 21
#	Ler x da primeira pos da memoria, se x>0 y = x⁴ + x³ - 2x²
# 	se x<=0 y = x⁵ - x³ + 1, escrever y na segunda pos da memoria
#INICIO
.data
x: .word -3
.text
.globl main
main: 
	ori $t0, $0, 0x1001	#t0 -> 0x10010000 (first position)
	sll $t0, $t0, 16	#t0 << 16
	
	lw $s0, 0($t0)		#s0 = MEM[$t0]
	mult $s0, $s0		#s0 * s0
	mflo $t2		#t2 = x²
	mult $t2, $s0		#t2 * s0
	mflo $t3		#t3 = x³
	mult $t3, $s0		#t3 * s0
	mflo $t4		#t4 = x⁴
	sle $t1, $s0, $0 	#ifs0 <=0){ t1 = 1 } else { t1 = 0 }
	bne $t1, $0, maior	#if(t1!=0){ goto impar }
	addi $s1, $t3, 1	#s1 = t3 + 1
	j fim			#goto fim
maior: 	addi $s1, $t4, -1	#s1 = t4 - 1
fim:	sw $s1, 4($t0)		#MEM[4+$t0] = s1

#FIM 
