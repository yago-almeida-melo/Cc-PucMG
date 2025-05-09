#ifndef GRAPH_ALGORITHMS_H
#define GRAPH_ALGORITHMS_H

#include "directedGraph.h"
#include "undirectedGraph.h"
#include <vector>
#include <string>

class GraphAlgorithms {
public:
    // Busca em Largura (BFS)
    static std::vector<std::string> BFS(const Graph& graph, const std::string& startVertex);

    // Busca em Profundidade (DFS)
    static std::vector<std::string> DFS(const Graph& graph, const std::string& startVertex);
};

#endif // GRAPH_ALGORITHMS_H