#	Programa 17
#	k = x * y
#	x sera lido da primeira posicao, o y da segunda, o k sera resscrito na terceira

#INICIO

.data
x: .word 9
y: .word 5
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
	
	and $t1, $0, $0		#t1 = 0
if:	beq $t1, $s1, fim	#if(t1==s1){ goto fim }
	add $s2, $s2, $s0	#t2 = t2 + s0
	addi $t1, $t1, 1	#t1 = t1++
	j if			#goto if
fim:	
	sw $s2, 8($t0)		#MEM[$t0+8] = s2

#FIM
