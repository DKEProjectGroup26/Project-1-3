package phase3.algorithms;
import phase3.everything.*;

import java.util.ArrayList;

/**
 * The Bren-Kerbosch algorithm used to find cliques
 */
public class BronKerbosch implements Algorithm.Connected {
    /**
     * Implementation of {@link Algorithm}, runs the algorithm on a given graph
     */
    public void run(Runner runner, Graph graph) {
        getMaximals(runner, new ArrayList<Node>(), new ArrayList<Node>(graph.nodes), new ArrayList<Node>());
    }
    
    private static void getMaximals(
        Runner runner,
        ArrayList<Node> clique,
        ArrayList<Node> possible,
        ArrayList<Node> excluded
    ) {
        if (possible.isEmpty() && excluded.isEmpty()) runner.lowerBound(clique.size());
        
        ArrayList<Node> originalPossible = new ArrayList<Node>(possible);
        
        for (Node node : originalPossible) {
            ArrayList<Node> newClique = new ArrayList<Node>(clique);
            newClique.add(node);
            
            ArrayList<Node> newPossible = new ArrayList<Node>();
            ArrayList<Node> newExcluded = new ArrayList<Node>();
            for (Node neighbor : node.getNeighbors()) {
                if (possible.contains(neighbor)) newPossible.add(neighbor);
                if (excluded.contains(neighbor)) newExcluded.add(neighbor);
            }
            getMaximals(runner, newClique, newPossible, newExcluded);
            
            possible.remove(node);
            excluded.add(node);
        }
    }
}