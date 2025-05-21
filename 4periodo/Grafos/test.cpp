// C++ program to show that priority_queue is by 
// default a Max Heap 
#include <bits/stdc++.h> 
using namespace std; 
 
/*
    How to compile aand run:
   cd C:\Programacao\Repos\Cc-PucMG\4periodo\Grafos
    g++ -o test test.cpp  
    ./test
    
*/
// Driver code 
int main () { 
    // Creates a max heap 
    priority_queue <int> pq; 
    pq.push(1); 
    pq.push(5); 
    pq.push(10); 
    pq.push(20); 
    pq.push(30); 
  
    // One by one extract items from max heap 
    while (pq.empty() == false) { 
        cout << pq.top() << " "; 
        pq.pop(); 
    } 
  
    return 0; 
} 