import numpy as np
import time
import heapq
from collections import deque

class PuzzleState:
    """
    Classe que representa um estado do quebra-cabeça 8-puzzle
    """
    def __init__(self, board, parent=None, action=None, cost=0):
        self.board = np.array(board, dtype=np.int8)
        self.parent = parent
        self.action = action  # ação que levou a este estado
        self.cost = cost      # custo acumulado
        self.blank_pos = tuple(np.argwhere(self.board == 0)[0])
        
    def __eq__(self, other):
        return np.array_equal(self.board, other.board)
    
    def __lt__(self, other):
        # Necessário para uso em heapq
        return self.cost < other.cost
    
    def __hash__(self):
        # Necessário para uso em conjuntos
        return hash(self.board.tobytes())
    
    def __str__(self):
        s = ""
        for i in range(3):
            for j in range(3):
                if self.board[i, j] == 0:
                    s += "_ "
                else:
                    s += str(self.board[i, j]) + " "
            s += "\n"
        return s
    
    def get_blank_position(self):
        return self.blank_pos
    
    def is_goal(self, goal_state):
        return np.array_equal(self.board, goal_state.board)
    
    def get_possible_actions(self):
        """
        Retorna as possíveis ações (movimentos) a partir do estado atual
        """
        actions = []
        i, j = self.blank_pos
        
        # Verificar movimentos possíveis (cima, baixo, esquerda, direita)
        if i > 0:  # mover para biaxo
            actions.append('DOWN')
        if i < 2:  # mover para cima
            actions.append('UP')
        if j > 0:  # mover para direita
            actions.append('RIGHT')
        if j < 2:  # mover para esquerda
            actions.append('LEFT')
            
        return actions
    
    def get_successor(self, action):
        """
        Retorna o estado resultante de executar a ação especificada
        """
        i, j = self.blank_pos
        new_board = self.board.copy()
        
        if action == 'DOWN':
            new_board[i, j], new_board[i-1, j] = new_board[i-1, j], new_board[i, j]
        elif action == 'UP':
            new_board[i, j], new_board[i+1, j] = new_board[i+1, j], new_board[i, j]
        elif action == 'RIGHT':
            new_board[i, j], new_board[i, j-1] = new_board[i, j-1], new_board[i, j]
        elif action == 'LEFT':
            new_board[i, j], new_board[i, j+1] = new_board[i, j+1], new_board[i, j]
            
        return PuzzleState(new_board, self, action, self.cost + 1)
    
    def manhattan_distance(self, goal_state):
        """
        Calcula a distância de Manhattan (h1) entre este estado e o estado objetivo
        """
        distance = 0
        for i in range(3):
            for j in range(3):
                if self.board[i, j] != 0:  # ignora espaço vazio
                    # Encontrar a posição desse número no estado objetivo
                    goal_pos = np.argwhere(goal_state.board == self.board[i, j])[0]
                    # Calcular distância de Manhattan
                    distance += abs(i - goal_pos[0]) + abs(j - goal_pos[1])
        return distance
    
    def misplaced_tiles(self, goal_state):
        """
        Calcula o número de peças fora do lugar (h2) entre este estado e o estado objetivo
        """
        count = 0
        for i in range(3):
            for j in range(3):
                if self.board[i, j] != 0 and self.board[i, j] != goal_state.board[i, j]:
                    count += 1
        return count
    
    def get_path(self):
        """
        Retorna o caminho da raiz até este estado
        """
        path = []
        current = self
        while current:
            path.append(current)
            current = current.parent
        return path[::-1]  # inverte o caminho para começar pela raiz


