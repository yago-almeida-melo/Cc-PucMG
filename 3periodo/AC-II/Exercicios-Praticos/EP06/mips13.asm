#	Programa 13
#	Escreva um programa que leia um valor A da memória, identifique se é negativo ou não e encontre seu modulo
#	O valor deve ser reescrito sobre A

#INICIO

.data
A: .word -9
	
.text
.globl main
main: 
	#t0 -> first address
		
	#A -> $s0
	
	ori $t0, $0, 0x1001	#t0 = 0x1001
	sll $t0, $t0, 16	#t0 = 0x10010000
	
	lw $s0, 0($t0) 		#s0 = A
	sra $t1, $s0, 31	#t1 = s0 >> 31 (mantem o bit de sinal)
	beq $t1, $0, positivo	#if(t1==0){ goto positivo; } 
	sub $s0, $0, $s0	#s0 = 0 - s0
positivo:
	sw $s0, 0($t0)		#MEM[t0] = s0


#FIM
