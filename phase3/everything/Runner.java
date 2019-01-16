package phase3.everything;

import java.util.ArrayList;

public class Runner {
    // protected to make it accessible to extending class OGRunner
    protected final SuperRunner parent;
    private Graph graph;
    //private 
    final ArrayList<Algorithm> algorithms;
    
    // volatile for the same reason as in SuperRunner
    protected volatile int currentLowerBound = 1;
    protected volatile int currentUpperBound;
    
    public int getLowerBound() {
        return currentLowerBound;
    }
    
    public int getUpperBound() {
        return currentUpperBound;
    }
    
    public Runner (SuperRunner parent, Graph graph, ArrayList<Class<? extends Algorithm>> algorithmClasses) {
        this.parent = parent;
        this.graph = graph;
        algorithms = new ArrayList<Algorithm>();
        
        for (Class<? extends Algorithm> algorithmClass : algorithmClasses) {
            try {
                algorithms.add(algorithmClass.getConstructor().newInstance());
            } catch (InstantiationException e) {
                throw new Error("class " + algorithmClass.getSimpleName() + " can't be instantiated");
            } catch (Exception e) {
                // for all other exceptions
                throw new RuntimeException(e);
            }
        }
        
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
    
    public void interruptAll() {
        // interrupt all interruptable algorithms
        for (Algorithm algorithm : algorithms)
            if (algorithm instanceof Interruptable)
                ((Interruptable) algorithm).interrupt();
    }
    
    public void chromaticNumberFound(int chromaticNumber) {
        lowerBound(chromaticNumber);
        upperBound(chromaticNumber);
        
        interruptAll();
    }
    
    public void boundCheck() {
        if (currentLowerBound == currentUpperBound)
            chromaticNumberFound(currentLowerBound);
    }
    
    public void lowerBound(int newLowerBound) {
        if (newLowerBound > currentLowerBound) {
            currentLowerBound = newLowerBound;
            parent.lowerBoundFound(newLowerBound);
            
            // send new lower bound message to all accepting algorithms
            for (Algorithm algorithm : algorithms)
                if (algorithm instanceof Interruptable.WithLowerBoundUpdates)
                    ((Interruptable.WithLowerBoundUpdates) algorithm).newLowerBound(newLowerBound);
            
            boundCheck();
        }
    }
        
    public void upperBound(int newUpperBound) {
        if (newUpperBound < currentUpperBound) {
            currentUpperBound = newUpperBound;
            parent.upperBoundFound(newUpperBound);
            
            // send new upper bound message to all accepting algorithms
            for (Algorithm algorithm : algorithms)
                if (algorithm instanceof Interruptable.WithUpperBoundUpdates)
                    ((Interruptable.WithUpperBoundUpdates) algorithm).newUpperBound(newUpperBound);
            
            boundCheck();
        }
    }
}