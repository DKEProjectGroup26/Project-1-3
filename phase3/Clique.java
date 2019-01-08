package phase3;

import java.util.ArrayList;

// NOT FINISHED
public class Clique implements Algorithm {
    public void run(Runner runner, Graph graph) {
        if (any2(graph)) runner.lowerBound(2);
    }
    
    private static boolean any2(Graph graph) {
        for (Node node : graph.nodes)
            if (!node.neighbors.isEmpty()) return true;
        
        return false;
    }
}