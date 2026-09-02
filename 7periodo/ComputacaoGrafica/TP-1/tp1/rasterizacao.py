"""Algoritmos clássicos de rasterização implementados sem atalhos gráficos."""

from __future__ import annotations

from math import hypot

from .modelos import AlgoritmoReta, ObjetoGrafico, Ponto, TipoObjeto

Pixel = tuple[int, int]


def reta_dda(inicio: Ponto, fim: Ponto) -> list[Pixel]:
    """Rasteriza uma reta pelo algoritmo Digital Differential Analyzer."""

    x0, y0 = inicio.arredondado()
    x1, y1 = fim.arredondado()
    dx, dy = x1 - x0, y1 - y0
    passos = int(max(abs(dx), abs(dy)))
    if passos == 0:
        return [(round(x0), round(y0))]

    incremento_x = dx / passos
    incremento_y = dy / passos
    pixels: list[Pixel] = []
    x, y = x0, y0
    for _ in range(passos + 1):
        pixel = (round(x), round(y))
        if not pixels or pixels[-1] != pixel:
            pixels.append(pixel)
        x += incremento_x
        y += incremento_y
    return pixels


def reta_bresenham(inicio: Ponto, fim: Ponto) -> list[Pixel]:
    """Rasteriza uma reta em qualquer octante usando apenas passos inteiros."""

    x0, y0 = inicio.arredondado()
    x1, y1 = fim.arredondado()
    dx = abs(x1 - x0)
    sx = 1 if x0 < x1 else -1
    dy = -abs(y1 - y0)
    sy = 1 if y0 < y1 else -1
    erro = dx + dy
    pixels: list[Pixel] = []

    while True:
        pixels.append((x0, y0))
        if x0 == x1 and y0 == y1:
            break
        erro_duplo = 2 * erro
        if erro_duplo >= dy:
            erro += dy
            x0 += sx
        if erro_duplo <= dx:
            erro += dx
            y0 += sy
    return pixels


def circunferencia_bresenham(centro: Ponto, ponto_raio: Ponto) -> list[Pixel]:
    """Rasteriza uma circunferência pelo método do ponto médio/Bresenham."""

    cx, cy = centro.arredondado()
    raio = max(1, round(hypot(ponto_raio.x - centro.x, ponto_raio.y - centro.y)))
    x, y = 0, raio
    decisao = 3 - 2 * raio
    pixels: set[Pixel] = set()

    def adicionar_simetria(px: int, py: int) -> None:
        pixels.update(
            {
                (cx + px, cy + py),
                (cx - px, cy + py),
                (cx + px, cy - py),
                (cx - px, cy - py),
                (cx + py, cy + px),
                (cx - py, cy + px),
                (cx + py, cy - px),
                (cx - py, cy - px),
            }
        )

    while x <= y:
        adicionar_simetria(x, y)
        if decisao <= 0:
            decisao += 4 * x + 6
        else:
            decisao += 4 * (x - y) + 10
            y -= 1
        x += 1
    return sorted(pixels)


def rasterizar_objeto(objeto: ObjetoGrafico) -> list[Pixel]:
    """Converte uma primitiva da cena em uma lista de posições de pixels."""

    if objeto.tipo == TipoObjeto.PONTO:
        return [objeto.vertices[0].arredondado()]
    if objeto.tipo == TipoObjeto.CIRCUNFERENCIA:
        return circunferencia_bresenham(objeto.vertices[0], objeto.vertices[1])

    rasterizador = reta_dda if objeto.algoritmo == AlgoritmoReta.DDA else reta_bresenham
    pixels: list[Pixel] = []
    vistos: set[Pixel] = set()
    for inicio, fim in objeto.segmentos():
        for pixel in rasterizador(inicio, fim):
            if pixel not in vistos:
                vistos.add(pixel)
                pixels.append(pixel)
    return pixels
