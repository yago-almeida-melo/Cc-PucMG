import numpy as np

# --- Funções de Ativação e suas Derivadas ---
def sigmoid(x):
    with np.errstate(over='ignore', under='ignore'): # Ignorar overflow/underflow temporariamente
        res = 1 / (1 + np.exp(-np.clip(x, -500, 500))) # Clip para estabilidade numérica
    return res

def sigmoid_derivative(x):
    s = sigmoid(x)
    return s * (1 - s)

def tanh(x):
    with np.errstate(over='ignore', under='ignore'):
        res = np.tanh(np.clip(x, -500, 500))
    return res

def tanh_derivative(x):
    return 1 - tanh(x)**2

def relu(x):
    return np.maximum(0, x)

def relu_derivative(x):
    return np.where(x > 0, 1, 0)

# --- Classe da Rede Neural ---
class NeuralNetwork:
    def __init__(self, n_inputs, n_hidden, n_outputs, activation_func_name='sigmoid', use_bias=True):
        self.n_inputs = n_inputs
        self.n_hidden = n_hidden
        self.n_outputs = n_outputs
        self.use_bias = use_bias

        # Inicialização dos pesos e biases
        # Camada oculta
        self.weights_hidden = np.random.randn(self.n_inputs, self.n_hidden) * 0.1
        if self.use_bias:
            self.bias_hidden = np.zeros((1, self.n_hidden))
        else:
            self.bias_hidden = np.zeros((1, self.n_hidden)) # Mantém a estrutura, mas não será atualizado se use_bias=False

        # Camada de saída
        self.weights_output = np.random.randn(self.n_hidden, self.n_outputs) * 0.1
        if self.use_bias:
            self.bias_output = np.zeros((1, self.n_outputs))
        else:
            self.bias_output = np.zeros((1, self.n_outputs))

        # Seleção da função de ativação
        if activation_func_name == 'sigmoid':
            self.activation_func = sigmoid
            self.activation_derivative = sigmoid_derivative
        elif activation_func_name == 'tanh':
            self.activation_func = tanh
            self.activation_derivative = tanh_derivative
        elif activation_func_name == 'relu':
            self.activation_func = relu
            self.activation_derivative = relu_derivative
        else:
            raise ValueError("Função de ativação não suportada. Escolha 'sigmoid', 'tanh' ou 'relu'.")

        # Para armazenar valores intermediários do forward pass para o backward pass
        self.hidden_layer_input = None
        self.hidden_layer_activation = None
        self.output_layer_input = None
        self.output_layer_activation = None

    def _forward(self, X):
        # Camada Oculta
        self.hidden_layer_input = np.dot(X, self.weights_hidden)
        if self.use_bias:
            self.hidden_layer_input += self.bias_hidden
        self.hidden_layer_activation = self.activation_func(self.hidden_layer_input)

        # Camada de Saída
        self.output_layer_input = np.dot(self.hidden_layer_activation, self.weights_output)
        if self.use_bias:
            self.output_layer_input += self.bias_output
        # Para problemas de classificação binária, a sigmoide é comum na saída,
        # mas vamos manter a função de ativação configurável por enquanto.
        # Se for ReLU na saída e o target for 0/1, pode não ser ideal sem pós-processamento.
        # Por simplicidade, usamos a mesma função de ativação.
        # Para AND/OR/XOR, sigmoide na saída é mais natural.
        if self.activation_func == relu and self.n_outputs == 1: # Usar sigmoide na saída para problemas binários se ReLU for a principal
            self.output_layer_activation = sigmoid(self.output_layer_input)
        else:
            self.output_layer_activation = self.activation_func(self.output_layer_input)

        return self.output_layer_activation

    def _backward(self, X, y, output, learning_rate):
        # Calcula o erro na camada de saída
        error = y - output
        
        # Derivada da função de ativação da camada de saída
        if self.activation_func == relu and self.n_outputs == 1: # Se usou sigmoide na saída
            d_output = error * sigmoid_derivative(self.output_layer_input)
        else:
            d_output = error * self.activation_derivative(self.output_layer_input)


        # Gradientes para pesos e bias da camada de saída
        # (m é o número de exemplos no batch, X.shape[0])
        m = X.shape[0]
        grad_weights_output = np.dot(self.hidden_layer_activation.T, d_output) / m
        
        if self.use_bias:
            grad_bias_output = np.sum(d_output, axis=0, keepdims=True) / m

        # Propaga o erro para a camada oculta
        error_hidden = np.dot(d_output, self.weights_output.T)
        d_hidden_layer = error_hidden * self.activation_derivative(self.hidden_layer_input)

        # Gradientes para pesos e bias da camada oculta
        grad_weights_hidden = np.dot(X.T, d_hidden_layer) / m
        if self.use_bias:
            grad_bias_hidden = np.sum(d_hidden_layer, axis=0, keepdims=True) / m
            
        # Atualiza os pesos e biases
        self.weights_output += learning_rate * grad_weights_output
        self.weights_hidden += learning_rate * grad_weights_hidden
        if self.use_bias:
            self.bias_output += learning_rate * grad_bias_output
            self.bias_hidden += learning_rate * grad_bias_hidden

    def train(self, X, y, epochs, learning_rate, verbose=True, print_every=100):
        history_loss = []
        for epoch in range(epochs):
            # Forward propagation
            output = self._forward(X)

            # Backward propagation e atualização de pesos
            self._backward(X, y, output, learning_rate)

            # Calcula o erro (MSE)
            loss = np.mean((y - output) ** 2)
            history_loss.append(loss)

            if verbose and (epoch % print_every == 0 or epoch == epochs -1):
                print(f"Epoch {epoch}/{epochs-1} - Loss: {loss:.6f}")
        return history_loss

    def predict(self, X):
        output = self._forward(X)
        # Para problemas de classificação binária, arredondamos a saída
        return np.round(output)

