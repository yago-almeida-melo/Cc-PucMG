#   Guia_0101.py
#   806454 - Yago Almeida Melo
#

def dec2bin(x):
    j = []
    print(x,end=" (10) = ")
    while x != 0:
        resto = x % 2
        j.append(resto)
        x = x // 2
    j.reverse() 
    for i in j:
        print(i, end="")
    print(" (2)")

