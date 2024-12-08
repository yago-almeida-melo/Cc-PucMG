.data
x: 2
y: 4
.text
.globl main
main: 
	lui $t0, 0x1001
	lw $a0, 0($t0)
	lw $a1, 4($t0)
	jal potencia
	or $s0, $0, $v0
	sw $s0, 8($t0)
	j fim
potencia:
	addi $t1, $a1, -2
	mult $a0, $a0
	mflo $t2
loop: 	mult $t2, $a0
	mflo $t2
	addi $t1, $t1, -1
	bne $t1, $0, loop
	add $v0, $0, $t2
	jr $ra
fim: