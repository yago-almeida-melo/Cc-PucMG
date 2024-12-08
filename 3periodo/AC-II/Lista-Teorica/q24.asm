.data
x: 0x10010008
y: 32
.text
.globl main
main:
	lui $t0, 0x1001
	lw $a0, 0($t0)
	lw $a1, 4($t0)
	jal vetor
	or $s0, $v0, $0
	j fim
	
vetor: 	or $t1, $a0, $0
	or $t2, $a1, $0
	or $t3, $0, $0
	addi $sp, $sp, -4
	sw $ra, 0($sp)
	or $t3, $0, $0
loop:	beq $t3, $t2, fimLoop
	andi $a0, $t3, 0xFFFF
	jal quadrado
	nop
	sll $t4, $t3, 31
	beq $t4, $0, par
	add $t5, $v0, $v0
	add $t5, $t5, $t3
	add $t5, $t5, $t3
	addi $t5, $t5, 1
	sw $t5, 0($t1)
	addi  $t1, $t1, 4
	addi $t3, $t3, 1
	add $s1, $s1, $t5
	j loop
par:	add $t5, $0, $v0
	sw $t5, 0($t1)
	addi  $t1, $t1, 4
	addi $t3, $t3, 1
	add $s1, $s1, $t5
	j loop
fimLoop:
	lw $ra, 4($sp)
	addi $sp, $sp, 4
	or $v0, $s1, $0
	jr $ra

quadrado:
	mult $a0, $a0
	mflo $v0
	jr $ra
fim:

