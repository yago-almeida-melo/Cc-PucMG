#	Programa 2
#	x=1;;
#	y = 5*x + 15;;

#INICIO

.text
.globl main
main: 
	ori $s0, $zero, 1	#x = 1
	add $t0, $s0, $s0	#t0 = 2x
	add $t1, $t0, $t0	#t1 = 4x
	add $s1, $t1, $s0	#y = 4x + x
	addi $s1, $s1, 15	#y = 5x + 15   
#FIM