class PuzzleSolver:
    """
    Classe que implementa os algoritmos de busca para resolver o quebra-cabeça
    """
    def __init__(self, initial_state, goal_state):
        self.initial_state = initial_state
        self.goal_state = goal_state
        
    def breadth_first_search(self):
        """
        Implementação do algoritmo de busca em largura (BFS)
        """
        start_time = time.time()
        
        # Inicializar a fila, conjuntos de estados visitados e explorados
        queue = deque([self.initial_state])
        visited = {hash(self.initial_state.board.tobytes())}
        
        nodes_explored = 0
        max_queue_size = 1
        
        while queue:
            max_queue_size = max(max_queue_size, len(queue))
            
            state = queue.popleft()
            nodes_explored += 1
            
            # Verificar se é o estado objetivo
            if state.is_goal(self.goal_state):
                path = state.get_path()
                end_time = time.time()
                return {
                    "success": True,
                    "path": path,
                    "path_length": len(path) - 1,
                    "nodes_explored": nodes_explored,
                    "max_frontier_size": max_queue_size,
                    "execution_time": end_time - start_time
                }
            
            # Expandir o estado atual
            for action in state.get_possible_actions():
                successor = state.get_successor(action)
                successor_hash = hash(successor.board.tobytes())
                
                if successor_hash not in visited:
                    queue.append(successor)
                    visited.add(successor_hash)
        
        end_time = time.time()
        return {
            "success": False,
            "nodes_explored": nodes_explored,
            "max_frontier_size": max_queue_size,
            "execution_time": end_time - start_time
        }
    
    def depth_first_search(self):
        """
        Implementação do algoritmo de busca em profundidade (DFS)
        """
        start_time = time.time()
        
        # Inicializar a pilha, conjuntos de estados visitados
        stack = [self.initial_state]
        visited = {hash(self.initial_state.board.tobytes())}
        
        nodes_explored = 0
        max_stack_size = 1
        
        while stack:
            max_stack_size = max(max_stack_size, len(stack))
            
            state = stack.pop()
            nodes_explored += 1
            
            # Verificar se é o estado objetivo
            if state.is_goal(self.goal_state):
                path = state.get_path()
                end_time = time.time()
                return {
                    "success": True,
                    "path": path,
                    "path_length": len(path) - 1,
                    "nodes_explored": nodes_explored,
                    "max_frontier_size": max_stack_size,
                    "execution_time": end_time - start_time
                }
            
            # Expandir o estado atual (em ordem inversa para manter a ordem original na pilha)
            for action in reversed(state.get_possible_actions()):
                successor = state.get_successor(action)
                successor_hash = hash(successor.board.tobytes())
                
                if successor_hash not in visited:
                    stack.append(successor)
                    visited.add(successor_hash)
        
        end_time = time.time()
        return {
            "success": False,
            "nodes_explored": nodes_explored,
            "max_frontier_size": max_stack_size,
            "execution_time": end_time - start_time
        }
    
    def uniform_cost_search(self):
        """
        Implementação do algoritmo de busca de custo uniforme
        """
        start_time = time.time()
        
        # Inicializar a fila de prioridade (min-heap)
        frontier = [(self.initial_state.cost, 0, self.initial_state)]
        frontier_set = {hash(self.initial_state.board.tobytes())}
        explored = set()
        
        nodes_explored = 0
        max_frontier_size = 1
        counter = 1  # Para desempate quando os custos são iguais
        
        while frontier:
            max_frontier_size = max(max_frontier_size, len(frontier))
            
            _, _, state = heapq.heappop(frontier)
            state_hash = hash(state.board.tobytes())
            
            # Tentar remover apenas se existir no conjunto
            if state_hash in frontier_set:
                frontier_set.remove(state_hash)
            
            # Verificar se é o estado objetivo
            if state.is_goal(self.goal_state):
                path = state.get_path()
                end_time = time.time()
                return {
                    "success": True,
                    "path": path,
                    "path_length": len(path) - 1,
                    "nodes_explored": nodes_explored,
                    "max_frontier_size": max_frontier_size,
                    "execution_time": end_time - start_time
                }
            
            explored.add(hash(state.board.tobytes()))
            nodes_explored += 1
            
            # Expandir o estado atual
            for action in state.get_possible_actions():
                successor = state.get_successor(action)
                successor_hash = hash(successor.board.tobytes())
                
                if successor_hash not in explored and successor_hash not in frontier_set:
                    heapq.heappush(frontier, (successor.cost, counter, successor))
                    frontier_set.add(successor_hash)
                    counter += 1
                # Se já está na fronteira, atualizar se o novo caminho for melhor
                elif successor_hash in frontier_set:
                    # Esta parte é mais complicada no Python com heapq
                    # Simplificando, adicionamos novamente e deixamos o algoritmo lidar com isso
                    heapq.heappush(frontier, (successor.cost, counter, successor))
                    frontier_set.add(successor_hash)
                    counter += 1
        
        end_time = time.time()
        return {
            "success": False,
            "nodes_explored": nodes_explored,
            "max_frontier_size": max_frontier_size,
            "execution_time": end_time - start_time
        }
    
    def greedy_search(self, heuristic_func):
        """
        Implementação do algoritmo de busca gulosa
        """
        start_time = time.time()
        
        # Inicializar a fila de prioridade (min-heap)
        # Prioridade é baseada apenas na heurística
        h_value = heuristic_func(self.initial_state, self.goal_state)
        frontier = [(h_value, 0, self.initial_state)]
        frontier_set = {hash(self.initial_state.board.tobytes())}
        explored = set()
        
        nodes_explored = 0
        max_frontier_size = 1
        counter = 1  # Para desempate quando os valores heurísticos são iguais
        
        while frontier:
            max_frontier_size = max(max_frontier_size, len(frontier))
            
            _, _, state = heapq.heappop(frontier)
            state_hash = hash(state.board.tobytes())
            
            # Tentar remover apenas se existir no conjunto
            if state_hash in frontier_set:
                frontier_set.remove(state_hash)
            
            # Verificar se é o estado objetivo
            if state.is_goal(self.goal_state):
                path = state.get_path()
                end_time = time.time()
                return {
                    "success": True,
                    "path": path,
                    "path_length": len(path) - 1,
                    "nodes_explored": nodes_explored,
                    "max_frontier_size": max_frontier_size,
                    "execution_time": end_time - start_time
                }
            
            explored.add(hash(state.board.tobytes()))
            nodes_explored += 1
            
            # Expandir o estado atual
            for action in state.get_possible_actions():
                successor = state.get_successor(action)
                successor_hash = hash(successor.board.tobytes())
                
                if successor_hash not in explored and successor_hash not in frontier_set:
                    h_value = heuristic_func(successor, self.goal_state)
                    heapq.heappush(frontier, (h_value, counter, successor))
                    frontier_set.add(successor_hash)
                    counter += 1
        
        end_time = time.time()
        return {
            "success": False,
            "nodes_explored": nodes_explored,
            "max_frontier_size": max_frontier_size,
            "execution_time": end_time - start_time
        }
    
    def a_star_search(self, heuristic_func):
        """
        Implementação do algoritmo A*
        """
        start_time = time.time()
        
        # Inicializar a fila de prioridade (min-heap)
        # Prioridade é baseada em f(n) = g(n) + h(n)
        h_value = heuristic_func(self.initial_state, self.goal_state)
        f_value = self.initial_state.cost + h_value
        frontier = [(f_value, 0, self.initial_state)]
        frontier_dict = {hash(self.initial_state.board.tobytes()): (f_value, self.initial_state)}
        explored = set()
        
        nodes_explored = 0
        max_frontier_size = 1
        counter = 1  # Para desempate quando os valores f são iguais
        
        while frontier:
            max_frontier_size = max(max_frontier_size, len(frontier))
            
            _, _, state = heapq.heappop(frontier)
            state_hash = hash(state.board.tobytes())
            
            # Tentar remover apenas se existir no dicionário
            if state_hash in frontier_dict:
                del frontier_dict[state_hash]
            
            # Verificar se é o estado objetivo
            if state.is_goal(self.goal_state):
                path = state.get_path()
                end_time = time.time()
                return {
                    "success": True,
                    "path": path,
                    "path_length": len(path) - 1,
                    "nodes_explored": nodes_explored,
                    "max_frontier_size": max_frontier_size,
                    "execution_time": end_time - start_time
                }
            
            explored.add(hash(state.board.tobytes()))
            nodes_explored += 1
            
            # Expandir o estado atual
            for action in state.get_possible_actions():
                successor = state.get_successor(action)
                successor_hash = hash(successor.board.tobytes())
                
                if successor_hash in explored:
                    continue
                
                h_value = heuristic_func(successor, self.goal_state)
                f_value = successor.cost + h_value
                
                if successor_hash not in frontier_dict:
                    heapq.heappush(frontier, (f_value, counter, successor))
                    frontier_dict[successor_hash] = (f_value, successor)
                    counter += 1
                elif successor_hash in frontier_dict and f_value < frontier_dict[successor_hash][0]:
                    # Encontramos um caminho melhor para este estado
                    # Atualizar o estado na fronteira
                    heapq.heappush(frontier, (f_value, counter, successor))
                    frontier_dict[successor_hash] = (f_value, successor)
                    counter += 1
        
        end_time = time.time()
        return {
            "success": False,
            "nodes_explored": nodes_explored,
            "max_frontier_size": max_frontier_size,
            "execution_time": end_time - start_time
        }


