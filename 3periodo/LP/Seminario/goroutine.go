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

func function(id int, t time.Duration) {
	for i := 0; i < 10; i++ {
		time.Sleep(t)
		fmt.Printf("Function %02d: %d\n", id, i)
	}
}

func main() {
	fmt.Println("Starting...")

	go function(1, time.Second*1)
	go function(2, time.Second*1)
	go function(3, time.Second*1)

	time.Sleep(time.Second * 5)
	fmt.Println("Ending...")
}
