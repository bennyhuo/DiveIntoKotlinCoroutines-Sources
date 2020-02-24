coro = {}
coro.main = function()

end
coro.current = coro.main

function coro.create(f)
    local co = function(val)
        f(val)
        error("Coroutine ended")
    end

    return coroutine.create(co)
end

function coro.transfer(co, ...)
    parameters = table.unpack({ ... })
    while co do
        if coro.current == coro.main then
            if co == coro.main then
                return
            end
            coro.current = co
            results = { coroutine.resume(co, parameters) }
            status, co = table.unpack(results, 1, 2)
            parameters = table.unpack(results, 3)
            coro.current = coro.main
        else
            coro.current = coro.main
            return coroutine.yield(co, ...)
        end
    end
end

co1 = coro.create(function()
    print("1")
    coro.transfer(coro.main)
    print("1 End")
end)

co2 = coro.create(function()
    print("2")
    coro.transfer(co1)
    print("2 End")
end)

co3 = coro.create(function(...)
    print("3")
    coro.transfer(co2)
    print("3 End")
end)

print("start")
coro.transfer(co3)
print("End")