package phase3;

import java.util.ArrayList;

public class Runner {
    // protected to make it accessible to extending class OGRunner
    protected final SuperRunner parent;
    private final Graph graph;
    private final ArrayList<Algorithm> algorithms;
    
    // volatile for the same reason as in SuperRunner
    public volatile int currentLowerBound = 1;
    public volatile int currentUpperBound;
    
    public Runner(SuperRunner parent, Graph graph, ArrayList<Algorithm> algorithms) {
        this.parent = parent;
        this.graph = graph;
        this.algorithms = algorithms;
        currentUpperBound = graph.nodes.size();
    }
    
    public void start() {
        // threaded version
        Runner self = this;
        for (Algorithm algorithm : algorithms) {
            // improve this and implement timeout
            new Thread() {
                public void run() {
                    algorithm.run(self, graph);
                }
            }.start();
        }
        
        // unthreaded version
        // for (Algorithm algorithm : algorithms) {
        //     algorithm.run(this, graph);
        // }
    }
    
    public void chromaticNumberFound(int chromaticNumber) {
        // currentLowerBound = chromaticNumber;
        // currentUpperBound = chromaticNumber;
        //
        // if (chromaticNumber > currentLowerBound)
        //     parent.lowerBoundFound(chromaticNumber);
        //
        // if (chromaticNumber < currentUpperBound)
        //     parent.upperBoundFound(chromaticNumber);
        
        lowerBound(chromaticNumber);
        upperBound(chromaticNumber);
        
        // stop all threads
    }
    
    public void boundCheck() {
        if (currentLowerBound == currentUpperBound)
            chromaticNumberFound(currentLowerBound);
    }
    
    public void lowerBound(int newLowerBound) {
        if (newLowerBound > currentLowerBound) {
            currentLowerBound = newLowerBound;
            parent.lowerBoundFound(newLowerBound);
        }
    }
        
    public void upperBound(int newUpperBound) {
        if (newUpperBound < currentUpperBound) {
            currentUpperBound = newUpperBound;
            parent.upperBoundFound(newUpperBound);
        }
    }
}