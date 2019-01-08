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
    
    public Graph(ArrayList<Node> ns) {
        // this constructor is used when splitting
        nodes = ns;
        
        ArrayList<int[]> edges = new ArrayList<int[]>();
        
        for (Node node : nodes) {
            midLoop: for (Node neighbor : node.neighbors) {
                int node0 = node.index;
                int node1 = neighbor.index;
                
                int[] edge = {Math.min(node0, node1), Math.max(node0, node1)};
                
                // check
                for (int[] check : edges)
                    if (check[0] == edge[0] && check[1] == edge[1]) continue midLoop;
                
                edges.add(edge);
            }
        }
        
        numberOfEdges = edges.size();
    }
}