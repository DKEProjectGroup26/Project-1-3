package phase3;

import java.io.IOException;
import java.util.ArrayList;

// TODO: remember to check TODOs, (especially the one in SuperRunner)
public class Main {
    public static void main(String[] args) throws IOException {
        // this is where the program starts
        
        Graph graph = Reader.readGraph("phase3/graphs_phase1/graph20.txt");
        
        // prints out how many separate graphs there are
        // System.out.println(graphs.size());
        
        SuperRunner runner = new SuperRunner(graph);
    }
}