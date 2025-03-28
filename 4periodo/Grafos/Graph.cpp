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
};

int main(){
    Graph g(7);
    g.addEdge(0, 1);
    g.addEdge(0, 3);
    g.addEdge(1, 3);
    g.addEdge(1, 4);
    g.addEdge(2, 3);
    g.addEdge(3, 4);
    g.addEdge(4, 5);
    g.addEdge(5, 6);
    g.addEdge(6, 4);
    vector<int> dfs = g.dfsOfGraph();
    for(auto it : dfs){
        cout << it << " ";
    }
    //0134562
}