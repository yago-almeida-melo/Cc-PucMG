#include <iostream>
#include <vector>
#include <set>

using namespace std;

class Graph {
private:
    int V;
    vector<vector<int>> adj;
    set<vector<int>> foundCycles;

public:
    Graph(int V) : V(V), adj(V) {}

    void addEdge(int u, int v) {
        adj[u].push_back(v);
        adj[v].push_back(u);
    }

    void dfs(int start, int v, vector<bool>& visited, vector<int>& path) {
        visited[v] = true;
        path.push_back(v);

        for (int neighbor : adj[v]) {
            if (!visited[neighbor]) {
                dfs(start, neighbor, visited, path);
            } else if (neighbor == start && path.size() > 2) {
                vector<int> cycle = path;
                cycle.push_back(start);
                foundCycles.insert(cycle);
            }
        }

        visited[v] = false;
        path.pop_back();
    }

    void enumerateCycles() {
        vector<bool> visited(V, false);
        vector<int> path;

        for (int i = 0; i < V; i++) {
            dfs(i, i, visited, path);
        }

        cout << "Ciclos encontrados:" << endl;
        for (const auto& cycle : foundCycles) {
            for (int v : cycle) {
                cout << v << " ";
            }
            cout << endl;
        }
    }
};

int main() {
    Graph g(4);
    g.addEdge(0, 1);
    g.addEdge(1, 2);
    g.addEdge(2, 3);
    g.addEdge(3, 0);
    g.addEdge(0, 2);
    g.addEdge(1, 3);

    g.enumerateCycles();
    return 0;
}
