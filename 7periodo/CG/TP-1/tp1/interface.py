"""Janela do programa e desenho dos pixels no plano cartesiano."""

import tkinter as tk
from collections import deque
from math import ceil, floor, isfinite
from tkinter import colorchooser, ttk

from modelos import AlgoritmoReta, Cena, ObjetoGrafico, Ponto, Preenchimento, Retangulo, TipoObjeto
from preenchimento import calcular_preenchimento
from rasterizacao import rasterizar_objeto
from recorte import cohen_sutherland, liang_barsky, objeto_intersecta_retangulo, recortar_segmentos
from transformacoes import centro_da_selecao, escalar_objeto, reflexao, rotacao, transformar_objeto, translacao


class App:
    ESCALA_PIXEL = 24
    COR_TINTA = "#10243e"

    def __init__(self, raiz: tk.Tk) -> None:
        self.raiz = raiz
        raiz.title("TP1 - Computação Gráfica")
        raiz.geometry("1280x800")
        raiz.minsize(1000, 720)
        self.cena = Cena()
        self.historico = []
        self.modo = "reta_bresenham"
        self.pontos_temporarios = []
        self.inicio_arraste = self.fim_arraste = self.posicao_mouse = None
        self.cor_desenho = self.cor_borda = self.COR_TINTA
        self.cor_preenchimento = "#246bfd"
        self.botoes_modo = {}
        self.botoes_cor = {}
        self.dx = tk.DoubleVar(value=2)
        self.dy = tk.DoubleVar(value=2)
        self.angulo = tk.DoubleVar(value=30)
        self.sx = tk.DoubleVar(value=1.25)
        self.sy = tk.DoubleVar(value=1.25)
        self.pivo_no_centro = tk.BooleanVar(value=True)
        self.conectividade = tk.IntVar(value=4)
        self.delay = tk.IntVar(value=30)
        self.status = tk.StringVar()
        self.coordenadas = tk.StringVar(value="x: 0  y: 0")
        self.resumo_selecao = tk.StringVar(value="Nenhum objeto selecionado")
        self._fila_pixels = deque()
        self._pixels_animados = {}
        self._apos = None
        self._construir_layout()
        self._vincular_eventos()
        self.definir_modo(self.modo)
        raiz.protocol("WM_DELETE_WINDOW", self.fechar)

    def _construir_layout(self) -> None:
        barra = ttk.Frame(self.raiz, padding=5)
        barra.pack(fill="x")
        for modo, texto in (
            ("ponto", "Ponto"), ("reta_dda", "Reta DDA"),
            ("reta_bresenham", "Reta Bresenham"), ("circunferencia", "Circunferência"),
            ("poligono", "Polígono"), ("selecao", "Selecionar"), ("janela", "Janela de recorte"),
        ):
            self._botao_modo(barra, modo, texto)
        ttk.Button(barra, text="Concluir polígono", command=self.concluir_poligono).pack(side="left")

        rodape = ttk.Frame(self.raiz, padding=5)
        rodape.pack(side="bottom", fill="x")
        ttk.Label(rodape, textvariable=self.status).pack(side="left")
        ttk.Label(rodape, textvariable=self.coordenadas).pack(side="right")
        painel = ttk.Frame(self.raiz, padding=8)
        painel.pack(side="right", fill="y")
        self.canvas = tk.Canvas(self.raiz, bg="white", highlightthickness=0, cursor="crosshair")
        self.canvas.pack(fill="both", expand=True)

        self._botoes(painel, [("Desfazer", self.desfazer), ("Limpar cena", self.limpar_cena)])
        ttk.Label(painel, text="Delay (ms) por pixel; 0 = imediato").pack(anchor="w", pady=(8, 0))
        tk.Scale(painel, from_=0, to=250, resolution=5, orient="horizontal", variable=self.delay).pack(fill="x")
        ttk.Button(painel, text="Reanimar cena", command=lambda: self._animar(self.cena.objetos)).pack(fill="x")
        cores = self._grupo(painel, "Cores e preenchimento")
        for atributo, rotulo in (("cor_desenho", "Desenho"), ("cor_preenchimento", "Preenchimento"), ("cor_borda", "Borda")):
            linha = ttk.Frame(cores)
            linha.pack(fill="x")
            ttk.Label(linha, text=rotulo).pack(side="left")
            botao = tk.Button(linha, width=9, command=lambda a=atributo: self.escolher_cor(a))
            botao.pack(side="right")
            self.botoes_cor[atributo] = botao
            self._atualizar_botao_cor(atributo)
        ttk.Button(cores, text="Aplicar cor do desenho à seleção", command=self.aplicar_cor_selecionados).pack(fill="x")
        linha = ttk.Frame(cores)
        linha.pack(fill="x", pady=3)
        ttk.Label(linha, text="Conectividade:").pack(side="left")
        for valor in (4, 8):
            ttk.Radiobutton(linha, text=str(valor), variable=self.conectividade, value=valor).pack(side="left")
        linha = ttk.Frame(cores)
        linha.pack(fill="x")
        self._botao_modo(linha, "flood_fill", "Flood-Fill")
        self._botao_modo(linha, "boundary_fill", "Boundary-Fill")

        selecao = self._grupo(painel, "Seleção")
        ttk.Label(selecao, textvariable=self.resumo_selecao).pack(anchor="w")
        self._botoes(selecao, [("Todos", self.selecionar_todos), ("Nenhum", self.desselecionar_todos), ("Excluir", self.excluir_selecionados)])
        transformar = self._grupo(painel, "Transformações")
        self._campos(transformar, [("dx", self.dx), ("dy", self.dy)], "Transladar", self.aplicar_translacao)
        self._campos(transformar, [("Ângulo", self.angulo)], "Rotacionar", self.aplicar_rotacao)
        self._campos(transformar, [("sx", self.sx), ("sy", self.sy)], "Escalar", self.aplicar_escala)
        ttk.Checkbutton(transformar, text="Pivô no centro (desmarcado: origem)", variable=self.pivo_no_centro).pack(anchor="w")
        self._botoes(transformar, [(f"Refletir {eixo}", lambda e=eixo: self.aplicar_reflexao(e)) for eixo in ("X", "Y", "XY")])
        recorte = self._grupo(painel, "Recorte de retas")
        ttk.Label(recorte, text="Defina a janela e selecione as retas.").pack(anchor="w")
        self._botoes(recorte, [("Cohen–Sutherland", lambda: self.aplicar_recorte("cohen")), ("Liang–Barsky", lambda: self.aplicar_recorte("liang"))])

    def _grupo(self, pai, texto):
        grupo = ttk.LabelFrame(pai, text=texto, padding=4)
        grupo.pack(fill="x", pady=2)
        return grupo

    def _botoes(self, pai, acoes) -> None:
        linha = ttk.Frame(pai)
        linha.pack(fill="x", pady=2)
        for texto, acao in acoes:
            ttk.Button(linha, text=texto, command=acao).pack(side="left", fill="x", expand=True)

    def _campos(self, pai, campos, texto, acao) -> None:
        linha = ttk.Frame(pai)
        linha.pack(fill="x", pady=3)
        for rotulo, variavel in campos:
            ttk.Label(linha, text=rotulo).pack(side="left")
            ttk.Entry(linha, textvariable=variavel, width=5).pack(side="left", padx=3)
        ttk.Button(linha, text=texto, command=acao).pack(side="right")

    def _botao_modo(self, pai, modo, texto) -> None:
        botao = ttk.Button(pai, text=texto, command=lambda: self.definir_modo(modo))
        botao.pack(side="left", fill="x", expand=True)
        self.botoes_modo[modo] = botao

    def _vincular_eventos(self) -> None:
        for evento, acao in (("<ButtonPress-1>", self.ao_pressionar), ("<B1-Motion>", self.ao_arrastar),
                             ("<ButtonRelease-1>", self.ao_soltar), ("<Motion>", self.ao_mover)):
            self.canvas.bind(evento, acao)
        self.canvas.bind("<Button-3>", lambda _: self.concluir_poligono())
        self.canvas.bind("<Configure>", lambda _: self.redesenhar())
        self.raiz.bind("<Control-z>", lambda _: self.desfazer())
        self.raiz.bind("<Escape>", lambda _: self.cancelar_operacao())

    def definir_modo(self, modo: str) -> None:
        self.modo = modo
        self.pontos_temporarios.clear()
        self.inicio_arraste = self.fim_arraste = None
        instrucoes = {
            "ponto": "Clique para inserir um ponto.",
            "reta_dda": "DDA: clique nas duas extremidades.",
            "reta_bresenham": "Bresenham: clique nas duas extremidades.",
            "circunferencia": "Clique no centro e depois na borda.",
            "poligono": "Clique nos vértices. Conclua pelo botão ou com o botão direito.",
            "selecao": "Arraste um retângulo sobre os objetos.",
            "janela": "Arraste para definir a janela de recorte das retas.",
            "flood_fill": "Flood-Fill: clique na região que deseja recolorir.",
            "boundary_fill": "Boundary-Fill: escolha a cor da borda e clique dentro da figura.",
        }
        self.status.set(instrucoes[modo])
        for nome, botao in self.botoes_modo.items():
            botao.state(["pressed"] if nome == modo else ["!pressed"])
        self.redesenhar()

    def _canvas_para_mundo(self, x, y) -> Ponto:
        return Ponto(round((x - self.canvas.winfo_width() / 2) / self.ESCALA_PIXEL),
                     round((self.canvas.winfo_height() / 2 - y) / self.ESCALA_PIXEL))

    def _mundo_para_canvas(self, ponto: Ponto) -> tuple[float, float]:
        return (self.canvas.winfo_width() / 2 + ponto.x * self.ESCALA_PIXEL,
                self.canvas.winfo_height() / 2 - ponto.y * self.ESCALA_PIXEL)

    def _limites_pixels_visiveis(self) -> Retangulo:
        x = self.canvas.winfo_width() / (2 * self.ESCALA_PIXEL)
        y = self.canvas.winfo_height() / (2 * self.ESCALA_PIXEL)
        return Retangulo(ceil(-x), ceil(-y), floor(x), floor(y))

    def ao_pressionar(self, evento) -> None:
        ponto = self._canvas_para_mundo(evento.x, evento.y)
        if self.modo in ("flood_fill", "boundary_fill"):
            self.aplicar_preenchimento(ponto)
        elif self.modo in ("selecao", "janela"):
            self.inicio_arraste = self.fim_arraste = ponto
        elif self.modo == "ponto":
            self._adicionar_objeto(TipoObjeto.PONTO, [ponto])
        else:
            self.pontos_temporarios.append(ponto)
            if self.modo != "poligono" and len(self.pontos_temporarios) == 2:
                tipo = TipoObjeto.CIRCUNFERENCIA if self.modo == "circunferencia" else TipoObjeto.RETA
                if tipo == TipoObjeto.CIRCUNFERENCIA and self.pontos_temporarios[0] == ponto:
                    self.pontos_temporarios.pop()
                    self.status.set("O raio precisa ser maior que zero.")
                else:
                    self._adicionar_objeto(tipo, self.pontos_temporarios)
            elif self.modo == "poligono":
                self.status.set(f"{len(self.pontos_temporarios)} vértices. Botão direito para concluir.")
        self.redesenhar()

    def _adicionar_objeto(self, tipo, vertices) -> None:
        self._registrar_estado()
        algoritmo = AlgoritmoReta.DDA if self.modo == "reta_dda" else AlgoritmoReta.BRESENHAM
        objeto = self.cena.adicionar(tipo, vertices, algoritmo=algoritmo, cor=self.cor_desenho)
        self.pontos_temporarios.clear()
        self.status.set(f"{objeto.nome} criado. Esc conclui a animação.")
        self._animar([objeto])

    def concluir_poligono(self) -> None:
        if self.modo != "poligono":
            return
        if len(self.pontos_temporarios) < 3:
            self.status.set("Escolha pelo menos três vértices.")
            return
        self._adicionar_objeto(TipoObjeto.POLIGONO, self.pontos_temporarios)

    def ao_arrastar(self, evento) -> None:
        if self.inicio_arraste is not None:
            self.fim_arraste = self._canvas_para_mundo(evento.x, evento.y)
            self.redesenhar()

    def ao_soltar(self, evento) -> None:
        if self.inicio_arraste is None:
            return
        regiao = Retangulo.de_pontos(self.inicio_arraste, self._canvas_para_mundo(evento.x, evento.y))
        if regiao.xmin == regiao.xmax or regiao.ymin == regiao.ymax:
            self.status.set("Arraste um retângulo com largura e altura.")
        elif self.modo == "selecao":
            for objeto in self.cena.objetos:
                objeto.selecionado = objeto_intersecta_retangulo(objeto, regiao)
            self._atualizar_resumo_selecao()
        elif self.modo == "janela":
            self._registrar_estado()
            self.cena.janela_recorte = regiao
            self.status.set("Janela definida. Selecione as retas e aplique o recorte.")
        self.inicio_arraste = self.fim_arraste = None
        self.redesenhar()

    def ao_mover(self, evento) -> None:
        self.posicao_mouse = self._canvas_para_mundo(evento.x, evento.y)
        self.coordenadas.set(f"x: {self.posicao_mouse.x}  y: {self.posicao_mouse.y}")
        if self.pontos_temporarios:
            self.redesenhar()

    def cancelar_operacao(self) -> None:
        self._parar_animacao()
        self.pontos_temporarios.clear()
        self.inicio_arraste = self.fim_arraste = None
        self.status.set("Prévia cancelada; desenhos concluídos.")
        self.redesenhar()

    def _atualizar_botao_cor(self, atributo) -> None:
        cor = getattr(self, atributo)
        r, g, b = (int(cor[i:i + 2], 16) for i in (1, 3, 5))
        texto = "black" if r + g + b > 400 else "white"
        self.botoes_cor[atributo].configure(bg=cor, text=cor, fg=texto)

    def escolher_cor(self, atributo) -> None:
        _, cor = colorchooser.askcolor(color=getattr(self, atributo), parent=self.raiz)
        if cor:
            setattr(self, atributo, cor.lower())
            self._atualizar_botao_cor(atributo)
            self.redesenhar()

    def aplicar_cor_selecionados(self) -> None:
        objetos = self.cena.selecionados()
        if not objetos or all(obj.cor == self.cor_desenho for obj in objetos):
            return
        self._registrar_estado()
        for objeto in objetos:
            objeto.cor = self.cor_desenho
        self.redesenhar()

    def aplicar_preenchimento(self, semente: Ponto) -> None:
        faixas = calcular_preenchimento(self.cena, semente, self._limites_pixels_visiveis(),
                                       self.modo, self.cor_preenchimento, self.cor_borda,
                                       conectividade=self.conectividade.get())
        if not faixas:
            self.status.set("Nenhum pixel alterado. Confira as cores e o ponto clicado.")
            return
        self._registrar_estado()
        self.cena.adicionar_preenchimento(self.cor_preenchimento, faixas)
        quantidade = sum(fim - inicio + 1 for _, inicio, fim in faixas)
        self.status.set(f"{quantidade} pixels preenchidos com conectividade {self.conectividade.get()}.")
        self.redesenhar()

    def selecionar_todos(self) -> None:
        self._selecionar(True)

    def desselecionar_todos(self) -> None:
        self._selecionar(False)

    def _selecionar(self, selecionado) -> None:
        for objeto in self.cena.objetos:
            objeto.selecionado = selecionado
        self._atualizar_resumo_selecao()
        self.redesenhar()

    def _atualizar_resumo_selecao(self) -> None:
        self.resumo_selecao.set(f"{len(self.cena.selecionados())} objeto(s) selecionado(s)")

    def excluir_selecionados(self) -> None:
        if self.cena.selecionados():
            self._registrar_estado()
            self.cena.objetos = [obj for obj in self.cena.objetos if not obj.selecionado]
            self._atualizar_resumo_selecao()
            self.redesenhar()

    def aplicar_translacao(self) -> None:
        self._transformar("translacao")

    def aplicar_rotacao(self) -> None:
        self._transformar("rotacao")

    def aplicar_escala(self) -> None:
        self._transformar("escala")

    def aplicar_reflexao(self, eixo: str) -> None:
        self._transformar(eixo)

    def _transformar(self, operacao) -> None:
        objetos = self.cena.selecionados()
        if not objetos:
            self.status.set("Selecione os objetos antes de transformar.")
            return
        pivo = centro_da_selecao(objetos) if self.pivo_no_centro.get() else Ponto(0, 0)
        try:
            if operacao == "escala":
                sx, sy = self.sx.get(), self.sy.get()
                valores = (sx, sy)
            elif operacao == "translacao":
                valores = (self.dx.get(), self.dy.get())
                matriz = translacao(*valores)
            elif operacao == "rotacao":
                valores = (self.angulo.get(),)
                matriz = rotacao(valores[0], pivo)
            else:
                valores = ()
                matriz = reflexao(operacao)
            if not all(isfinite(valor) for valor in valores):
                raise ValueError("Valor não finito")
        except (tk.TclError, ValueError):
            self.status.set("Informe números válidos; use ponto nas casas decimais.")
            return
        self._registrar_estado()
        for objeto in objetos:
            if operacao == "escala":
                escalar_objeto(objeto, sx, sy, pivo)
            else:
                transformar_objeto(objeto, matriz)
        self.status.set("Transformação aplicada.")
        self._animar(objetos)

    def aplicar_recorte(self, nome_algoritmo: str) -> None:
        if self.cena.janela_recorte is None:
            self.status.set("Defina primeiro a janela de recorte.")
            return
        if not any(obj.tipo == TipoObjeto.RETA for obj in self.cena.selecionados()):
            self.status.set("Selecione pelo menos uma reta para recortar.")
            return
        self._registrar_estado()
        funcao = cohen_sutherland if nome_algoritmo == "cohen" else liang_barsky
        self.cena.objetos, quantidade = recortar_segmentos(
            self.cena.objetos, self.cena.janela_recorte, funcao, proximo_id=self.cena._proximo_id,
        )
        self.cena._proximo_id = max(
            (elemento.identificador for elemento in self.cena.elementos_em_ordem()), default=0,
        ) + 1
        self._atualizar_resumo_selecao()
        self.status.set(f"{quantidade} reta(s) processada(s).")
        self._animar([obj for obj in self.cena.selecionados() if obj.tipo == TipoObjeto.RETA])

    def _registrar_estado(self) -> None:
        self._parar_animacao()
        self.historico.append(self.cena.copiar())
        self.historico = self.historico[-30:]

    def desfazer(self) -> None:
        if self.historico:
            self.cancelar_operacao()
            self.cena = self.historico.pop()
            self._atualizar_resumo_selecao()
            self.status.set("Última alteração desfeita.")
            self.redesenhar()

    def limpar_cena(self) -> None:
        if self.cena.objetos or self.cena.preenchimentos or self.cena.janela_recorte:
            self._registrar_estado()
            self.cena = Cena()
        self.cancelar_operacao()
        self._atualizar_resumo_selecao()
        self.status.set("Cena limpa.")

    def _animar(self, objetos) -> None:
        self._parar_animacao()
        if self.delay.get() > 0:
            for objeto in objetos:
                identificador = objeto.identificador
                self._pixels_animados[identificador] = []
                rasterizar_objeto(objeto, lambda x, y: self._fila_pixels.append((identificador, x, y)))
        self.redesenhar()
        if self._fila_pixels:
            self._apos = self.raiz.after(self.delay.get(), self._passo_animacao)

    def _passo_animacao(self) -> None:
        self._apos = None
        if self.delay.get() == 0:
            self._parar_animacao()
        elif self._fila_pixels:
            identificador, x, y = self._fila_pixels.popleft()
            self._pixels_animados[identificador].append((x, y))
        if self._fila_pixels:
            self._apos = self.raiz.after(self.delay.get(), self._passo_animacao)
        else:
            self._pixels_animados.clear()
        self.redesenhar()

    def _parar_animacao(self) -> None:
        if self._apos is not None:
            self.raiz.after_cancel(self._apos)
            self._apos = None
        self._fila_pixels.clear()
        self._pixels_animados.clear()

    def fechar(self) -> None:
        self._parar_animacao()
        self.raiz.destroy()

    def redesenhar(self) -> None:
        self.canvas.delete("all")
        self._desenhar_grade()
        for elemento in self.cena.elementos_em_ordem():
            if isinstance(elemento, Preenchimento):
                for y, inicio, fim in elemento.faixas:
                    for x in range(inicio, fim + 1):
                        self._plotar_pixel(x, y, elemento.cor)
            else:
                self._desenhar_objeto(elemento)
        if self.cena.janela_recorte:
            self._desenhar_retangulo(self.cena.janela_recorte, "#7a3ff2")
        self._desenhar_previa()
        self.canvas.tag_raise("eixos")
        self._desenhar_numeros()

    def _desenhar_grade(self) -> None:
        largura, altura = self.canvas.winfo_width(), self.canvas.winfo_height()
        ox, oy = largura / 2, altura / 2
        passo = self.ESCALA_PIXEL
        x, y = (ox - passo / 2) % passo, (oy - passo / 2) % passo
        while x < largura:
            self.canvas.create_line(x, 0, x, altura, fill="#e4e4e4")
            x += passo
        while y < altura:
            self.canvas.create_line(0, y, largura, y, fill="#e4e4e4")
            y += passo
        self.canvas.create_line(0, oy, largura, oy, fill="#888888", arrow="last", tags="eixos")
        self.canvas.create_line(ox, altura, ox, 0, fill="#888888", arrow="last", tags="eixos")

    def _desenhar_numeros(self) -> None:
        limites = self._limites_pixels_visiveis()
        ox, oy = self._mundo_para_canvas(Ponto(0, 0))
        for x in range(limites.xmin, limites.xmax + 1):
            cx, _ = self._mundo_para_canvas(Ponto(x, 0))
            if x:
                self.canvas.create_text(cx, oy + 8, text=str(x), anchor="n", font=("Arial", 8), tags="numeros")
        for y in range(limites.ymin, limites.ymax + 1):
            _, cy = self._mundo_para_canvas(Ponto(0, y))
            if y:
                self.canvas.create_text(ox - 8, cy, text=str(y), anchor="e", font=("Arial", 8), tags="numeros")
        for x, y, texto in ((ox - 8, oy + 12, "0"), (self.canvas.winfo_width() - 12, oy - 14, "X"), (ox + 14, 12, "Y")):
            self.canvas.create_text(x, y, text=texto, font=("Arial", 9), tags="numeros")
        for texto in self.canvas.find_withtag("numeros"):
            x1, y1, x2, y2 = self.canvas.bbox(texto)
            dx = max(0, -x1) + min(0, self.canvas.winfo_width() - x2)
            dy = max(0, -y1) + min(0, self.canvas.winfo_height() - y2)
            self.canvas.move(texto, dx, dy)
            fundo = self.canvas.create_rectangle(*self.canvas.bbox(texto), fill="white", outline="")
            self.canvas.tag_lower(fundo, texto)

    def _plotar_pixel(self, x, y, cor, previa=False) -> None:
        cx, cy = self._mundo_para_canvas(Ponto(x, y))
        metade = self.ESCALA_PIXEL / 2 - 1
        self.canvas.create_rectangle(cx - metade, cy - metade, cx + metade, cy + metade,
                                     fill=cor, outline="", stipple="gray25" if previa else "", tags="pixel")

    def _desenhar_objeto(self, objeto) -> None:
        if objeto.identificador in self._pixels_animados:
            for x, y in self._pixels_animados[objeto.identificador]:
                self._plotar_pixel(x, y, objeto.cor)
        else:
            rasterizar_objeto(objeto, lambda x, y: self._plotar_pixel(x, y, objeto.cor))
        if objeto.selecionado:
            if objeto.tipo == TipoObjeto.CIRCUNFERENCIA:
                c, r = objeto.vertices[0], objeto.raio
                regiao = Retangulo(c.x - r - 0.5, c.y - r - 0.5, c.x + r + 0.5, c.y + r + 0.5)
            else:
                xs, ys = [p.x for p in objeto.vertices], [p.y for p in objeto.vertices]
                regiao = Retangulo(min(xs) - 0.5, min(ys) - 0.5, max(xs) + 0.5, max(ys) + 0.5)
            self._desenhar_retangulo(regiao, "#e06020")

    def _desenhar_previa(self) -> None:
        if self.inicio_arraste is not None and self.fim_arraste is not None:
            self._desenhar_retangulo(Retangulo.de_pontos(self.inicio_arraste, self.fim_arraste), "#246bfd")
            return
        if not self.pontos_temporarios:
            return
        pontos = self.pontos_temporarios + ([self.posicao_mouse] if self.posicao_mouse else [])
        if len(pontos) == 1:
            tipo = TipoObjeto.PONTO
        elif self.modo == "circunferencia":
            tipo = TipoObjeto.CIRCUNFERENCIA
        else:
            tipo = TipoObjeto.POLIGONO if len(pontos) >= 3 else TipoObjeto.RETA
        algoritmo = AlgoritmoReta.DDA if self.modo == "reta_dda" else AlgoritmoReta.BRESENHAM
        previa = ObjetoGrafico(0, tipo, pontos, algoritmo=algoritmo)
        rasterizar_objeto(previa, lambda x, y: self._plotar_pixel(x, y, self.cor_desenho, previa=True))

    def _desenhar_retangulo(self, retangulo, cor) -> None:
        a = self._mundo_para_canvas(Ponto(retangulo.xmin, retangulo.ymax))
        b = self._mundo_para_canvas(Ponto(retangulo.xmax, retangulo.ymin))
        self.canvas.create_rectangle(*a, *b, outline=cor, dash=(4, 3), width=2)


def executar() -> None:
    raiz = tk.Tk()
    App(raiz)
    raiz.mainloop()
