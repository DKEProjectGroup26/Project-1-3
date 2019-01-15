package phase3;

import java.util.ArrayList;

public class Node implements Comparable {
    int index;
    ArrayList<Node> neighbors;
    
    public Node(int ind) {
        index = ind;
        neighbors = new ArrayList<Node>();
    }
    
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