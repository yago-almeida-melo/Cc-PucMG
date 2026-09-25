# Relatório técnico — TP1-CG

## 1. Visão geral

O TP1-CG foi desenvolvido em Python com Tkinter. A biblioteca fornece somente a
janela, os botões e o `Canvas`; as primitivas do trabalho são mantidas como dados
vetoriais e convertidas explicitamente em pixels pelos algoritmos implementados.

O sistema de coordenadas é cartesiano: a origem fica no centro da área de desenho, X
cresce para a direita e Y cresce para cima. Cada posição lógica é mostrada por um
quadrado de 24 × 24 pixels físicos. A grade delimita cada pixel lógico e os eixos
mostram todas as coordenadas inteiras visíveis.

## 2. Estruturas de dados

`Ponto` armazena as coordenadas X e Y. `ObjetoGrafico` possui identificador, tipo,
lista de vértices, algoritmo de reta, cor, nome e estado de seleção.

- ponto: um vértice;
- reta: dois vértices;
- polígono: três ou mais vértices;
- circunferência: centro e um ponto pertencente ao raio.

`Cena` mantém a lista de objetos, os preenchimentos, o próximo identificador e a
janela de recorte. Cada `Preenchimento` armazena a cor e faixas horizontais imutáveis
de pixels `(y, x_inicial, x_final)`. Objetos e pinturas compartilham a sequência de
identificadores, preservando a ordem de sobreposição. A cópia da cena é usada pelo
histórico de desfazer e compartilha com segurança as pinturas imutáveis.

## 3. Rasterização

Os rasterizadores chamam `plotar_pixel(x, y)` a cada pixel calculado. A interface
guarda os pixels em uma fila e os exibe aos poucos usando `after` do Tkinter, sem
bloquear os eventos. O delay global começa em 30 ms e pode ser zerado para exibição
imediata. Objetos novos, transformados e recortados usam essa animação.

A cena e o histórico guardam os dados vetoriais dos objetos. Para preencher, uma
matriz de cores temporária recebe os pixels rasterizados e as pinturas anteriores,
sem incluir as guias da interface.

### 3.1 Reta DDA

São calculadas as diferenças `dx` e `dy`. A quantidade de passos é o maior valor entre
`|dx|` e `|dy|`. Os incrementos são `dx/passos` e `dy/passos`; a cada iteração, as
coordenadas acumuladas são arredondadas para o pixel mais próximo.

### 3.2 Reta de Bresenham

O algoritmo usa diferenças, sinais de avanço e uma variável inteira de erro. A cada
passo, o erro determina se X, Y ou ambos devem avançar. A implementação aceita todos
os oito octantes e os dois sentidos do segmento.

### 3.3 Circunferência de Bresenham

O cálculo começa em `(0, raio)` e avança enquanto `x <= y`. A variável de decisão
escolhe entre o pixel a leste e o pixel a sudeste. Cada cálculo produz simultaneamente
oito pontos por simetria em torno do centro.

## 4. Transformações geométricas

As transformações usam coordenadas homogêneas e matrizes 3 × 3.

Translação:

```text
[ 1  0  dx ]
[ 0  1  dy ]
[ 0  0   1 ]
```

Rotação:

```text
[ cos θ  -sen θ  0 ]
[ sen θ   cos θ  0 ]
[   0       0     1 ]
```

Escala:

```text
[ sx  0   0 ]
[ 0   sy  0 ]
[ 0   0   1 ]
```

As matrizes de reflexão trocam o sinal de Y para o eixo X, o sinal de X para o eixo Y
e ambos os sinais para XY. Para transformar ao redor de um pivô, o sistema compõe uma
translação até a origem, a transformação principal e a translação inversa.

## 5. Seleção retangular

O usuário indica a região com o gesto de pressionar, arrastar e soltar. Pontos são
testados por contenção; circunferências usam o ponto do retângulo mais próximo ao
centro; retas e arestas usam um teste paramétrico de interseção. Também é considerado
o caso em que o retângulo de seleção está inteiramente dentro de um polígono.

## 6. Recorte

Os dois algoritmos são aplicados somente a objetos do tipo reta. Os demais objetos,
incluindo polígonos selecionados, permanecem intactos.

### 6.1 Cohen–Sutherland

Cada extremidade recebe um código de quatro bits: esquerda, direita, abaixo e acima.
O OR igual a zero representa aceitação trivial; o AND diferente de zero representa
rejeição trivial. Nos demais casos, o ponto externo é substituído por sua interseção
com uma borda e o processo é repetido.

### 6.2 Liang–Barsky

O segmento é representado por `P(u) = P0 + u(P1 - P0)`, com `0 <= u <= 1`. As quatro
bordas atualizam os parâmetros de entrada `u1` e saída `u2`. Se `u1 > u2`, o segmento
é rejeitado; caso contrário, os dois parâmetros definem o trecho visível.

## 7. Decisões de interface

A interface usa controles padrão do `ttk`, com campos numéricos para deslocamentos,
ângulo e escalas. Criação, seleção e definição da janela de recorte são feitas com
o mouse. Um controle deslizante **Delay (ms)** ajusta a animação; a barra inferior
mostra as orientações e a posição cartesiana do cursor.

## 8. Verificação

A suíte de testes usa apenas `unittest`, da biblioteca padrão. Ela cobre os algoritmos
independentemente da interface. Para a apresentação, o roteiro de vídeo inclui testes
visuais de todas as funcionalidades obrigatórias.

## 9. Preenchimento e cores

O **Boundary-Fill** parte de uma semente e percorre pixels cuja cor é diferente da
cor de borda informada. O **Flood-Fill** captura a cor inicial da semente e percorre
apenas pixels conectados dessa mesma cor. Ambos usam uma pilha explícita, evitando
estouro da pilha de recursão. A conectividade pode ser 4 (esquerda, direita, acima e
abaixo) ou 8 (inclui as diagonais). Na opção 8, uma passagem diagonal entre pixels
da borda pode conectar o interior ao exterior da figura.
Um conjunto de visitados evita ciclos; no Boundary-Fill, permite atravessar pixels
que já tinham a cor de preenchimento sem tratá-los como bordas.

A leitura e a pintura são fornecidas por funções. Cada pixel modificado é atualizado
durante a execução. Os pixels alterados são depois compactados em faixas horizontais
para exibição e histórico; o Canvas desenha essas faixas já calculadas. O custo do
percurso é linear na região visitada, com memória auxiliar também linear. Regiões
abertas são limitadas à área visível no momento do clique.

O seletor nativo de cores oferece cores independentes para desenho, preenchimento
e borda. Objetos selecionados mantêm a cor real e são destacados pela caixa
tracejada. Pinturas são operações raster fixas no sistema de coordenadas do mundo;
transformações e recorte continuam atuando somente nos objetos vetoriais.

Os testes verificam contornos multicoloridos, regiões desconectadas, diagonais,
sementes externas, regiões grandes, cores iguais, sobreposição, cópias do histórico
e interação pelo Tkinter, incluindo desfazer, limpar, redimensionar e recolorir.
