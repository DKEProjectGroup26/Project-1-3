package phase3;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        // this is where the program starts
        
        Graph graph = Reader.readGraph("phase3/graphs/block3_2018_graph03.txt");
        Runner runner = new Runner(graph);
    }
}

class Runner {
    private Graph graph;
    private int currentLowerBound = 1;
    private int currentUpperBound;
    
    public Runner(Graph g) {
        graph = g;
        currentUpperBound = g.nodes.size();
        
        run();
    }
    
    private void run() {
        // call all other classes
        // make this threads
        
        BrooksTheorem.brooksUpperBound(this, graph);
        Tree.isTree(this, graph);
        Clique.findCliques(this, graph);
    }
    
    public void chromaticNumberFound(int chromaticNumber) {
        System.out.println("CHROMATIC NUMBER = " + chromaticNumber);
        System.exit(0);
    }
    
    private void boundCheck() {
        if (currentLowerBound == currentUpperBound)
            chromaticNumberFound(currentLowerBound);
    }
        
    public void upperBound(int newUpperBound) {
        if (newUpperBound < currentUpperBound) {
            System.out.println("NEW BEST UPPER BOUND = " + newUpperBound);
            currentUpperBound = newUpperBound;
            
            boundCheck();
        }
    }
    
    public void lowerBound(int newLowerBound) {
        if (newLowerBound > currentLowerBound) {
            System.out.println("NEW BEST LOWER BOUND = " + newLowerBound);
            currentLowerBound = newLowerBound;
            
            boundCheck();
        }
    }
}