package main

import (
	"fmt"
	"time"
)

// Função que simula uma tarefa
func function(id int, t time.Duration) {
	for i := 0; i < 5; i++ {
		time.Sleep(t)
		fmt.Printf("Function %02d: %d\n", id, i)
	}
}

func main() {
	fmt.Println("Iniciando comparação...")

	// Medindo tempo sem concorrência
	startNoConcurrency := time.Now()
	function(1, time.Millisecond*500)
	function(2, time.Millisecond*500)
	function(3, time.Millisecond*500)
	durationNoConcurrency := time.Since(startNoConcurrency)

	fmt.Printf("\nTempo sem concorrência: %v\n\n", durationNoConcurrency)

	// Medindo tempo com concorrência
	startConcurrency := time.Now()
	done := make(chan bool, 3)

	go func() {
		function(1, time.Millisecond*500)
		done <- true
	}()
	go func() {
		function(2, time.Millisecond*500)
		done <- true
	}()
	go func() {
		function(3, time.Millisecond*500)
		done <- true
	}()

	// Aguardando goroutines terminarem
	for i := 0; i < 3; i++ {
		<-done
	}
	durationConcurrency := time.Since(startConcurrency)

	fmt.Printf("\nTempo com concorrência: %v\n", durationConcurrency)

	// Exibindo comparação final
	fmt.Println("\nComparação concluída.")
}
