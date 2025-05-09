#include "../include/graphAlgorithms.h"
#include <queue>
#include <stack>
#include <unordered_set>

std::vector<std::string> GraphAlgorithms::BFS(const Graph& graph, const std::string& startVertex) {
    std::vector<std::string> visited;
    std::queue<std::string> toVisit;
    std::unordered_set<std::string> visitedSet;

    toVisit.push(startVertex);
    visitedSet.insert(startVertex);

    while (!toVisit.empty()) {
        std::string current = toVisit.front();
        toVisit.pop();
        visited.push_back(current);

        for (const auto& neighbor : graph.getNeighbors(current)) {
            if (visitedSet.find(neighbor) == visitedSet.end()) {
                toVisit.push(neighbor);
                visitedSet.insert(neighbor);
            }
        }
    }

    return visited;
}

std::vector<std::string> GraphAlgorithms::DFS(const Graph& graph, const std::string& startVertex) {
    std::vector<std::string> visited;
    std::stack<std::string> toVisit;
    std::unordered_set<std::string> visitedSet;

    toVisit.push(startVertex);

    while (!toVisit.empty()) {
        std::string current = toVisit.top();
        toVisit.pop();

        if (visitedSet.find(current) == visitedSet.end()) {
            visited.push_back(current);
            visitedSet.insert(current);

            for (const auto& neighbor : graph.getNeighbors(current)) {
                toVisit.push(neighbor);
            }
        }
    }

    return visited;
}