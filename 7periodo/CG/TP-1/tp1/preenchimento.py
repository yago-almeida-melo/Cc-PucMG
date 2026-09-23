"""Boundary-Fill e Flood-Fill iterativos, com vizinhança de quatro pixels."""

from __future__ import annotations

from collections.abc import Callable
from math import ceil, floor

from modelos import Cena, FaixaPixels, Ponto, Preenchimento, Retangulo
from rasterizacao import Pixel, rasterizar_objeto

COR_FUNDO = "#ffffff"
LerPixel = Callable[[int, int], str]
PintarPixel = Callable[[int, int, str], None]


def _preencher(
    semente: Ponto,
    limites: Retangulo,
    cor: str,
    ler_pixel: LerPixel,
    pintar_pixel: PintarPixel,
    pode_preencher: Callable[[str], bool],
) -> int:
    """Percorre a região sem recursão e pinta cada pixel alterado imediatamente."""

    xmin, ymin = ceil(limites.xmin), ceil(limites.ymin)
    xmax, ymax = floor(limites.xmax), floor(limites.ymax)
    x, y = semente.arredondado()
    if not (xmin <= x <= xmax and ymin <= y <= ymax):
        return 0

    pilha = [(x, y)]
    visitados = {(x, y)}
    quantidade = 0
    while pilha:
        x, y = pilha.pop()
        atual = ler_pixel(x, y)
        if not pode_preencher(atual):
            continue
        if atual != cor:
            pintar_pixel(x, y, cor)
            quantidade += 1
        for vizinho in ((x - 1, y), (x + 1, y), (x, y - 1), (x, y + 1)):
            vx, vy = vizinho
            if xmin <= vx <= xmax and ymin <= vy <= ymax and vizinho not in visitados:
                visitados.add(vizinho)
                pilha.append(vizinho)
    return quantidade


def boundary_fill(
    semente: Ponto,
    limites: Retangulo,
    cor_preenchimento: str,
    cor_borda: str,
    ler_pixel: LerPixel,
    pintar_pixel: PintarPixel,
) -> int:
    """Preenche até a cor de borda ou os limites, independentemente da cor interna.

    Pixels já pintados com a cor escolhida também podem ser atravessados; o
    conjunto de visitados evita ciclos sem transformá-los em falsas bordas.
    """

    if cor_preenchimento == cor_borda:
        return 0
    return _preencher(
        semente, limites, cor_preenchimento, ler_pixel, pintar_pixel,
        lambda atual: atual != cor_borda,
    )


def flood_fill(
    semente: Ponto,
    limites: Retangulo,
    cor_preenchimento: str,
    ler_pixel: LerPixel,
    pintar_pixel: PintarPixel,
) -> int:
    """Substitui somente a região conexa que possui a cor original da semente."""

    x, y = semente.arredondado()
    if not limites.contem(Ponto(x, y)):
        return 0
    cor_original = ler_pixel(x, y)
    if cor_original == cor_preenchimento:
        return 0
    return _preencher(
        semente, limites, cor_preenchimento, ler_pixel, pintar_pixel,
        lambda atual: atual == cor_original,
    )


def matriz_da_cena(cena: Cena, limites: Retangulo) -> dict[Pixel, str]:
    """Lê as cores reais da cena, sem grade, eixos, prévias ou seleção."""

    pixels: dict[Pixel, str] = {}

    def plotar_pixel(x: int, y: int) -> None:
        if limites.xmin <= x <= limites.xmax and limites.ymin <= y <= limites.ymax:
            pixels[x, y] = elemento.cor

    for elemento in cena.elementos_em_ordem():
        if isinstance(elemento, Preenchimento):
            for y, inicio, fim in elemento.faixas:
                if limites.ymin <= y <= limites.ymax:
                    for x in range(max(inicio, ceil(limites.xmin)), min(fim, floor(limites.xmax)) + 1):
                        plotar_pixel(x, y)
        else:
            rasterizar_objeto(elemento, plotar_pixel)
    return pixels


def calcular_preenchimento(
    cena: Cena,
    semente: Ponto,
    limites: Retangulo,
    algoritmo: str,
    cor: str,
    cor_borda: str,
) -> tuple[FaixaPixels, ...]:
    """Calcula uma pintura sem alterar a cena; guarda só os pixels modificados."""

    pixels = matriz_da_cena(cena, limites)
    linhas: dict[int, list[int]] = {}

    def ler_pixel(x: int, y: int) -> str:
        return pixels.get((x, y), COR_FUNDO)

    def pintar_pixel(x: int, y: int, nova_cor: str) -> None:
        pixels[x, y] = nova_cor
        linhas.setdefault(y, []).append(x)

    if algoritmo == "boundary_fill":
        boundary_fill(semente, limites, cor, cor_borda, ler_pixel, pintar_pixel)
    elif algoritmo == "flood_fill":
        flood_fill(semente, limites, cor, ler_pixel, pintar_pixel)
    else:
        raise ValueError(f"Algoritmo de preenchimento desconhecido: {algoritmo}")

    # Uma faixa horizontal representa vários pixels, reduzindo os itens do
    # Canvas e a memória do histórico, inclusive ao pintar toda a área visível.
    faixas: list[FaixaPixels] = []
    for y, xs in sorted(linhas.items()):
        xs.sort()
        inicio = fim = xs[0]
        for x in xs[1:]:
            if x == fim + 1:
                fim = x
            else:
                faixas.append((y, inicio, fim))
                inicio = fim = x
        faixas.append((y, inicio, fim))
    return tuple(faixas)
