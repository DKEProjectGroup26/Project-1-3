package phase3.everything;

import java.util.ArrayList;

/**
 * The internal representation of a graph, stores all vertex and edge information
 */
public class Graph {
    /**
     * An {@link ArrayList} containing {@link Node} objects, the connections are stored by the nodes
     */
    public ArrayList<Node> nodes;
    
    /**
     * The number of edges the graph has, this is useful as edges aren't stored separately
     */
    public final int numberOfEdges;
    
    /**
     * A constructor from number of nodes and an array of edges
     *
     * @param  nNodes  the number of nodes of the graph
     * @param  edges   the list of 2-element int arrays representing the edges of the graph
     */
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
        
        checkNumbering(); // TODO: remove
    }
    
    /**
     * A constructor from a list of {@link Node} objects
     *
     * @param  nodes  the nodes of the section
     */
    public Graph(ArrayList<Node> nodes) {
        // this constructor is used when splitting
        this.nodes = nodes;
        
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
        
        checkNumbering(); // TODO: remove
    }
    
    /**
     * A deep clone constructor
     *
     * @param  graph  the input graph, this object will not change after the clone is constructed
     */
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
        
        checkNumbering(); // TODO: remove
    }
    
    /**
     * Assigns the {@link everything.Node#index} attribute to each node's index
     * in the {@link Graph#nodes} list
     */
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