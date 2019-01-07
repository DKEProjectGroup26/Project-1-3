package phase3;

import java.util.ArrayList;

public class Node {
    int index;
    ArrayList<Node> neighbors;
    
    public Node(int ind) {
        index = ind;
        neighbors = new ArrayList<Node>();
    }
}