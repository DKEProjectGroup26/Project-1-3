package phase3;

import java.util.ArrayList;

// CURRENTLY BROKEN
public class Clique implements Algorithm {
    public void run(Runner runner, Graph graph) {
        // start from lower bound, no point checking below
        int startSize = Math.max(2, runner.currentLowerBound);
        
        // stores the nodes with enough connections for the last size
        // start with all the nodes, select in loop
        ArrayList<Node> candidates = graph.nodes;
        
        // stores the last successfully found clique, starts empty
        ArrayList<Node> clique = new ArrayList<Node>();
        
        for (int size = startSize;; size++) {
            // remove all candidates with not enough connections
            candidates = selectViable(candidates, size);
            
            // if there aren't enough candidates to make a clique, stop checking
            if (candidates == null) break;
            
            // look for a clique
            clique = findClique(size, new ArrayList<Node>(), candidates);
            
            // if no clique was found, stop checking
            if (clique == null) break;
            
            // clique != null
            runner.lowerBound(size); // clique found, set bound and continue searching
        }
    }
    
    private static boolean isViable(Node candidate, int size) {
        // if the candidate doesn't have enough neighbors, it's not viable
        if (candidate.neighbors.size() < size - 1) return false;
        
        // check if the candidate has enough neighbors with enough neighbors each
        // if not, it's not viable
        int viableNeighbors = 0;
        for (Node neighbor : candidate.neighbors) {
            if (neighbor.neighbors.size() >= size - 1) {
                viableNeighbors++;
                
                // if target reached, stop checking
                if (viableNeighbors >= size - 1) break;
            }
        }
        
        if (viableNeighbors < size - 1) return false;
        
        return true;
    }

    private static ArrayList<Node> selectViable(ArrayList<Node> lastCandidates, int size) {
        // return null if there are not enough candidates
        if (lastCandidates.size() < size) return null;
        
        // find viable candidates
        ArrayList<Node> candidates = new ArrayList<Node>();
        
        // how many candidates can be skipped before there aren't enough left to make a clique
        int canBeSkipped = lastCandidates.size() - size;
        
        for (Node candidate : lastCandidates) {
            if (isViable(candidate, size))
                candidates.add(candidate);
            else {
                // skipped one
                canBeSkipped--;
                
                // if too many were skipped, stop checking
                if (canBeSkipped < 0) return null;
            }
        }
        
        // if not enough were found, return null
        if (candidates.size() < size) return null;
        
        return candidates;
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