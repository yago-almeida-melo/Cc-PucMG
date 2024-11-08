#	Programa 9
#	Escrever um programa que leia todos os números, 
#	calcule e substitua o valor da variável soma por este valor.

#INICIO

.data
x1: .word 15
x2: .word 25
x3: .word 13
x4: .word 17
soma: .word -1
	
.text
.globl main
main: 
	#t0 -> first address
	#t1 -> offset
	
	ori $t0, $0, 0x1001	#t0 = 0x1001
	sll $t0, $t0, 16	#t0 = 0x10010000
	
	lw $s0, ($t0)	#s0 = x1
	lw $s1, 4($t0)	#s1 = x2
	lw $s2, 8($t0)	#s2 = x3
	lw $s3, 12($t0)	#s3 = x4
	
	add $s4, $s0, $s1 	#s4 = x1 + x2
	add $s4, $s4, $s2	#s4 = s4 + x3
	add $s4, $s4, $s3 	#s4 = s4 + x4
	
	sw $s4, 16($t0)		#MEM[10010016] = soma (s4)
	
#FIM
