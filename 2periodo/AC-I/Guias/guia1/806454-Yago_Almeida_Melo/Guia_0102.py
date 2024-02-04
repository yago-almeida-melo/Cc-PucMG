#   Guia_0102.py
#   806454 - Yago Almeida Melo
#

def bin2dec(x):
    res = 0
    tam = len(x)
    for i in range(tam):
        if x[i] == "1":
            res = res + 2**(tam-i-1)
        else:
            pass
    print(int(res))
