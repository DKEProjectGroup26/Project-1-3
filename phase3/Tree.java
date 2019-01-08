package phase3;

import java.util.ArrayList;

public class Tree implements Algorithm {
    public void run(Runner runner, Graph graph) {
        // only works on a split graph
        boolean result = subTree(graph.nodes.get(0), null, new ArrayList<Node>());

        // if the graph is a tree, set upper bound to 2
        if (result) runner.upperBound(2);
    }
    
    private static boolean subTree(Node node, Node lastNode, ArrayList<Node> seen) {
        ArrayList<Node> newSeen = new ArrayList<Node>(seen);
        newSeen.add(node);
        
        for (Node neighbor : node.neighbors) {
            if (neighbor == lastNode) continue;
            if (seen.contains(neighbor)) return false;
            
            if (!subTree(neighbor, node, newSeen)) return false;
        }
        return true;
    }
}