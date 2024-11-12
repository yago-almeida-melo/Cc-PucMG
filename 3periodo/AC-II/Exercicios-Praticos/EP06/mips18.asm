#	Programa 18
#	k = x^y
#	x sera lido da primeira posicao, o y da segunda, o k sera resscrito na terceira

#INICIO

.data
x: .word 2
y: .word 3
.text
.globl main
main: 
	#t0 -> 0x10010000 (first position)
	#x -> s0
	#y -> s1
	#k -> s2
	
	ori $t0, $0, 0x1001	#t0 = 0x1001 
	sll $t0, $t0 16		#t0 = 0x10010000
	
	lw $s0, 0($t0)		#s0 = MEM[$t0]
	lw $s1, 4($t0)		#s1 = MEM[$t0+4]
	
	ori $s2, $0, 1		#s2 = 1
	or $t2, $0, $s1		#t2 = y
	
while:	beq $t2, $0, fim	#if(t2==0){ goto fim }
	mul $s2, $s2, $s0	#s2 = s2 * s0
	addi $t2, $t2, -1	#t2 = t2-1
	j while			#goto while
fim:	
	sw $s2, 8($t0)		#MEM[$t0+8] = s2

#FIM
