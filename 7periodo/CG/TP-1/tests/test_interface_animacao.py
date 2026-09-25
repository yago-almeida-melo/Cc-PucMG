import tkinter as tk
import unittest
from unittest.mock import patch

from interface import App
from modelos import Ponto, Retangulo, TipoObjeto


class TesteInterfaceAnimacao(unittest.TestCase):
    def setUp(self) -> None:
        try:
            self.raiz = tk.Tk()
        except tk.TclError as erro:
            self.skipTest(f"Display Tk indisponível: {erro}")
        self.app = App(self.raiz)
        self.fechada = False
        self.addCleanup(self.fechar)
        self.raiz.update()
        self.app.delay.set(250)

    def fechar(self) -> None:
        if not self.fechada:
            self.app.fechar()
            self.fechada = True

    def clicar(self, x: int, y: int) -> None:
        cx, cy = self.app._mundo_para_canvas(Ponto(x, y))
        self.app.canvas.event_generate("<ButtonPress-1>", x=round(cx), y=round(cy))
        self.app.canvas.event_generate("<ButtonRelease-1>", x=round(cx), y=round(cy))

    def desenhar(self, modo: str, vertices: list[tuple[int, int]]) -> None:
        self.app.botoes_modo[modo].invoke()
        for x, y in vertices:
            self.clicar(x, y)
        if modo == "poligono":
            self.app.concluir_poligono()

    def pixels_visiveis(self) -> set[tuple[int, int]]:
        pixels = set()
        for item in self.app.canvas.find_withtag("pixel"):
            x1, y1, x2, y2 = self.app.canvas.coords(item)
            ponto = self.app._canvas_para_mundo((x1 + x2) / 2, (y1 + y2) / 2)
            pixels.add(ponto.arredondado())
        return pixels

    def avancar(self) -> None:
        # Executa o callback registrado no Tk sem depender do relógio do teste.
        identificador = self.app._apos
        self.assertIsNotNone(identificador)
        comando, _ = self.raiz.tk.call("after", "info", identificador)
        self.raiz.tk.call("after", "cancel", identificador)
        self.raiz.tk.eval(comando)

    def test_delay_e_avanco_gradual_de_todas_as_primitivas(self) -> None:
        casos = (
            ("ponto", [(1, 1)], {(1, 1)}),
            ("reta_dda", [(-2, -1), (2, 1)], {(-2, -1), (-1, 0), (0, 0), (1, 0), (2, 1)}),
            ("reta_bresenham", [(-2, -1), (2, 1)], {(-2, -1), (-1, 0), (0, 0), (1, 1), (2, 1)}),
            ("circunferencia", [(0, 0), (2, 0)], {
                (0, 2), (0, -2), (2, 0), (-2, 0), (1, 2), (1, -2),
                (-1, 2), (-1, -2), (2, 1), (-2, 1), (2, -1), (-2, -1),
            }),
            ("poligono", [(0, 0), (2, 0), (0, 2)], {(0, 0), (1, 0), (2, 0), (1, 1), (0, 2), (0, 1)}),
        )
        for modo, vertices, esperado in casos:
            with self.subTest(modo=modo):
                self.app.limpar_cena()
                with patch.object(self.raiz, "after", wraps=self.raiz.after) as agendar:
                    self.desenhar(modo, vertices)
                    agendar.assert_called_with(250, self.app._passo_animacao)
                self.assertEqual(set(), self.pixels_visiveis())
                self.avancar()
                anteriores = self.pixels_visiveis()
                self.assertEqual(1, len(anteriores))
                for _ in range(30):
                    self.app.redesenhar()
                    self.assertEqual(anteriores, self.pixels_visiveis())
                    if self.app._apos is None:
                        break
                    self.avancar()
                    atuais = self.pixels_visiveis()
                    self.assertTrue(anteriores <= atuais <= esperado)
                    self.assertLessEqual(len(atuais - anteriores), 1)
                    anteriores = atuais
                self.assertIsNone(self.app._apos)
                self.assertEqual(esperado, self.pixels_visiveis())

    def test_delay_zero_desenha_imediatamente(self) -> None:
        self.app.delay.set(0)
        with patch.object(self.raiz, "after", wraps=self.raiz.after) as agendar:
            self.desenhar("reta_dda", [(-2, 0), (2, 0)])
            agendar.assert_not_called()
        self.assertEqual({(x, 0) for x in range(-2, 3)}, self.pixels_visiveis())
        self.assertIsNone(self.app._apos)

    def test_mudar_delay_para_zero_conclui_animacao(self) -> None:
        self.desenhar("reta_bresenham", [(-2, 0), (2, 0)])
        self.avancar()
        self.app.delay.set(0)
        self.avancar()
        self.assertEqual({(x, 0) for x in range(-2, 3)}, self.pixels_visiveis())
        self.assertIsNone(self.app._apos)

    def test_desfazer_e_limpar_cancelam_callback(self) -> None:
        for acao in (self.app.desfazer, self.app.limpar_cena):
            with self.subTest(acao=acao.__name__):
                self.desenhar("reta_bresenham", [(-2, 0), (2, 0)])
                self.avancar()
                identificador = self.app._apos
                self.assertIsNotNone(identificador)
                acao()
                self.assertNotIn(identificador, self.raiz.tk.call("after", "info"))
                self.assertIsNone(self.app._apos)
                self.assertFalse(self.app.cena.objetos)
                self.app.redesenhar()
                self.assertEqual(set(), self.pixels_visiveis())

    def test_fechar_cancela_callback(self) -> None:
        self.desenhar("reta_bresenham", [(-2, 0), (2, 0)])
        identificador = self.app._apos
        self.assertIsNotNone(identificador)
        self.fechar()
        self.assertNotIn(identificador, self.raiz.tk.call("after", "info"))
        self.assertIsNone(self.app._apos)

    def test_conectividade_escolhida_na_interface(self) -> None:
        self.app.delay.set(0)
        for modo in ("flood_fill", "boundary_fill"):
            for conectividade, quantidade in ((4, 1), (8, 2)):
                with self.subTest(modo=modo, conectividade=conectividade):
                    self.app.limpar_cena()
                    self.desenhar("poligono", [(-1, -1), (2, -1), (2, 2), (-1, 2)])
                    self.desenhar("ponto", [(0, 1)])
                    self.desenhar("ponto", [(1, 0)])
                    self.app.conectividade.set(conectividade)
                    self.app.botoes_modo[modo].invoke()
                    self.clicar(0, 0)
                    faixas = self.app.cena.preenchimentos[-1].faixas
                    self.assertEqual(quantidade, sum(fim - inicio + 1 for _, inicio, fim in faixas))
                    self.assertIn(f"conectividade {conectividade}", self.app.status.get())

    def test_recorte_ignora_poligono_selecionado(self) -> None:
        self.app.delay.set(0)
        for algoritmo in ("cohen", "liang"):
            with self.subTest(algoritmo=algoritmo):
                self.app.limpar_cena()
                self.desenhar("poligono", [(-3, -3), (3, -3), (0, 3)])
                poligono = self.app.cena.objetos[0]
                vertices = list(poligono.vertices)
                self.app.cena.janela_recorte = Retangulo(-1, -1, 1, 1)
                self.app.selecionar_todos()
                historico = len(self.app.historico)
                self.app.aplicar_recorte(algoritmo)
                self.assertEqual(historico, len(self.app.historico))
                self.assertEqual(vertices, poligono.vertices)
                self.assertIn("Selecione pelo menos uma reta", self.app.status.get())
                self.desenhar("reta_bresenham", [(-3, 0), (3, 0)])
                self.app.selecionar_todos()
                self.app.aplicar_recorte(algoritmo)
                self.assertEqual(vertices, poligono.vertices)
                self.assertIn(poligono, self.app.cena.objetos)
                reta = next(obj for obj in self.app.cena.objetos if obj.tipo == TipoObjeto.RETA)
                self.assertEqual([Ponto(-1, 0), Ponto(1, 0)], reta.vertices)

    def test_transformacao_anima_e_rejeita_valores_invalidos(self) -> None:
        self.desenhar("reta_bresenham", [(-2, 0), (2, 0)])
        self.app.selecionar_todos()
        for variavel, acao in ((self.app.dx, self.app.aplicar_translacao),
                               (self.app.angulo, self.app.aplicar_rotacao),
                               (self.app.sx, self.app.aplicar_escala)):
            for valor in ("", "abc", "inf"):
                with self.subTest(variavel=variavel, valor=valor):
                    antigo = variavel.get()
                    variavel.set(valor)
                    historico = len(self.app.historico)
                    acao()
                    self.assertEqual(historico, len(self.app.historico))
                    self.assertEqual([Ponto(-2, 0), Ponto(2, 0)], self.app.cena.objetos[0].vertices)
                    variavel.set(antigo)
        self.app.dx.set(1)
        self.app.dy.set(2)
        self.app.aplicar_translacao()
        self.assertEqual(set(), self.pixels_visiveis())
        self.assertIsNotNone(self.app._apos)
        self.avancar()
        self.assertEqual({(-1, 2)}, self.pixels_visiveis())

    def test_redimensionar_preserva_animacao_e_coordenadas(self) -> None:
        self.desenhar("reta_bresenham", [(-2, 0), (2, 0)])
        self.avancar()
        self.raiz.geometry("1000x720")
        self.raiz.update_idletasks()
        self.assertEqual({(-2, 0)}, self.pixels_visiveis())
        self.avancar()
        self.assertEqual({(-2, 0), (-1, 0)}, self.pixels_visiveis())
        limites = self.app._limites_pixels_visiveis()
        numeros = [self.app.canvas.itemcget(item, "text") for item in self.app.canvas.find_withtag("numeros")]
        for valor in range(limites.xmin, limites.xmax + 1):
            self.assertIn(str(valor), numeros)
        for valor in range(limites.ymin, limites.ymax + 1):
            self.assertIn(str(valor), numeros)


if __name__ == "__main__":
    unittest.main()
