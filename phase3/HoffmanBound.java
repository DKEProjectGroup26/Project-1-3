package phase3;

import java.util.ArrayList;

// NOT FINISHED
public class HoffmanBound implements Algorithm.Connected {
    public void run(Runner runner, Graph graph) {
        throw new Error("HoffmanBound is not implemented, don't use it");
    }
    
    // move this method to Graph with lazy access
    private int[][] adjacencyMatrix(Graph graph) {
        int numberOfNodes = graph.nodes.size();
        
        // all int arrays are full of 0s by default
        // no need to fill the array with 0s
        int[][] matrix = new int[numberOfNodes][numberOfNodes];
        
        for (Node node : graph.nodes) {
            for (Node neighbor : node.neighbors) {
                matrix[node.index][neighbor.index] = 1;
            }
        }
        
        return matrix;
    }
}