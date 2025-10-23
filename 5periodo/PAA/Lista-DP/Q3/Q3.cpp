#include <iostream>
#include <algorithm>
#include <vector>
#include <string>

using namespace std;

vector<vector<int>> maiorSubsequencia(const string& a, const string& b){
    vector<vector<int>> res(a.length()+1, vector<int>(b.length()+1, 0));
    for(int i=1;i<=a.length();i++){
        for(int j=1;j<=b.length();j++){
            if(a[i-1]==b[j-1]){
                res[i][j] = res[i-1][j-1]+1;   
            } else{
                res[i][j] = max(res[i-1][j], res[i][j-1]);
            }
        }
    }
    return res;
}

int main() {
    cout << "salve" << endl;
    vector<vector<int>> resp = maiorSubsequencia("ace", "abcde");
    for (int i = 0; i < resp.size(); i++) {
        for (int j = 0; j < resp[i].size(); j++) {
            cout << resp[i][j] << " "; // imprime o elemento seguido de espaço
        }
        cout << endl; // quebra de linha ao final de cada linha da matriz
    }
    cout << "Resposta: " << resp[resp.size() - 1][resp[0].size() - 1] << endl;
    return 0;
}