package phase3;

import java.io.IOException;

// NOT FINISHED
public class BrooksTheorem implements Algorithm {
    public void run(Runner runner, Graph graph) {
        int maxNeighbors = 0;
        
        for (Node node : graph.nodes) {
            int neighbors = node.neighbors.size();
            
            if (neighbors > maxNeighbors) maxNeighbors = neighbors;
        }
        
        // delta + 1 is upper bound
        runner.upperBound(maxNeighbors + 1);
        
        // check if delta is upper bound
    }
}