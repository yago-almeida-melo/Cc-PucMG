package main

import "fmt"

// Função que altera o valor de uma variável através de um ponteiro
func alterarValor(ptr *int) {
	*ptr = *ptr * 2 // Dereferencia o ponteiro e modifica o valor
}

func main() {
	numero := 10
	fmt.Println("Antes de alterar:", numero)

	// Passa o endereço de 'numero' para a função
	alterarValor(&numero)

	fmt.Println("Depois de alterar:", numero)
}
