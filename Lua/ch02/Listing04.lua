function producer()
    for i = 0, 3 do
        print("send "..i)
        coroutine.yield(i)
    end
    print("End Producer")
end

function consumer(value)
    repeat
        print("receive "..value)
        value = coroutine.yield()
    until(not value)
    print("End Consumer")
end

producerCoroutine = coroutine.create(producer)
consumerCoroutine = coroutine.create(consumer)


repeat
    status, product = coroutine.resume(producerCoroutine)
    coroutine.resume(consumerCoroutine, product)
until(not status)
print("End Main")


