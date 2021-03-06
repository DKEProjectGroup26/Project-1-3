package phase3.algorithms;
import phase3.everything.*;

import java.io.IOException;

/**
 * A very light algorithm used to find a basic upper bound based on the highest-degree node in a graph
 */
public class BrooksTheorem implements Algorithm.Connected {
    /**
     * Implementation of {@link Algorithm}, runs the algorithm on a given graph
     */
    public void run(Runner runner, Graph graph) {
        int maxNeighbors = 0;
        // check if the graph is a cycle in the same loop
        boolean isCycle = true;
        
        for (Node node : graph.nodes) {
            int neighbors = node.getNeighbors().size();
            
            if (neighbors > maxNeighbors) maxNeighbors = neighbors;
            if (isCycle && neighbors != 2) isCycle = false; // not a cycle and stop checking
        }
        
        // delta + 1 is upper bound
        runner.upperBound(maxNeighbors + 1);
        
        if (isCycle) {
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
}