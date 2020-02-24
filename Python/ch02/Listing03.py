import time

def numbers():
    i = 0
    while True:
        yield_here(i) # ................. ①
        i += 1
        time.sleep(1)

def yield_here(i):
    yield(i)

num_generator = numbers()

print(f"[0] {next(num_generator)}") # ... ②
print(f"[1] {next(num_generator)}") # ... ③

for i in num_generator: # ............... ④
    print(f"[Loop] {i}")