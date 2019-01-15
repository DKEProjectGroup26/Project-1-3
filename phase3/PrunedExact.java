package phase3;

import java.util.ArrayList;

// NOT TESTED
public class PrunedExact extends BasicExact {
    private int localLowerBound;
    private Runner storedRunner;
    private Graph storedGraph;
    
    public void newLowerBound(int lowerBound) {
        super.newLowerBound(lowerBound);
        
        if (lowerBound > localLowerBound) {
            // stop solving
            super.interrupt();
            
            // reprune and retry
            _run();
        }
    }
    
    // entry point for Runner
    public void run(Runner inputRunner, Graph inputGraph) {
        storedRunner = inputRunner;
        
        // make a clone of the graph to avoid messing up the original
        storedGraph = new Graph(inputGraph);
        
        // call the main run method
        _runAccessible = true;
        _run();
    }
    
    // prevents _run() from being called through newLowerBound before it's safe or multiple times
    // this is important, it prevents a bunch of null pointer exceptions
    private boolean _runAccessible = false;
    private void _run() {
        if (!_runAccessible) return;
        _runAccessible = false;
        
        localLowerBound = storedRunner.getLowerBound();
        
        // prune all the nodes with fewer connections than the current lower bound
        boolean anyPruned = prune(storedGraph, localLowerBound - 1);
        
        // if none were pruned, wait until a better lower bound is found
        if (!anyPruned) {
            _runAccessible = true;
            return;
        } else if (storedGraph.nodes.isEmpty()) {
            // TODO: prove this
            storedRunner.chromaticNumberFound(localLowerBound);
            return;
        }
        
        // fix node numbering
        storedGraph.renumber();
        
        // for safety
        storedGraph.checkNumbering();
        
        // pass pruned graph to BasicExact's run method
        super.run(storedRunner, storedGraph);
        
        // after done or interrupted, make _run accessible
        _runAccessible = true;
    }
    
    private static boolean prune(Graph graph, int maxConnections) {
        boolean anyPruned = false;
        
        while (true) {
            // the nodes to be pruned
            ArrayList<Node> toPrune = new ArrayList<Node>();
            
            for (Node node : graph.nodes)
                if (node.neighbors.size() <= maxConnections)
                    toPrune.add(node);
            
            // ran out of nodes to prune
            if (toPrune.isEmpty()) break;
            
            anyPruned = true;
            
            for (Node node : toPrune)
                pruneNode(graph, node);
        }
        
        return anyPruned;
    }
    
    private static void pruneNode(Graph graph, Node node) {
        graph.nodes.remove(node);
        for (Node neighbor : node.neighbors)
            neighbor.neighbors.remove(node);
    }
}