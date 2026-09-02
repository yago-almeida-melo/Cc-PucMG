import unittest

from pixel_lab.modelos import AlgoritmoReta, ObjetoGrafico, Ponto, TipoObjeto
from pixel_lab.rasterizacao import (
    circunferencia_bresenham,
    rasterizar_objeto,
    reta_bresenham,
    reta_dda,
)


class TesteDDA(unittest.TestCase):
    def test_inclui_extremidades(self) -> None:
        pixels = reta_dda(Ponto(0, 0), Ponto(5, 3))
        self.assertEqual((0, 0), pixels[0])
        self.assertEqual((5, 3), pixels[-1])
        self.assertEqual(6, len(pixels))

    def test_reta_vertical(self) -> None:
        self.assertEqual(
            [(2, 1), (2, 2), (2, 3), (2, 4)],
            reta_dda(Ponto(2, 1), Ponto(2, 4)),
        )

    def test_ponto_isolado(self) -> None:
        self.assertEqual([(4, -2)], reta_dda(Ponto(4, -2), Ponto(4, -2)))


class TesteBresenham(unittest.TestCase):
    def test_funciona_em_todos_os_octantes(self) -> None:
        destinos = [(5, 2), (2, 5), (-2, 5), (-5, 2), (-5, -2), (-2, -5), (2, -5), (5, -2)]
        for destino in destinos:
            with self.subTest(destino=destino):
                pixels = reta_bresenham(Ponto(0, 0), Ponto(*destino))
                self.assertEqual((0, 0), pixels[0])
                self.assertEqual(destino, pixels[-1])
                for atual, seguinte in zip(pixels, pixels[1:]):
                    self.assertLessEqual(abs(atual[0] - seguinte[0]), 1)
                    self.assertLessEqual(abs(atual[1] - seguinte[1]), 1)

    def test_sentido_inverso_preserva_extremos_e_tamanho(self) -> None:
        ida = reta_bresenham(Ponto(-3, 1), Ponto(5, 7))
        volta = reta_bresenham(Ponto(5, 7), Ponto(-3, 1))
        self.assertEqual(ida[0], volta[-1])
        self.assertEqual(ida[-1], volta[0])
        self.assertEqual(len(ida), len(volta))


class TesteCircunferencia(unittest.TestCase):
    def test_pontos_cardinais_e_simetria(self) -> None:
        pixels = set(circunferencia_bresenham(Ponto(2, 3), Ponto(7, 3)))
        for pixel in ((7, 3), (-3, 3), (2, 8), (2, -2)):
            self.assertIn(pixel, pixels)
        for x, y in pixels:
            self.assertIn((4 - x, y), pixels)
            self.assertIn((x, 6 - y), pixels)

    def test_poligono_fecha_ultima_aresta(self) -> None:
        objeto = ObjetoGrafico(
            1,
            TipoObjeto.POLIGONO,
            [Ponto(0, 0), Ponto(4, 0), Ponto(4, 4)],
            AlgoritmoReta.BRESENHAM,
        )
        pixels = rasterizar_objeto(objeto)
        self.assertIn((2, 2), pixels)


if __name__ == "__main__":
    unittest.main()
