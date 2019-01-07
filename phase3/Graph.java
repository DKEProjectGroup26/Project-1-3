package phase3;

import java.util.ArrayList;

public class Graph {
    final ArrayList<Node> nodes;
    final int numberOfEdges;
    
    public Graph(int nNodes, ArrayList<int[]> edges) {
        nodes = new ArrayList<Node>();
        numberOfEdges = edges.size();
        
        // make empty node objects
        for (int i = 0; i < nNodes; i++) nodes.add(new Node(i));
        
        for (int[] edge : edges) {
            Node node0 = nodes.get(edge[0]);
            Node node1 = nodes.get(edge[1]);
            
            node0.neighbors.add(node1);
            node1.neighbors.add(node0);
        }
    }
    
    public boolean connected(int node0, int node1) {
        return nodes.get(node0).neighbors.contains(nodes.get(node1));
    }
}