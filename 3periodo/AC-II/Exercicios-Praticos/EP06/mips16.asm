#	Programa 16
#	Avalie a expressao (x*y)/z
#	Use x=1600000(0x186A00), y=80000(0x13880) e z = 400000(0x61A80), inicialize nos regs

#INICIO

.data
.text
.globl main
main: 
	#x -> s0
	#y -> s1
	#z -> s2
	
	ori $s0, $0, 0x186A
	sll $s0, $s0, 4
	ori $s1, $0, 0x1388
	sll $s1, $s1, 4
	ori $s2, $0, 0x61A8
	sll $s2, $s2, 4
	
	and $t0, $0, $0
if:	beq $t0, $s1, fim
	add $t0, $t0, $s0
	addi $t0, $t0, 1
	j if	
fim:

#FIM
