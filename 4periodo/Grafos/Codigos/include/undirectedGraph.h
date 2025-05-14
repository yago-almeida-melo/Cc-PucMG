#ifndef UNDIRECTED_GRAPH_H
#define UNDIRECTED_GRAPH_H
#define GRAPH_INTERNAL_ACCESS

#include "Graph.h"
using namespace std;

class UndirectedGraph : public Graph {
public:
    void addEdge(const string& from, const string& to, double weight = 1.0);
    void removeEdge(const string& from, const string& to);
};

#endif