package phase3;

import java.util.ArrayList;

// NOT FINISHED, TODO: WORK ON PRUNING
public class Exact1 implements Algorithm.Connected {
    public void run(Runner runner, Graph graph) {
        if (1 == 1)
        throw new Error("Exact1 is not implemented, don't use it");
        // replace graph with a deep clone
        // i.e. new Node objects
        graph = new Graph(graph);
        
        // to avoid giving a tree the chromatic number 1
        int minColors = graph.nodes.size() > 1 ? 2 : 1;
        
        System.out.println("old nodes: " + graph.nodes.size());
        
        // changes graph object in place
        removeHangingNodes(graph);
        
        System.out.println("new nodes: " + graph.nodes.size());
    }
    
    private static void removeHangingNodes(Graph graph) {
        ArrayList<Node> nodes = new ArrayList<Node>(graph.nodes);
        
        while (true) {
            ArrayList<Node> hanging = new ArrayList<Node>();
            
            for (Node node : nodes)
                if (node.neighbors.size() == 1) hanging.add(node);
            
            if (hanging.isEmpty()) break;
            
            for (Node node : hanging) {
                // remove the node from nodes arraylist
                nodes.remove(node);
                
                // remove the node from all it's neighbors' neighbors lists
                for (Node neighbor : node.neighbors)
                    neighbor.neighbors.remove(node);
            }
        }
        
        // renumber the nodes
        for (int i = 0; i < nodes.size(); i++)
            nodes.get(i).index = i;
        
        graph.nodes = nodes;
    }
}