def generate_random_puzzle():
    """
    Gera um quebra-cabeça 8-puzzle aleatório que seja solucionável
    """
    numbers = list(range(9))  # 0-8, onde 0 representa o espaço vazio
    np.random.shuffle(numbers)
    
    # Verificar se é solucionável
    inversions = 0
    for i in range(9):
        if numbers[i] == 0:
            continue
        for j in range(i + 1, 9):
            if numbers[j] == 0:
                continue
            if numbers[i] > numbers[j]:
                inversions += 1
    
    # Se o número de inversões for ímpar, o quebra-cabeça não é solucionável
    if inversions % 2 == 1:
        # Trocar duas peças para torná-lo solucionável (exceto o espaço vazio)
        for i in range(9):
            if numbers[i] != 0 and i + 1 < 9 and numbers[i + 1] != 0:
                numbers[i], numbers[i + 1] = numbers[i + 1], numbers[i]
                break
    
    # Converter para matriz 3x3
    board = np.array(numbers).reshape(3, 3)
    return board


def print_results(results, algorithm_name, heuristic_name=None):
    """
    Imprime os resultados de um algoritmo de busca
    """
    print(f"\n--- {algorithm_name}{' com ' + heuristic_name if heuristic_name else ''} ---")
    
    if results["success"]:
        print(f"Solução encontrada em {results['path_length']} movimentos!")
        print(f"Nós explorados: {results['nodes_explored']}")
        print(f"Tamanho máximo da fronteira: {results['max_frontier_size']}")
        print(f"Tempo de execução: {results['execution_time']:.4f} segundos")
        
        # Mostrar o caminho
        print("\nCaminho da solução:")
        for i, state in enumerate(results["path"]):
            if i > 0:
                print(f"Movimento {i}: {state.action}")
            print(state)
    else:
        print("Não foi possível encontrar uma solução.")
        print(f"Nós explorados: {results['nodes_explored']}")
        print(f"Tamanho máximo da fronteira: {results['max_frontier_size']}")
        print(f"Tempo de execução: {results['execution_time']:.4f} segundos")


