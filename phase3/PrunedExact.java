package phase3;

import java.util.ArrayList;

public class PrunedExact implements Algorithm.Connected {
    public void run(Runner runner, Graph graph) {
        int currentLowerBound = runner.currentLowerBound;
        
        // make a clone of the graph so that it doesn't mess up the original
        graph = new Graph(graph);
        
        int originalNumberOfNodes = graph.nodes.size();
        
        // prune all the nodes with fewer connections than the current lower bound
        prune(graph, currentLowerBound - 1);
        
        // fix node numbering
        graph.renumber();
        
        // for safety
        graph.checkNumbering();
        
        int newNumberOfNodes = graph.nodes.size();
        System.out.println(originalNumberOfNodes + " -> " + newNumberOfNodes);
    }
    
    private static void prune(Graph graph, int maxConnections) {        
        while (true) {
            // the nodes to be pruned
            ArrayList<Node> toPrune = new ArrayList<Node>();
            
            for (Node node : graph.nodes)
                if (node.neighbors.size() <= maxConnections)
                    toPrune.add(node);
            
            // ran out of nodes to prune
            if (toPrune.isEmpty()) break;
            
            for (Node node : toPrune)
                pruneNode(graph, node);
        }
    }
    
    private static void pruneNode(Graph graph, Node node) {
        graph.nodes.remove(node);
        for (Node neighbor : node.neighbors)
            neighbor.neighbors.remove(node);
    }
}