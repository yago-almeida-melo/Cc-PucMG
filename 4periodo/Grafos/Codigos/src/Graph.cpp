#define GRAPH_INTERNAL_ACCESS

#include "Graph.h"
#include "Vertex.h"
#include "Edge.h"

#include <iostream>
#include <memory>
#include <string>
#include <unordered_map>
#include <vector>
#include <stdexcept>
#include <algorithm>

using namespace std;

Graph::Graph(): len(0) {}

int Graph::addVertex(const string& label, double heuristicWeight){
    if(labelToIndex.count(label)){
        return labelToIndex[label];
    }

    int index = vertices.size();

    Vertex v(label, heuristicWeight);
    vertices.push_back(v);
    labelToIndex[label] = index;
    adjList.emplace_back();

    len++;

    return index;
}

void Graph::removeVertex(const string& label){
    if(!labelToIndex.count(label)){
        throw invalid_argument("Vertex '" + label + "' does not exists.");
    }

    int index = labelToIndex[label];
    vertices[index].active = false;
    adjList[index].clear();

    for (auto& edges : adjList) {
        edges.erase(
            remove_if(edges.begin(), edges.end(),
                [index](const Edge& e) { return e.to == index; }),
            edges.end());
    }

    len--;
}

int Graph::getLenght(){
    return len;
}

vector<string> Graph::getNeighbors(const string& label) const{
    if(!labelToIndex.count(label)){
        throw invalid_argument("Vertex '" + label + "' does not exists.");
    }

    int index = labelToIndex.at(label);
    vector<string> neighbors;

    for(Edge neighbor : adjList[index]){
        string neighborLabel = vertices[neighbor.to].label;
        neighbors.push_back(neighborLabel);
    }

    return neighbors;
}

void Graph::print() const {
    for(int i = 0; i < int(adjList.size()); i++){

        if(vertices[i].active){
            
            cout << vertices[i].label << ": ";

            for(int j = 0; j < int(adjList[i].size()); j++ ){
                cout << "(" << vertices[adjList[i][j].to].label << ", " << adjList[i][j].weight << "); ";
            }
            cout << "\n";
        }
    }
}
