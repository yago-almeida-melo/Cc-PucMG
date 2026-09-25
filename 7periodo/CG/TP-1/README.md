# TP1-CG

Aplicação didática em Python para demonstrar transformações geométricas 2D,
rasterização, preenchimento e recorte de segmentos em uma área correspondente a uma matriz de
pixels. Os objetos são desenhados e selecionados com o mouse, e os valores das
transformações são ajustados em campos numéricos.

## Funcionalidades

- objetos de cena: pontos, retas, polígonos e circunferências;
- rasterização de retas por DDA e Bresenham;
- rasterização de circunferências por Bresenham;
- preenchimento por Boundary-Fill e Flood-Fill com conectividade 4 ou 8;
- escolha das cores de desenho, preenchimento e borda por seletor de cores;
- translação com deslocamentos X e Y informados pelo usuário;
- rotação com ângulo e pivô configuráveis;
- escala independente em X e Y, também com pivô configurável;
- reflexões X, Y e XY em relação aos eixos cartesianos;
- seleção de objetos por região retangular indicada com o mouse;
- recorte de retas por Cohen–Sutherland e Liang–Barsky;
- pixels de 24 × 24, grade por pixel e eixos numerados em cada coordenada inteira;
- animação da rasterização com delay ajustável;
- pré-visualização durante o desenho, janela de recorte visível e destaque da seleção;
- histórico das últimas 30 alterações pelo botão **Desfazer**.

Os algoritmos calculam as coordenadas inteiras e chamam `plotar_pixel(x, y)` para
cada resultado. A interface usa esses pixels para desenhar os objetos e suas prévias.

## Requisitos

- Python 3.10 ou superior;
- Tkinter, normalmente incluído no instalador do Python para Windows.

O projeto não possui dependências externas para execução.

## Executar pelo código-fonte

No diretório do projeto:

```bash
python tp1/main.py
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

O controle **Delay (ms)** define o intervalo entre pixels de pontos, retas,
circunferências e polígonos. O padrão é 30 ms; use 0 para desenhar imediatamente.
A animação também aparece após transformações e recortes, sem bloquear a interface.

### Seleção e transformações

1. Escolha **Selecionar**.
2. Arraste uma região retangular sobre um ou mais objetos. A seleção considera a
   interseção entre o objeto e a região.
3. Ajuste os valores nos campos numéricos do painel lateral.
4. Clique na transformação desejada.

Rotação e escala podem ocorrer em torno do centro da seleção ou da origem `(0, 0)`.
O botão de pivô alterna entre as duas opções. As reflexões sempre usam os eixos
cartesianos desenhados na tela.

Uma escala diferente em X e Y transforma geometricamente uma circunferência em
elipse. Como a estrutura solicitada inclui polígonos, o programa preserva essa elipse
como um polígono de 72 vértices depois da transformação.

### Cores e preenchimento

1. No painel **CORES E PREENCHIMENTO**, clique na amostra de **Desenho** para
   escolher a cor dos próximos pontos, retas, polígonos e circunferências.
   Para recolorir objetos existentes, selecione-os e use **Aplicar cor do desenho à seleção**.
2. Escolha a cor de **Preenchimento**.
3. Para **Boundary-Fill**, escolha também a cor de **Borda**, igual à do contorno
   fechado e diferente da cor de preenchimento. Ative o botão e clique dentro da figura.
   O algoritmo atravessa as cores internas até encontrar a borda escolhida.
4. Para **Flood-Fill**, ative o botão e clique na região: somente os pixels conectados
   com a mesma cor do pixel clicado são substituídos. A cor de borda não é usada.
5. Use **Desfazer** (ou `Ctrl+Z`) para reverter pinturas e alterações de cor.

Escolha a conectividade **4** (vizinhos horizontais e verticais) ou **8** (inclui
diagonais). Com 8, o preenchimento pode atravessar um contorno que só fecha pela
união diagonal de pixels. Um contorno aberto ou uma cor de borda incorreta também
permite ao Boundary-Fill atingir o exterior. Ambos param nos limites da área visível;
grade, eixos, prévias e janela de recorte não bloqueiam o preenchimento.

As pinturas são camadas raster, preservadas ao redesenhar e redimensionar a janela.
Elas permanecem nas coordenadas originais: seleção, transformação, exclusão de
objetos e recorte atuam nas primitivas vetoriais. Para transformar uma figura e
preenchê-la, faça a transformação antes da pintura. **Limpar cena** remove também
as pinturas. Novos desenhos e pinturas aparecem sobre os anteriores.

### Recorte

1. Escolha **Janela de recorte** e arraste a região desejada.
2. Escolha **Selecionar** e marque as retas.
3. Clique em **Cohen–Sutherland** ou **Liang–Barsky**.

O recorte altera apenas as retas selecionadas. Polígonos, pontos e circunferências
são preservados, mesmo quando fazem parte da seleção.

## Testes automatizados

No diretório do projeto, execute:

```bash
cd tp1
python -m unittest discover -s ../tests -v
```

Os testes verificam octantes de Bresenham, casos-limite do DDA, simetria da
circunferência, matrizes de transformação, seleção retangular e aceitação, rejeição e
recorte nos dois algoritmos, além dos preenchimentos, cores, limites e histórico.
Os testes de interface são ignorados quando não há display gráfico. Em Linux, com
Xvfb instalado, execute todos eles a partir de `tp1` com
`xvfb-run -a python3 -m unittest discover -s ../tests -v`.

## Gerar o executável para Windows

Em um computador Windows com Python instalado, execute no diretório do projeto:

```bat
build_windows.bat
```

O script instala o PyInstaller e gera a pasta executável
`dist\TP1-CG\TP1-CG.exe`. O executável inclui o interpretador e as bibliotecas
necessárias, portanto o computador de destino não precisa ter Python instalado.
Para distribuir a versão portátil, copie toda a pasta `dist\TP1-CG`.

## Gerar o instalador para Windows

1. Gere primeiro o executável com `build_windows.bat`.
2. Instale o [Inno Setup 6](https://jrsoftware.org/isinfo.php).
3. Abra `installer.iss` no Inno Setup e escolha **Compile**, ou execute
   `build_installer.bat` se o Inno Setup estiver no caminho padrão.

O instalador será criado em `installer_saida\TP1-CG-Setup.exe` e oferecerá atalhos
no menu Iniciar e, opcionalmente, na área de trabalho.

## Organização do projeto

```text
tp1/
  main.py             Ponto de entrada
  interface.py        Interface, eventos de mouse e desenho da matriz
  modelos.py          Pontos, retas, polígonos, circunferências e cena
  rasterizacao.py     DDA e Bresenham para retas/circunferências
  preenchimento.py    Boundary-Fill, Flood-Fill e leitura das cores da cena
  recorte.py          Cohen–Sutherland, Liang–Barsky e seleção
  transformacoes.py   Matrizes homogêneas e transformações 2D
tests/                 Testes automatizados
TP1-CG.spec            Configuração do executável
build_windows.bat      Geração do executável no Windows
build_installer.bat    Geração do instalador no Windows
installer.iss          Projeto de instalador do Inno Setup
```

Consulte também [RELATORIO_TECNICO.md](RELATORIO_TECNICO.md) para a explicação dos
algoritmos e [ROTEIRO_VIDEO.md](ROTEIRO_VIDEO.md) para um roteiro de demonstração.
