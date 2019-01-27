package phase3.everything;

import java.util.ArrayList;

/**
 * The class that is responsible for running all appropriate algorithms on a section of a graph
 */
public class Runner {
    /**
     * The {@link SuperRunner} object responsible for this runner, this is where lower/upper bound
     * and chromatic number messages are sent
     */
    // protected to make it accessible to extending class OGRunner
    protected final SuperRunner parent;
    
    private final Graph graph;
    
    /**
     * The {@link Algorithm} objects operating on the graph, instantiated from the classes passed
     * to the runner at initialization
     */
    protected final ArrayList<Algorithm> algorithms;
    
    // volatile for the same reason as in SuperRunner
    /**
     * The current highest lower bound for the graph stored in {@link Runner#graph} bound found by any
     * algorithm in {@link Runner#algorithms}
     */
    protected volatile int currentLowerBound = 1;
    
    /**
     * The current lowest upper bound for the graph stored in {@link Runner#graph} bound found by any
     * algorithm in {@link Runner#algorithms}
     */
    protected volatile int currentUpperBound;
    
    /**
     * Getter for {@link Runner#currentLowerBound}
     *
     * @return {@link Runner#currentLowerBound}
     */
    public int getLowerBound() {
        return currentLowerBound;
    }
    
    /**
     * Getter for {@link Runner#currentUpperBound}
     *
     * @return {@link Runner#currentUpperBound}
     */
    public int getUpperBound() {
        return currentUpperBound;
    }
    
    /**
     * Called by {@link SuperRunner}
     *
     * @param  parent  the {@link SuperRunner} object that instantiated this runner, stored in
     *  {@link Runner#parent}
     * @param  graph  the graph to be processed, stored in {@link Runner#graph}
     * @param  algorithmClasses  the classes implementing {@link Algorithm} that will operate on the
     *  given graph, these are instantiated in the constructor and stored in {@link Runner#algorithms}
     */
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
    
    /**
     * Called by {@link SuperRunner} to start calculating, creates a thread (using {@link Thread}) for
     * each algorithms and calls the algorithm's {@link Algorithm#run} method
     */
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
    
    /**
     * Calls the {@link Interruptible#interrupt} method of all algorithms in {@link Runner#algorithms}
     * that implement the {@link Interruptible} interface
     */
    public void interruptAll() {
        // interrupt all interruptible algorithms
        for (Algorithm algorithm : algorithms)
            if (algorithm instanceof Interruptible)
                ((Interruptible) algorithm).interrupt();
    }
    
    /**
     * Called by algorithms when a chromatic number has been found
     *
     * @param  chromaticNumber  the chromatic number that was found
     */
    public void chromaticNumberFound(int chromaticNumber) {
        lowerBound(chromaticNumber);
        upperBound(chromaticNumber);
        
        interruptAll();
    }
    
    /**
     * If {@link Runner#currentLowerBound} and {@link Runner#currentUpperBound} are equal,
     * calls {@link Runner#chromaticNumberFound}
     */
    public void boundCheck() {
        if (currentLowerBound == currentUpperBound)
            chromaticNumberFound(currentLowerBound);
    }
    
    /**
     * Called by algorithms when a new lower bound has been found
     *
     * @param  newLowerBound  the new lower bound that has been found
     */
    public void lowerBound(int newLowerBound) {
        if (newLowerBound > currentLowerBound) {
            currentLowerBound = newLowerBound;
            parent.lowerBoundFound(newLowerBound);
            
            // send new lower bound message to all accepting algorithms
            for (Algorithm algorithm : algorithms)
                if (algorithm instanceof Interruptible.WithLowerBoundUpdates)
                    ((Interruptible.WithLowerBoundUpdates) algorithm).newLowerBound(newLowerBound);
            
            boundCheck();
        }
    }
    
    /**
     * Called by algorithms when a new upper bound has been found
     *
     * @param  newUpperBound  the new upper bound that has been found
     */
    public void upperBound(int newUpperBound) {
        if (newUpperBound < currentUpperBound) {
            currentUpperBound = newUpperBound;
            parent.upperBoundFound(newUpperBound);
            
            boundCheck();
        }
    }
}