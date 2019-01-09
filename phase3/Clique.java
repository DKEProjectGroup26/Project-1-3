package phase3;

import java.util.ArrayList;

// NOT FINISHED
public class Clique implements Algorithm {
    public void run(Runner runner, Graph graph) {
        if (any2(graph)) runner.lowerBound(2);
        
        // use timeout
        // start from lower bound, no point checking below
        int startSize = Math.max(2, runner.currentLowerBound);
        for (int size = startSize;; size++) {
            if (!cliquePossible(graph, size)) break;
            // if cliqueExists, lowerBound, else break
        }
    }
    
    private static boolean any2(Graph graph) {
        // checks if there are 2 nodes connected anywhere in the graph
        // if there are -> lower bound = 2
        for (Node node : graph.nodes)
            if (!node.neighbors.isEmpty()) return true;
        
        return false;
    }
    
    private static boolean cliquePossible(Graph graph, int size) {
        // checks, based on numbers of neighbors, whether it's possible
        // to find a clique of the given size
        int minNeighbors = size - 1;
        int nodesFound = 0;
        
        for (Node node : graph.nodes) {
            if (node.neighbors.size() >= minNeighbors) {
                nodesFound++;
                if (nodesFound >= size) return true;
            }
        }
        
        return false;
    }
}