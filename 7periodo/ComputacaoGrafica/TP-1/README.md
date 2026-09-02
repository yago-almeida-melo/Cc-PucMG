# TP1 - CG

Aplicação didática em Python para demonstrar transformações geométricas 2D,
rasterização e recorte de segmentos em uma área correspondente a uma matriz de
pixels. Toda a operação principal é feita com o mouse: cliques, arrastes, botões e
controles deslizantes.

## Funcionalidades

- objetos de cena: pontos, retas, polígonos e circunferências;
- rasterização de retas por DDA e Bresenham;
- rasterização de circunferências por Bresenham;
- translação com deslocamentos X e Y informados pelo usuário;
- rotação com ângulo e pivô configuráveis;
- escala independente em X e Y, também com pivô configurável;
- reflexões X, Y e XY em relação aos eixos cartesianos;
- seleção de objetos por região retangular indicada com o mouse;
- recorte por Cohen–Sutherland e Liang–Barsky;
- pré-visualização durante o desenho, janela de recorte visível e destaque da seleção;
- histórico das últimas 30 alterações pelo botão **Desfazer**.

As linhas visíveis na área de desenho não usam a primitiva de linha do Tkinter. Os
algoritmos do projeto produzem as coordenadas inteiras e cada resultado é desenhado
como uma célula da matriz lógica de pixels.

## Requisitos

- Python 3.10 ou superior;
- Tkinter, normalmente incluído no instalador do Python para Windows.

O projeto não possui dependências externas para execução.

## Executar pelo código-fonte

No diretório do projeto:

```bash
python main.py
```

Em algumas distribuições Linux, pode ser necessário instalar o pacote do sistema que
fornece o Tkinter, como `python3-tk`.

## Guia de uso

### Desenho

1. Escolha uma ferramenta na barra superior.
2. Para um ponto, clique uma vez.
3. Para uma reta, clique nas duas extremidades.
4. Para uma circunferência, clique no centro e depois em um ponto da borda.
5. Para um polígono, clique nos vértices e use **Concluir polígono**. O botão direito
   também conclui a figura.

### Seleção e transformações

1. Escolha **Selecionar**.
2. Arraste uma região retangular sobre um ou mais objetos. A seleção considera a
   interseção entre o objeto e a região.
3. Ajuste os fatores nos controles deslizantes do painel lateral.
4. Clique na transformação desejada.

Rotação e escala podem ocorrer em torno do centro da seleção ou da origem `(0, 0)`.
O botão de pivô alterna entre as duas opções. As reflexões sempre usam os eixos
cartesianos desenhados na tela.

Uma escala diferente em X e Y transforma geometricamente uma circunferência em
elipse. Como a estrutura solicitada inclui polígonos, o programa preserva essa elipse
como um polígono de 72 vértices depois da transformação.

### Recorte

1. Escolha **Janela de recorte** e arraste a região desejada.
2. Escolha **Selecionar** e marque as retas ou os polígonos.
3. Clique em **Cohen–Sutherland** ou **Liang–Barsky**.

O recorte é aplicado aos segmentos selecionados. As arestas visíveis de um polígono
passam a ser retas independentes, pois os dois algoritmos pedidos são algoritmos de
recorte de segmentos.

## Testes automatizados

Execute:

```bash
python -m unittest discover -v
```

Os testes verificam octantes de Bresenham, casos-limite do DDA, simetria da
circunferência, matrizes de transformação, seleção retangular e aceitação, rejeição e
recorte nos dois algoritmos.

## Gerar o executável para Windows

Em um computador Windows com Python instalado, execute:

```bat
build_windows.bat
```

O script instala o PyInstaller e gera a pasta executável
`dist\PixelLabCG\PixelLabCG.exe`. O executável inclui o interpretador e as bibliotecas
necessárias, portanto o computador de destino não precisa ter Python instalado.

## Gerar o instalador para Windows

1. Gere primeiro o executável com `build_windows.bat`.
2. Instale o [Inno Setup 6](https://jrsoftware.org/isinfo.php).
3. Abra `installer.iss` no Inno Setup e escolha **Compile**, ou execute
   `build_installer.bat` se o Inno Setup estiver no caminho padrão.

O instalador será criado em `installer_saida\PixelLabCG-Setup.exe` e oferecerá atalhos
no menu Iniciar e, opcionalmente, na área de trabalho.

## Organização do projeto

```text
pixel_lab/
  interface.py        Interface, eventos de mouse e desenho da matriz
  modelos.py          Pontos, retas, polígonos, circunferências e cena
  rasterizacao.py     DDA e Bresenham para retas/circunferências
  recorte.py          Cohen–Sutherland, Liang–Barsky e seleção
  transformacoes.py   Matrizes homogêneas e transformações 2D
tests/                 Testes automatizados
main.py                Ponto de entrada
installer.iss          Projeto de instalador do Inno Setup
```

Consulte também [RELATORIO_TECNICO.md](RELATORIO_TECNICO.md) para a explicação dos
algoritmos e [ROTEIRO_VIDEO.md](ROTEIRO_VIDEO.md) para um roteiro de demonstração.

