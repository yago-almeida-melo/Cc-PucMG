import tkinter as tk
import unittest
from unittest.mock import patch

from interface import App
from modelos import Ponto, TipoObjeto
from preenchimento import matriz_da_cena


class TesteInterfacePreenchimento(unittest.TestCase):
    """Exercita eventos reais do Tk; use xvfb-run em ambientes sem monitor."""

    def setUp(self) -> None:
        try:
            self.raiz = tk.Tk()
        except tk.TclError as erro:
            self.skipTest(f"Display Tk indisponível: {erro}")
        self.app = App(self.raiz)
        self.addCleanup(self.app.fechar)
        self.app.delay.set(0)
        self.raiz.update()

    def clicar(self, x: int, y: int) -> None:
        cx, cy = self.app._mundo_para_canvas(Ponto(x, y))
        self.app.canvas.event_generate("<ButtonPress-1>", x=round(cx), y=round(cy))
        self.app.canvas.event_generate("<ButtonRelease-1>", x=round(cx), y=round(cy))

    def quadrado(self) -> None:
        self.app.definir_modo("poligono")
        for x, y in ((-3, -3), (3, -3), (3, 3), (-3, 3)):
            self.clicar(x, y)
        self.app.concluir_poligono()

    def test_preencher_recolorir_redesenhar_redimensionar_e_desfazer(self) -> None:
        self.quadrado()
        self.app.botoes_modo["boundary_fill"].invoke()
        self.clicar(0, 0)
        primeira_pintura = self.app.cena.preenchimentos[0]
        self.assertEqual(25, sum(b - a + 1 for _, a, b in primeira_pintura.faixas))
        self.app.cor_preenchimento = "#11aa33"
        self.app.botoes_modo["flood_fill"].invoke()
        self.clicar(0, 0)
        self.assertEqual(2, len(self.app.cena.preenchimentos))
        self.raiz.geometry("1120x700")
        self.raiz.update()
        self.app.redesenhar()
        pixels = matriz_da_cena(self.app.cena, self.app._limites_pixels_visiveis())
        self.assertEqual("#11aa33", pixels[0, 0])
        self.app.desfazer()
        self.assertEqual([primeira_pintura], self.app.cena.preenchimentos)
        self.app.desfazer()
        self.assertFalse(self.app.cena.preenchimentos)
        self.assertEqual(1, len(self.app.cena.objetos))

    def test_limpar_cena_apenas_com_pintura_e_desfazer(self) -> None:
        self.app.definir_modo("flood_fill")
        self.clicar(0, 0)
        self.assertTrue(self.app.cena.preenchimentos)
        self.app.limpar_cena()
        self.assertFalse(self.app.cena.preenchimentos)
        self.app.desfazer()
        self.assertTrue(self.app.cena.preenchimentos)

    def test_sem_alteracao_nao_cria_historico(self) -> None:
        self.quadrado()
        self.app.definir_modo("boundary_fill")
        quantidade = len(self.app.historico)
        self.clicar(3, 0)
        self.assertEqual(quantidade, len(self.app.historico))
        self.app.cor_preenchimento = self.app.cor_borda
        self.clicar(0, 0)
        self.assertEqual(quantidade, len(self.app.historico))
        self.app.definir_modo("flood_fill")
        self.app.cor_preenchimento = "#ffffff"
        self.clicar(0, 0)
        self.assertEqual(quantidade, len(self.app.historico))

    def test_cores_escolhidas_para_todas_as_primitivas_e_selecao(self) -> None:
        with patch("interface.colorchooser.askcolor", return_value=((170, 51, 255), "#AA33FF")):
            self.app.botoes_cor["cor_desenho"].invoke()
        for modo in ("ponto", "reta_dda", "reta_bresenham", "circunferencia"):
            self.app.definir_modo(modo)
            self.clicar(0, 0)
            if modo != "ponto":
                self.clicar(10, 10)
        self.quadrado()
        self.assertEqual(5, len(self.app.cena.objetos))
        self.assertTrue(all(obj.cor == "#aa33ff" for obj in self.app.cena.objetos))
        self.app.selecionar_todos()
        self.app.cor_desenho = "#00aa00"
        self.app.aplicar_cor_selecionados()
        self.assertTrue(all(obj.cor == "#00aa00" for obj in self.app.cena.objetos))
        self.app.desfazer()
        self.assertTrue(all(obj.cor == "#aa33ff" for obj in self.app.cena.objetos))

    def test_seletores_independentes_e_cancelamento(self) -> None:
        for atributo, cor in (("cor_preenchimento", "#aabbcc"), ("cor_borda", "#112233")):
            with patch("interface.colorchooser.askcolor", return_value=((0, 0, 0), cor)):
                self.app.botoes_cor[atributo].invoke()
            self.assertEqual(cor, getattr(self.app, atributo))
            with patch("interface.colorchooser.askcolor", return_value=(None, None)):
                self.app.botoes_cor[atributo].invoke()
            self.assertEqual(cor, getattr(self.app, atributo))
        self.assertEqual(self.app.COR_TINTA, self.app.cor_desenho)

    def test_recortar_depois_de_pintar_e_desenhar_preserva_ordem(self) -> None:
        self.quadrado()
        self.app.definir_modo("boundary_fill")
        self.clicar(0, 0)
        self.app.selecionar_todos()
        reta = self.app.cena.adicionar(TipoObjeto.RETA, [Ponto(-5, 0), Ponto(5, 0)])
        reta.selecionado = True
        self.app.cena.janela_recorte = self.app._limites_pixels_visiveis()
        self.app.aplicar_recorte("cohen")
        self.app.definir_modo("ponto")
        self.clicar(1, 1)
        elementos = self.app.cena.elementos_em_ordem()
        ids = [elemento.identificador for elemento in elementos]
        self.assertEqual(len(ids), len(set(ids)))
        self.assertEqual(TipoObjeto.PONTO, elementos[-1].tipo)


if __name__ == "__main__":
    unittest.main()
