package phase3;

import java.util.ArrayList;
import java.util.Arrays;

public class SuperRunner {
    final Graph originalGraph;
    final ArrayList<Graph> graphs;
    final ArrayList<Runner> runners;
    final OGRunner originalGraphRunner;
    
    // the volatile keyword guarantees that any access to these two variables
    // will return the last known value (even if modified by another thread)
    // instead of a cached value which might not match the actual value
    // and lead to synchronization problems (actual example: upper bound 7 before upper bound 8)
    public volatile int globalLowerBound = 1;
    public volatile int globalUpperBound;
    
    public SuperRunner(Graph inputGraph, ArrayList<Class<? extends Algorithm>> allAlgorithms) {
        originalGraph = new Graph(inputGraph);
        graphs = Splitter.split(inputGraph);
        
        // this is just for safety, if a graph's nodes are not in order, it will throw an error
        originalGraph.checkNumbering();
        for (Graph graph : graphs) graph.checkNumbering();
        
        // divide algorithms into the ones taking only connected graphs and the ones taking any graph
        ArrayList<Class<? extends Algorithm>> connectedAlgorithms
            = new ArrayList<Class<? extends Algorithm>>();
        ArrayList<Class<? extends Algorithm>> anyAlgorithms
            = new ArrayList<Class<? extends Algorithm>>();
        
        for (Class<? extends Algorithm> algorithm : allAlgorithms) {
            for (Class implemented : algorithm.getInterfaces()) {
                // if interface extends Algorithm
                if (implemented.getInterfaces()[0] == Algorithm.class) {
                    if (implemented == Algorithm.Connected.class)
                        connectedAlgorithms.add(algorithm);
                    else if (implemented == Algorithm.Any.class)
                        anyAlgorithms.add(algorithm);
                    else throw new Error("invalid algorithm interface: " + algorithm.getSimpleName());
                    
                    break;
                }
            }
        }
        
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
    
    public void lowerBoundFound(int newLowerBound) {
        if (newLowerBound <= globalLowerBound) return;
        
        // a lower bound for any graph is a lower bound for the whole thing
        int maxLowerBound = newLowerBound;
        for (Runner runner : runners)
            if (runner.currentLowerBound > maxLowerBound)
                maxLowerBound = runner.currentLowerBound;
        
        if (maxLowerBound > globalLowerBound) {
            System.out.println("NEW BEST LOWER BOUND = " + maxLowerBound);
            globalLowerBound = maxLowerBound;
            
            // set each runner's lower bound to the new global lower bound
            for (Runner runner : runners) {
                runner.currentLowerBound = globalLowerBound;
                runner.boundCheck();
            }
            
            boundCheck();
        }
    }
    
    public void upperBoundFound(int newUpperBound) {
        if (newUpperBound >= globalUpperBound) return;
        
        // only the largest upper bound of all is the actual upper bound
        int maxUpperBound = newUpperBound;
        for (Runner runner : runners) {
            if (runner.currentUpperBound > maxUpperBound) {
                maxUpperBound = runner.currentUpperBound;
                
                if (maxUpperBound >= globalUpperBound) return;
            }
        }
        
        if (maxUpperBound < globalUpperBound) {
            System.out.println("NEW BEST UPPER BOUND = " + maxUpperBound);
            globalUpperBound = maxUpperBound;
            
            boundCheck();
        }
    }
    
    public void ogUpperBoundFound(int newOGUpperBound) {
        // this method is only called by OGRunner
        if (newOGUpperBound >= globalUpperBound) return;
        
        System.out.println("NEW BEST UPPER BOUND = " + newOGUpperBound);
        globalUpperBound = newOGUpperBound;
        imposeOGUpperBound();
    }
    
    public void imposeLowerBound() {
        for (Runner runner : runners) {
            if (runner.currentLowerBound < globalLowerBound) {
                runner.currentLowerBound = globalLowerBound;
                runner.boundCheck();
            }
        }
    }
    
    public void imposeOGUpperBound() {
        // this method is only called by ogUpperBoundFound
        for (Runner runner : runners) {
            if (runner.currentUpperBound > globalUpperBound) {
                runner.currentUpperBound = globalUpperBound;
                runner.boundCheck();
            }
        }
    }
    
    private void chromaticNumberFound(int chromaticNumber) {
        // forward method
        ogChromaticNumberFound(chromaticNumber);
    }
    
    public void ogChromaticNumberFound(int chromaticNumber) {
        // this method is only called by OGRunner
        
        System.out.println("CHROMATIC NUMBER = " + chromaticNumber);
        System.exit(0);
    }
}