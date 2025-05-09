#include <iostream>
#include "../include/allHeaders.h"
#include <string>
#include <vector>

using namespace std;

int main(){
    UndirectedGraph g;

    g.addVertex("1");
    g.addVertex("2");
    g.addVertex("3");
    g.addVertex("4");
    g.addVertex("5");
    g.addVertex("6");
    g.addVertex("7");
    g.addVertex("8");
    g.addVertex("9");

    try {
        g.addEdge("1", "2");
        g.addEdge("1", "6");
        g.addEdge("2", "3");
        g.addEdge("2", "4");
        g.addEdge("6", "7");
        g.addEdge("6", "9");
        g.addEdge("4", "5");
        g.addEdge("5", "8");
        g.addEdge("7", "8");
        g.print();
        vector<string> nbs = g.getNeighbors("1");
        for(string nb : nbs){
            cout << nb << " ";
        }
        cout << endl;
        vector<string> bfs = GraphAlgorithms::BFS(g, "1");
        cout << "BFS: ";
        for (const auto& vertex : bfs) {
            cout << vertex << " ";
        }
        cout << endl;
        vector<string> dfs = GraphAlgorithms::DFS(g, "1");
        cout << "DFS: ";
        for (const auto& vertex : dfs) {
            cout << vertex << " ";
        }
        cout << endl;
    } catch (const exception& e) {
        cerr << "Error: " << e.what() << endl;
    }

    return 0;
}
