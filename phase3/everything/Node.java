package phase3.everything;

import java.util.ArrayList;

/**
 * The class representing a node (or vertex) in a graph, edge information is stored
 * in this class as a list of other {@link Node} objects stored as {@link Node#neighbors}
 */
public class Node implements Comparable {
    /**
     * The index of the node inside its parent graph, this needs to be kept up-to-date, eliminates
     * the time that would be needed to look up a node in a large graph just to find its index.
     * Protected so that a {@link Graph} object can renumber its nodes without creating copies
     * but algorithms can't inadvertently mess up node indices (alorithms can call {@link Graph#renumber})
     */
    protected int index;
    
    /**
     * A list of the other {@link Node} objects in this node's parent graph which share an edge with this node
     */
    protected ArrayList<Node> neighbors;
    
    /**
     * @return the node's {@link Node#index}
     */
    public int getIndex() {
        return index;
    }
    
    /**
     * @return the node's {@link Node#neighbors}
     */
    public ArrayList<Node> getNeighbors() {
        return neighbors;
    }
    
    /**
     * The main constructor
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
     * @return -1, 0, or 1 depending on whether the node's index is less than, equal to, or greater than
     *  the other node's index, respectively
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