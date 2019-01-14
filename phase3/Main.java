package phase3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

// TODO: remember to check TODOs
public class Main {
    public static void main(String[] args) throws IOException {
        // this is where the program starts
        
        Graph graph = Reader.readGraph("phase3/graphs_phase1/graph01.txt");
        
        // prints out how many separate graphs there are
        // System.out.println(graphs.size() + " section in graph");
        
        
        
        int minC = graph.nodes.get(0).neighbors.size();
        for (Node node : graph.nodes) if (node.neighbors.size() < minC) minC = node.neighbors.size();
        System.out.println("min: " + minC);
        
        
        
        // all usable algorithms
        ArrayList<Class<? extends Algorithm>> algorithms = new ArrayList<Class<? extends Algorithm>>(
            Arrays.asList(
                // BrooksTheorem.class,
                // Tree.class,
                // Clique.class,
                // StephansAlgorithm.class,
                // AdjacencyMatrix.class,
                BasicExact.class
                
                // these are work in progress, don't use them
                // PrunedExact.class
                // HoffmanBound.class,
            )
        );
        
        SuperRunner runner = new SuperRunner(graph, algorithms);
    }
}