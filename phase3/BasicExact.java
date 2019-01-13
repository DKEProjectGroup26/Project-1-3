package phase3;

import java.util.Arrays;

public class BasicExact implements Algorithm.Connected {
    public void run(Runner runner, Graph graph) {
        int startNumberOfColors = runner.currentLowerBound;
        for (int numberOfColors = startNumberOfColors;; numberOfColors++) {
            // all numbers of colors below this have been checked and didn't work
            // so this is the new lower bound
            runner.lowerBound(numberOfColors);
            
            if (solvableWith(graph, numberOfColors)) {
                runner.chromaticNumberFound(numberOfColors);
                break;
            }
        }
    }
    
    // non-recursive version
    private static boolean solvableWith(Graph graph, int numberOfColors) {
        // initialize color array to -1 (not colored)
        int[] colors = new int[graph.nodes.size()];
        Arrays.fill(colors, -1);
        
        // TODO: assign 0 to first node and start at 1
        // the index of the node currently being colored
        int index = 0;
        
        while (true) {
            // if the index is smaller than 0, the graph cannot be colored with this many colors
            if (index < 0) return false;
            
            // if the index is outside the colors array, all nodes are colored
            if (index >= colors.length) return true;
            
            // create a local reference to the node being colored
            Node node = graph.nodes.get(index);
            
            boolean successfullyColored = false;
            
            // try all available colors starting with the color after the current color
            colorLoop: for (int color = colors[index] + 1; color < numberOfColors; color++) {
                // check if the color conflicts with neighboring nodes
                for (Node neighbor : node.neighbors)
                    if (colors[neighbor.index] == color)
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
    
    // untested recursive version
    // private static boolean solvableWith(Graph graph, int numberOfColors) {
    //     int[] colors = new int[graph.nodes.size()];
    //
    //     // initialize with -1 (no color)
    //     Arrays.fill(colors, -1);
    //
    //     return solveWith(graph, numberOfColors, colors, 0);
    // }
    //
    // private static boolean solveWith(Graph graph, int numberOfColors, int[] colors, int index) {
    //     // if the index is outside the colors array, all nodes are successfully colored
    //     if (index >= colors.length) return true;
    //
    //     // create a local reference to the node being colored
    //     Node node = graph.nodes.get(index);
    //
    //     // try all available colors
    //     colorLoop: for (int color = 0; color < numberOfColors; color++) {
    //         // check if the color conflicts with any neighbors, if it does, skip
    //         for (Node neighbor : node.neighbors)
    //             if (colors[neighbor.index] == color) continue colorLoop;
    //
    //         // make a clone of the colors array
    //         int[] newColors = Arrays.copyOf(colors, colors.length);
    //         newColors[index] = color;
    //
    //         // recursive call
    //         if (solveWith(graph, numberOfColors, newColors, index + 1)) return true;
    //     }
    //
    //     // if the color loop finished without returning true,
    //     // none of the colors can be used
    //     return false;
    // }
}