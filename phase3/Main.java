package phase3;

import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {
        // this is where the program starts
        
        Graph graph = Reader.readGraph("phase3/graphs/block3_2018_graph09.txt");
        
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
    
    private static ArrayList<Node> splitOff(Graph graph) {
        ArrayList<Node> nodes = new ArrayList<Node>();
        Node firstNode = graph.nodes.get(0);
        graph.nodes.remove(firstNode);
        nodes.add(firstNode);
        
        ArrayList<Node> holding = new ArrayList<Node>();
        while (true) {
            for (Node node : nodes) {
                for (Node neighbor : node.neighbors) {
                    // if remove returns true, neighbor was still in the graph
                    if (graph.nodes.remove(neighbor)) {
                        holding.add(neighbor);
                        
                        if (graph.nodes.isEmpty()) {
                            // no nodes left, early finish
                            for (Node held : holding) nodes.add(held);
                            return nodes;
                        }
                    }
                }
            }
            
            if (holding.isEmpty()) return nodes;
            
            for (Node held : holding) nodes.add(held);

            // if no nodes left, no point in doing another loop
            if (graph.nodes.isEmpty()) return nodes;
            
            holding.clear();
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
        
        // just in case nothing better is found
        System.out.println("NEW BEST LOWER BOUND = 1");
        
        runners = new ArrayList<Runner>();
        int maxUpperBound = 0;
        for (Graph graph : graphs) {
            if (graph.nodes.size() > maxUpperBound)
                maxUpperBound = graph.nodes.size();
            
            Runner runner = new Runner(this, graph);
            runners.add(runner);
        }
        
        globalUpperBound = maxUpperBound;
        
        // just in case nothing better is found
        System.out.println("NEW BEST UPPER BOUND = " + globalUpperBound);
        
        boundCheck();
        
        // preparations complete, start runners
        for (Runner runner : runners) runner.start();
    }
    
    private void boundCheck() {
        if (globalUpperBound == globalLowerBound) {
            System.out.println("CHROMATIC NUMBER = " + globalLowerBound);
            System.exit(0);
        }
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
    
    public void imposeLowerBound() {
        for (Runner runner : runners) {
            if (runner.currentLowerBound < globalLowerBound) {
                runner.currentLowerBound = globalLowerBound;
                runner.boundCheck();
            }
        }
    }
}