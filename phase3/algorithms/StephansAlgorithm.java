package phase3.algorithms;
import phase3.everything.*;

import java.util.ArrayList;
import java.util.Arrays;

public class StephansAlgorithm implements Algorithm.Connected {
    public void run(Runner runner, Graph graph) {
        int[] colors = new int[graph.nodes.size()];
        Arrays.fill(colors, -1);
        
        int maxColor = 0;
        for (int i = 0; i < colors.length; i++) {
            Node node = graph.nodes.get(i);
            
            ArrayList<Integer> neighborColors = new ArrayList<Integer>();
            for (Node neighbor : node.getNeighbors())
                neighborColors.add(colors[neighbor.getIndex()]);
            
            // skip all colors taken by neighbors
            int newColor = 1;
            while (neighborColors.contains(newColor)) newColor++;
            
            colors[node.getIndex()] = newColor;
            if (newColor > maxColor) {
                // if the smallest upper bound this algorithm will find
                // is already above the current upper bound, stop checking
                if (newColor > runner.getUpperBound()) return;
                
                maxColor = newColor;
            }
        }
        
        runner.upperBound(maxColor);
    }
}