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
    void printGraph()
    {
        for (int i = 0; i < this->V; i++)
        {
            cout << i << " => ";
            for (int j = 0; j < this->adj[i].size(); j++)
            {
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
};

int main(){
    Graph g(4);
    g.addEdge(2, 0);
    g.addEdge(0, 3);
    g.addEdge(0, 1);
    vector<int> bfs = g.bfs(2);
    for(auto it : bfs){
        cout << it << " ";
    }
}