.data
.text
.globl main
	main:
	lui $s0, 0x1001
	ori $s0, $s0, 0x0008
	ori $s1, $0, 5
	add $a0, $0, $s0
	add $a1, $0, $s1
	jal soma
	or $s3, $v0, $0
	
	lui $v0, 0
	ori $v0, $v0, 10
	syscall


soma: 	or $v0, $0, $0
	or $s2, $0, $0
	or $t1, $0, $a0
loop:	beq $s2, $a1, fim
	sll $t0, $s2, 31
	beq $t0, $0, par
	sw $s2, 0($t1)
	add $v0, $v0, $s2
	addi $t1, $t1, 4
	addi $s2, $s2, 1
	j loop
par:	add $t2, $s2, $s2
	addi $t2, $t2, -1
	sw $t2, 0($t1)	
	add $v0, $v0, $t2
	addi $t1, $t1, 4
	addi $s2, $s2, 1 
	j loop	
fim:	jr $ra	
	
 
	