package phase3;

import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {
        // this is where the program starts
        
        Graph graph = Reader.readGraph("phase3/graphs/block3_2018_graph06.txt");
        
        // split graph into constituent graphs
        ArrayList<Graph> graphs = split(graph);
        
        // prints out how many separate graphs there are
        // System.out.println(graphs.size());
        
        SuperRunner runner = new SuperRunner(graphs);
    }
    
    private static ArrayList<Graph> split(Graph graph) {
        ArrayList<Graph> sections = new ArrayList<Graph>();
        
        while (graph.nodes.size() > 0) {
            Graph section = splitOff(graph);
            
            // remove removed nodes from original graph
            for (Node node : section.nodes) graph.nodes.remove(node);
            
            sections.add(section);
        }
        
        return sections;
    }
    
    private static Graph splitOff(Graph graph) {
        ArrayList<Node> nodes = new ArrayList<Node>();
        Node node = graph.nodes.get(0);
        nodes.add(node);
        
        // changes the nodes ArrayList
        splitOff(node, nodes);
        
        return new Graph(nodes);
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
    private final ArrayList<Graph> graphs;
    private final ArrayList<Runner> runners;
    
    private int globalLowerBound = 1;
    private int globalUpperBound;
    
    public SuperRunner(ArrayList<Graph> graphs) {
        this.graphs = graphs;
        
        runners = new ArrayList<Runner>();
        int maxUpperBound = 0;
        for (Graph graph : graphs) {
            Runner runner = new Runner(this, graph);
            
            if (runner.currentUpperBound > maxUpperBound)
                maxUpperBound = runner.currentUpperBound;
            
            runners.add(runner);
        }
        globalUpperBound = maxUpperBound;
    }
    
    private void boundCheck() {
        if (globalUpperBound == globalLowerBound) {
            System.out.println("CHROMATIC NUMBER = " + globalLowerBound);
            System.exit(0);
        }
    }
    
    public void lowerBoundFound(int newLowerBound) {
        if (newLowerBound < globalLowerBound) return;
        
        int minLowerBound = newLowerBound;
        for (Runner runner : runners) {
            if (runner.currentLowerBound < minLowerBound)
                minLowerBound = runner.currentLowerBound;
        }
        if (minLowerBound > globalLowerBound) {
            System.out.println("NEW BEST LOWER BOUND = " + minLowerBound);
            globalLowerBound = minLowerBound;
            
            boundCheck();
        }
    }
    
    public void upperBoundFound(int newUpperBound) {
        if (newUpperBound > globalUpperBound) return;
        
        int maxUpperBound = newUpperBound;
        for (Runner runner : runners) {
            if (runner.currentUpperBound > maxUpperBound)
                maxUpperBound = runner.currentUpperBound;
        }
        if (maxUpperBound < globalUpperBound) {
            System.out.println("NEW BEST UPPER BOUND = " + maxUpperBound);
            globalUpperBound = maxUpperBound;
            
            boundCheck();
        }
    }
}