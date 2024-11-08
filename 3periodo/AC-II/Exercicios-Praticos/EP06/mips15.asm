#	Programa 15
#	Crie um vetor de 100 elementos, onde v[i] = 2*1+1
#	Apos a ultima posicao, escrever a soma de todos os valores armazenados no vetor

#INICIO

.data
.text
.globl main
main: 
	#t0 -> first address
	#t1 -> offset
	
	#v[i] -> $t4
	#soma -> s1
	#i -> $t2   (0)
	#tam -> $t3 (100)
	
	ori $t0, $0, 0x1001	#t0 = 0x1001
	sll $t0, $t0, 16	#t0 = 0x10010000
	or $t1, $t0, $0		#t1 = t0
	or $t2, $0, $0		#i = 0
	ori $t3, $0, 100	#tam = 100
	
if:	
	beq $t2, $t3, end	#if(t2==t3){ goto end; }
	add $t4, $t2, $t2 	#t4 = 2*1 
	addi $t4, $t4, 1	#t4 = 2*i + 1 
	add $s0, $s0, $t4	#soma = soma + t4
	sw $t4, 0($t1)		#MEM[$t1] = t4
	addi $t1, $t1, 4	#t1 = t1 + 4 
	addi $t2, $t2, 1	#i++
	j if			#goto if
end:	
	sw $s0, 0($t1)		#MEM[$t1] = s0

#FIM
