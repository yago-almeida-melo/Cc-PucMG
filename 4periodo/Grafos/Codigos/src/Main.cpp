#include <iostream>
#include "../include/allHeaders.h"
#include <string>
#include <vector>

using namespace std;
/*
    How to compile and run this code:
    cd C:\Programacao\Repos\Cc-PucMG\4periodo\Grafos\Codigos\src    &&   mingw32-make run
*/

int main(){
    UndirectedGraph g;
    g.addVertex("A");
    g.addVertex("B");
    g.addVertex("C");
    g.addVertex("D");
    g.addVertex("E");
    g.addVertex("F");

    try {
        g.addEdge("A", "B", 7);
        g.addEdge("A", "C", 1);
        g.addEdge("B", "D", 4);
        g.addEdge("C", "D", 5);
        g.addEdge("C", "E", 2);
        g.addEdge("E", "F", 3);
        /*g.print();
        vector<string> nbs = g.getNeighbors("A");
        for(string nb : nbs){
            cout << nb << " ";
        }*/
        int a = labeltoIndex["A"];
        //int x = GraphAlgorithms::spanningTree(g.getLenght(), g.getAdjList().size(), g.getAdjList());
        //cout << "Minimum Spanning Tree weight: " << x << endl;
    } catch (const exception& e) {
        cerr << "Error: " << e.what() << endl;
    }

    return 0;
}
