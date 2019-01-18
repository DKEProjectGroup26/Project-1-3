package phase3;
import phase3.algorithms.*;
import phase3.everything.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

// TODO: remember to check TODOs
// TODO: finish javadoc
// TODO: there still is a problem of upper bounds going back up, check if that's ok and if it still happens

/**
 * The entry point for the program, contains the {@link Main#main} method
 */
public class Main {
    /**
     * The program's main method, called when the program is run
     * @param  args  the command line arguments, args[0] is the path to the graph
     *
     * @throws  IOException  if the graph file can't be loaded
     */
    public static void main(String[] args) throws IOException {
        // this is where the program starts
        
        // read graph as specified in args
        // TODO: uncomment this and remove the hard-coded graph loading
        if (args.length < 1) throw new IllegalArgumentException("no file specified");
        Graph graph = Reader.readGraph(args[0]); // throws IOException
        
        // hard-coded graph loading
        // TODO: remove this
        // Graph graph = Reader.readGraph("phase3/graphs_phase1/graph16.txt");
        
        // prints out how many separate graphs there are
        // System.out.println(graphs.size() + " section in graph");
        
        // TODO: check the ordering of the algorithms
        // (fastest -> slowest)
        // (non-interruptable -> interruptable)
        // (no updates -> with updates)
        // all usable algorithms
        ArrayList<Class<? extends Algorithm>> algorithms = new ArrayList<Class<? extends Algorithm>>(
            // WARNING: commas with nothing after them cause an error, remove trailing commas
            Arrays.asList(
                // BrooksTheorem.class,
                // Tree.class,
                // Clique.class
                // Greedy.class,
                // IgnoringExact.class,
                // AdjacencyMatrix.class,
                // BasicExact.class,
                // WelshPowell.class
                // BronKerbosch.class

                // these are work in progress, don't use them
                // IgnoringExact.class,
                // HoffmanBound.class
            )
        );

        SuperRunner runner = new SuperRunner(graph, algorithms);
    }
}
