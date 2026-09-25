# Roteiro sugerido para o vídeo de apresentação

Duração estimada: 6 a 9 minutos.

## 1. Abertura — 30 segundos

- Apresente o objetivo do trabalho e a tela principal.
- Mostre a origem, os eixos numerados e a grade com pixels de 24 × 24.
- Explique que cada quadradinho das figuras é produzido pelos algoritmos do projeto.

## 2. Estruturas e entrada pelo mouse — 1 minuto

- Insira um ponto.
- Desenhe um triângulo ou quadrilátero e conclua pelo botão da interface.
- Mostre uma seleção por região retangular.
- Mostre os campos numéricos usados para ajustar as transformações.

## 3. Rasterização — 1 minuto e 30 segundos

- Desenhe retas semelhantes com DDA e Bresenham, preferencialmente com inclinação.
- Ajuste **Delay (ms)** para acompanhar os pixels e depois use 0 para desenho imediato.
- Desenhe uma circunferência e explique a simetria dos oito pontos de Bresenham.
- Observe também a animação ao inserir um ponto e concluir um polígono.
- Compare Flood-Fill e Boundary-Fill com conectividade 4 e 8, incluindo uma passagem
  diagonal para mostrar a diferença.

## 4. Transformações — 2 minutos

- Selecione um polígono e aplique uma translação com X e Y escolhidos nos controles.
- Ajuste um ângulo e aplique rotação em torno do centro da seleção.
- Troque o pivô para a origem e repita para deixar a diferença visível.
- Ajuste fatores X e Y e aplique escala.
- Demonstre separadamente as reflexões X, Y e XY, usando **Desfazer** entre elas.

## 5. Recorte — 2 minutos

- Desenhe várias retas cruzando uma região central.
- Defina a janela roxa com o mouse e selecione as retas.
- Aplique Cohen–Sutherland e explique brevemente os códigos de região.
- Desfaça, aplique Liang–Barsky e explique os parâmetros de entrada e saída.
- Mostre ao menos uma reta aceita, uma rejeitada e uma parcialmente recortada.
- Inclua um polígono na seleção e mostre que o recorte preserva a figura.

## 6. Código e testes — 1 minuto

- Mostre rapidamente a organização em módulos.
- Aponte as funções dos três rasterizadores, das matrizes e dos dois recortadores.
- No diretório `tp1`, execute `python -m unittest discover -s ../tests -v` e mostre
  todos os testes aprovados.

## 7. Encerramento — 30 segundos

- Execute a versão empacotada para demonstrar que ela não depende do editor.
- Mostre o instalador do Windows e a opção de atalho na área de trabalho.
- Encerre retomando os requisitos atendidos.

Use áudio claro e faça pausas curtas entre as demonstrações. Antes de gravar, feche
notificações, ajuste a resolução para que o painel inteiro apareça e ensaie os cliques.
