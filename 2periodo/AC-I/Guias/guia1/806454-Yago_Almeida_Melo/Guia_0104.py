#   Guia_0104.py
#   806454 - Yago Almeida Melo
#

#Função que passa de binario para base 4
def bin2quat(x):
    quat = ''
    while x > 0:
        quat = str(x % 4) + quat
        x //= 4
    return quat


#Main

def main():
    while True:
        entrada = input("Digite um numero binario (digite == 0 ou < 0 para sair): ")
        if int(entrada) <= 0:
            break
        entrada = int(entrada,2)
        base = input("Digite a base (4, 8, 16): ")
        if base == '4':
            print(bin2quat(entrada))
        elif base == '8':
            print(oct(entrada))
        elif base == '16':
            print(hex(entrada))
        else:
            print("Base inválida. Por favor, escolha entre 4, 8 ou 16.")

#chamada da main
main()