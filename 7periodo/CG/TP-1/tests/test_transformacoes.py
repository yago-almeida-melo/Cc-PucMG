import unittest

from modelos import ObjetoGrafico, Ponto, TipoObjeto
from transformacoes import (
    aplicar_matriz,
    centro_da_selecao,
    escalar_objeto,
    escala,
    reflexao,
    rotacao,
    translacao,
)


class TesteTransformacoes(unittest.TestCase):
    def assertPontoQuaseIgual(self, esperado: Ponto, atual: Ponto) -> None:
        self.assertAlmostEqual(esperado.x, atual.x, places=7)
        self.assertAlmostEqual(esperado.y, atual.y, places=7)

    def test_translacao(self) -> None:
        resultado = aplicar_matriz(Ponto(2, 3), translacao(-5, 7))
        self.assertPontoQuaseIgual(Ponto(-3, 10), resultado)

    def test_rotacao_de_noventa_graus(self) -> None:
        resultado = aplicar_matriz(Ponto(4, 0), rotacao(90))
        self.assertPontoQuaseIgual(Ponto(0, 4), resultado)

    def test_rotacao_ao_redor_de_pivo(self) -> None:
        resultado = aplicar_matriz(Ponto(3, 2), rotacao(180, Ponto(2, 2)))
        self.assertPontoQuaseIgual(Ponto(1, 2), resultado)

    def test_escala_anisotropica(self) -> None:
        resultado = aplicar_matriz(Ponto(3, -2), escala(2, 0.5))
        self.assertPontoQuaseIgual(Ponto(6, -1), resultado)

    def test_reflexoes(self) -> None:
        ponto = Ponto(3, -4)
        self.assertEqual(Ponto(3, 4), aplicar_matriz(ponto, reflexao("X")))
        self.assertEqual(Ponto(-3, -4), aplicar_matriz(ponto, reflexao("Y")))
        self.assertEqual(Ponto(-3, 4), aplicar_matriz(ponto, reflexao("XY")))

    def test_circunferencia_vira_poligono_em_escala_anisotropica(self) -> None:
        objeto = ObjetoGrafico(
            1,
            TipoObjeto.CIRCUNFERENCIA,
            [Ponto(0, 0), Ponto(10, 0)],
        )
        escalar_objeto(objeto, 2, 1, Ponto(0, 0))
        self.assertEqual(TipoObjeto.POLIGONO, objeto.tipo)
        self.assertEqual(72, len(objeto.vertices))
        xs = [ponto.x for ponto in objeto.vertices]
        ys = [ponto.y for ponto in objeto.vertices]
        self.assertAlmostEqual(40, max(xs) - min(xs))
        self.assertAlmostEqual(20, max(ys) - min(ys))

    def test_centro_de_circunferencia_usa_centro_geometrico(self) -> None:
        objeto = ObjetoGrafico(
            1,
            TipoObjeto.CIRCUNFERENCIA,
            [Ponto(10, 20), Ponto(15, 20)],
        )
        self.assertEqual(Ponto(10, 20), centro_da_selecao([objeto]))


if __name__ == "__main__":
    unittest.main()
