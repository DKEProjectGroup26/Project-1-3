package phase3;

import java.util.ArrayList;

public class Graph {
    ArrayList<Node> nodes;
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
        
        // reset indices
        for (int i = 0; i < nodes.size(); i++)
            nodes.get(i).index = i;
        
        int nEdges = 0;
        // stores the nodes for which all edges have been counted
        
        for (int i = 0; i < nodes.size(); i++) {
            for (Node neighbor : nodes.get(i).neighbors)
                // if neigbor.index < i, it's already been counted
                if (neighbor.index > i) nEdges++;
        }
        
        numberOfEdges = nEdges;
    }
    
    public Graph(Graph graph) {
        // deep clone constructor
        ArrayList<Node> newNodes = new ArrayList<Node>();
        
        for (Node node : graph.nodes)
            newNodes.add(new Node(node.index));
        
        // recreate neighbors
        for (Node node : graph.nodes) {
            Node newNode = newNodes.get(node.index);
            
            for (Node neighbor : node.neighbors)
                newNode.neighbors.add(newNodes.get(neighbor.index));
        }
        
        nodes = newNodes;
        numberOfEdges = graph.numberOfEdges;
    }
}