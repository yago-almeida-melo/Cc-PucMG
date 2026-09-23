"""Interface gráfica do TP1-CG, orientada à interação por mouse."""

from __future__ import annotations

import tkinter as tk
from math import ceil, floor
from tkinter import colorchooser, ttk

from modelos import AlgoritmoReta, Cena, ObjetoGrafico, Ponto, Preenchimento, Retangulo, TipoObjeto
from preenchimento import calcular_preenchimento
from rasterizacao import rasterizar_objeto
from recorte import cohen_sutherland, liang_barsky, objeto_intersecta_retangulo, recortar_segmentos
from transformacoes import (
    centro_da_selecao,
    escala,
    escalar_objeto,
    reflexao,
    rotacao,
    transformar_objeto,
    translacao,
)


class App:
    """Aplicação principal do laboratório de Computação Gráfica."""

    COR_FUNDO = "#eef2f6"
    COR_PAINEL = "#f8fafc"
    COR_TINTA = "#10243e"
    COR_SECUNDARIA = "#53657d"
    COR_DESTAQUE = "#f05d23"
    COR_AZUL = "#246bfd"
    COR_GRADE = "#d9e1ea"
    COR_EIXO = "#9cabbc"
    ESCALA_PIXEL = 2

    def __init__(self, raiz: tk.Tk) -> None:
        self.raiz = raiz
        self.raiz.title("TP1-CG — Rasterização, Preenchimento, Transformações e Recorte")
        self.raiz.geometry("1420x820")
        self.raiz.minsize(1120, 700)
        self.raiz.configure(bg=self.COR_FUNDO)

        self.cena = Cena()
        self.historico: list[Cena] = []
        self.modo = "reta_bresenham"
        self.pontos_temporarios: list[Ponto] = []
        self.inicio_arraste: Ponto | None = None
        self.fim_arraste: Ponto | None = None
        self.posicao_mouse: Ponto | None = None
        self.pivo_no_centro = True
        self.botoes_modo: dict[str, tk.Button] = {}
        self.cor_desenho = self.COR_TINTA
        self.cor_preenchimento = self.COR_AZUL
        self.cor_borda = self.COR_TINTA
        self.botoes_cor: dict[str, tk.Button] = {}

        self.dx = tk.DoubleVar(value=20)
        self.dy = tk.DoubleVar(value=20)
        self.angulo = tk.DoubleVar(value=30)
        self.sx = tk.DoubleVar(value=1.25)
        self.sy = tk.DoubleVar(value=1.25)
        self.status = tk.StringVar(value="Escolha uma ferramenta e clique na área de desenho.")
        self.coordenadas = tk.StringVar(value="x: —  y: —")
        self.resumo_selecao = tk.StringVar(value="Nenhum objeto selecionado")
        self.texto_pivo = tk.StringVar(value="Pivô: centro da seleção")

        self._configurar_estilos()
        self._construir_layout()
        self._vincular_eventos()
        self._atualizar_modo_visual()
        self.redesenhar()

    def _configurar_estilos(self) -> None:
        estilo = ttk.Style(self.raiz)
        try:
            estilo.theme_use("clam")
        except tk.TclError:
            pass
        estilo.configure("TFrame", background=self.COR_FUNDO)
        estilo.configure("Painel.TFrame", background=self.COR_PAINEL)
        estilo.configure(
            "Titulo.TLabel",
            background=self.COR_FUNDO,
            foreground=self.COR_TINTA,
            font=("Segoe UI", 18, "bold"),
        )
        estilo.configure(
            "Subtitulo.TLabel",
            background=self.COR_FUNDO,
            foreground=self.COR_SECUNDARIA,
            font=("Segoe UI", 9),
        )
        estilo.configure(
            "Painel.TLabel",
            background=self.COR_PAINEL,
            foreground=self.COR_TINTA,
            font=("Segoe UI", 9),
        )
        estilo.configure(
            "Secao.TLabel",
            background=self.COR_PAINEL,
            foreground=self.COR_TINTA,
            font=("Segoe UI", 11, "bold"),
        )
        estilo.configure("TButton", font=("Segoe UI", 9), padding=(8, 6))
        estilo.configure("Acao.TButton", font=("Segoe UI", 9, "bold"), padding=(8, 7))

    def _construir_layout(self) -> None:
        cabecalho = ttk.Frame(self.raiz, padding=(16, 10, 16, 8))
        cabecalho.pack(fill="x")
        bloco_titulo = ttk.Frame(cabecalho)
        bloco_titulo.pack(side="left")
        ttk.Label(bloco_titulo, text="TP1-CG", style="Titulo.TLabel").pack(anchor="w")
        ttk.Label(
            bloco_titulo,
            text="Laboratório interativo de algoritmos gráficos 2D",
            style="Subtitulo.TLabel",
        ).pack(anchor="w")

        acoes = ttk.Frame(cabecalho)
        acoes.pack(side="right")
        ttk.Button(acoes, text="↶ Desfazer", command=self.desfazer).pack(side="left", padx=3)
        ttk.Button(acoes, text="? Ajuda", command=self.mostrar_ajuda).pack(side="left", padx=3)
        ttk.Button(acoes, text="Limpar cena", command=self.limpar_cena).pack(side="left", padx=3)

        barra = tk.Frame(self.raiz, bg="#dfe6ee", padx=12, pady=8)
        barra.pack(fill="x")
        ferramentas = [
            ("ponto", "•  Ponto"),
            ("reta_dda", "╱  Reta DDA"),
            ("reta_bresenham", "╱  Reta Bresenham"),
            ("circunferencia", "○  Circunferência"),
            ("poligono", "⬠  Polígono"),
            ("selecao", "▧  Selecionar"),
            ("janela", "⌗  Janela de recorte"),
        ]
        for modo, texto in ferramentas:
            botao = tk.Button(
                barra,
                text=texto,
                command=lambda valor=modo: self.definir_modo(valor),
                relief="flat",
                borderwidth=0,
                padx=11,
                pady=7,
                font=("Segoe UI", 9, "bold"),
                cursor="hand2",
            )
            botao.pack(side="left", padx=2)
            self.botoes_modo[modo] = botao

        self.botao_concluir = tk.Button(
            barra,
            text="✓ Concluir polígono",
            command=self.concluir_poligono,
            relief="flat",
            borderwidth=0,
            padx=11,
            pady=7,
            bg=self.COR_DESTAQUE,
            fg="white",
            activebackground="#d94b17",
            activeforeground="white",
            font=("Segoe UI", 9, "bold"),
            cursor="hand2",
        )

        corpo = ttk.Frame(self.raiz, padding=(12, 10, 12, 8))
        corpo.pack(fill="both", expand=True)

        moldura_canvas = tk.Frame(corpo, bg="#bcc8d6", padx=1, pady=1)
        moldura_canvas.pack(side="left", fill="both", expand=True)
        self.canvas = tk.Canvas(
            moldura_canvas,
            bg="white",
            highlightthickness=0,
            cursor="crosshair",
            takefocus=False,
        )
        self.canvas.pack(fill="both", expand=True)

        painel_externo = ttk.Frame(corpo, style="Painel.TFrame", width=330)
        painel_externo.pack(side="right", fill="y", padx=(10, 0))
        painel_externo.pack_propagate(False)

        painel_canvas = tk.Canvas(
            painel_externo,
            width=310,
            bg=self.COR_PAINEL,
            highlightthickness=0,
            borderwidth=0,
        )
        rolagem = ttk.Scrollbar(painel_externo, orient="vertical", command=painel_canvas.yview)
        self.painel = ttk.Frame(painel_canvas, style="Painel.TFrame", padding=14)
        janela_painel = painel_canvas.create_window((0, 0), window=self.painel, anchor="nw", width=310)
        painel_canvas.configure(yscrollcommand=rolagem.set)
        painel_canvas.pack(side="left", fill="both", expand=True)
        rolagem.pack(side="right", fill="y")
        self.painel.bind(
            "<Configure>",
            lambda _evento: painel_canvas.configure(scrollregion=painel_canvas.bbox("all")),
        )
        painel_canvas.bind(
            "<Configure>",
            lambda evento: painel_canvas.itemconfigure(janela_painel, width=evento.width),
        )
        painel_canvas.bind_all(
            "<MouseWheel>",
            lambda evento: painel_canvas.yview_scroll(int(-evento.delta / 120), "units"),
        )

        self._construir_painel()

        rodape = tk.Frame(self.raiz, bg=self.COR_TINTA, padx=14, pady=7)
        rodape.pack(fill="x")
        tk.Label(
            rodape,
            textvariable=self.status,
            bg=self.COR_TINTA,
            fg="white",
            anchor="w",
            font=("Segoe UI", 9),
        ).pack(side="left", fill="x", expand=True)
        tk.Label(
            rodape,
            textvariable=self.coordenadas,
            bg=self.COR_TINTA,
            fg="#bcd0e8",
            font=("Consolas", 9),
        ).pack(side="right")

    def _construir_painel(self) -> None:
        ttk.Label(self.painel, text="CORES E PREENCHIMENTO", style="Secao.TLabel").pack(anchor="w")
        for atributo, rotulo in (
            ("cor_desenho", "Desenho"),
            ("cor_preenchimento", "Preenchimento"),
            ("cor_borda", "Borda (Boundary-Fill)"),
        ):
            linha = ttk.Frame(self.painel, style="Painel.TFrame")
            linha.pack(fill="x", pady=(5, 0))
            ttk.Label(linha, text=rotulo, style="Painel.TLabel").pack(side="left")
            botao = tk.Button(
                linha,
                command=lambda valor=atributo: self.escolher_cor(valor),
                width=9,
                relief="flat",
                cursor="hand2",
                font=("Consolas", 9, "bold"),
            )
            botao.pack(side="right")
            self.botoes_cor[atributo] = botao
            self._atualizar_botao_cor(atributo)
        ttk.Button(
            self.painel,
            text="Aplicar cor do desenho à seleção",
            command=self.aplicar_cor_selecionados,
        ).pack(fill="x", pady=(7, 5))
        linha_preenchimento = ttk.Frame(self.painel, style="Painel.TFrame")
        linha_preenchimento.pack(fill="x")
        for modo, texto in (("boundary_fill", "Boundary-Fill"), ("flood_fill", "Flood-Fill")):
            botao = tk.Button(
                linha_preenchimento,
                text=texto,
                command=lambda valor=modo: self.definir_modo(valor),
                relief="flat",
                borderwidth=0,
                pady=7,
                font=("Segoe UI", 9, "bold"),
                cursor="hand2",
            )
            botao.pack(side="left", expand=True, fill="x", padx=2)
            self.botoes_modo[modo] = botao
        ttk.Label(
            self.painel,
            text="Escolha o algoritmo e clique na região. No Boundary-Fill, a cor da borda deve coincidir com o contorno.",
            style="Painel.TLabel",
            wraplength=275,
        ).pack(anchor="w", pady=(5, 0))

        self._separador()
        ttk.Label(self.painel, text="SELEÇÃO", style="Secao.TLabel").pack(anchor="w")
        ttk.Label(
            self.painel,
            text="Arraste um retângulo sobre os objetos.",
            style="Painel.TLabel",
            wraplength=275,
        ).pack(anchor="w", pady=(2, 5))
        ttk.Label(
            self.painel,
            textvariable=self.resumo_selecao,
            style="Painel.TLabel",
            wraplength=275,
        ).pack(anchor="w", pady=(0, 6))
        linha_selecao = ttk.Frame(self.painel, style="Painel.TFrame")
        linha_selecao.pack(fill="x")
        ttk.Button(linha_selecao, text="Todos", command=self.selecionar_todos).pack(
            side="left", expand=True, fill="x"
        )
        ttk.Button(linha_selecao, text="Nenhum", command=self.desselecionar_todos).pack(
            side="left", expand=True, fill="x", padx=4
        )
        ttk.Button(linha_selecao, text="Excluir", command=self.excluir_selecionados).pack(
            side="left", expand=True, fill="x"
        )

        self._separador()
        ttk.Label(self.painel, text="TRANSLAÇÃO", style="Secao.TLabel").pack(anchor="w")
        self._controle_deslizante("Deslocamento X", self.dx, -150, 150, 1)
        self._controle_deslizante("Deslocamento Y", self.dy, -150, 150, 1)
        ttk.Button(
            self.painel,
            text="Aplicar translação",
            style="Acao.TButton",
            command=self.aplicar_translacao,
        ).pack(fill="x", pady=(4, 0))

        self._separador()
        ttk.Label(self.painel, text="ROTAÇÃO", style="Secao.TLabel").pack(anchor="w")
        self._controle_deslizante("Ângulo (graus)", self.angulo, -180, 180, 1)
        ttk.Button(
            self.painel,
            text="Aplicar rotação",
            style="Acao.TButton",
            command=self.aplicar_rotacao,
        ).pack(fill="x", pady=(4, 0))

        self._separador()
        ttk.Label(self.painel, text="ESCALA", style="Secao.TLabel").pack(anchor="w")
        self._controle_deslizante("Fator X", self.sx, 0.25, 3.0, 0.05)
        self._controle_deslizante("Fator Y", self.sy, 0.25, 3.0, 0.05)
        ttk.Button(
            self.painel,
            text="Aplicar escala",
            style="Acao.TButton",
            command=self.aplicar_escala,
        ).pack(fill="x", pady=(4, 0))
        ttk.Button(
            self.painel,
            textvariable=self.texto_pivo,
            command=self.alternar_pivo,
        ).pack(fill="x", pady=(5, 0))

        self._separador()
        ttk.Label(self.painel, text="REFLEXÕES", style="Secao.TLabel").pack(anchor="w")
        linha_reflexao = ttk.Frame(self.painel, style="Painel.TFrame")
        linha_reflexao.pack(fill="x", pady=(5, 0))
        for eixo in ("X", "Y", "XY"):
            ttk.Button(
                linha_reflexao,
                text=f"Eixo {eixo}",
                command=lambda valor=eixo: self.aplicar_reflexao(valor),
            ).pack(side="left", expand=True, fill="x", padx=2)

        self._separador()
        ttk.Label(self.painel, text="RECORTE", style="Secao.TLabel").pack(anchor="w")
        ttk.Label(
            self.painel,
            text="Defina a janela, selecione retas/polígonos e escolha o algoritmo.",
            style="Painel.TLabel",
            wraplength=275,
        ).pack(anchor="w", pady=(2, 6))
        ttk.Button(
            self.painel,
            text="Cohen–Sutherland",
            style="Acao.TButton",
            command=lambda: self.aplicar_recorte("cohen"),
        ).pack(fill="x")
        ttk.Button(
            self.painel,
            text="Liang–Barsky",
            style="Acao.TButton",
            command=lambda: self.aplicar_recorte("liang"),
        ).pack(fill="x", pady=(5, 0))

    def _controle_deslizante(
        self,
        rotulo: str,
        variavel: tk.DoubleVar,
        minimo: float,
        maximo: float,
        resolucao: float,
    ) -> None:
        ttk.Label(self.painel, text=rotulo, style="Painel.TLabel").pack(anchor="w", pady=(5, 0))
        controle = tk.Scale(
            self.painel,
            from_=minimo,
            to=maximo,
            resolution=resolucao,
            orient="horizontal",
            variable=variavel,
            showvalue=True,
            bg=self.COR_PAINEL,
            fg=self.COR_TINTA,
            troughcolor="#dce5ef",
            activebackground=self.COR_AZUL,
            highlightthickness=0,
            borderwidth=0,
            sliderlength=18,
            font=("Segoe UI", 8),
        )
        controle.pack(fill="x")

    def _separador(self) -> None:
        ttk.Separator(self.painel, orient="horizontal").pack(fill="x", pady=12)

    def _atualizar_botao_cor(self, atributo: str) -> None:
        cor = getattr(self, atributo)
        r, g, b = (int(cor[i:i + 2], 16) for i in (1, 3, 5))
        contraste = "#10243e" if 0.299 * r + 0.587 * g + 0.114 * b > 150 else "white"
        self.botoes_cor[atributo].configure(
            text=cor.upper(), bg=cor, fg=contraste,
            activebackground=cor, activeforeground=contraste,
        )

    def escolher_cor(self, atributo: str) -> None:
        titulos = {
            "cor_desenho": "Cor do desenho",
            "cor_preenchimento": "Cor do preenchimento",
            "cor_borda": "Cor da borda para Boundary-Fill",
        }
        _, cor = colorchooser.askcolor(
            color=getattr(self, atributo), title=titulos[atributo], parent=self.raiz,
        )
        if cor is None:
            return
        setattr(self, atributo, cor.lower())
        self._atualizar_botao_cor(atributo)
        self.status.set(f"{titulos[atributo]}: {cor.upper()}.")
        self.redesenhar()

    def aplicar_cor_selecionados(self) -> None:
        selecionados = self._obter_selecionados("alterar a cor")
        if not selecionados:
            return
        if all(objeto.cor == self.cor_desenho for objeto in selecionados):
            self.status.set("A seleção já possui a cor escolhida.")
            return
        self._registrar_estado()
        for objeto in selecionados:
            objeto.cor = self.cor_desenho
        self.status.set(f"Cor {self.cor_desenho.upper()} aplicada a {len(selecionados)} objeto(s).")
        self.redesenhar()

    def _vincular_eventos(self) -> None:
        self.canvas.bind("<ButtonPress-1>", self.ao_pressionar)
        self.canvas.bind("<B1-Motion>", self.ao_arrastar)
        self.canvas.bind("<ButtonRelease-1>", self.ao_soltar)
        self.canvas.bind("<Motion>", self.ao_mover)
        self.canvas.bind("<Button-3>", lambda _evento: self.concluir_poligono())
        self.canvas.bind("<Configure>", lambda _evento: self.redesenhar())
        self.raiz.bind("<Control-z>", lambda _evento: self.desfazer())
        self.raiz.bind("<Escape>", lambda _evento: self.cancelar_operacao())

    def definir_modo(self, modo: str) -> None:
        if self.modo == "poligono" and modo != "poligono" and self.pontos_temporarios:
            self.pontos_temporarios.clear()
        self.modo = modo
        self.inicio_arraste = None
        self.fim_arraste = None
        self.pontos_temporarios.clear()
        instrucoes = {
            "ponto": "Clique para inserir um ponto.",
            "reta_dda": "Clique nas duas extremidades da reta DDA.",
            "reta_bresenham": "Clique nas duas extremidades da reta Bresenham.",
            "circunferencia": "Clique no centro e depois em um ponto do raio.",
            "poligono": "Clique nos vértices e use “Concluir polígono” ou o botão direito.",
            "selecao": "Arraste uma região retangular para selecionar por interseção.",
            "janela": "Arraste para definir a janela de recorte.",
            "boundary_fill": "Boundary-Fill: escolha a cor da borda e clique dentro de um contorno fechado.",
            "flood_fill": "Flood-Fill: clique para substituir uma região conectada da mesma cor.",
        }
        self.status.set(instrucoes[modo])
        self._atualizar_modo_visual()
        self.redesenhar()

    def _atualizar_modo_visual(self) -> None:
        for modo, botao in self.botoes_modo.items():
            ativo = modo == self.modo
            botao.configure(
                bg=self.COR_AZUL if ativo else "#f5f7fa",
                fg="white" if ativo else self.COR_TINTA,
                activebackground=self.COR_AZUL if ativo else "#e7edf4",
                activeforeground="white" if ativo else self.COR_TINTA,
            )
        if self.modo == "poligono":
            self.botao_concluir.pack(side="right", padx=4)
        else:
            self.botao_concluir.pack_forget()

    def _canvas_para_mundo(self, x: float, y: float) -> Ponto:
        origem_x = self.canvas.winfo_width() / 2
        origem_y = self.canvas.winfo_height() / 2
        return Ponto(
            round((x - origem_x) / self.ESCALA_PIXEL),
            round((origem_y - y) / self.ESCALA_PIXEL),
        )

    def _mundo_para_canvas(self, ponto: Ponto) -> tuple[float, float]:
        origem_x = self.canvas.winfo_width() / 2
        origem_y = self.canvas.winfo_height() / 2
        return (
            origem_x + ponto.x * self.ESCALA_PIXEL,
            origem_y - ponto.y * self.ESCALA_PIXEL,
        )

    def ao_pressionar(self, evento: tk.Event) -> None:
        ponto = self._canvas_para_mundo(evento.x, evento.y)
        if self.modo in ("boundary_fill", "flood_fill"):
            self.aplicar_preenchimento(ponto)
            return
        if self.modo in ("selecao", "janela"):
            self.inicio_arraste = ponto
            self.fim_arraste = ponto
            self.redesenhar()
            return
        if self.modo == "ponto":
            self._registrar_estado()
            self.cena.adicionar(TipoObjeto.PONTO, [ponto], cor=self.cor_desenho)
            self.status.set(f"Ponto inserido em ({ponto.x:.0f}, {ponto.y:.0f}).")
            self.redesenhar()
            return

        self.pontos_temporarios.append(ponto)
        if self.modo in ("reta_dda", "reta_bresenham") and len(self.pontos_temporarios) == 2:
            algoritmo = AlgoritmoReta.DDA if self.modo == "reta_dda" else AlgoritmoReta.BRESENHAM
            self._registrar_estado()
            self.cena.adicionar(
                TipoObjeto.RETA, self.pontos_temporarios, algoritmo=algoritmo, cor=self.cor_desenho,
            )
            self.pontos_temporarios = []
            self.status.set(f"Reta rasterizada com {algoritmo.value}.")
        elif self.modo == "circunferencia" and len(self.pontos_temporarios) == 2:
            if self.pontos_temporarios[0] == self.pontos_temporarios[1]:
                self.pontos_temporarios.pop()
                self.status.set("O raio precisa ser maior que zero. Clique novamente na borda.")
            else:
                self._registrar_estado()
                self.cena.adicionar(
                    TipoObjeto.CIRCUNFERENCIA, self.pontos_temporarios, cor=self.cor_desenho,
                )
                self.pontos_temporarios = []
                self.status.set("Circunferência rasterizada com Bresenham.")
        elif self.modo == "poligono":
            self.status.set(
                f"{len(self.pontos_temporarios)} vértice(s). Continue clicando ou conclua o polígono."
            )
        self.redesenhar()

    def ao_arrastar(self, evento: tk.Event) -> None:
        if self.inicio_arraste is not None:
            self.fim_arraste = self._canvas_para_mundo(evento.x, evento.y)
            self.redesenhar()

    def ao_soltar(self, evento: tk.Event) -> None:
        if self.inicio_arraste is None or self.modo not in ("selecao", "janela"):
            return
        self.fim_arraste = self._canvas_para_mundo(evento.x, evento.y)
        regiao = Retangulo.de_pontos(self.inicio_arraste, self.fim_arraste)
        if regiao.xmax - regiao.xmin < 2 or regiao.ymax - regiao.ymin < 2:
            self.status.set("Arraste uma região maior para concluir a operação.")
        elif self.modo == "selecao":
            quantidade = 0
            for objeto in self.cena.objetos:
                objeto.selecionado = objeto_intersecta_retangulo(objeto, regiao)
                quantidade += int(objeto.selecionado)
            self.status.set(f"{quantidade} objeto(s) selecionado(s) pela região.")
            self._atualizar_resumo_selecao()
        else:
            self._registrar_estado()
            self.cena.janela_recorte = regiao
            self.status.set("Janela de recorte definida. Agora selecione os objetos a recortar.")
        self.inicio_arraste = None
        self.fim_arraste = None
        self.redesenhar()

    def ao_mover(self, evento: tk.Event) -> None:
        self.posicao_mouse = self._canvas_para_mundo(evento.x, evento.y)
        self.coordenadas.set(f"x: {self.posicao_mouse.x:>4.0f}  y: {self.posicao_mouse.y:>4.0f}")
        if self.pontos_temporarios:
            self.redesenhar()

    def concluir_poligono(self) -> None:
        if self.modo != "poligono":
            return
        if len(self.pontos_temporarios) < 3:
            self.status.set("Um polígono precisa de pelo menos três vértices.")
            return
        self._registrar_estado()
        self.cena.adicionar(TipoObjeto.POLIGONO, self.pontos_temporarios, cor=self.cor_desenho)
        quantidade = len(self.pontos_temporarios)
        self.pontos_temporarios = []
        self.status.set(f"Polígono com {quantidade} vértices concluído.")
        self.redesenhar()

    def cancelar_operacao(self) -> None:
        self.pontos_temporarios.clear()
        self.inicio_arraste = None
        self.fim_arraste = None
        self.status.set("Operação em andamento cancelada.")
        self.redesenhar()

    def _limites_pixels_visiveis(self) -> Retangulo:
        meio_x = self.canvas.winfo_width() / (2 * self.ESCALA_PIXEL)
        meio_y = self.canvas.winfo_height() / (2 * self.ESCALA_PIXEL)
        return Retangulo(ceil(-meio_x), ceil(-meio_y), floor(meio_x), floor(meio_y))

    def aplicar_preenchimento(self, semente: Ponto) -> None:
        if self.modo == "boundary_fill" and self.cor_preenchimento == self.cor_borda:
            self.status.set("No Boundary-Fill, escolha cores diferentes para a borda e o preenchimento.")
            return
        faixas = calcular_preenchimento(
            self.cena, semente, self._limites_pixels_visiveis(),
            self.modo, self.cor_preenchimento, self.cor_borda,
        )
        if not faixas:
            self.status.set("Nenhum pixel alterado. Confira a cor escolhida e o ponto clicado.")
            return
        self._registrar_estado()
        self.cena.adicionar_preenchimento(self.cor_preenchimento, faixas)
        quantidade = sum(fim - inicio + 1 for _, inicio, fim in faixas)
        nome = "Boundary-Fill" if self.modo == "boundary_fill" else "Flood-Fill"
        self.status.set(f"{nome}: {quantidade} pixel(s) preenchido(s). Use Desfazer para restaurar.")
        self.redesenhar()

    def selecionar_todos(self) -> None:
        for objeto in self.cena.objetos:
            objeto.selecionado = True
        self._atualizar_resumo_selecao()
        self.status.set(f"Todos os {len(self.cena.objetos)} objetos foram selecionados.")
        self.redesenhar()

    def desselecionar_todos(self) -> None:
        for objeto in self.cena.objetos:
            objeto.selecionado = False
        self._atualizar_resumo_selecao()
        self.status.set("Seleção removida.")
        self.redesenhar()

    def excluir_selecionados(self) -> None:
        quantidade = len(self.cena.selecionados())
        if not quantidade:
            self.status.set("Não há objetos selecionados para excluir.")
            return
        self._registrar_estado()
        self.cena.objetos = [objeto for objeto in self.cena.objetos if not objeto.selecionado]
        self._atualizar_resumo_selecao()
        self.status.set(f"{quantidade} objeto(s) excluído(s). Use Desfazer para restaurar.")
        self.redesenhar()

    def aplicar_translacao(self) -> None:
        selecionados = self._obter_selecionados("transladar")
        if not selecionados:
            return
        self._registrar_estado()
        matriz = translacao(self.dx.get(), self.dy.get())
        for objeto in selecionados:
            transformar_objeto(objeto, matriz)
        self.status.set(f"Translação ({self.dx.get():.0f}, {self.dy.get():.0f}) aplicada.")
        self.redesenhar()

    def aplicar_rotacao(self) -> None:
        selecionados = self._obter_selecionados("rotacionar")
        if not selecionados:
            return
        self._registrar_estado()
        pivo = centro_da_selecao(selecionados) if self.pivo_no_centro else Ponto(0, 0)
        matriz = rotacao(self.angulo.get(), pivo)
        for objeto in selecionados:
            transformar_objeto(objeto, matriz)
        self.status.set(f"Rotação de {self.angulo.get():.0f}° aplicada.")
        self.redesenhar()

    def aplicar_escala(self) -> None:
        selecionados = self._obter_selecionados("escalar")
        if not selecionados:
            return
        self._registrar_estado()
        pivo = centro_da_selecao(selecionados) if self.pivo_no_centro else Ponto(0, 0)
        for objeto in selecionados:
            escalar_objeto(objeto, self.sx.get(), self.sy.get(), pivo)
        self.status.set(f"Escala ({self.sx.get():.2f}, {self.sy.get():.2f}) aplicada.")
        self.redesenhar()

    def aplicar_reflexao(self, eixo: str) -> None:
        selecionados = self._obter_selecionados("refletir")
        if not selecionados:
            return
        self._registrar_estado()
        matriz = reflexao(eixo)
        for objeto in selecionados:
            transformar_objeto(objeto, matriz)
        self.status.set(f"Reflexão no eixo {eixo} aplicada em relação à origem.")
        self.redesenhar()

    def alternar_pivo(self) -> None:
        self.pivo_no_centro = not self.pivo_no_centro
        texto = "centro da seleção" if self.pivo_no_centro else "origem (0, 0)"
        self.texto_pivo.set(f"Pivô: {texto}")
        self.status.set(f"Pivô de rotação e escala alterado para {texto}.")

    def aplicar_recorte(self, nome_algoritmo: str) -> None:
        if self.cena.janela_recorte is None:
            self.status.set("Primeiro defina uma janela com a ferramenta “Janela de recorte”.")
            return
        selecionados_validos = [
            objeto
            for objeto in self.cena.selecionados()
            if objeto.tipo in (TipoObjeto.RETA, TipoObjeto.POLIGONO)
        ]
        if not selecionados_validos:
            self.status.set("Selecione ao menos uma reta ou polígono para recortar.")
            return
        self._registrar_estado()
        funcao = cohen_sutherland if nome_algoritmo == "cohen" else liang_barsky
        nome_exibicao = "Cohen–Sutherland" if nome_algoritmo == "cohen" else "Liang–Barsky"
        self.cena.objetos, afetados = recortar_segmentos(
            self.cena.objetos,
            self.cena.janela_recorte,
            funcao,
            proximo_id=self.cena._proximo_id,
        )
        self.cena._proximo_id = max(
            (elemento.identificador for elemento in self.cena.elementos_em_ordem()), default=0
        ) + 1
        self._atualizar_resumo_selecao()
        self.status.set(f"{afetados} objeto(s) recortado(s) com {nome_exibicao}.")
        self.redesenhar()

    def _obter_selecionados(self, verbo: str) -> list[ObjetoGrafico]:
        selecionados = self.cena.selecionados()
        if not selecionados:
            self.status.set(f"Selecione ao menos um objeto antes de {verbo}.")
        return selecionados

    def _registrar_estado(self) -> None:
        self.historico.append(self.cena.copiar())
        if len(self.historico) > 30:
            self.historico.pop(0)

    def desfazer(self) -> None:
        if not self.historico:
            self.status.set("Não há ações para desfazer.")
            return
        self.cena = self.historico.pop()
        self.pontos_temporarios.clear()
        self._atualizar_resumo_selecao()
        self.status.set("Última alteração desfeita.")
        self.redesenhar()

    def limpar_cena(self) -> None:
        if not self.cena.objetos and not self.cena.preenchimentos and self.cena.janela_recorte is None:
            self.status.set("A cena já está vazia.")
            return
        self._registrar_estado()
        self.cena = Cena()
        self.pontos_temporarios.clear()
        self._atualizar_resumo_selecao()
        self.status.set("Cena limpa. Use Desfazer para restaurar.")
        self.redesenhar()

    def _atualizar_resumo_selecao(self) -> None:
        selecionados = self.cena.selecionados()
        if not selecionados:
            self.resumo_selecao.set("Nenhum objeto selecionado")
        elif len(selecionados) == 1:
            self.resumo_selecao.set(selecionados[0].nome)
        else:
            self.resumo_selecao.set(f"{len(selecionados)} objetos selecionados")

    def redesenhar(self) -> None:
        if not hasattr(self, "canvas"):
            return
        self.canvas.delete("all")
        self._desenhar_grade()
        if self.cena.janela_recorte:
            self._desenhar_retangulo(
                self.cena.janela_recorte,
                cor="#7a3ff2",
                largura=2,
                tracejado=(7, 4),
                preenchimento="#f3edff",
            )
        for elemento in self.cena.elementos_em_ordem():
            if isinstance(elemento, Preenchimento):
                self._desenhar_preenchimento(elemento)
            else:
                self._desenhar_objeto(elemento)
        self._desenhar_previa()

    def _desenhar_grade(self) -> None:
        largura = self.canvas.winfo_width()
        altura = self.canvas.winfo_height()
        origem_x, origem_y = largura / 2, altura / 2
        passo = 25 * self.ESCALA_PIXEL
        x = origem_x % passo
        while x < largura:
            self.canvas.create_line(x, 0, x, altura, fill=self.COR_GRADE)
            x += passo
        y = origem_y % passo
        while y < altura:
            self.canvas.create_line(0, y, largura, y, fill=self.COR_GRADE)
            y += passo
        self.canvas.create_line(0, origem_y, largura, origem_y, fill=self.COR_EIXO, width=2)
        self.canvas.create_line(origem_x, 0, origem_x, altura, fill=self.COR_EIXO, width=2)
        self.canvas.create_text(
            origem_x + 10,
            origem_y + 13,
            text="0",
            fill=self.COR_SECUNDARIA,
            font=("Segoe UI", 8),
        )
        self.canvas.create_text(
            largura - 13,
            origem_y - 11,
            text="X",
            fill=self.COR_SECUNDARIA,
            font=("Segoe UI", 9, "bold"),
        )
        self.canvas.create_text(
            origem_x + 13,
            12,
            text="Y",
            fill=self.COR_SECUNDARIA,
            font=("Segoe UI", 9, "bold"),
        )

    def _desenhar_objeto(self, objeto: ObjetoGrafico) -> None:
        cor = objeto.cor

        def plotar_pixel(x: int, y: int) -> None:
            cx, cy = self._mundo_para_canvas(Ponto(x, y))
            tamanho = self.ESCALA_PIXEL
            self.canvas.create_rectangle(
                cx - tamanho / 2,
                cy - tamanho / 2,
                cx + tamanho / 2,
                cy + tamanho / 2,
                fill=cor,
                outline=cor,
            )

        rasterizar_objeto(objeto, plotar_pixel)
        if objeto.selecionado:
            xmin, ymin, xmax, ymax = self._limites_objeto(objeto)
            a = self._mundo_para_canvas(Ponto(xmin, ymax))
            b = self._mundo_para_canvas(Ponto(xmax, ymin))
            self.canvas.create_rectangle(*a, *b, outline=self.COR_DESTAQUE, dash=(3, 3))

    def _desenhar_preenchimento(self, preenchimento: Preenchimento) -> None:
        metade = self.ESCALA_PIXEL / 2
        for y, inicio, fim in preenchimento.faixas:
            x0, cy = self._mundo_para_canvas(Ponto(inicio, y))
            x1, _ = self._mundo_para_canvas(Ponto(fim, y))
            self.canvas.create_rectangle(
                x0 - metade, cy - metade, x1 + metade, cy + metade,
                fill=preenchimento.cor, outline="",
            )

    def _limites_objeto(self, objeto: ObjetoGrafico) -> tuple[float, float, float, float]:
        if objeto.tipo == TipoObjeto.CIRCUNFERENCIA:
            centro, raio = objeto.vertices[0], objeto.raio
            return centro.x - raio, centro.y - raio, centro.x + raio, centro.y + raio
        xs = [ponto.x for ponto in objeto.vertices]
        ys = [ponto.y for ponto in objeto.vertices]
        margem = 2
        return min(xs) - margem, min(ys) - margem, max(xs) + margem, max(ys) + margem

    def _desenhar_previa(self) -> None:
        if self.inicio_arraste and self.fim_arraste:
            regiao = Retangulo.de_pontos(self.inicio_arraste, self.fim_arraste)
            cor = self.COR_AZUL if self.modo == "selecao" else "#7a3ff2"
            self._desenhar_retangulo(regiao, cor=cor, largura=2, tracejado=(5, 3))
            return
        if not self.pontos_temporarios:
            return

        pontos = list(self.pontos_temporarios)
        if self.posicao_mouse:
            pontos.append(self.posicao_mouse)
        for ponto in self.pontos_temporarios:
            x, y = self._mundo_para_canvas(ponto)
            self.canvas.create_oval(x - 4, y - 4, x + 4, y + 4, fill=self.COR_AZUL, outline="white")
        if len(pontos) < 2:
            return

        if self.modo == "circunferencia":
            previa = ObjetoGrafico(0, TipoObjeto.CIRCUNFERENCIA, pontos[:2], cor=self.cor_desenho)
        elif self.modo == "poligono" and len(pontos) >= 3:
            previa = ObjetoGrafico(0, TipoObjeto.POLIGONO, pontos, cor=self.cor_desenho)
        else:
            algoritmo = AlgoritmoReta.DDA if self.modo == "reta_dda" else AlgoritmoReta.BRESENHAM
            previa = ObjetoGrafico(0, TipoObjeto.RETA, pontos[-2:], algoritmo=algoritmo, cor=self.cor_desenho)

        def plotar_pixel(px: int, py: int) -> None:
            cx, cy = self._mundo_para_canvas(Ponto(px, py))
            self.canvas.create_rectangle(cx - 1, cy - 1, cx + 1, cy + 1, fill=self.cor_desenho, outline="")

        rasterizar_objeto(previa, plotar_pixel)

    def _desenhar_retangulo(
        self,
        retangulo: Retangulo,
        cor: str,
        largura: int,
        tracejado: tuple[int, int],
        preenchimento: str = "",
    ) -> None:
        superior_esquerdo = self._mundo_para_canvas(Ponto(retangulo.xmin, retangulo.ymax))
        inferior_direito = self._mundo_para_canvas(Ponto(retangulo.xmax, retangulo.ymin))
        self.canvas.create_rectangle(
            *superior_esquerdo,
            *inferior_direito,
            outline=cor,
            fill=preenchimento,
            stipple="gray12" if preenchimento else "",
            width=largura,
            dash=tracejado,
        )

    def mostrar_ajuda(self) -> None:
        janela = tk.Toplevel(self.raiz)
        janela.title("Como usar")
        janela.geometry("620x760")
        janela.configure(bg="white")
        janela.transient(self.raiz)
        conteudo = (
            "COMO DESENHAR\n\n"
            "Ponto: clique uma vez.\n"
            "Retas: clique nas duas extremidades e escolha DDA ou Bresenham.\n"
            "Circunferência: clique no centro e depois na borda.\n"
            "Polígono: clique em cada vértice e conclua pelo botão laranja ou botão direito.\n\n"
            "CORES E PREENCHIMENTO\n\n"
            "Clique nas amostras do painel para escolher as cores. A cor do desenho vale "
            "para novos objetos; use o botão de aplicar para mudar os selecionados.\n"
            "Boundary-Fill: escolha a cor do contorno fechado e clique dentro dele.\n"
            "Flood-Fill: clique para trocar a cor de uma região conectada.\n"
            "As pinturas ficam nos pixels originais; as transformações afetam os objetos vetoriais. "
            "Use Desfazer para remover uma pintura.\n\n"
            "COMO TRANSFORMAR\n\n"
            "Selecione objetos arrastando um retângulo. Ajuste os fatores pelos controles "
            "deslizantes e clique em Aplicar. Rotação e escala podem usar o centro da seleção "
            "ou a origem; reflexões X, Y e XY usam os eixos cartesianos visíveis.\n\n"
            "COMO RECORTAR\n\n"
            "1. Escolha Janela de recorte e arraste o retângulo roxo.\n"
            "2. Escolha Selecionar e arraste sobre retas ou polígonos.\n"
            "3. Clique em Cohen–Sutherland ou Liang–Barsky.\n\n"
            "A área branca é uma matriz lógica de pixels. Cada quadradinho colorido foi "
            "calculado pelos algoritmos do projeto, e não por uma primitiva de linha do Canvas."
        )
        tk.Label(
            janela,
            text="Guia rápido",
            bg="white",
            fg=self.COR_TINTA,
            font=("Segoe UI", 18, "bold"),
        ).pack(anchor="w", padx=24, pady=(22, 8))
        tk.Label(
            janela,
            text=conteudo,
            bg="white",
            fg=self.COR_TINTA,
            justify="left",
            anchor="nw",
            wraplength=560,
            font=("Segoe UI", 10),
        ).pack(fill="both", expand=True, padx=24)
        ttk.Button(janela, text="Entendi", command=janela.destroy).pack(pady=18)


def executar() -> None:
    raiz = tk.Tk()
    App(raiz)
    raiz.mainloop()
