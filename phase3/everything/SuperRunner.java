package phase3.everything;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * The class responsible for calling algorithms on graphs and sections of graphs using
 * {@link Runner} and {@link OGRunner} objects
 */
public class SuperRunner {
    /**
     * A deep clone of original {@link Graph} object passed to the constructor by {@link phase3.Main#main}
     */
    final Graph originalGraph;
    
    /**
     * A list of {@link Graph} objects representing the sections the original graph was split into
     */
    final ArrayList<Graph> graphs;
    
    /**
     * A list of {@link Runner} objects used to keep track of lower and upper bounds and send
     * interrupt and new bound messages, all algorithms will be included in these runners
     */
    final ArrayList<Runner> runners;
    
    /**
     * The runner that operates on the original graph, this is treated in a special way, only algorithms
     * extending the {@link Algorithm.Any} interface will be passed to this runner, unless the graph only
     * consists of one section
     */
    final OGRunner originalGraphRunner;
    
    // the volatile keyword guarantees that any access to these two variables
    // will return the last known value (even if modified by another thread)
    // instead of a cached value which might not match the actual value
    // and lead to synchronization problems (actual example: upper bound 7 before upper bound 8)
    /**
     * The global lower bound of the graph, this is set to the highest lower bound found by any
     * {@link Runner} object, since the objects work in separate threads, the variable is volatile
     * to ensure that every thread has access to the latest lower bound.
     */
    public volatile int globalLowerBound = 1;
    
    /**
     * The global upper bound of the graph, this is set to the highest upper bound found by any
     * {@link Runner} object or any upper bound found by the {@link OGRunner} object stored as
     * {@link SuperRunner#originalGraphRunner}, since the objects work in separate threads,
     * the variable is volatile to ensure that every thread has access to the latest upper bound.
     */
    public volatile int globalUpperBound;
    
    /**
     * The main constructor, this is only called once by {@link phase3.Main#main} and sets up the
     * {@link SuperRunner}'s structure and splits the graph as well as creating all the
     * {@link Runner} objects and the {@link OGRunner} object all with their own graphs and
     * sections of graphs
     *
     * @param  inputGraph  the graph to be split and processed
     * @param  allAlgorithms  a list of classes to be partitioned depending on the interfaces
     *  they implement and passed on to {@link Runner} and {@link OGRunner} objects accordingly
     */
    public SuperRunner(Graph inputGraph, ArrayList<Class<? extends Algorithm>> allAlgorithms) {
        originalGraph = new Graph(inputGraph);
        graphs = Splitter.split(inputGraph);
        
        // divide algorithms into the ones taking only connected graphs and the ones taking any graph
        ArrayList<Class<? extends Algorithm>> connectedAlgorithms
            = new ArrayList<Class<? extends Algorithm>>();
        ArrayList<Class<? extends Algorithm>> anyAlgorithms
            = new ArrayList<Class<? extends Algorithm>>();
        
        for (Class<? extends Algorithm> algorithm : allAlgorithms) {
            if (Algorithm.Connected.class.isAssignableFrom(algorithm))
                connectedAlgorithms.add(algorithm);
            else if (Algorithm.Any.class.isAssignableFrom(algorithm))
                anyAlgorithms.add(algorithm);
            else throw new Error(
                "algorithm " + algorithm.getSimpleName()
                + " doesn't implement either Algorithm.Connected or Algorithm.Any"
            );
        }
        
        // any are connected too
        for (Class<? extends Algorithm> anyAlgorithm : anyAlgorithms)
            connectedAlgorithms.add(anyAlgorithm);
        
        // print out very basic lower and upper bounds just in case
        System.out.println("NEW BEST LOWER BOUND = 1");
        
        // calculate a basic upper bound
        int maxNumberOfNodes = 0;
        for (Graph graph : graphs)
            if (graph.nodes.size() > maxNumberOfNodes)
                maxNumberOfNodes = graph.nodes.size();
        globalUpperBound = maxNumberOfNodes;
        System.out.println("NEW BEST UPPER BOUND = " + globalUpperBound);
        
        // initialize runners ArrayList
        runners = new ArrayList<Runner>();
        
        if (graphs.size() == 1) {
            // if there is only one section in the graph,
            // it's connected and all algorithms can operate on it
            
            // create the runner
            originalGraphRunner = new OGRunner(this, originalGraph, allAlgorithms);
        } else {
            // otherwise, only Algorithm.Any algorithms can operate on the whole graph
            // and the other algorithms will operate on section of it
            
            // create runners with Algorithm.Connected algorithms
            for (Graph graph : graphs)
                runners.add(new Runner(this, graph, connectedAlgorithms));
            
            // create the original graph runner with Algorithm.Any algorithms only
            originalGraphRunner = new OGRunner(this, originalGraph, anyAlgorithms);
        }
        
        boundCheck();
        
        // preparations complete, start runners
        for (Runner runner : runners) runner.start();
        originalGraphRunner.start();
    }
    