def run_comparison(initial_board, goal_board):
    """
    Executa todos os algoritmos de busca e compara seus desempenhos
    """
    initial_state = PuzzleState(initial_board)
    goal_state = PuzzleState(goal_board)
    solver = PuzzleSolver(initial_state, goal_state)
    
    print("\n===== Estado Inicial =====")
    print(initial_state)
    print("===== Estado Objetivo =====")
    print(goal_state)
    
    # Resultados para comparação
    results = {}
    
    # Executar BFS
    print("\nExecutando Busca em Largura (BFS)...")
    results["BFS"] = solver.breadth_first_search()
    print_results(results["BFS"], "Busca em Largura (BFS)")
    
    # Executar DFS
    print("\nExecutando Busca em Profundidade (DFS)...")
    results["DFS"] = solver.depth_first_search()
    print_results(results["DFS"], "Busca em Profundidade (DFS)")
    
    # Executar Busca de Custo Uniforme
    print("\nExecutando Busca de Custo Uniforme...")
    results["UCS"] = solver.uniform_cost_search()
    print_results(results["UCS"], "Busca de Custo Uniforme")
    
    # Executar Busca Gulosa (com heurística de Manhattan)
    print("\nExecutando Busca Gulosa (Heurística: Distância de Manhattan)...")
    results["Greedy_Manhattan"] = solver.greedy_search(lambda s, g: s.manhattan_distance(g))
    print_results(results["Greedy_Manhattan"], "Busca Gulosa", "Distância de Manhattan")
    
    # Executar A* (com heurística de Manhattan)
    print("\nExecutando A* (Heurística: Distância de Manhattan)...")
    results["A_Star_Manhattan"] = solver.a_star_search(lambda s, g: s.manhattan_distance(g))
    print_results(results["A_Star_Manhattan"], "A*", "Distância de Manhattan")
    
    # Executar A* (com heurística de peças fora do lugar)
    print("\nExecutando A* (Heurística: Peças Fora do Lugar)...")
    results["A_Star_Misplaced"] = solver.a_star_search(lambda s, g: s.misplaced_tiles(g))
    print_results(results["A_Star_Misplaced"], "A*", "Peças Fora do Lugar")
    
    # Comparar resultados
    print("\n===== Comparação de Algoritmos =====")
    headers = ["Algoritmo", "Solução", "Movimentos", "Nós Explorados", "Tam. Máx. Fronteira", "Tempo (s)"]
    print(f"{headers[0]:<25} {headers[1]:<10} {headers[2]:<12} {headers[3]:<15} {headers[4]:<20} {headers[5]:<12}")
    
    for algo, result in results.items():
        success = "Sim" if result["success"] else "Não"
        moves = result["path_length"] if result["success"] else "-"
        print(f"{algo:<25} {success:<10} {moves:<12} {result['nodes_explored']:<15} "
              f"{result['max_frontier_size']:<20} {result['execution_time']:.4f}")
    
    return results


