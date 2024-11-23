.data
array: .word 100, 99, 98, 97, 96, 95, 94, 93, 92, 91, 90, 89, 88, 87, 86, 85, 84, 83, 82, 81, 80, 79, 78, 77, 76, 75, 74, 73, 72, 71, 70, 69, 68, 67, 66, 65, 64, 63, 62, 61, 60, 59, 58, 57, 56, 55, 54, 53, 52, 51, 50, 49, 48, 47, 46, 45, 44, 43, 42, 41, 40, 39, 38, 37, 36, 35, 34, 33, 32, 31, 30, 29, 28, 27, 26, 25, 24, 23, 22, 21, 20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1
.text
.globl main
main:
	lui $t0, 0x1001		    #t0 = 0x10010000
	ori $s3, $0, 100  	    #size = 100
	ori $t2, $0, 0		    #i = 0
outter:	ori $t3, $0, 0		#j = 0
	slt $t4, $t2, $s3	    #t4 = (i<size)? 1  : 0
	beq $t4, $0, fimOutter	#if(t4==0){goto fimOutter}
inner:	
    sub $t5, $s3, $t2	    #t5 = size - i
	addi $t5, $t5, -1	    #t5 = t5 - 1
	slt $t6, $t3, $t5	    #t6 = (j == t5)? 1  : 0
	beq $t6, $0, imais	    #if(t6==0){goto imais} 
	sll $t1, $t3, 2		    #t1 = j << 2
	add $t1, $t1, $t0	    #t1 = t1 + 0x10010000
	lw $s1, 0($t1)		    #s1 = MEM[t1]
	lw $s2, 4($t1)		    #s2 = MEM[4+t1]
	slt $t7, $s2, $s1	    #t7 =(s2<s1)? 1 : 0
	beq $t7, $0, jmais	    #if(t7==0){goto jmais}
	or $s0, $s1, $0		    #temp = s1
	sw $s2,	0($t1)		    #MEM[t1] = s2
	sw $s0, 4($t1)		    #MEM[4+t1] = temp
jmais: 	
    addi $t3, $t3, 1
	j inner	
imais: 	
    addi $t2, $t2, 1
	j outter
fimOutter:
	
	
	
	
	
	
