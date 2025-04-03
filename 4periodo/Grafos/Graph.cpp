#include <iostream>
#include <vector>
#include <algorithm>
#include <queue>

using namespace std;

class Graph {
private:
    int V;
    int E;
    std::vector<std::vector<int>> adj;

public:
    Graph(int V) : V(V), adj(V) {}
    void addEdge(int u, int v) {
        adj[u].push_back(v);
        adj[v].push_back(u);
    }
    void printGraph(){
        for (int i = 0; i < this->V; i++) {
            cout << i << " => ";
            for (int j = 0; j < this->adj[i].size(); j++) {
                cout << this->adj[i][j] << " ";
            }
            cout << endl;
        }
    }

    vector<int> bfs(int x) {
        vector<bool> visited(V, false);
        visited[x] = true;
        queue<int> q;
        q.push(x);
        vector<int> bfs;
        while (!q.empty()){
            int node = q.front();
            q.pop();
            bfs.push_back(node);
            for (auto it : adj[node]) {
                if (!visited[it]){
                    visited[it] = true;
                    q.push(it);
                }
            }
        }
        return bfs;
    }

    void dfs(int node, vector<bool> &vis, vector<int> &storeDfs) {
        storeDfs.push_back(node); 
        vis[node] = true; 
        for(auto it : adj[node]) {
            if(!vis[it]) {
                dfs(it, vis, storeDfs); 
            }
        }
    }
	vector<int>dfsOfGraph(){
	    vector<int> storeDfs; 
	    vector<bool> vis(V, false); 
        for(int i = 0;i<V;i++) {
            if(!vis[i]) dfs(i, vis, storeDfs); 
        }
	    return storeDfs; 
	}
    // Função para ordenar os sucessores de cada vertice
    void insercao(){
        for(int x=0;x<this->V;x++){
            for (int i = 1; i < adj[x].size(); i++) {
		        int tmp = this->adj[x][i];
                int j = i - 1;
                while ((j >= 0) && (tmp < this->adj[x][j])) {
                    this->adj[x][j + 1] = this->adj[x][j];
                    j--;
                }
                this->adj[x][j + 1] = tmp;
            }
        }
    }
};

int main(){
    Graph g(8);
    g.addEdge(0, 3);
    g.addEdge(0, 1);
    g.addEdge(1, 4);
    g.addEdge(1, 3);
    g.addEdge(2, 3);
    g.addEdge(3, 4);
    g.addEdge(4, 5);
    g.addEdge(5, 6);
    g.addEdge(6, 4);
    g.addEdge(3, 7);
    /*
    vector<int> dfs = g.dfsOfGraph();
    cout << "DFS: ";
    for(auto it : dfs){
        cout << it << " ";
    }
    cout << endl;
    vector<int> bfs = g.bfs(0);
    cout << "BFS: ";
    for(auto it : bfs){
        cout << it << " ";
    }
    cout << endl;
    */
    g.printGraph();
    g.insercao();
    cout << "Grafo ordenado: " << endl;
    g.printGraph();
}