def main():
    choice = -1
    while(choice != 0):
        print("8-Puzzle Solver")
        print("=" * 30)
    
        # Estado objetivo comum: números em ordem
        goal_board = np.array([
            [1, 2, 3],
            [4, 5, 6],
            [7, 8, 0]
        ])
    
        # Opções
        print("\nEscolha uma opção:")
        print("0. Sair")
        print("1. Usar um quebra-cabeça aleatório")
        print("2. Usar um quebra-cabeça personalizado")
        print("3. Usar um quebra-cabeça fácil pré-definido")
        print("4. Usar um quebra-cabeça médio pré-definido")
        print("5. Usar um quebra-cabeça difícil pré-definido")
    
        choice = input("\nEscolha (1-5): ")
    
        if choice == '1':
            initial_board = generate_random_puzzle()
        elif choice == '2':
            print("\nDigite o quebra-cabeça como uma sequência de 9 números (0-8, onde 0 é o espaço vazio).")
            print("Exemplo: 1 2 3 4 0 5 6 7 8")
            try:
                numbers = list(map(int, input("Sequência: ").split()))
                if len(numbers) != 9 or set(numbers) != set(range(9)):
                    raise ValueError("Entrada inválida: deve ter exatamente 9 números de 0 a 8")
                initial_board = np.array(numbers).reshape(3, 3)
            except Exception as e:
                print(f"Erro: {e}")
                print("Usando um quebra-cabeça aleatório")
                initial_board = generate_random_puzzle()
        elif choice == '3':
            # Quebra-cabeça fácil (2-3 movimentos)
            initial_board = np.array([
                [1, 2, 3],
                [4, 5, 6],
                [7, 0, 8]
            ])
        elif choice == '4':
            # Quebra-cabeça médio (8-12 movimentos)
            initial_board = np.array([
                [1, 3, 0],
                [4, 2, 5],
                [7, 8, 6]
            ])
        elif choice == '5':
            # Quebra-cabeça difícil (20+ movimentos)
            initial_board = np.array([
                [8, 6, 7],
                [2, 5, 4],
                [3, 0, 1]
            ])
        elif choice == '0':
            print("SAINDO...")
            break
    
        run_comparison(initial_board, goal_board)


if __name__ == "__main__":
    main()