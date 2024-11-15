package main

import (
	"fmt"
	"time"
)

/*
	Uma goroutine é uma função que pode ser executada
	de forma concorrente com outras goroutines.
	Para criar uma goroutine basta adicionar
	a palavra chave go à frente da função invocada.
*/

// Função que simula uma tarefa, recebendo um id e uma duração
func function(id int, t time.Duration, done chan bool) {
	for i := 0; i < 10; i++ {
		time.Sleep(t)
		fmt.Printf("Function %02d: %d\n", id, i)
	}
	// Envia um sinal para o canal indicando que terminou
	done <- true
}

func main() {
	fmt.Println("Starting...")

	//Canal de sincronização
	done := make(chan bool, 3)

	/* Inicializa as goroutines e
	passa o canal como argumento*/
	go function(1, time.Second*1, done)
	go function(2, time.Second*1, done)
	go function(3, time.Second*1, done)

	/* Espera o sinal de cada goroutine
	para saber que todas terminaram*/
	for i := 0; i < 3; i++ {
		<-done
	}

	fmt.Println("Ending...")
}
