package main

import (
	"fmt"
	"golang.org/x/sys/windows"
	"time"
)

func main() {
	channel := make(chan int)
	var readChannel <-chan int = channel
	var writeChannel chan<- int = channel
	go func() {
		fmt.Println(windows.GetCurrentThreadId(), "wait for read")
		for i := range readChannel {
			fmt.Println(windows.GetCurrentThreadId(), "read", i)
		}
		fmt.Println(windows.GetCurrentThreadId(), "read end")
	}()
	go func() {
		for i := 0; i < 3; i++{
			fmt.Println(windows.GetCurrentThreadId(), "write", i)
			writeChannel <- i
		}
		close(writeChannel)
	}()

	time.Sleep(time.Second * 5)
}