package phase3;

import java.util.ArrayList;
import java.util.Collections;

public class WelshPowell implements Algorithm.Connected {
    public void run(Runner runner, Graph graph) {
        // replace the graph with a deep clone to avoid messing up the original
        graph = new Graph(graph);

        // sort the nodes by number of connections in descending order
        Collections.sort(graph.nodes, Collections.reverseOrder());

        // create an array to store the colors of the nodes, initialized to 0 by default
        int[] colors = new int[graph.nodes.size()];
        
        // keep track of the highest color used
        int maxColor = 1;
        
        // colors start from 1
        for (int color = 1;; color++) {
            boolean nodeColored = false;
            
            // assign "color" to any node that can accept it in order
            nodeLoop: for (Node node : graph.nodes) {
                // if the node has a color, skip
                // TODO: keep a list of uncolored nodes
                if (colors[node.index] > 0) continue;
                
                for (Node neighbor : node.neighbors)
                    if (colors[neighbor.index] == color)
                        continue nodeLoop;

                // the node has no neighbors with color "color", set its color
                colors[node.index] = color;
                nodeColored = true;
            }
            
            // if any node was colored, "color" is the new max color
            // otherwise, all nodes are colored and the last color was the max color needed
            if (nodeColored)
                maxColor = color;
            else break;
        }
        
        runner.upperBound(maxColor);
    }
}