# --- Geração de Dados Lógicos ---
def generate_logic_data(n_inputs, logic_function_name):
    num_samples = 2**n_inputs
    X = np.zeros((num_samples, n_inputs), dtype=int)
    y = np.zeros((num_samples, 1), dtype=int)

    for i in range(num_samples):
        binary_representation = bin(i)[2:].zfill(n_inputs) # Representação binária
        X[i] = [int(bit) for bit in binary_representation]

        if logic_function_name == 'AND':
            y[i] = 1 if sum(X[i]) == n_inputs else 0
        elif logic_function_name == 'OR':
            y[i] = 1 if sum(X[i]) > 0 else 0
        elif logic_function_name == 'XOR':
            y[i] = 1 if sum(X[i]) % 2 != 0 else 0
        else:
            raise ValueError("Função lógica não suportada. Escolha 'AND', 'OR' ou 'XOR'.")
    return X, y


# --- Função Principal para Executar Experimentos ---
def run_experiment():
    print("Bem-vindo ao experimento de Backpropagation para funções lógicas!")
    
    while True:
        try:
            n_inputs = int(input("Digite o número de entradas booleanas (n >= 2): "))
            if n_inputs >= 2:
                break
            else:
                print("O número de entradas deve ser maior ou igual a 2.")
        except ValueError:
            print("Entrada inválida. Por favor, digite um número inteiro.")

    while True:
        logic_function = input("Escolha a função lógica (AND, OR, XOR): ").upper()
        if logic_function in ['AND', 'OR', 'XOR']:
            break
        else:
            print("Função lógica inválida.")

    # Geração dos dados
    X_train, y_train = generate_logic_data(n_inputs, logic_function)
    print(f"\n--- Dados para {logic_function} com {n_inputs} entradas ---")
    # for i in range(len(X_train)):
    # print(f"Entrada: {X_train[i]}, Saída Esperada: {y_train[i]}")
    # print("-------------------------------------\n")


    # --- Investigação 1: Taxa de Aprendizado ---
    print("\n--- Investigação 1: Importância da Taxa de Aprendizado ---")
    print(f"Testando para {logic_function} com {n_inputs} entradas, Sigmoide, com Bias.")
    learning_rates_to_test = [0.001, 0.01, 0.1, 0.5, 1.0] #, 2.0]
    epochs_lr = 3000 if logic_function == 'XOR' and n_inputs > 2 else 1500
    n_hidden_lr = n_inputs * 2 if logic_function == 'XOR' else n_inputs # Mais neurônios para XOR

    for lr in learning_rates_to_test:
        print(f"\nTestando Taxa de Aprendizado: {lr}")
        nn_lr = NeuralNetwork(n_inputs=n_inputs, n_hidden=n_hidden_lr, n_outputs=1,
                              activation_func_name='sigmoid', use_bias=True)
        try:
            loss_history = nn_lr.train(X_train, y_train, epochs=epochs_lr, learning_rate=lr, verbose=False)
            final_loss = loss_history[-1]
            print(f"Taxa: {lr} - Loss Final após {epochs_lr} épocas: {final_loss:.6f}")
            if final_loss > 0.15 and logic_function == 'XOR': # Um limiar para indicar possível não convergência
                 print("  (Pode não ter convergido bem ou taxa inadequada)")
            elif final_loss > 0.05 and logic_function != 'XOR':
                 print("  (Pode não ter convergido bem ou taxa inadequada)")
            predictions = nn_lr.predict(X_train)
            accuracy = np.mean(predictions == y_train) * 100
            print(f"  Acurácia no treino: {accuracy:.2f}%")

        except Exception as e:
            print(f"Taxa: {lr} - Erro durante o treinamento: {e}")

    # --- Investigação 2: Importância do Bias ---
    print("\n--- Investigação 2: Importância do Bias ---")
    print(f"Testando para {logic_function} com {n_inputs} entradas, Sigmoide, LR=0.1.")
    lr_bias_test = 0.1
    epochs_bias = 3000 if logic_function == 'XOR' and n_inputs > 2 else 1500
    n_hidden_bias = n_inputs * 2 if logic_function == 'XOR' else n_inputs

    for bias_setting in [True, False]:
        print(f"\nTestando com Bias: {bias_setting}")
        nn_bias = NeuralNetwork(n_inputs=n_inputs, n_hidden=n_hidden_bias, n_outputs=1,
                                activation_func_name='sigmoid', use_bias=bias_setting)
        loss_history = nn_bias.train(X_train, y_train, epochs=epochs_bias, learning_rate=lr_bias_test, verbose=False)
        final_loss = loss_history[-1]
        print(f"Bias: {bias_setting} - Loss Final após {epochs_bias} épocas: {final_loss:.6f}")
        predictions = nn_bias.predict(X_train)
        accuracy = np.mean(predictions == y_train) * 100
        print(f"  Acurácia no treino: {accuracy:.2f}%")
        if final_loss > 0.15 and logic_function == 'XOR' and not bias_setting:
            print("  (Sem bias, XOR pode ter dificuldade em convergir)")

    # --- Investigação 3: Importância da Função de Ativação ---
    print("\n--- Investigação 3: Importância da Função de Ativação ---")
    print(f"Testando para {logic_function} com {n_inputs} entradas, com Bias, LR=0.1 (Sigmoide/Tanh) ou LR=0.01 (ReLU).")
    activation_functions_to_test = ['sigmoid', 'tanh', 'relu']
    epochs_activation = 4000 if logic_function == 'XOR' and n_inputs > 3 else 2000
    epochs_activation = max(epochs_activation, 1500) # Mínimo de épocas
    n_hidden_activation = n_inputs * 2 if logic_function == 'XOR' else max(n_inputs, 2) # Garantir pelo menos 2 neurônios ocultos

    for act_func_name in activation_functions_to_test:
        # ReLU geralmente precisa de uma taxa de aprendizado menor para estabilidade inicial
        lr_act_test = 0.01 if act_func_name == 'relu' else 0.1
        # Se for XOR com muitas entradas e ReLU, talvez precise de LR ainda menor ou mais épocas
        if logic_function == 'XOR' and n_inputs > 3 and act_func_name == 'relu':
            lr_act_test = 0.005
            epochs_activation_current = epochs_activation * 2 # Mais épocas para ReLU em XOR complexo
        else:
            epochs_activation_current = epochs_activation


        print(f"\nTestando Função de Ativação: {act_func_name.capitalize()} (LR={lr_act_test})")
        nn_activation = NeuralNetwork(n_inputs=n_inputs, n_hidden=n_hidden_activation, n_outputs=1,
                                      activation_func_name=act_func_name, use_bias=True)
        try:
            loss_history = nn_activation.train(X_train, y_train, epochs=epochs_activation_current, learning_rate=lr_act_test, verbose=False)
            final_loss = loss_history[-1]
            print(f"Função: {act_func_name.capitalize()} - Loss Final após {epochs_activation_current} épocas: {final_loss:.6f}")
            predictions = nn_activation.predict(X_train)
            accuracy = np.mean(predictions == y_train) * 100
            print(f"  Acurácia no treino: {accuracy:.2f}%")
            if final_loss > 0.1:
                 print("  (Pode não ter convergido otimamente, considere ajustar épocas/LR)")
        except Exception as e:
            print(f"Função: {act_func_name.capitalize()} - Erro durante o treinamento: {e}")

    print("\n--- Fim dos Experimentos ---")
    print("Observações Gerais:")
    print("1. Taxa de Aprendizado: Muito alta pode causar instabilidade; muito baixa torna o treino lento.")
    print("2. Bias: Essencial para flexibilidade do modelo, especialmente em problemas não linearmente separáveis como XOR.")
    print("3. Função de Ativação: Introduz não-linearidade. ReLU é muitas vezes mais rápida para convergir, mas Sigmoide/Tanh são clássicas.")
    print("   - Para XOR, uma camada oculta é crucial. O número de neurônios ocultos também impacta.")
    print("   - A estabilidade e velocidade de convergência dependem da combinação de todos esses hiperparâmetros.")

if __name__ == "__main__":
    run_experiment()