package phase3.algorithms;
import phase3.everything.*;

import java.util.ArrayList;

/**
 * An upper bound based on adjacency matrices
 */
public class AdjacencyMatrix implements Algorithm.Any {
    /**
     * Implementation of {@link Algorithm}, runs the algorithm on a given graph
     */
    public void run(Runner runner, Graph graph) {
        int numberOfNodes = graph.nodes.size();
        
        // array is automatically initialized to 0s
		int[][] matrix = new int[numberOfNodes][numberOfNodes];
        
        // start from 0 colors
		int maxColor = 0;
            
        // create the adjacency matrix, only one triangle
        for (Node node : graph.nodes) {
            for (Node neighbor : node.getNeighbors()) {
                // skip if this node has been done already
                if (neighbor.getIndex() < node.getIndex()) continue;
                
                matrix[node.getIndex()][neighbor.getIndex()] = 1;
            }
        }
        
        // i controls the columns and j controls the rows
		for (int i = 0; i < numberOfNodes; i++) {
			//matrix[i][i] is the color that the vertex i can have, start from 1
            matrix[i][i] = 1;
			
            // Use this loop to check the vertex i can't be in the same color with which
            // other vertex (by checking the column i), and put the color the vertax i can't
            // be into the row j in ascending order
            for (int j = 0; j < i; j++){
                if (matrix[j][i] == 1)
                    // nodes j and i share an edge
                    matrix[i][matrix[j][j] - 1] = matrix[j][j];
			}
            
            // find which color that the vertex i can have
            int a = indexOf(matrix[i], 0);
		    
            // this step is to ensure the program go into the "if" only if there's 0 in
            // the rows, since if there's no 0, a will be -1
            if (a >= 0) {
                if (a < i)
					matrix[i][i] = a + 1;
					// when there's no other 0 in the lower diagnal of that row,
                    // then the color of vertex i should be a new color
				else
                    matrix[i][i] = i + 1;
			} else {
                // if there's no 0 in that row, then the vertex i could be a new color
                matrix[i][i] = i + 1;
			}
			
            // if the node's color is larger than the maximum so far, it's the new maximum
            if (matrix[i][i] > maxColor) maxColor = matrix[i][i];
        }
        
        runner.upperBound(maxColor);
	}
    
    private static int indexOf(int[] array, int value) {
        for (int i = 0; i < array.length; i++) if (array[i] == value) return i;
        return -1;
    }
}