    private void boundCheck() {
        if (globalLowerBound == globalUpperBound)
            chromaticNumberFound(globalLowerBound);
    }
    
    /**
     * This method is called by {@link Runner} and {@link OGRunner} objects to notify this superrunner
     * that a new lower bound has been found, sets {@link SuperRunner#globalLowerBound} and updates
     * all runners with the new lower bound
     *
     * @param  newLowerBound  the lower bound that was found by the {@link Runner} object that called
     *  this method
     */
    public void lowerBoundFound(int newLowerBound) {
        if (newLowerBound <= globalLowerBound) return;
        
        // a lower bound for any graph is a lower bound for the whole thing
        int maxLowerBound = newLowerBound;
        for (Runner runner : runners)
            if (runner.getLowerBound() > maxLowerBound)
                maxLowerBound = runner.getLowerBound();
        
        if (maxLowerBound > globalLowerBound) {
            System.out.println("NEW BEST LOWER BOUND = " + maxLowerBound);
            globalLowerBound = maxLowerBound;
            
            // set each runner's lower bound to the new global lower bound
            for (Runner runner : runners) {
                // runner.currentLowerBound = globalLowerBound;
                runner.lowerBound(globalLowerBound);
                runner.boundCheck();
            }
            
            boundCheck();
        }
    }
    
    /**
     * This method is called by {@link Runner} objects to notify this superrunner that a new upper bound
     * has been found, checks the current highest upper bound and, if it's been lowered, notifies all
     * runners of the new value
     *
     * @param  newUpperBound  the upper bound that was found by the {@link Runner} object that called
     *  this method
     */
    public void upperBoundFound(int newUpperBound) {
        if (newUpperBound >= globalUpperBound) return;
        
        // only the largest upper bound of all is the actual upper bound
        int maxUpperBound = newUpperBound;
        for (Runner runner : runners) {
            if (runner.getUpperBound() > maxUpperBound) {
                maxUpperBound = runner.getUpperBound();
                
                if (maxUpperBound >= globalUpperBound) return;
            }
        }
        
        if (maxUpperBound < globalUpperBound) {
            System.out.println("NEW BEST UPPER BOUND = " + maxUpperBound);
            globalUpperBound = maxUpperBound;
            
            boundCheck();
        }
    }
    
    /**
     * This method is called by {@link OGRunner} to notify this superrunner that it found a new upper bound
     * since {@link OGRunner} works on the whole graph, this new upper bound can be set in all {@link Runner}
     * objects as well
     *
     * @param  newOGUpperBound  the upper bound that was found by the {@link OGRunner} object that called
     *  this method
     */
    public void ogUpperBoundFound(int newOGUpperBound) {
        // this method is only called by OGRunner
        if (newOGUpperBound >= globalUpperBound) return;
        
        System.out.println("NEW BEST UPPER BOUND = " + newOGUpperBound);
        globalUpperBound = newOGUpperBound;
        imposeOGUpperBound();
        
        boundCheck();
    }
    
    private void imposeLowerBound() {
        for (Runner runner : runners)
            runner.lowerBound(globalLowerBound);
    }
    
    private void imposeOGUpperBound() {
        // this method is only called by ogUpperBoundFound
        for (Runner runner : runners)
            runner.upperBound(globalUpperBound);
    }
    
    /**
     * Called by {@link Runner} objects to notify this superrunner that they found a chromatic number for
     * their section of the graph
     *
     * @param  chromaticNumber  the chromatic number that was found
     */
    private void chromaticNumberFound(int chromaticNumber) {
        if (chromaticNumber > globalLowerBound) lowerBoundFound(chromaticNumber);
        if (chromaticNumber < globalUpperBound) upperBoundFound(chromaticNumber);
    }
    
    /**
     * Called by {@link OGRunner} object to notify this superrunner that a chromatic number
     * for the whole graph was found, prints the chromatic number line and terminates the program
     *
     * @param  chromaticNumber  the chromatic number that was found
     */
    public void ogChromaticNumberFound(int chromaticNumber) {
        // this method is only called by OGRunner
        
        System.out.println("CHROMATIC NUMBER = " + chromaticNumber);
        System.exit(0);
    }
}