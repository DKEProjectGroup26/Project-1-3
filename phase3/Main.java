package phase3;
import phase3.algorithms.*;
import phase3.everything.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

// TODO: remember to check TODOs
// TODO: there still is a problem of upper bounds going back up, check if that's ok
// TODO: implement a global pruning and re-pruning system
public class Main {
    public static void main(String[] args) throws IOException {
        // this is where the program starts

        Graph graph = Reader.readGraph("phase3/graphs_phase1/graph20.txt");

        // prints out how many separate graphs there are
        // System.out.println(graphs.size() + " section in graph");

        // TODO: check the ordering of the algorithms (fastest first)
        // all usable algorithms (commas with nothing after them cause an error)
        ArrayList<Class<? extends Algorithm>> algorithms = new ArrayList<Class<? extends Algorithm>>(
            Arrays.asList(
                // BrooksTheorem.class,
                // Tree.class,
                // Clique.class,
                StephansAlgorithm.class
                // AdjacencyMatrix.class,
                // BasicExact.class,
                // WelshPowell.class,

                // these are work in progress, don't use them
                // PrunedExact.class,
                // HoffmanBound.class
            )
        );

        SuperRunner runner = new SuperRunner(graph, algorithms);
    }
}
