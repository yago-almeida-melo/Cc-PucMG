#	Programa 14
#	Escreva um programa que leia um valor da memoria e identifique se é par ou não
#	Devera ser escrito na segunda posição da memoria:
#  	0 para par ou 1 para impar

#INICIO

.data
A: .word 9
	
.text
.globl main
main: 
	#t0 -> first address
		
	#A -> $s0
	#isEven -> $s1
	
	ori $t0, $0, 0x1001	#t0 = 0x1001
	sll $t0, $t0, 16	#t0 = 0x10010000
	lw $s0, 0($t0) 		#s0 = A
	andi $t1, $s0, 1	#t1 = s0 & 0x00000001
	bne $t1, $0, else	#if(t1!=0){ goto else; } 
if:	and $s1, $0, $0		#s1 = 0 & 0
	j fim			#goto fim
else:	or $s1, $0, 1		#s1 = 0 | 1
fim:	sw $s1, 4($t0)		#MEM[4+t0] = s1

#FIM
