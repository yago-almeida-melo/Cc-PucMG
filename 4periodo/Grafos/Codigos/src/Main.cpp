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


    try {
        g.addEdge("A", "B", 3.5);
        g.addEdge("A", "C");
        //g.removeEdge("C", "B");
        g.print();
        
        vector<string> nbs = g.getNeighbors("A");
        for(string nb : nbs){
            cout << nb << " ";
        }

    } catch (const exception& e) {
        cerr << "Error: " << e.what() << endl;
    }

    return 0;
}
