#ifndef GRAPH_ALGORITHMS_H
#define GRAPH_ALGORITHMS_H

#include "directedGraph.h"
#include "undirectedGraph.h"
#include <vector>
#include <string>

using namespace std;

class GraphAlgorithms {
public:
    // Breadth First Search (BFS)
    static vector<string> BFS(const Graph& graph, const string& startVertex);

    // Depth First Search (DFS)
    static vector<string> DFS(const Graph& graph, const string& startVertex);

    // Algoritmo de Prim para encontrar a árvore geradora mínima
    static int spanningTree(int V, int E, vector<vector<int>>& edges);

};

#endif // GRAPH_ALGORITHMS_H