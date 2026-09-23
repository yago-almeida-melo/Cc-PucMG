"""Estruturas de dados usadas pela cena gráfica."""

from __future__ import annotations

from dataclasses import dataclass, field
from enum import Enum
from math import hypot
from typing import Iterable


class TipoObjeto(str, Enum):
    """Tipos de primitivas que podem ser armazenadas na cena."""

    PONTO = "ponto"
    RETA = "reta"
    POLIGONO = "poligono"
    CIRCUNFERENCIA = "circunferencia"


class AlgoritmoReta(str, Enum):
    """Algoritmos disponíveis para rasterizar segmentos."""

    DDA = "DDA"
    BRESENHAM = "Bresenham"


@dataclass(frozen=True)
class Ponto:
    """Ponto 2D em coordenadas cartesianas da área de desenho."""

    x: float
    y: float

    def arredondado(self) -> tuple[int, int]:
        return round(self.x), round(self.y)


@dataclass
class ObjetoGrafico:
    """Primitiva vetorial que será convertida em pixels na exibição."""

    identificador: int
    tipo: TipoObjeto
    vertices: list[Ponto]
    algoritmo: AlgoritmoReta = AlgoritmoReta.BRESENHAM
    cor: str = "#10243e"
    selecionado: bool = False
    nome: str = ""

    def __post_init__(self) -> None:
        limites = {
            TipoObjeto.PONTO: (1, 1),
            TipoObjeto.RETA: (2, 2),
            TipoObjeto.POLIGONO: (3, None),
            TipoObjeto.CIRCUNFERENCIA: (2, 2),
        }
        minimo, maximo = limites[self.tipo]
        if len(self.vertices) < minimo or (maximo is not None and len(self.vertices) > maximo):
            raise ValueError(f"Quantidade de vértices inválida para {self.tipo.value}")
        if not self.nome:
            self.nome = f"{self.tipo.value.capitalize()} {self.identificador}"

    @property
    def centro(self) -> Ponto:
        """Retorna o centro geométrico usado como pivô de transformação."""

        if self.tipo == TipoObjeto.CIRCUNFERENCIA:
            return self.vertices[0]
        quantidade = len(self.vertices)
        return Ponto(
            sum(p.x for p in self.vertices) / quantidade,
            sum(p.y for p in self.vertices) / quantidade,
        )

    @property
    def raio(self) -> float:
        if self.tipo != TipoObjeto.CIRCUNFERENCIA:
            raise ValueError("Apenas circunferências possuem raio")
        centro, borda = self.vertices
        return hypot(borda.x - centro.x, borda.y - centro.y)

    def segmentos(self) -> list[tuple[Ponto, Ponto]]:
        """Decompõe retas e polígonos em segmentos."""

        if self.tipo == TipoObjeto.RETA:
            return [(self.vertices[0], self.vertices[1])]
        if self.tipo == TipoObjeto.POLIGONO:
            return list(zip(self.vertices, self.vertices[1:] + self.vertices[:1]))
        return []

    def copiar(self) -> "ObjetoGrafico":
        return ObjetoGrafico(
            identificador=self.identificador,
            tipo=self.tipo,
            vertices=list(self.vertices),
            algoritmo=self.algoritmo,
            cor=self.cor,
            selecionado=self.selecionado,
            nome=self.nome,
        )


@dataclass(frozen=True)
class Retangulo:
    """Retângulo normalizado, com limites mínimos e máximos."""

    xmin: float
    ymin: float
    xmax: float
    ymax: float

    @classmethod
    def de_pontos(cls, primeiro: Ponto, segundo: Ponto) -> "Retangulo":
        return cls(
            min(primeiro.x, segundo.x),
            min(primeiro.y, segundo.y),
            max(primeiro.x, segundo.x),
            max(primeiro.y, segundo.y),
        )

    def contem(self, ponto: Ponto) -> bool:
        return self.xmin <= ponto.x <= self.xmax and self.ymin <= ponto.y <= self.ymax


FaixaPixels = tuple[int, int, int]  # y, x inicial, x final (inclusive)


@dataclass(frozen=True)
class Preenchimento:
    """Pintura raster imutável em coordenadas do mundo, compactada por linha."""

    identificador: int
    cor: str
    faixas: tuple[FaixaPixels, ...]


@dataclass
class Cena:
    """Objetos, pinturas raster e janela de recorte da aplicação."""

    objetos: list[ObjetoGrafico] = field(default_factory=list)
    janela_recorte: Retangulo | None = None
    _proximo_id: int = 1
    preenchimentos: list[Preenchimento] = field(default_factory=list)

    def adicionar(
        self,
        tipo: TipoObjeto,
        vertices: Iterable[Ponto],
        algoritmo: AlgoritmoReta = AlgoritmoReta.BRESENHAM,
        cor: str = "#10243e",
        nome: str = "",
    ) -> ObjetoGrafico:
        objeto = ObjetoGrafico(
            identificador=self._proximo_id,
            tipo=tipo,
            vertices=list(vertices),
            algoritmo=algoritmo,
            cor=cor,
            nome=nome,
        )
        self._proximo_id += 1
        self.objetos.append(objeto)
        return objeto

    def selecionados(self) -> list[ObjetoGrafico]:
        return [objeto for objeto in self.objetos if objeto.selecionado]

    def elementos_em_ordem(self) -> list[ObjetoGrafico | Preenchimento]:
        """Mantém desenhos e pinturas na ordem em que foram criados."""

        return sorted(
            [*self.objetos, *self.preenchimentos],
            key=lambda elemento: elemento.identificador,
        )

    def adicionar_preenchimento(self, cor: str, faixas: tuple[FaixaPixels, ...]) -> None:
        self.preenchimentos.append(Preenchimento(self._proximo_id, cor, faixas))
        self._proximo_id += 1

    def copiar(self) -> "Cena":
        return Cena(
            objetos=[objeto.copiar() for objeto in self.objetos],
            janela_recorte=self.janela_recorte,
            _proximo_id=self._proximo_id,
            preenchimentos=list(self.preenchimentos),
        )
