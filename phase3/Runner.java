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
    }
    
    public void start() {
        Algorithm algorithms[] = {
            // new BrooksTheorem(),
            // new Tree(),
            // new Clique(),
            // new StephansAlgorithm()
            new AdjacencyMatrix()
        };
        
        Runner self = this;
        
        for (Algorithm algorithm : algorithms) {
            // improve this and implement timeout
            new Thread() {
                public void run() {
                    algorithm.run(self, graph);
                }
            }.start();
        }
    }
    
    public void chromaticNumberFound(int chromaticNumber) {
        currentLowerBound = chromaticNumber;
        currentUpperBound = chromaticNumber;
        
        if (chromaticNumber > currentLowerBound)
            parent.lowerBoundFound(chromaticNumber);
        
        if (chromaticNumber < currentUpperBound)
            parent.upperBoundFound(chromaticNumber);
        
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