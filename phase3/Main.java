package phase3;

import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {
        // this is where the program starts
        
        Graph graph = Reader.readGraph("phase3/graphs/block3_2018_graph05.txt");
        
        // split graph into constituent graphs
        ArrayList<Graph> graphs = split(graph);
        
        // prints out how many separate graphs there are
        // System.out.println(graphs.size());
        
        SuperRunner runner = new SuperRunner(graphs);
    }
    
    private static ArrayList<Graph> split(Graph graph) {
        ArrayList<Graph> sections = new ArrayList<Graph>();
        
        boolean firstIteration = true;
        
        while (graph.nodes.size() > 0) {
            ArrayList<Node> section = splitOff(graph);
            
            if (firstIteration) {
                firstIteration = false;
                
                if (section.size() == graph.nodes.size()) {
                    // the first section is the whole graph
                    
                    sections.add(graph);
                    return sections;
                }
            }
            
            // remove removed nodes from original graph
            for (Node node : section) graph.nodes.remove(node);
            
            sections.add(new Graph(section));
        }
        
        return sections;
    }
    
    // this is about the same speed as depth-first (they both work correctly)
    // breadth-first
    // private static ArrayList<Node> splitOff(Graph graph) {
    //     ArrayList<Node> nodes = new ArrayList<Node>();
    //     ArrayList<Node> holding = new ArrayList<Node>();
    //
    //     nodes.add(graph.nodes.get(0));
    //     while (true) {
    //         // add all nodes's nodes' neighbors to holding
    //         for (Node node : nodes)
    //             for (Node neighbor : node.neighbors)
    //                 if (!nodes.contains(neighbor) && !holding.contains(neighbor))
    //                     holding.add(neighbor);
    //
    //         if (holding.size() == 0) {
    //             System.out.println("returning");
    //             return nodes;
    //         }
    //         System.out.println("holding full: " + holding.size());
    //
    //         for (Node held : holding)
    //             nodes.add(held);
    //
    //         System.out.println("added");
    //
    //         System.out.println(nodes.size() + " nodes");
    //
    //         holding.clear();
    //         // holding = new ArrayList<Node>(); // might be faster
    //     }
    // }
    
    // depth-first
    private static ArrayList<Node> splitOff(Graph graph) {
        ArrayList<Node> nodes = new ArrayList<Node>();
        Node node = graph.nodes.get(0);
        nodes.add(node);

        // changes the nodes ArrayList
        splitOff(node, nodes);

        return nodes;
    }

    private static void splitOff(Node node, ArrayList<Node> nodes) {
        for (Node neighbor : node.neighbors) {
            if (nodes.contains(neighbor)) continue;
            nodes.add(neighbor);
            splitOff(neighbor, nodes);
        }
    }
}

class SuperRunner {
    final ArrayList<Graph> graphs;
    final ArrayList<Runner> runners;
    
    public int globalLowerBound = 1;
    public int globalUpperBound;
    
    public SuperRunner(ArrayList<Graph> graphs) {
        this.graphs = graphs;
        
        // just in case
        System.out.println("NEW BEST LOWER BOUND = 1");
        
        runners = new ArrayList<Runner>();
        int maxUpperBound = 0;
        for (Graph graph : graphs) {
            // calculates initial upper bound when initialized
            Runner runner = new Runner(this, graph);
            
            if (runner.currentUpperBound > maxUpperBound)
                maxUpperBound = runner.currentUpperBound;
            
            runners.add(runner);
        }
        
        globalUpperBound = maxUpperBound;
        
        // just in case
        System.out.println("NEW BEST UPPER BOUND = " + globalUpperBound);
        
        boundCheck();
    }
    
    private void boundCheck() {
        if (globalUpperBound == globalLowerBound) {
            System.out.println("CHROMATIC NUMBER = " + globalLowerBound);
            System.exit(0);
        }
    }
    
    public void lowerBoundFound(int newLowerBound) {
        if (newLowerBound < globalLowerBound) return;
        
        // a lower bound for any graph is a lower bound for the whole thing
        int maxLowerBound = newLowerBound;
        for (Runner runner : runners)
            if (runner.currentLowerBound > maxLowerBound)
                maxLowerBound = runner.currentLowerBound;
        
        if (maxLowerBound > globalLowerBound) {
            System.out.println("NEW BEST LOWER BOUND = " + maxLowerBound);
            globalLowerBound = maxLowerBound;
            
            boundCheck();
        }
    }
    
    public void upperBoundFound(int newUpperBound) {
        if (newUpperBound > globalUpperBound) return;
        
        // only the largest upper bound of all is the actual upper bound
        int maxUpperBound = newUpperBound;
        for (Runner runner : runners)
            if (runner.currentUpperBound > maxUpperBound)
                maxUpperBound = runner.currentUpperBound;
        
        if (maxUpperBound < globalUpperBound) {
            System.out.println("NEW BEST UPPER BOUND = " + maxUpperBound);
            globalUpperBound = maxUpperBound;
            
            boundCheck();
        }
    }
    
    public void imposeLowerBound() {
        for (Runner runner : runners) {
            if (runner.currentLowerBound < globalLowerBound) {
                runner.currentLowerBound = globalLowerBound;
                runner.boundCheck();
            }
        }
    }
}