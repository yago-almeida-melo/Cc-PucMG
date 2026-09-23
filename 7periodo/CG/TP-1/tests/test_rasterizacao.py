import unittest
from collections.abc import Callable

from modelos import AlgoritmoReta, ObjetoGrafico, Ponto, TipoObjeto
from rasterizacao import (
    Pixel,
    circunferencia_bresenham,
    rasterizar_objeto,
    reta_bresenham,
    reta_dda,
)


def coletar_pixels(
    rasterizador: Callable[..., None], *argumentos: Ponto | ObjetoGrafico
) -> list[Pixel]:
    """Guarda os pixels somente nos testes para conferir as coordenadas plotadas."""

    pixels: list[Pixel] = []
    rasterizador(*argumentos, lambda x, y: pixels.append((x, y)))
    return pixels


class TesteDDA(unittest.TestCase):
    def test_inclui_extremidades(self) -> None:
        pixels = coletar_pixels(reta_dda, Ponto(0, 0), Ponto(5, 3))
        self.assertEqual((0, 0), pixels[0])
        self.assertEqual((5, 3), pixels[-1])
        self.assertEqual(6, len(pixels))

    def test_reta_vertical(self) -> None:
        self.assertEqual(
            [(2, 1), (2, 2), (2, 3), (2, 4)],
            coletar_pixels(reta_dda, Ponto(2, 1), Ponto(2, 4)),
        )

    def test_ponto_isolado(self) -> None:
        self.assertEqual([(4, -2)], coletar_pixels(reta_dda, Ponto(4, -2), Ponto(4, -2)))


class TesteBresenham(unittest.TestCase):
    def test_funciona_em_todos_os_octantes(self) -> None:
        destinos = [(5, 2), (2, 5), (-2, 5), (-5, 2), (-5, -2), (-2, -5), (2, -5), (5, -2)]
        for destino in destinos:
            with self.subTest(destino=destino):
                pixels = coletar_pixels(reta_bresenham, Ponto(0, 0), Ponto(*destino))
                self.assertEqual((0, 0), pixels[0])
                self.assertEqual(destino, pixels[-1])
                for atual, seguinte in zip(pixels, pixels[1:]):
                    self.assertLessEqual(abs(atual[0] - seguinte[0]), 1)
                    self.assertLessEqual(abs(atual[1] - seguinte[1]), 1)

    def test_sentido_inverso_preserva_extremos_e_tamanho(self) -> None:
        ida = coletar_pixels(reta_bresenham, Ponto(-3, 1), Ponto(5, 7))
        volta = coletar_pixels(reta_bresenham, Ponto(5, 7), Ponto(-3, 1))
        self.assertEqual(ida[0], volta[-1])
        self.assertEqual(ida[-1], volta[0])
        self.assertEqual(len(ida), len(volta))

    def test_ponto_isolado(self) -> None:
        self.assertEqual([(4, -2)], coletar_pixels(reta_bresenham, Ponto(4, -2), Ponto(4, -2)))


class TesteCircunferencia(unittest.TestCase):
    def test_pontos_cardinais_e_simetria(self) -> None:
        pixels = set(coletar_pixels(circunferencia_bresenham, Ponto(2, 3), Ponto(7, 3)))
        for pixel in ((7, 3), (-3, 3), (2, 8), (2, -2)):
            self.assertIn(pixel, pixels)
        for x, y in pixels:
            self.assertIn((4 - x, y), pixels)
            self.assertIn((x, 6 - y), pixels)

    def test_nao_repete_pixels_nos_eixos_e_diagonais(self) -> None:
        for raio in (0, 1, 3, 5, 10):
            with self.subTest(raio=raio):
                pixels = coletar_pixels(circunferencia_bresenham, Ponto(0, 0), Ponto(raio, 0))
                self.assertEqual(len(pixels), len(set(pixels)))


class TesteObjeto(unittest.TestCase):
    def test_ponto(self) -> None:
        objeto = ObjetoGrafico(1, TipoObjeto.PONTO, [Ponto(2.2, -3.8)])
        self.assertEqual([(2, -4)], coletar_pixels(rasterizar_objeto, objeto))

    def test_reta_usa_algoritmo_escolhido(self) -> None:
        for algoritmo, rasterizador in (
            (AlgoritmoReta.DDA, reta_dda),
            (AlgoritmoReta.BRESENHAM, reta_bresenham),
        ):
            with self.subTest(algoritmo=algoritmo):
                objeto = ObjetoGrafico(
                    1, TipoObjeto.RETA, [Ponto(0, 0), Ponto(4, 2)], algoritmo
                )
                self.assertEqual(
                    coletar_pixels(rasterizador, *objeto.vertices),
                    coletar_pixels(rasterizar_objeto, objeto),
                )

    def test_circunferencia(self) -> None:
        objeto = ObjetoGrafico(1, TipoObjeto.CIRCUNFERENCIA, [Ponto(0, 0), Ponto(1, 0)])
        self.assertEqual(
            {(0, 1), (0, -1), (1, 0), (-1, 0)},
            set(coletar_pixels(rasterizar_objeto, objeto)),
        )

    def test_poligono_fecha_ultima_aresta(self) -> None:
        objeto = ObjetoGrafico(
            1,
            TipoObjeto.POLIGONO,
            [Ponto(0, 0), Ponto(4, 0), Ponto(4, 4)],
            AlgoritmoReta.BRESENHAM,
        )
        pixels = coletar_pixels(rasterizar_objeto, objeto)
        self.assertIn((2, 2), pixels)


if __name__ == "__main__":
    unittest.main()
