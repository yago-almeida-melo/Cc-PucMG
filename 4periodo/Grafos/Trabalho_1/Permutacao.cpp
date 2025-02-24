#include <iostream>
#include <vector>
#include <algorithm>

using namespace std;

class Graph {
private:
    int V; // Número de vértices
    vector<vector<int>> adj; // Lista de adjacência

public:
    Graph(int V) : V(V), adj(V) {}

    void addEdge(int u, int v) {
        adj[u].push_back(v);
        adj[v].push_back(u); // Grafo não direcionado
    }

    // Verifica se a permutação forma um ciclo válido
    bool isValidCycle(const vector<int>& perm) {
        int n = perm.size();
        for (int i = 0; i < n; i++) {
            int u = perm[i];
            int v = perm[(i + 1) % n]; // Próximo vértice (com volta ao início)
            
            // Verifica se há aresta entre u e v
            if (find(adj[u].begin(), adj[u].end(), v) == adj[u].end()) {
                return false;
            }
        }
        return true;
    }

    // Gera todas as permutações possíveis e verifica ciclos
    void enumerateCycles() {
        vector<int> vertices(V);
        for (int i = 0; i < V; i++) {
            vertices[i] = i;
        }

        vector<vector<int>> foundCycles;

        do {
            if (vertices[0] != 0) break; // Evita ciclos repetidos (fixando primeiro vértice)
            if (isValidCycle(vertices)) {
                foundCycles.push_back(vertices);
            }
        } while (next_permutation(vertices.begin() + 1, vertices.end()));

        // Exibe os ciclos encontrados
        cout << "Ciclos encontrados:" << endl;
        for (const auto& cycle : foundCycles) {
            for (int v : cycle) {
                cout << v << " ";
            }
            cout << cycle[0] << endl; // Retorna ao início para completar o ciclo
        }
    }
};

int main() {
    Graph g(4); // Criando um grafo com 4 vértices
    g.addEdge(0, 1);
    g.addEdge(1, 2);
    g.addEdge(2, 3);
    g.addEdge(3, 0);
    g.addEdge(0, 2);
    g.addEdge(1, 3);

    g.enumerateCycles();
    return 0;
}
