import tkinter as tk
from tkinter import messagebox


def dda(x1, y1, x2, y2):
    """
    Algoritmo DDA para calcular os pontos de uma reta.
    Retorna uma lista contendo os pontos (x, y).
    """

    dx = x2 - x1
    dy = y2 - y1

    # Número de passos é definido pela maior variação
    passos = max(abs(dx), abs(dy))

    # Caso os dois pontos sejam iguais
    if passos == 0:
        return [(x1, y1)]

    incremento_x = dx / passos
    incremento_y = dy / passos

    x = x1
    y = y1

    pontos = []

    for _ in range(passos + 1):
        pontos.append((round(x), round(y)))

        x += incremento_x
        y += incremento_y

    return pontos


def desenhar_reta():
    try:
        x1 = int(entrada_x1.get())
        y1 = int(entrada_y1.get())
        x2 = int(entrada_x2.get())
        y2 = int(entrada_y2.get())

    except ValueError:
        messagebox.showerror(
            "Erro",
            "Digite apenas valores inteiros."
        )
        return

    # Limpa o Canvas
    canvas.delete("all")

    # Calcula os pontos utilizando DDA
    pontos = dda(x1, y1, x2, y2)

    # Desenha cada ponto da reta
    for x, y in pontos:
        canvas.create_rectangle(
            x,
            y,
            x + 1,
            y + 1,
            fill="black",
            outline="black"
        )

    # Marca o ponto inicial
    canvas.create_oval(
        x1 - 3,
        y1 - 3,
        x1 + 3,
        y1 + 3,
        fill="green"
    )

    # Marca o ponto final
    canvas.create_oval(
        x2 - 3,
        y2 - 3,
        x2 + 3,
        y2 + 3,
        fill="red"
    )

    label_resultado.config(
        text=f"dx = {x2 - x1} | "
             f"dy = {y2 - y1} | "
             f"Pontos gerados = {len(pontos)}"
    )


def limpar():
    canvas.delete("all")

    entrada_x1.delete(0, tk.END)
    entrada_y1.delete(0, tk.END)
    entrada_x2.delete(0, tk.END)
    entrada_y2.delete(0, tk.END)

    label_resultado.config(text="")


# =====================================
# Janela principal
# =====================================

janela = tk.Tk()
janela.title("Algoritmo DDA")
janela.geometry("800x600")
janela.resizable(False, False)


# =====================================
# Área de entrada
# =====================================

frame_controles = tk.Frame(janela)
frame_controles.pack(pady=10)


tk.Label(frame_controles, text="X1:").grid(
    row=0, column=0, padx=5
)

entrada_x1 = tk.Entry(frame_controles, width=8)
entrada_x1.grid(row=0, column=1, padx=5)


tk.Label(frame_controles, text="Y1:").grid(
    row=0, column=2, padx=5
)

entrada_y1 = tk.Entry(frame_controles, width=8)
entrada_y1.grid(row=0, column=3, padx=5)


tk.Label(frame_controles, text="X2:").grid(
    row=0, column=4, padx=5
)

entrada_x2 = tk.Entry(frame_controles, width=8)
entrada_x2.grid(row=0, column=5, padx=5)


tk.Label(frame_controles, text="Y2:").grid(
    row=0, column=6, padx=5
)

entrada_y2 = tk.Entry(frame_controles, width=8)
entrada_y2.grid(row=0, column=7, padx=5)


# =====================================
# Botões
# =====================================

botao_desenhar = tk.Button(
    frame_controles,
    text="Desenhar",
    command=desenhar_reta
)

botao_desenhar.grid(
    row=0,
    column=8,
    padx=10
)


botao_limpar = tk.Button(
    frame_controles,
    text="Limpar",
    command=limpar
)

botao_limpar.grid(
    row=0,
    column=9,
    padx=5
)


# =====================================
# Informações
# =====================================

label_resultado = tk.Label(
    janela,
    text="",
    font=("Arial", 10)
)

label_resultado.pack(pady=5)


# =====================================
# Canvas
# =====================================

canvas = tk.Canvas(
    janela,
    width=750,
    height=480,
    bg="white",
    highlightthickness=1,
    highlightbackground="black"
)

canvas.pack(pady=10)


# =====================================
# Valores iniciais para teste
# =====================================

entrada_x1.insert(0, "100")
entrada_y1.insert(0, "100")

entrada_x2.insert(0, "600")
entrada_y2.insert(0, "400")


# =====================================
# Executa a aplicação
# =====================================

janela.mainloop()