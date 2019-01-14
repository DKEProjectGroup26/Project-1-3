package phase3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

// TODO: remember to check TODOs
// TODO: implement a system to tell running algorithms to stop and/or update their bounds
// this is so that exact algorithms don't keep checking below the current lower bound
public class Main {
    public static void main(String[] args) throws IOException {
        // this is where the program starts
        
        Graph graph = Reader.readGraph("phase3/graphs_phase1/graph03.txt");
        
        // prints out how many separate graphs there are
        // System.out.println(graphs.size());
        
        // all usable algorithms
        ArrayList<Class<? extends Algorithm>> algorithms = new ArrayList<Class<? extends Algorithm>>(
            Arrays.asList(
                BrooksTheorem.class,
                Tree.class,
                Clique.class,
                StephansAlgorithm.class,
                AdjacencyMatrix.class,
                BasicExact.class
                
                // these are work in progress, don't use them
                // PrunedExact.class,
                // HoffmanBound.class,
            )
        );
        
        SuperRunner runner = new SuperRunner(graph, algorithms);
    }
}