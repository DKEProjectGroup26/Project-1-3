package phase3.algorithms;
import phase3.everything.*;

import java.util.Arrays;

public class BasicExact implements Algorithm.Connected, Interruptable.WithLowerBoundUpdates {
    private boolean running = true;
    private int localLowerBound;
    
    public void interrupt() {
        running = false;
    }
    
    public void newLowerBound(int lowerBound) {
        if (lowerBound > localLowerBound)
            localLowerBound = lowerBound;
    }
    
    public void run(Runner runner, Graph graph) {
        localLowerBound = runner.getLowerBound();
        
        for (int numberOfColors = localLowerBound;; numberOfColors++) {
            // check if interrupted
            if (!running) break;
            
            // if there's a new lower bound, skip to it
            if (localLowerBound > numberOfColors) numberOfColors = localLowerBound;
            
            // all numbers of colors below this have been checked and didn't work
            // so this is the new lower bound
            runner.lowerBound(numberOfColors);
            
            if (solvableWith(graph, numberOfColors)) {
                runner.chromaticNumberFound(numberOfColors);
                break;
            }
        }
    }
    
    // non-recursive algorithm for less memory usage
    private boolean solvableWith(Graph graph, int numberOfColors) {
        // initialize color array to -1 (not colored)
        int[] colors = new int[graph.nodes.size()];
        Arrays.fill(colors, -1);
        
        // set first node to color 0
        colors[0] = 0;
        
        // the index of the node currently being colored, start from second node
        int index = 1;
        
        while (true) {
            // check if interrupted of if a better lower bound was found
            if (!running || localLowerBound > numberOfColors) return false;
            
            // if the index is smaller than 0, the graph cannot be colored with this many colors
            if (index <= 0) return false;
            
            // if the index is outside the colors array, all nodes are colored
            if (index >= colors.length) return true;
            
            // create a local reference to the node being colored
            Node node = graph.nodes.get(index);
            
            boolean successfullyColored = false;
            
            // try all available colors starting with the color after the current color
            colorLoop: for (int color = colors[index] + 1; color < numberOfColors; color++) {
                // check if the color conflicts with neighboring nodes
                for (Node neighbor : node.getNeighbors())
                    if (colors[neighbor.getIndex()] == color)
                        continue colorLoop;
                
                // if it doesn't, color the node and continue
                colors[index] = color;
                successfullyColored = true;
                break;
            }
            
            // if the node was colored, move on to the next node, otherwise, reset the color and move back
            if (successfullyColored)
                index++;
            else {
                colors[index] = -1;
                index--;
            }
        }
    }
}