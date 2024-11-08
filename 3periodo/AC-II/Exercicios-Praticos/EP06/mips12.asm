#	Programa 12
#	Considere int ***x; 
#	A primeira posição da memória é do int, coloque os outros valores em reg's e use endereços de memória 
#	O programa deve ler o valor k, multiplicar por 2 e reescrever no local da memoria

#INICIO

.data
k: .word 5
a: .word 0x10010000
b: .word 0x10010004
c: .word 0x10010008
	
.text
.globl main
main: 
	#t0 -> first address
		
	#k -> $s0				
	#a -> $s1
	#b -> $s2
	#c -> $s3
	
	ori $t0, $0, 0x1001	#t0 = 0x1001
	sll $t0, $t0, 16	#t0 = 0x10010000
	
	lw $s0, 12($t0)	#s0 = c
	lw $s1, 0($s0)	#s1 = b
	lw $s2, 0($s1)	#s2 = a
	lw $s3, 0($s2)	#s3 = k
	
	sll $s3, $s3, 1	#s3 = 2*k
	
	sw $s3, 0($s2)	# ***c = (***c) * 2
	
#FIM
