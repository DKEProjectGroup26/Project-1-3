package phase3;

import java.io.*;
import java.util.*;
import java.util.Arrays;
import java.util.ArrayList;

// CURRENTLY BROKEN
public class AdjacencyMatrix implements Algorithm {
    public void run(Runner runner, Graph graph) {
        int n = graph.nodes.size();
        
		Integer[][] matrix = new Integer[n][n];
        
		// Initialize the chromatic number
		int chromaticNumber = 0;
        
		// Because it's now Integer, which cannot be initialized into 0 automatically.
		// This step is to initialize it
		for (int i = 0; i < n; i++) {
			Arrays.fill(matrix[i], 0);
		}
            
        // put values into matrix as rules of adjacent matrix, and to make it easier and less loops later,
		// let the values int the upper diagnal of the matrix
        for (Node node : graph.nodes) {
            for (Node neighbor : node.neighbors) {
                // skip if this node has been done already
                if (neighbor.index < node.index) continue;
                
                matrix[node.index][neighbor.index] = 1;
            }
        }
        
        // i controls the columns and j controls the rows
		for (int i = 0; i < n; i++){
			//matrix[i][i] is the colour that the vertex i can choose and firstly initialize it as 1
			matrix[i][i] = 1;
			
            // Use this for loop to check the vertex i+1 can't be in the same color with which
            // other vertex (by checking the column i), and put the color the vertax i can't
            // be into the row j in ascending order
            for (int j = 0; j < i; j++){
                if (matrix[j][i] == 1)
                    matrix[i][matrix[j][j] - 1] = matrix[j][j];
			}
            
			// Transform array to ArrayList to find the index of 0 without any loop to make it run quiker
            ArrayList<Integer> list = new ArrayList<Integer>(Arrays.asList(matrix[i]));
            
            // Using 0 is to find out which color that the vertex i+1 could be
            int a = list.indexOf(0);
		    
            //This step is to ensure the program go into the "if" only if there's 0 in
            // the rows, since if there's no 0, a will be -1
            if (a >= 0) {
                if(a < i)
					matrix[i][i] = a + 1;
					// When there's no other 0 in the lower diagnal of that row,
                    // then the color of vertax i should be a new color
				else
                    matrix[i][i] = i + 1;
			} else {
                // If there's no 0 in that row, then the vertax i+1 could be a new color
                matrix[i][i] = i + 1;
			}
			
            // Find out the maximum number in the diagnal line, and then it is the new chromatic number
            if (matrix[i][i] > chromaticNumber) chromaticNumber = matrix[i][i];
        }
        
        runner.upperBound(chromaticNumber);
	}
}