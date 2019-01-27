package phase3.everything;

import java.util.ArrayList;

/**
 * The class representing a node (or vertex) in a graph, edge information is stored
 * in this class as a list of other {@link Node} objects stored as {@link Node#neighbors}
 */
public class Node implements Comparable {
    /**
     * The index of the node inside its parent graph's {@link Graph#nodes} list
     */
    protected int index;
    
    /**
     * A list of the other {@link Node} objects in this node's parent graph which share an edge with this node
     */
    protected ArrayList<Node> neighbors;
    
    /**
     * @return {@link Node#index}
     */
    public int getIndex() {
        return index;
    }
    
    /**
     * @return {@link Node#neighbors}
     */
    public ArrayList<Node> getNeighbors() {
        return neighbors;
    }
    
    /**
     * Constructor
     *
     * @param  index  the index to be assigned to this node
     */
    public Node(int index) {
        this.index = index;
        neighbors = new ArrayList<Node>();
    }
    
    /**
     * {@link Node}'s implementation of the {@link Comparable} interface, compares nodes by index
     *
     * @param  other  another object of type {@link Node} which this node will be compared to
     * @return -1, 0, or 1
     */
    public int compareTo(Object other) {
        // -1 if my index < other index
        // 0 if my index == other index
        // 1 if my index > other index
        if (other instanceof Node) {
            int otherIndex = ((Node) other).index;
            return index < otherIndex ? -1 : index == otherIndex ? 0 : 1;
        } else throw new Error("tried to compare Node to " + other.getClass().getSimpleName());
    }
}