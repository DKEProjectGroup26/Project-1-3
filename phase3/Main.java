package phase3;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        // this is where the program starts
        
        Graph graph = Reader.readGraph("phase3/graphs/block3_2018_graph03.txt");
        Runner runner = new Runner(graph);
    }
}