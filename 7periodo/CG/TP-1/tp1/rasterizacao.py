"""Algoritmos clássicos de rasterização implementados sem atalhos gráficos."""

from __future__ import annotations

from collections.abc import Callable
from math import hypot

from modelos import AlgoritmoReta, ObjetoGrafico, Ponto, TipoObjeto

Pixel = tuple[int, int]
PlotarPixel = Callable[[int, int], None]


def reta_dda(inicio: Ponto, fim: Ponto, plotar_pixel: PlotarPixel) -> None:
    """Plota cada pixel da reta assim que o DDA calcula suas coordenadas."""

    x0, y0 = inicio.arredondado()
    x1, y1 = fim.arredondado()
    dx, dy = x1 - x0, y1 - y0
    passos = int(max(abs(dx), abs(dy)))
    if passos == 0:
        plotar_pixel(x0, y0)
        return

    incremento_x = dx / passos
    incremento_y = dy / passos
    ultimo_pixel: Pixel | None = None
    x, y = x0, y0
    for _ in range(passos + 1):
        pixel = (round(x), round(y))
        if pixel != ultimo_pixel:
            plotar_pixel(*pixel)
            ultimo_pixel = pixel
        x += incremento_x
        y += incremento_y


def reta_bresenham(inicio: Ponto, fim: Ponto, plotar_pixel: PlotarPixel) -> None:
    """Plota uma reta em qualquer octante usando apenas passos inteiros."""

    x0, y0 = inicio.arredondado()
    x1, y1 = fim.arredondado()
    dx = abs(x1 - x0)
    sx = 1 if x0 < x1 else -1
    dy = -abs(y1 - y0)
    sy = 1 if y0 < y1 else -1
    erro = dx + dy

    while True:
        plotar_pixel(x0, y0)
        if x0 == x1 and y0 == y1:
            break
        erro_duplo = 2 * erro
        if erro_duplo >= dy:
            erro += dy
            x0 += sx
        if erro_duplo <= dx:
            erro += dx
            y0 += sy


def circunferencia_bresenham(
    centro: Ponto, ponto_raio: Ponto, plotar_pixel: PlotarPixel
) -> None:
    """Plota os pixels simétricos a cada passo do ponto médio/Bresenham."""

    cx, cy = centro.arredondado()
    raio = max(1, round(hypot(ponto_raio.x - centro.x, ponto_raio.y - centro.y)))
    x, y = 0, raio
    decisao = 3 - 2 * raio

    while x <= y:
        plotar_pixel(cx + x, cy + y)
        plotar_pixel(cx + x, cy - y)
        if x != 0:
            plotar_pixel(cx - x, cy + y)
            plotar_pixel(cx - x, cy - y)
        # Nos eixos e nas diagonais, algumas simetrias coincidem.
        if x != y:
            plotar_pixel(cx + y, cy + x)
            plotar_pixel(cx - y, cy + x)
            if x != 0:
                plotar_pixel(cx + y, cy - x)
                plotar_pixel(cx - y, cy - x)
        if decisao <= 0:
            decisao += 4 * x + 6
        else:
            decisao += 4 * (x - y) + 10
            y -= 1
        x += 1


def rasterizar_objeto(objeto: ObjetoGrafico, plotar_pixel: PlotarPixel) -> None:
    """Plota uma primitiva durante o cálculo, sem acumular pixels.

    A interface fornece ``plotar_pixel(x, y)`` para desenhar no Canvas.
    Arestas que se encontram podem plotar novamente o mesmo pixel.
    """

    if objeto.tipo == TipoObjeto.PONTO:
        plotar_pixel(*objeto.vertices[0].arredondado())
        return
    if objeto.tipo == TipoObjeto.CIRCUNFERENCIA:
        circunferencia_bresenham(objeto.vertices[0], objeto.vertices[1], plotar_pixel)
        return

    rasterizador = reta_dda if objeto.algoritmo == AlgoritmoReta.DDA else reta_bresenham
    for inicio, fim in objeto.segmentos():
        rasterizador(inicio, fim, plotar_pixel)
