package phase3.everything;

import java.util.ArrayList;

public class Graph {
    public ArrayList<Node> nodes;
    public final int numberOfEdges;
    
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
        
        checkNumbering();
    }
    
    public Graph(ArrayList<Node> ns) {
        // this constructor is used when splitting
        nodes = ns;
        
        // reset indices
        // for (int i = 0; i < nodes.size(); i++)
            // nodes.get(i).index = i;
        renumber();
        
        int nEdges = 0;
        
        for (int i = 0; i < nodes.size(); i++) {
            for (Node neighbor : nodes.get(i).neighbors)
                // if neigbor.index < i, it's already been counted
                if (neighbor.index > i) nEdges++;
        }
        
        numberOfEdges = nEdges;
        
        checkNumbering();
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
        
        checkNumbering();
    }
    
    public void renumber() {
        // fix the numbering of the nodes so that node 0 has index 0 and so on
        for (int i = 0; i < nodes.size(); i++)
            nodes.get(i).index = i;
    }
    
    // this is only used for checking safety
    // throws an error if the nodes are not in the correct order
    // this is critical for many algorithms to work so only remove this after
    // thorough testing of final code
    // TODO: thoroughly test the final code and remove this
    public void checkNumbering() {
        for (int i = 0; i < nodes.size(); i++)
            if (nodes.get(i).index != i)
                throw new Error("node index mismatch (" + nodes.get(i).index + " at index " + i + ")");
    }
}