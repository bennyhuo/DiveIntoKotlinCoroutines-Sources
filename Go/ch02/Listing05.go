package main

import (
	"fmt"
	"time"
)

func main() {
	channel := make(chan int) // .......... ①
	var readChannel <-chan int = channel
	var writeChannel chan<- int = channel
	
	// reader
	go func() { // ........................ ②
		fmt.Println("wait for read")
		for i := range readChannel { // ... ③
			fmt.Println("read", i)
		}
		fmt.Println("read end")
	}()  // ............................... ④
	
	
	// writer
	go func() {
		for i := 0; i < 3; i++{
			fmt.Println("write", i)
			writeChannel <- i // .......... ⑤
			time.Sleep(time.Second)
		}
		close(writeChannel)
	}()

	time.Sleep(time.Second * 5)
}