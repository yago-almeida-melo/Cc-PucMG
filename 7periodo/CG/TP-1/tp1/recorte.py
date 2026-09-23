"""Recorte de segmentos por Cohen-Sutherland e Liang-Barsky."""

from __future__ import annotations

from collections.abc import Callable
from math import hypot

from modelos import ObjetoGrafico, Ponto, Retangulo, TipoObjeto

Segmento = tuple[Ponto, Ponto]

ESQUERDA, DIREITA, ABAIXO, ACIMA = 1, 2, 4, 8


def _codigo_regiao(ponto: Ponto, janela: Retangulo) -> int:
    codigo = 0
    if ponto.x < janela.xmin:
        codigo |= ESQUERDA
    elif ponto.x > janela.xmax:
        codigo |= DIREITA
    if ponto.y < janela.ymin:
        codigo |= ABAIXO
    elif ponto.y > janela.ymax:
        codigo |= ACIMA
    return codigo


def cohen_sutherland(inicio: Ponto, fim: Ponto, janela: Retangulo) -> Segmento | None:
    """Recorta um segmento usando códigos de região de quatro bits."""

    x0, y0 = inicio.x, inicio.y
    x1, y1 = fim.x, fim.y

    while True:
        codigo0 = _codigo_regiao(Ponto(x0, y0), janela)
        codigo1 = _codigo_regiao(Ponto(x1, y1), janela)
        if not (codigo0 | codigo1):
            return Ponto(x0, y0), Ponto(x1, y1)
        if codigo0 & codigo1:
            return None

        codigo_fora = codigo0 or codigo1
        if codigo_fora & ACIMA:
            if y1 == y0:
                return None
            x = x0 + (x1 - x0) * (janela.ymax - y0) / (y1 - y0)
            y = janela.ymax
        elif codigo_fora & ABAIXO:
            if y1 == y0:
                return None
            x = x0 + (x1 - x0) * (janela.ymin - y0) / (y1 - y0)
            y = janela.ymin
        elif codigo_fora & DIREITA:
            if x1 == x0:
                return None
            y = y0 + (y1 - y0) * (janela.xmax - x0) / (x1 - x0)
            x = janela.xmax
        else:
            if x1 == x0:
                return None
            y = y0 + (y1 - y0) * (janela.xmin - x0) / (x1 - x0)
            x = janela.xmin

        if codigo_fora == codigo0:
            x0, y0 = x, y
        else:
            x1, y1 = x, y


def liang_barsky(inicio: Ponto, fim: Ponto, janela: Retangulo) -> Segmento | None:
    """Recorta um segmento usando a sua representação paramétrica."""

    dx, dy = fim.x - inicio.x, fim.y - inicio.y
    p = (-dx, dx, -dy, dy)
    q = (
        inicio.x - janela.xmin,
        janela.xmax - inicio.x,
        inicio.y - janela.ymin,
        janela.ymax - inicio.y,
    )
    u1, u2 = 0.0, 1.0

    for pi, qi in zip(p, q):
        if pi == 0:
            if qi < 0:
                return None
            continue
        razao = qi / pi
        if pi < 0:
            u1 = max(u1, razao)
        else:
            u2 = min(u2, razao)
        if u1 > u2:
            return None

    return (
        Ponto(inicio.x + u1 * dx, inicio.y + u1 * dy),
        Ponto(inicio.x + u2 * dx, inicio.y + u2 * dy),
    )


def objeto_intersecta_retangulo(objeto: ObjetoGrafico, regiao: Retangulo) -> bool:
    """Testa seleção por interseção com uma região retangular."""

    if objeto.tipo == TipoObjeto.PONTO:
        return regiao.contem(objeto.vertices[0])
    if objeto.tipo == TipoObjeto.CIRCUNFERENCIA:
        centro = objeto.vertices[0]
        x_proximo = min(max(centro.x, regiao.xmin), regiao.xmax)
        y_proximo = min(max(centro.y, regiao.ymin), regiao.ymax)
        return hypot(centro.x - x_proximo, centro.y - y_proximo) <= objeto.raio
    if any(liang_barsky(inicio, fim, regiao) is not None for inicio, fim in objeto.segmentos()):
        return True
    if objeto.tipo == TipoObjeto.POLIGONO:
        cantos = (
            Ponto(regiao.xmin, regiao.ymin),
            Ponto(regiao.xmin, regiao.ymax),
            Ponto(regiao.xmax, regiao.ymin),
            Ponto(regiao.xmax, regiao.ymax),
        )
        return any(_ponto_no_poligono(canto, objeto.vertices) for canto in cantos)
    return False


def _ponto_no_poligono(ponto: Ponto, vertices: list[Ponto]) -> bool:
    """Teste par-ímpar usado quando a região está contida no polígono."""

    dentro = False
    anterior = vertices[-1]
    for atual in vertices:
        cruza_altura = (atual.y > ponto.y) != (anterior.y > ponto.y)
        if cruza_altura:
            x_intersecao = (
                (anterior.x - atual.x) * (ponto.y - atual.y) / (anterior.y - atual.y)
                + atual.x
            )
            if ponto.x < x_intersecao:
                dentro = not dentro
        anterior = atual
    return dentro


def recortar_segmentos(
    objetos: list[ObjetoGrafico],
    janela: Retangulo,
    algoritmo: Callable[[Ponto, Ponto, Retangulo], Segmento | None],
    proximo_id: int | None = None,
) -> tuple[list[ObjetoGrafico], int]:
    """Recorta retas e arestas de polígonos, mantendo outros tipos intactos."""

    resultado: list[ObjetoGrafico] = []
    proximo_id = max(
        proximo_id or 1,
        max((objeto.identificador for objeto in objetos), default=0) + 1,
    )
    afetados = 0

    for objeto in objetos:
        if not objeto.selecionado or objeto.tipo not in (TipoObjeto.RETA, TipoObjeto.POLIGONO):
            resultado.append(objeto)
            continue

        afetados += 1
        for indice, (inicio, fim) in enumerate(objeto.segmentos(), start=1):
            segmento = algoritmo(inicio, fim, janela)
            if segmento is None:
                continue
            resultado.append(
                ObjetoGrafico(
                    identificador=proximo_id,
                    tipo=TipoObjeto.RETA,
                    vertices=list(segmento),
                    algoritmo=objeto.algoritmo,
                    cor=objeto.cor,
                    selecionado=True,
                    nome=f"Recorte de {objeto.nome} — trecho {indice}",
                )
            )
            proximo_id += 1
    return resultado, afetados
