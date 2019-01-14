package phase3;

import java.util.ArrayList;

public class OGRunner extends Runner {
    // OGRunner works with the original, intact graph and, therefore
    // doesn't need approval to set a new upper bound
    
    // forward constructor
    public OGRunner(SuperRunner parent, Graph graph, ArrayList<Class<? extends Algorithm>> algorithms) {
        super(parent, graph, algorithms);
    }
    
    // the upperBound method imposes its upper bound on all other runners
    // the method is the same except it calls parent.ogUpperBoundFound instead of parent.upperBoundFound
    @Override
    public void upperBound(int newUpperBound) {
        if (newUpperBound < currentUpperBound) {
            currentUpperBound = newUpperBound;
            parent.ogUpperBoundFound(newUpperBound);
            
            // send new upper bound message to all accepting algorithms
            for (Algorithm algorithm : algorithms)
                if (algorithm instanceof Interruptable.WithUpperBoundUpdates)
                    ((Interruptable.WithUpperBoundUpdates) algorithm).newUpperBound(newUpperBound);
            
            boundCheck();
        }
    }
    
    // the chromaticNumberFound method stops the whole program
    // if there's a chromatic number for the whole graph, that's it
    @Override
    public void chromaticNumberFound(int chromaticNumber) {
        parent.ogChromaticNumberFound(chromaticNumber);
    }
}