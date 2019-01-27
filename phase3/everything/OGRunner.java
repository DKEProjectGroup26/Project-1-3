package phase3.everything;

import java.util.ArrayList;

/**
 * Essentially the same functionality as {@link Runner} but this is used on whole graphs and is
 * treated differently by the parent {@link SuperRunner}
 */
public class OGRunner extends Runner {
    // OGRunner works with the original, intact graph and, therefore
    // can enforce its upper bound on all other runners
    
    /**
     * Calls the superclass constructor
     *
     * @param  parent  same as in {@link Runner}
     * @param  graph  same as in {@link Runner}
     * @param  algorithms  same as in {@link Runner}
     */
    // forward constructor
    public OGRunner(SuperRunner parent, Graph graph, ArrayList<Class<? extends Algorithm>> algorithms) {
        super(parent, graph, algorithms);
    }
    
    /**
     * The same as {@link Runner#upperBound} but calls {@link SuperRunner#ogUpperBoundFound}
     * instead of {@link SuperRunner#upperBoundFound} which imposes the new upper bound on all
     * other runners
     *
     * @param  newUpperBound  the new upper bound
     */
    // the upperBound method imposes its upper bound on all other runners
    // the method is the same except it calls parent.ogUpperBoundFound instead of parent.upperBoundFound
    @Override
    public void upperBound(int newUpperBound) {
        if (newUpperBound < currentUpperBound) {
            currentUpperBound = newUpperBound;
            parent.ogUpperBoundFound(newUpperBound);
            
            boundCheck();
        }
    }
    
    /**
     * Forwards a call to {@link Runner#chromaticNumberFound} to the
     * {@link SuperRunner#ogChromaticNumberFound} method of {@link OGRunner#parent}
     */
    // the chromaticNumberFound method stops the whole program
    // if there's a chromatic number for the whole graph, that's it
    @Override
    public void chromaticNumberFound(int chromaticNumber) {
        parent.ogChromaticNumberFound(chromaticNumber);
    }
}