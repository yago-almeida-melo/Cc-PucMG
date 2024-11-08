#	Programa 6
#	x=maior inteiro possivel (0x7FFFFFFF);
#	y=300000;
#	z=x-4y

#INICIO

.text
.globl main
main: 
	ori $t0, $zero, 0x7FFF	#t0 = 0x7FFF
	sll $t0, $t0, 16	#t0 = 0x7FFF0000
	ori $s0, $t0, 0xFFFF	#x = 0x7FFFFFFF
	
	ori $t1, $zero, 0x493E	#t1 = 0x493E
	sll $s1, $t1, 4		#y = 0x493E0 
	sll $t2, $t1, 2		#t2 = 0x124f80 (4y)
	
	sub $s2, $s0, $t2	#z = x-t2(x-4y)
	 
#FIM

