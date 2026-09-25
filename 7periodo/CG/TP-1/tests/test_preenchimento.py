import unittest

from modelos import Cena, Ponto, Retangulo, TipoObjeto
from preenchimento import (
    COR_FUNDO,
    boundary_fill,
    calcular_preenchimento,
    flood_fill,
    matriz_da_cena,
)
from recorte import cohen_sutherland, recortar_segmentos


class TestePreenchimento(unittest.TestCase):
    def preparar(self, linhas: list[str]) -> None:
        self.pixels = {(x, y): cor for y, linha in enumerate(linhas) for x, cor in enumerate(linha)}
        self.limites = Retangulo(0, 0, len(linhas[0]) - 1, len(linhas) - 1)
        self.pintados: set[tuple[int, int]] = set()

    def ler(self, x: int, y: int) -> str:
        # Acesso fora da matriz provoca erro, inclusive em testes de sementes inválidas.
        return self.pixels[x, y]

    def pintar(self, x: int, y: int, cor: str) -> None:
        self.assertIn((x, y), self.pixels)
        self.assertNotIn((x, y), self.pintados)
        self.pixels[x, y] = cor
        self.pintados.add((x, y))

    def executar(self, algoritmo: str, semente: Ponto, cor: str = "R", conectividade: int = 4) -> int:
        if algoritmo == "boundary_fill":
            return boundary_fill(semente, self.limites, cor, "B", self.ler, self.pintar, conectividade)
        return flood_fill(semente, self.limites, cor, self.ler, self.pintar, conectividade)

    def test_boundary_fill_pinta_interior_multicolorido_e_preserva_borda(self) -> None:
        self.preparar(["BBBBB", "BWGWB", "BBGBB", "BBBBB"])
        self.assertEqual(4, self.executar("boundary_fill", Ponto(1, 1)))
        self.assertEqual({(1, 1), (2, 1), (3, 1), (2, 2)}, self.pintados)
        self.assertEqual("B", self.pixels[0, 1])

    def test_boundary_fill_atravessa_cor_de_preenchimento_preexistente(self) -> None:
        self.preparar(["BBBBB", "BWRWB", "BBBBB"])
        self.assertEqual(2, self.executar("boundary_fill", Ponto(2, 1)))
        self.assertEqual({(1, 1), (3, 1)}, self.pintados)

    def test_boundary_fill_na_borda_ou_com_cores_iguais_nao_altera(self) -> None:
        self.preparar(["BBB", "BWB", "BBB"])
        self.assertEqual(0, self.executar("boundary_fill", Ponto(0, 0)))
        self.assertEqual(0, self.executar("boundary_fill", Ponto(1, 1), "B"))
        self.assertEqual(set(), self.pintados)

    def test_flood_fill_preserva_outras_cores_e_regioes_desconectadas(self) -> None:
        self.preparar(["WWBWW", "WGBWW", "WGBWW"])
        self.assertEqual(4, self.executar("flood_fill", Ponto(0, 0)))
        self.assertEqual({(0, 0), (1, 0), (0, 1), (0, 2)}, self.pintados)
        self.assertEqual("G", self.pixels[1, 1])
        self.assertEqual("W", self.pixels[3, 0])

    def test_flood_fill_mesma_cor_nao_altera(self) -> None:
        self.preparar(["RRR", "RRR"])
        self.assertEqual(0, self.executar("flood_fill", Ponto(1, 1)))
        self.assertEqual(set(), self.pintados)

    def test_vizinhanca_de_quatro_nao_atravessa_diagonais(self) -> None:
        for algoritmo in ("boundary_fill", "flood_fill"):
            with self.subTest(algoritmo=algoritmo):
                self.preparar(["WB", "BW"])
                self.assertEqual(1, self.executar(algoritmo, Ponto(0, 0)))
                self.assertEqual("W", self.pixels[1, 1])

    def test_vizinhanca_de_oito_atravessa_as_quatro_diagonais(self) -> None:
        for algoritmo in ("boundary_fill", "flood_fill"):
            with self.subTest(algoritmo=algoritmo):
                self.preparar(["WBW", "BWB", "WBW"])
                self.assertEqual(5, self.executar(algoritmo, Ponto(1, 1), conectividade=8))
                self.assertEqual({(0, 0), (2, 0), (1, 1), (0, 2), (2, 2)}, self.pintados)

    def test_vizinhanca_de_oito_preserva_regiao_separada_por_borda(self) -> None:
        for algoritmo in ("boundary_fill", "flood_fill"):
            with self.subTest(algoritmo=algoritmo):
                self.preparar(["WBW", "WBW", "WBW"])
                self.assertEqual(3, self.executar(algoritmo, Ponto(0, 0), conectividade=8))
                self.assertEqual({(0, 0), (0, 1), (0, 2)}, self.pintados)

    def test_regiao_aberta_para_nos_limites_sem_recursao(self) -> None:
        for algoritmo in ("boundary_fill", "flood_fill"):
            with self.subTest(algoritmo=algoritmo):
                self.preparar(["W" * 150] * 150)
                self.assertEqual(22500, self.executar(algoritmo, Ponto(75, 75)))

    def test_semente_fora_da_matriz(self) -> None:
        for algoritmo in ("boundary_fill", "flood_fill"):
            for semente in (Ponto(-1, 0), Ponto(3, 1), Ponto(0, -1), Ponto(0, 3)):
                with self.subTest(algoritmo=algoritmo, semente=semente):
                    self.preparar(["WWW"] * 3)
                    self.assertEqual(0, self.executar(algoritmo, semente))

    def test_matriz_de_um_pixel(self) -> None:
        for algoritmo in ("boundary_fill", "flood_fill"):
            with self.subTest(algoritmo=algoritmo):
                self.preparar(["W"])
                self.assertEqual(1, self.executar(algoritmo, Ponto(0, 0)))


