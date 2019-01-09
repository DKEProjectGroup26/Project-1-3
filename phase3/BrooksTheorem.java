package phase3;

import java.io.IOException;

// FINISHED, NOT CHECKED
public class BrooksTheorem implements Algorithm {
    public void run(Runner runner, Graph graph) {
        int maxNeighbors = 0;
        
        for (Node node : graph.nodes) {
            int neighbors = node.neighbors.size();
            
            if (neighbors > maxNeighbors) maxNeighbors = neighbors;
        }
        
        // delta + 1 is upper bound
        runner.upperBound(maxNeighbors + 1);
        
        if (isCycle(graph)) {
            // if graph is odd cycle, delta + 1 is the upper bound
            if (graph.nodes.size() % 2 == 1) return; // stop checking
        }
        
        if (isComplete(graph)) return; // stop checking
        
        // at this point the graph is not complete and not an odd cycle
        // so the actual upper bound is just delta
        runner.upperBound(maxNeighbors);
    }
    
    private static boolean isComplete(Graph graph) {
        // check if the graph is a clique
        int n = graph.nodes.size();
        return graph.numberOfEdges == (n * (n - 1)) / 2;
    }
    
    private static boolean isCycle(Graph graph) {
        // for a graph to be a cycle, it must be a
        // single circle with 2 edges per node
        for (Node node : graph.nodes)
            if (node.neighbors.size() != 2) return false;
        
        return true;
    }
}