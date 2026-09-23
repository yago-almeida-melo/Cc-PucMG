import unittest

from modelos import ObjetoGrafico, Ponto, Retangulo, TipoObjeto
from recorte import (
    cohen_sutherland,
    liang_barsky,
    objeto_intersecta_retangulo,
    recortar_segmentos,
)


class ComportamentoComumRecorte:
    algoritmo = staticmethod(cohen_sutherland)
    janela = Retangulo(0, 0, 10, 10)

    def test_aceita_segmento_interno(self) -> None:
        esperado = (Ponto(2, 3), Ponto(8, 7))
        self.assertEqual(esperado, self.algoritmo(*esperado, self.janela))

    def test_rejeita_segmento_externo(self) -> None:
        resultado = self.algoritmo(Ponto(-5, 12), Ponto(15, 12), self.janela)
        self.assertIsNone(resultado)

    def test_recorta_segmento_horizontal(self) -> None:
        resultado = self.algoritmo(Ponto(-5, 5), Ponto(15, 5), self.janela)
        self.assertEqual((Ponto(0, 5), Ponto(10, 5)), resultado)

    def test_recorta_segmento_vertical(self) -> None:
        resultado = self.algoritmo(Ponto(5, -5), Ponto(5, 15), self.janela)
        self.assertEqual((Ponto(5, 0), Ponto(5, 10)), resultado)


class TesteCohenSutherland(ComportamentoComumRecorte, unittest.TestCase):
    algoritmo = staticmethod(cohen_sutherland)


class TesteLiangBarsky(ComportamentoComumRecorte, unittest.TestCase):
    algoritmo = staticmethod(liang_barsky)


class TesteSelecao(unittest.TestCase):
    def setUp(self) -> None:
        self.regiao = Retangulo(-2, -2, 2, 2)

    def test_seleciona_ponto_interno(self) -> None:
        objeto = ObjetoGrafico(1, TipoObjeto.PONTO, [Ponto(0, 0)])
        self.assertTrue(objeto_intersecta_retangulo(objeto, self.regiao))

    def test_seleciona_reta_que_cruza(self) -> None:
        objeto = ObjetoGrafico(1, TipoObjeto.RETA, [Ponto(-10, 0), Ponto(10, 0)])
        self.assertTrue(objeto_intersecta_retangulo(objeto, self.regiao))

    def test_nao_seleciona_circunferencia_distante(self) -> None:
        objeto = ObjetoGrafico(1, TipoObjeto.CIRCUNFERENCIA, [Ponto(20, 20), Ponto(25, 20)])
        self.assertFalse(objeto_intersecta_retangulo(objeto, self.regiao))

    def test_seleciona_regiao_contida_no_poligono(self) -> None:
        objeto = ObjetoGrafico(
            1,
            TipoObjeto.POLIGONO,
            [Ponto(-10, -10), Ponto(10, -10), Ponto(10, 10), Ponto(-10, 10)],
        )
        self.assertTrue(objeto_intersecta_retangulo(objeto, self.regiao))

    def test_recorte_de_poligono_gera_ids_unicos(self) -> None:
        objeto = ObjetoGrafico(
            4,
            TipoObjeto.POLIGONO,
            [Ponto(-5, -5), Ponto(5, -5), Ponto(5, 5), Ponto(-5, 5)],
            selecionado=True,
        )
        resultado, afetados = recortar_segmentos([objeto], self.regiao, liang_barsky)
        self.assertEqual(1, afetados)
        ids = [item.identificador for item in resultado]
        self.assertEqual(len(ids), len(set(ids)))


if __name__ == "__main__":
    unittest.main()
