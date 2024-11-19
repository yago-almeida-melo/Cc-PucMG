#	Programa 7
#	ori $8, $0, 0x01
#	$8 = 0xFFFFFFFF

#INICIO

.text
.globl main
main: 
	ori $8, $0, 0x01	#$8 = 1
	sll $8, $8, 31
	sra $8, $8, 31
#FIM
