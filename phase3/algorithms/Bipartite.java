package phase3.algorithms;
import phase3.everything.*;

import java.util.ArrayList;

/**
 * A light algorithm to check if a graph is bipartite (if it can be solved with 2 colors)
 */
public class Bipartite implements Algorithm.Connected {
    /**
     * Implementation of {@link Algorithm}, runs the algorithm on a given graph
     */
    public void run(Runner runner, Graph graph) {
        // initialized to 0s
        int[] colors = new int[graph.nodes.size()];
        
        // colors used are 1 and 2
        int currentColor = 1;
        ArrayList<Node> layer = new ArrayList<Node>();
        
        // starting from the first node
        layer.add(graph.nodes.get(0));
        
        while (!layer.isEmpty()) {
            ArrayList<Node> newLayer = new ArrayList<Node>();
            for (Node node : layer) {
                int nodeColor = colors[node.getIndex()];
                
                if (nodeColor > 0 && nodeColor != currentColor) {
                    // graph is not 2-colorable
                    runner.lowerBound(3);
                    return;
                }
                
                if (nodeColor == 0) colors[node.getIndex()] = currentColor;
                
                // add neighbors to newLayer
                for (Node neighbor : node.getNeighbors()) {
                    // skip seen nodes
                    if (colors[neighbor.getIndex()] > 0) continue;
                    
                    // skip duplicate nodes
                    if (newLayer.contains(neighbor)) continue;
                    
                    newLayer.add(neighbor);
                }
            }
            
            // newLayer = uncolored neighbors of layer
            
            layer = newLayer;
            currentColor = 3 - currentColor; /* 1 <-> 2 */
        }
        
        // if while didn't return, the graph is 2-colorable
        runner.upperBound(2);
    }
}