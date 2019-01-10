package phase3;

import java.util.ArrayList;

public class StephansAlgorithm implements Algorithm {
    public void run(Runner runner, Graph graph) {
        int[] colors = new int[graph.nodes.size()];
        for (int i = 0; i < colors.length; i++) colors[i] = -1;
        
        int maxColor = 0; // = runner.currentLowerBound;
        
        for (int i = 0; i < colors.length; i++) {
            Node node = graph.nodes.get(i);
            
            ArrayList<Integer> neighborColors = new ArrayList<Integer>();
            for (Node neighbor : node.neighbors)
                neighborColors.add(colors[neighbor.index]);
            
            // skip all colors taken by neighbors
            int newColor = 1;
            for (; neighborColors.contains(newColor); newColor++);
            
            colors[node.index] = newColor;
            if (newColor > maxColor) maxColor = newColor;
        }
        
        runner.upperBound(maxColor);
    }
}