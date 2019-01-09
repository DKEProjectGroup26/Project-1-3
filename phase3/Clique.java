package phase3;

import java.util.ArrayList;

// FINISHED, NOT CHECKED
public class Clique implements Algorithm {
    public void run(Runner runner, Graph graph) {
        if (any2(graph)) runner.lowerBound(2);
        
        // use timeout
        // start from lower bound, no point checking below
        int startSize = Math.max(2, runner.currentLowerBound);
        for (int size = startSize;; size++) {
            ArrayList<Node> candidates = findCandidates(graph, size);
            if (candidates == null) break; // not enough nodes for a clique to exist
            
            ArrayList<Node> clique = findClique(size, new ArrayList<Node>(), candidates);
            if (clique == null) break; // no clique found, last one was the biggest
            runner.lowerBound(size); // clique found, set bound and continue searching
            
            // implement extension
        }
    }
    
    private static boolean any2(Graph graph) {
        // checks if there are 2 nodes connected anywhere in the graph
        // if there are -> lower bound = 2
        for (Node node : graph.nodes)
            if (!node.neighbors.isEmpty()) return true;
        
        return false;
    }
    
    private static ArrayList<Node> findCandidates(Graph graph, int size) {
        // checks, based on numbers of neighbors, whether it's possible
        // to find a clique of the given size
        // returns candidates or null if impossible
        
        int minNeighbors = size - 1;
        ArrayList<Node> nodesFound = new ArrayList<Node>();
        
        for (Node node : graph.nodes)
            if (node.neighbors.size() >= minNeighbors)
                nodesFound.add(node);
        // maybe add a check so that when there's not enough nodes left it gives up
        
        if (nodesFound.size() >= size) return nodesFound;
        return null;
    }
    
    private static ArrayList<Node> findClique(int size, ArrayList<Node> clique, ArrayList<Node> candidates) {
        for (Node node : candidates) {
            ArrayList<Node> newClique = new ArrayList<Node>(clique);
            newClique.add(node);
            
            if (newClique.size() == size) return newClique;
            
            ArrayList<Node> newCandidates = new ArrayList<Node>(candidates);
            for (Node candidate : candidates) {
                if (!node.neighbors.contains(candidate)) {
                    newCandidates.remove(candidate);
                    
                    if (newClique.size() + newCandidates.size() < size)
                        return null;
                }
            }
            
            // now newCandidates only contains the still viable nodes
            ArrayList<Node> result = findClique(size, newClique, newCandidates);
            if (result != null) return result;
        }
        
        return null;
    }
}