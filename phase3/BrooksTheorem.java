package phase3;

import java.io.IOException;

// NOT FINISHED
public class BrooksTheorem {
    public static void brooksUpperBound(Runner runner, Graph graph) {
        int maxNeighbors = 0;
        
        for (Node node : graph.nodes) {
            int neighbors = node.neighbors.size();
            
            if (neighbors > maxNeighbors) maxNeighbors = neighbors;
        }
        
        // do extra checks to see if delta or delta + 1 is the actual bound
        // for now assume delta + 1
        
        runner.upperBound(maxNeighbors);
    }
}