class TestePinturaDaCena(unittest.TestCase):
    BORDA = "#10243e"
    TINTA = "#ff0000"

    def setUp(self) -> None:
        self.cena = Cena()
        self.limites = Retangulo(-10, -10, 10, 10)
        self.quadrado = self.cena.adicionar(
            TipoObjeto.POLIGONO,
            [Ponto(-3, -3), Ponto(3, -3), Ponto(3, 3), Ponto(-3, 3)],
            cor=self.BORDA,
        )

    def pintar(self, algoritmo: str = "boundary_fill", cor: str = TINTA) -> None:
        faixas = calcular_preenchimento(
            self.cena, Ponto(0, 0), self.limites, algoritmo, cor, self.BORDA,
        )
        self.cena.adicionar_preenchimento(cor, faixas)

    def test_contorno_negativo_compactacao_e_calculo_sem_mutacao(self) -> None:
        for algoritmo in ("boundary_fill", "flood_fill"):
            with self.subTest(algoritmo=algoritmo):
                faixas = calcular_preenchimento(
                    self.cena, Ponto(0, 0), self.limites, algoritmo, self.TINTA, self.BORDA,
                )
                self.assertEqual(tuple((y, -2, 2) for y in range(-2, 3)), faixas)
                self.assertFalse(self.cena.preenchimentos)

    def test_calculo_da_cena_usa_conectividade_escolhida(self) -> None:
        cena = Cena()
        cena.adicionar(TipoObjeto.PONTO, [Ponto(1, 0)], cor=self.BORDA)
        cena.adicionar(TipoObjeto.PONTO, [Ponto(0, 1)], cor=self.BORDA)
        for algoritmo in ("boundary_fill", "flood_fill"):
            for conectividade in (4, 8):
                with self.subTest(algoritmo=algoritmo, conectividade=conectividade):
                    faixas = calcular_preenchimento(
                        cena, Ponto(0, 0), Retangulo(0, 0, 1, 1), algoritmo,
                        self.TINTA, self.BORDA, conectividade,
                    )
                    esperado = ((0, 0, 0),) if conectividade == 4 else ((0, 0, 0), (1, 1, 1))
                    self.assertEqual(esperado, faixas)
                    self.assertFalse(cena.preenchimentos)

    def test_ordem_de_pinturas_e_novos_objetos(self) -> None:
        ponto = self.cena.adicionar(TipoObjeto.PONTO, [Ponto(1, 1)], cor="#00ff00")
        self.pintar()
        pixels = matriz_da_cena(self.cena, self.limites)
        self.assertEqual(self.TINTA, pixels[1, 1])
        self.assertEqual(self.BORDA, pixels[3, 0])
        self.assertNotIn((4, 0), pixels)
        self.cena.adicionar(TipoObjeto.PONTO, [Ponto(1, 1)], cor=ponto.cor)
        self.assertEqual(ponto.cor, matriz_da_cena(self.cena, self.limites)[1, 1])

    def test_recolorir_pintura_com_flood_fill_e_branco(self) -> None:
        self.pintar()
        self.pintar("flood_fill", COR_FUNDO)
        self.assertEqual(COR_FUNDO, matriz_da_cena(self.cena, self.limites)[0, 0])
        self.assertEqual(self.BORDA, matriz_da_cena(self.cena, self.limites)[3, 0])

    def test_copia_do_historico_preserva_pinturas_e_cores(self) -> None:
        self.pintar()
        copia = self.cena.copiar()
        self.cena.preenchimentos.clear()
        self.quadrado.cor = "#00ff00"
        self.assertEqual(self.TINTA, matriz_da_cena(copia, self.limites)[0, 0])
        self.assertEqual(self.BORDA, copia.objetos[0].cor)

    def test_selecao_e_janela_de_recorte_nao_sao_bordas(self) -> None:
        self.quadrado.selecionado = True
        self.cena.janela_recorte = Retangulo(-1, -1, 1, 1)
        self.pintar()
        self.assertEqual(25, sum(b - a + 1 for _, a, b in self.cena.preenchimentos[0].faixas))

    def test_circunferencia_e_borda_diagonal_fecham_regiao(self) -> None:
        for tipo, vertices in (
            (TipoObjeto.CIRCUNFERENCIA, [Ponto(0, 0), Ponto(5, 0)]),
            (TipoObjeto.POLIGONO, [Ponto(-5, 0), Ponto(0, 5), Ponto(5, 0), Ponto(0, -5)]),
        ):
            for algoritmo in ("boundary_fill", "flood_fill"):
                with self.subTest(tipo=tipo, algoritmo=algoritmo):
                    self.cena = Cena()
                    self.cena.adicionar(tipo, vertices, cor=self.BORDA)
                    self.pintar(algoritmo)
                    pixels = matriz_da_cena(self.cena, self.limites)
                    self.assertEqual(self.TINTA, pixels[0, 0])
                    self.assertEqual(self.BORDA, pixels[5, 0])
                    self.assertNotIn((6, 0), pixels)

    def test_recorte_mantem_ids_unicos_entre_objetos_e_pinturas(self) -> None:
        self.pintar()
        self.quadrado.selecionado = True
        self.cena.objetos, _ = recortar_segmentos(
            self.cena.objetos, self.limites, cohen_sutherland,
            proximo_id=self.cena._proximo_id,
        )
        ids = [elemento.identificador for elemento in self.cena.elementos_em_ordem()]
        self.assertEqual(len(ids), len(set(ids)))


if __name__ == "__main__":
    unittest.main()
