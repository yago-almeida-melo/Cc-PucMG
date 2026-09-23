"""Transformações geométricas 2D expressas por matrizes homogêneas 3x3."""

from __future__ import annotations

from math import cos, pi, sin

from modelos import ObjetoGrafico, Ponto, TipoObjeto

Matriz = tuple[
    tuple[float, float, float],
    tuple[float, float, float],
    tuple[float, float, float],
]


def multiplicar(a: Matriz, b: Matriz) -> Matriz:
    return tuple(
        tuple(sum(a[i][k] * b[k][j] for k in range(3)) for j in range(3))
        for i in range(3)
    )  # type: ignore[return-value]


def aplicar_matriz(ponto: Ponto, matriz: Matriz) -> Ponto:
    x = matriz[0][0] * ponto.x + matriz[0][1] * ponto.y + matriz[0][2]
    y = matriz[1][0] * ponto.x + matriz[1][1] * ponto.y + matriz[1][2]
    w = matriz[2][0] * ponto.x + matriz[2][1] * ponto.y + matriz[2][2]
    return Ponto(x / w, y / w)


def translacao(dx: float, dy: float) -> Matriz:
    return ((1, 0, dx), (0, 1, dy), (0, 0, 1))


def rotacao(angulo_graus: float, pivo: Ponto = Ponto(0, 0)) -> Matriz:
    angulo = angulo_graus * pi / 180
    cosseno, seno = cos(angulo), sin(angulo)
    matriz_base: Matriz = ((cosseno, -seno, 0), (seno, cosseno, 0), (0, 0, 1))
    return _ao_redor_do_pivo(matriz_base, pivo)


def escala(sx: float, sy: float, pivo: Ponto = Ponto(0, 0)) -> Matriz:
    matriz_base: Matriz = ((sx, 0, 0), (0, sy, 0), (0, 0, 1))
    return _ao_redor_do_pivo(matriz_base, pivo)


def reflexao(eixo: str) -> Matriz:
    """Retorna reflexão nos eixos globais X, Y ou em ambos (origem)."""

    eixo = eixo.upper()
    matrizes: dict[str, Matriz] = {
        "X": ((1, 0, 0), (0, -1, 0), (0, 0, 1)),
        "Y": ((-1, 0, 0), (0, 1, 0), (0, 0, 1)),
        "XY": ((-1, 0, 0), (0, -1, 0), (0, 0, 1)),
    }
    if eixo not in matrizes:
        raise ValueError("O eixo deve ser X, Y ou XY")
    return matrizes[eixo]


def _ao_redor_do_pivo(matriz: Matriz, pivo: Ponto) -> Matriz:
    ida = translacao(-pivo.x, -pivo.y)
    volta = translacao(pivo.x, pivo.y)
    return multiplicar(volta, multiplicar(matriz, ida))


def transformar_objeto(objeto: ObjetoGrafico, matriz: Matriz) -> None:
    """Aplica uma matriz a todos os pontos de controle do objeto."""

    objeto.vertices = [aplicar_matriz(ponto, matriz) for ponto in objeto.vertices]


def escalar_objeto(objeto: ObjetoGrafico, sx: float, sy: float, pivo: Ponto) -> None:
    """Escala um objeto; circunferência anisotrópica vira polígono amostrado.

    Uma circunferência deixa de ser circunferência quando ``sx != sy``. Nesse caso,
    ela é convertida em um polígono de 72 lados para preservar o resultado visual.
    """

    matriz = escala(sx, sy, pivo)
    if objeto.tipo == TipoObjeto.CIRCUNFERENCIA and abs(abs(sx) - abs(sy)) > 1e-9:
        centro = objeto.vertices[0]
        raio = objeto.raio
        amostras = [
            Ponto(
                centro.x + raio * cos(2 * pi * i / 72),
                centro.y + raio * sin(2 * pi * i / 72),
            )
            for i in range(72)
        ]
        objeto.tipo = TipoObjeto.POLIGONO
        objeto.vertices = [aplicar_matriz(ponto, matriz) for ponto in amostras]
        objeto.nome = f"Polígono {objeto.identificador} (elipse)"
    else:
        transformar_objeto(objeto, matriz)


def centro_da_selecao(objetos: list[ObjetoGrafico]) -> Ponto:
    """Retorna o centro da caixa envolvente de todos os objetos selecionados."""

    if not objetos:
        return Ponto(0, 0)
    limites: list[tuple[float, float, float, float]] = []
    for objeto in objetos:
        if objeto.tipo == TipoObjeto.CIRCUNFERENCIA:
            centro, raio = objeto.vertices[0], objeto.raio
            limites.append(
                (centro.x - raio, centro.y - raio, centro.x + raio, centro.y + raio)
            )
        else:
            xs = [ponto.x for ponto in objeto.vertices]
            ys = [ponto.y for ponto in objeto.vertices]
            limites.append((min(xs), min(ys), max(xs), max(ys)))
    xmin = min(limite[0] for limite in limites)
    ymin = min(limite[1] for limite in limites)
    xmax = max(limite[2] for limite in limites)
    ymax = max(limite[3] for limite in limites)
    return Ponto((xmin + xmax) / 2, (ymin + ymax) / 2)
