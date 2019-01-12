package phase3;

import java.util.ArrayList;

public class OGRunner extends Runner {
    // OGRunner works with the original, intact graph and, therefore
    // doesn't need approval to set a new upper bound
    
    // forward constructor
    public OGRunner(SuperRunner parent, Graph graph, ArrayList<Algorithm> algorithms) {
        super(parent, graph, algorithms);
    }
    
    // the only change in functionality is the upperBound method
    // which, in this case, imposes its upper bound on all other runners
    @Override
    public void upperBound(int newUpperBound) {
        if (newUpperBound < currentUpperBound) {
            currentUpperBound = newUpperBound;
            parent.ogUpperBoundFound(newUpperBound);
        }
    }
}