package main

import (
	"fmt"
)

// Calcula a área de um retângulo
func calculaAreaRetangulo(altura, largura float64) float64 {
	return altura * largura
}

// Exibe o resultado da área do retângulo
func exibeResultado(area float64) {
	fmt.Printf("Área do retângulo: %.2f\n", area)
}

// MAIN
func main() {
	altura := 10.0
	largura := 5.0
	area := calculaAreaRetangulo(altura, largura)
	exibeResultado(area)
}
