.data 
array: .word
.text
.globl main
main: 
	lui $s1, 0x1001
	ori $t0, $s1, 0
	ori $t1, $0, 100
if:	beq $t1, $0, fim
	nop
	lw $t2, 0($t0)
	add $s2, $t2, $s2
	addi $t0, $t0, 4
	addi $t1, $t1, -1
	nop
	j if
fim:	sw $s2, 0($t0)