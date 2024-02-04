#   Guia_0103.py
#   806454 - Yago Almeida Melo
#

#Função que passa de binario para base 4
def dec10quat(x):
    quat = ''
    while x > 0:
        quat = str(x % 4) + quat
        x //= 4
    return quat


#Main

def bin2dec():
    while True:
        entrada = int(input("Digite um número (digite um número não-positivo para sair): "))
        if entrada <= 0:
            break

        base = input("Digite a base (4, 8, 16): ")
        if base == '4':
            print(dec10quat(entrada))
        elif base == '8':
            print(oct(entrada))
        elif base == '16':
            print(hex(entrada))
        else:
            print("Base inválida. Por favor, escolha entre 4, 8 ou 16.")

#chamada da main
bin2dec()