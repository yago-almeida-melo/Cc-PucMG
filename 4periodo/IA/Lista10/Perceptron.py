import numpy as np
import matplotlib.pyplot as plt
from itertools import product
import time

# ... (A classe Perceptron e as funções generate_boolean_data, generate_xor_data permanecem as mesmas de antes) ...
# COPIE E COLE A CLASSE PERCEPTRON E AS FUNÇÕES HELPER AQUI
# O CÓDIGO ABAIXO É APENAS A PARTE DE EXECUÇÃO MODIFICADA

class Perceptron:
    def __init__(self, num_inputs, learning_rate=0.1):
        """
        Inicializa o Perceptron.

        Args:
            num_inputs (int): Número de características de entrada.
            learning_rate (float): Taxa de aprendizado.
        """
        # Para reprodutibilidade, podemos fixar a semente, mas para demonstração geral, aleatório é bom.
        # np.random.seed(42)
        self.weights = np.random.rand(num_inputs) * 0.1 - 0.05 # Pesos pequenos aleatórios (entre -0.05 e 0.05)
        self.bias = np.random.rand(1)[0] * 0.1 - 0.05           # Bias pequeno aleatório
        self.learning_rate = learning_rate
        self.num_inputs = num_inputs

    def _step_function(self, z):
        """Função de ativação degrau."""
        return 1 if z >= 0 else 0

    def predict(self, inputs):
        """
        Realiza a predição para um conjunto de entradas.

        Args:
            inputs (np.array): Array numpy com as entradas.

        Returns:
            int: Saída prevista (0 ou 1).
        """
        summation = np.dot(inputs, self.weights) + self.bias
        return self._step_function(summation)

    def train(self, training_inputs, labels, epochs=100, plot_hyperplane=False, feature_names=None, title_suffix=""):
        """
        Treina o Perceptron.

        Args:
            training_inputs (np.array): Array numpy de amostras de treinamento.
            labels (np.array): Array numpy com os rótulos verdadeiros.
            epochs (int): Número máximo de épocas de treinamento.
            plot_hyperplane (bool): Se True e num_inputs == 2, plota o hiperplano.
            feature_names (list): Nomes das características para o plot (opcional).
            title_suffix (str): Sufixo para o título do gráfico.
        """
        history_weights = []
        history_bias = []
        converged = False

        for epoch in range(epochs):
            errors = 0
            # Guardar pesos para plotagem em intervalos
            if plot_hyperplane and self.num_inputs == 2:
                if epoch == 0 or epoch % max(1, epochs // 10) == 0 or epoch == epochs -1 :
                     history_weights.append(self.weights.copy())
                     history_bias.append(self.bias)

            for inputs, label in zip(training_inputs, labels):
                prediction = self.predict(inputs)
                error = label - prediction
                if error != 0:
                    errors += 1
                    self.weights += self.learning_rate * error * inputs
                    self.bias += self.learning_rate * error

            if errors == 0:
                print(f"Convergência alcançada na época {epoch + 1}.")
                if plot_hyperplane and self.num_inputs == 2: # Adiciona o estado final se convergiu
                    if not (len(history_weights) > 0 and np.array_equal(history_weights[-1], self.weights) and history_bias[-1] == self.bias):
                        history_weights.append(self.weights.copy())
                        history_bias.append(self.bias)
                converged = True
                break

        if not converged:
             print(f"Treinamento concluído após {epochs} épocas (pode não ter convergido).")
             if plot_hyperplane and self.num_inputs == 2: # Adiciona o estado final se não convergiu
                if not (len(history_weights) > 0 and np.array_equal(history_weights[-1], self.weights) and history_bias[-1] == self.bias):
                    history_weights.append(self.weights.copy())
                    history_bias.append(self.bias)


        if plot_hyperplane and self.num_inputs == 2:
            self._plot_decision_boundary_history(training_inputs, labels, history_weights, history_bias, feature_names, title_suffix)
        elif plot_hyperplane and self.num_inputs > 2:
            print("Plot do hiperplano é suportado apenas para 2 entradas. Para n > 2, a convergência dos pesos pode ser analisada numericamente.")


    def _plot_decision_boundary_history(self, X, y, history_weights, history_bias, feature_names=None, title_suffix=""):
        """
        Plota o histórico do hiperplano de separação para 2D.
        """
        plt.figure(figsize=(10, 7))

        # Scatter plot dos dados
        unique_labels = np.unique(y)
        colors_scatter = ['r', 'b', 'g', 'c', 'm', 'y', 'k']
        markers_scatter = ['x', 'o', '^', 's', 'p', '*', '+']

        for i, label_val in enumerate(unique_labels):
            plt.scatter(X[y == label_val, 0], X[y == label_val, 1],
                        marker=markers_scatter[i % len(markers_scatter)],
                        color=colors_scatter[i % len(colors_scatter)],
                        label=f'Classe {label_val}', alpha=0.7, s=80)


        x_min, x_max = X[:, 0].min() - 0.5, X[:, 0].max() + 0.5
        y_min, y_max = X[:, 1].min() - 0.5, X[:, 1].max() + 0.5

        plt.xlim(x_min, x_max)
        plt.ylim(y_min, y_max)

        x_plot_vals = np.array(plt.xlim())

        num_lines = len(history_weights)
        # Usar um colormap mais distinto para poucas linhas
        line_colors = plt.cm.cool(np.linspace(0.2, 0.8, num_lines)) if num_lines > 1 else ['k']


        for i, (w, b) in enumerate(zip(history_weights, history_bias)):
            line_label = None
            if num_lines <= 5: # Se poucas linhas, rotular todas
                line_label = f'Hiperplano {i+1}'
                if i == 0: line_label = 'Hiperplano Inicial'
                if i == num_lines -1 : line_label = 'Hiperplano Final'
            else: # Se muitas linhas, rotular apenas algumas chaves
                if i == 0: line_label = 'Hiperplano Inicial'
                elif i == num_lines -1 : line_label = 'Hiperplano Final'
                elif i == num_lines // 2: line_label = 'Hiperplano Intermediário'

            current_alpha = 0.4 if i < num_lines -1 else 1.0 # Destaca a linha final
            current_linestyle = '--' if i < num_lines -1 else '-'


            if w[1] != 0: # Evita divisão por zero se w2 for 0
                y_plot_vals = -(w[0] * x_plot_vals + b) / w[1]
                plt.plot(x_plot_vals, y_plot_vals, color=line_colors[i], linestyle=current_linestyle, alpha=current_alpha, linewidth=1.5, label=line_label)
            elif w[0] != 0: # Linha vertical
                y_plot_axis_vals = np.array(plt.ylim())
                x_val_const = -b / w[0]
                plt.plot([x_val_const, x_val_const], y_plot_axis_vals, color=line_colors[i], linestyle=current_linestyle, alpha=current_alpha, linewidth=1.5, label=line_label)
            # Se w[0] e w[1] forem zero, não há linha para plotar (improvável em treinamento normal)

        full_title = f"Evolução do Hiperplano do Perceptron ({title_suffix})" if title_suffix else "Evolução do Hiperplano de Separação do Perceptron"
        plt.title(full_title, fontsize=15)

        if feature_names:
            plt.xlabel(feature_names[0], fontsize=12)
            plt.ylabel(feature_names[1], fontsize=12)
        else:
            plt.xlabel("Entrada 1", fontsize=12)
            plt.ylabel("Entrada 2", fontsize=12)

        # Coleta handles e labels para a legenda, evitando duplicatas
        handles, labels_legend = plt.gca().get_legend_handles_labels() # Renomeado para evitar conflito
        by_label = dict(zip(labels_legend, handles)) # Remove duplicatas mantendo a ordem da primeira ocorrência
        plt.legend(by_label.values(), by_label.keys(), loc='best')
        plt.grid(True, linestyle=':', alpha=0.7)
        plt.axhline(0, color='black',linewidth=0.5)
        plt.axvline(0, color='black',linewidth=0.5)
        plt.show()

# --- Funções para gerar dados de treinamento ---

def generate_boolean_data(num_inputs, logic_function):
    """
    Gera dados de treinamento para funções lógicas AND ou OR com n entradas.

    Args:
        num_inputs (int): Número de entradas booleanas.
        logic_function (str): 'AND' ou 'OR'.

    Returns:
        tuple: (inputs_array, labels_array)
               inputs_array: Array numpy com todas as combinações de entrada.
               labels_array: Array numpy com os rótulos correspondentes.
    """
    if num_inputs < 1:
        raise ValueError("O número de entradas deve ser pelo menos 1.")

    input_combinations = list(product([0, 1], repeat=num_inputs))
    inputs_array = np.array(input_combinations, dtype=float) # Usar float para operações com pesos

    if logic_function.upper() == 'AND':
        labels_array = np.array([1 if all(combination) else 0 for combination in input_combinations], dtype=float)
    elif logic_function.upper() == 'OR':
        labels_array = np.array([1 if any(combination) else 0 for combination in input_combinations], dtype=float)
    else:
        raise ValueError("Função lógica deve ser 'AND' ou 'OR'.")

    return inputs_array, labels_array

def generate_xor_data():
    """Gera dados de treinamento para a função XOR de 2 entradas."""
    inputs_array = np.array([[0, 0], [0, 1], [1, 0], [1, 1]], dtype=float)
    labels_array = np.array([0, 1, 1, 0], dtype=float)
    return inputs_array, labels_array


if __name__ == '__main__':
    while True:
        plt.close('all') # Limpa plots anteriores a cada nova execução no loop

        print("\n--- Simulador de Perceptron para Funções Lógicas ---")
        print("Escolha uma opção:")
        print("1: Testar função AND com N entradas")
        print("2: Testar função OR com N entradas")
        print("3: Demonstrar Perceptron com XOR (2 entradas)")
        print("4: Sair")

        escolha = input("Digite sua escolha (1-4): ")

        if escolha == '1' or escolha == '2':
            logic_func_str = 'AND' if escolha == '1' else 'OR'
            while True:
                try:
                    num_entradas = int(input(f"Digite o número de entradas para a função {logic_func_str} (ex: 2, 3, 10): "))
                    if num_entradas <= 0:
                        print("O número de entradas deve ser um inteiro positivo.")
                    elif num_entradas > 15 and logic_func_str == 'AND': # 2^15 é grande, mas factível
                        print(f"Atenção: {2**num_entradas} combinações podem demorar para processar e gerar dados para AND.")
                        confirm = input("Deseja continuar? (s/n): ").lower()
                        if confirm != 's':
                            continue
                        break
                    elif num_entradas > 15 and logic_func_str == 'OR':
                        print(f"Atenção: {2**num_entradas} combinações podem demorar para processar e gerar dados para OR.")
                        confirm = input("Deseja continuar? (s/n): ").lower()
                        if confirm != 's':
                            continue
                        break
                    else:
                        break
                except ValueError:
                    print("Entrada inválida. Por favor, digite um número inteiro.")

            print(f"\n--- Testando Perceptron para {logic_func_str} com {num_entradas} entradas ---")
            X_data, y_data = generate_boolean_data(num_entradas, logic_func_str)

            print(f"Número de amostras de treinamento: {len(X_data)}")
            if len(X_data) <= 16: # Mostrar todas as amostras se forem poucas
                 print("Dados de Treinamento (Entradas -> Saída):")
                 for i, row in enumerate(X_data): print(f"{row} -> {y_data[i]}")
            else:
                print("Dados de Treinamento (Entradas -> Saída) - Primeiras e últimas 3 amostras:")
                for i in list(range(3)) + list(range(len(X_data)-3, len(X_data))):
                    print(f"{X_data[i]} -> {y_data[i]}")


            perceptron_custom = Perceptron(num_inputs=num_entradas, learning_rate=0.1)
            print(f"\nPesos Iniciais: {perceptron_custom.weights}, Bias Inicial: {perceptron_custom.bias:.4f}")

            # Ajustar épocas para problemas maiores se necessário
            epochs_custom = 100 if num_entradas <= 5 else 200 + (num_entradas - 5) * 50
            if 2**num_entradas > 1000: # Mais épocas para conjuntos de dados muito grandes
                epochs_custom = max(epochs_custom, int((2**num_entradas) / 10)) # Heurística simples

            feature_names_custom = [f'Entrada {i+1}' for i in range(num_entradas)]
            plot_custom = True if num_entradas == 2 else False

            perceptron_custom.train(X_data, y_data, epochs=epochs_custom,
                                    plot_hyperplane=plot_custom,
                                    feature_names=feature_names_custom if plot_custom else None,
                                    title_suffix=f"{logic_func_str} {num_entradas}-entradas")

            print(f"Pesos Finais: {perceptron_custom.weights}, Bias Final: {perceptron_custom.bias:.4f}")
            print(f"\nResultados da Predição ({logic_func_str} com {num_entradas} entradas) - Verificação:")
            correct_predictions = 0
            total_samples = len(X_data)
            for inputs_test, label_test in zip(X_data, y_data):
                prediction = perceptron_custom.predict(inputs_test)
                if prediction == label_test:
                    correct_predictions += 1
            print(f"Total de predições corretas: {correct_predictions}/{total_samples}")
            if correct_predictions == total_samples:
                print("O Perceptron convergiu e classificou todas as amostras corretamente!")
            else:
                print("O Perceptron NÃO classificou todas as amostras corretamente dentro das épocas fornecidas.")


        elif escolha == '3':
            print(f"\n--- Demonstrando Perceptron para XOR com 2 entradas ---")
            X_xor2, y_xor2 = generate_xor_data()
            print("Dados de Treinamento (Entradas -> Saída):")
            for i, row in enumerate(X_xor2): print(f"{row} -> {y_xor2[i]}")

            perceptron_xor2 = Perceptron(num_inputs=2, learning_rate=0.1)
            print(f"\nPesos Iniciais: {perceptron_xor2.weights}, Bias Inicial: {perceptron_xor2.bias:.4f}")
            perceptron_xor2.train(X_xor2, y_xor2, epochs=100, plot_hyperplane=True,
                                  feature_names=['Entrada A', 'Entrada B'], title_suffix="XOR 2-entradas")
            print(f"Pesos Finais: {perceptron_xor2.weights}, Bias Final: {perceptron_xor2.bias:.4f}")

            print("\nResultados da Predição (XOR com 2 entradas):")
            correct_predictions_xor = 0
            for inputs_test, label_test in zip(X_xor2, y_xor2):
                prediction = perceptron_xor2.predict(inputs_test)
                if prediction == label_test: correct_predictions_xor +=1
                print(f"Entrada: {inputs_test}, Saída Real: {label_test}, Saída Prevista: {prediction}")
            print(f"Total de predições corretas: {correct_predictions_xor}/{len(X_xor2)}")
            if correct_predictions_xor != len(X_xor2):
                print("O Perceptron NÃO conseguiu convergir para uma solução perfeita para o XOR, como esperado.")

        elif escolha == '4':
            print("Saindo do simulador.")
            break
        else:
            print("Escolha inválida. Por favor, tente novamente.")

        print("-" * 70)
        time.sleep(1) # Pausa para ler a saída antes do próximo loop