
#ifndef GRAPH_INTERNAL_ACCESS
#error "Graph.h is an internal header and should not be included directly. Include DirectedGraph.h or UndirectedGraph.h instead."
#endif

#ifndef GRAPH_H
#define GRAPH_H

#include <iostream>
#include <vector>
#include <string>
#include <memory>
#include <unordered_map>
#include "Vertex.h"
#include "Edge.h"

using namespace std;

class Graph {
    protected:
        vector<vector<Edge>> adjList;                      
        vector<Vertex> vertices;                               
        unordered_map<string, int> labelToIndex;    // converte label em indice para add na adjList
        int len;

        Graph();

    public:

        virtual int addVertex(const string& label, double heuristicWeight = 0.0);
        int getLenght();
        void removeVertex(const string& label);
        void print() const;
        vector<string> getNeighbors(const string& label) const;

};

#endif
