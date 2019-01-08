package phase3;

import java.util.ArrayList;

public class Runner {
    private final SuperRunner parent;
    private final Graph graph;
    
    public int currentLowerBound = 1;
    public int currentUpperBound;
    
    public Runner(SuperRunner parent, Graph graph) {
        this.parent = parent;
        this.graph = graph;
        currentUpperBound = graph.nodes.size();
        
        run();
    }
    
    private void run() {
        // call all other classes
        
        Algorithm algorithms[] = {
            new BrooksTheorem(),
            new Tree(),
            new Clique()
        };
        
        for (Algorithm algorithm : algorithms) {
            // make a thread
            algorithm.run(this, graph);
        }
    }
    
    public void chromaticNumberFound(int chromaticNumber) {
        if (chromaticNumber > currentLowerBound)
            parent.lowerBoundFound(chromaticNumber);
        
        if (chromaticNumber < currentUpperBound)
            parent.upperBoundFound(chromaticNumber);
        
        // stop all threads
    }
    
    private void boundCheck() {
        if (currentLowerBound == currentUpperBound)
            chromaticNumberFound(currentLowerBound);
    }
    
    public void lowerBound(int newLowerBound) {
        if (newLowerBound > currentLowerBound)
            parent.lowerBoundFound(newLowerBound);
    }
        
    public void upperBound(int newUpperBound) {
        if (newUpperBound < currentUpperBound)
            parent.upperBoundFound(newUpperBound);
    }
}