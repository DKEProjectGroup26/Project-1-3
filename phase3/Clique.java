package phase3;

import java.util.ArrayList;

public class Clique implements Algorithm.Connected, Interruptable.WithLowerBoundUpdates {
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
        localLowerBound = runner.currentLowerBound;
        
        // start from lower bound, no point checking below
        int startSize = Math.max(2, localLowerBound + 1);
        
        // stores the nodes with enough connections for the last size
        // start with all the nodes, select in loop
        ArrayList<Node> candidates = graph.nodes;
        
        // stores the last successfully found clique, starts empty
        ArrayList<Node> clique = new ArrayList<Node>();
        
        for (int size = startSize;; size++) {
            // check if interrupted
            if (!running) break;
            
            // check if the lower bound has changed, if so, skip to match
            if (localLowerBound >= size) size = localLowerBound + 1;
            
            // remove all candidates with not enough connections
            candidates = selectViable(candidates, size);
            
            // if there aren't enough candidates to make a clique, stop checking
            if (candidates == null) break;
            
            // look for a clique
            clique = findClique(size, new ArrayList<Node>(), candidates);
            
            // if no clique was found, stop checking
            if (clique == null) break;
            
            // clique != null
            if (size > localLowerBound) localLowerBound = size;
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
    
    private ArrayList<Node> findClique(int size, ArrayList<Node> clique, ArrayList<Node> candidates) {
        // check if interrupted
        if (!running) return null;
        
        // check if a better lower bound was found
        // empty array list instead of null to avoid terminating the algorithm
        if (localLowerBound >= size) return new ArrayList<Node>();
        
        mainLoop:
        for (Node node : candidates) {
            ArrayList<Node> newClique = new ArrayList<Node>(clique);
            newClique.add(node);
            
            if (newClique.size() == size) return newClique;
            
            ArrayList<Node> newCandidates = new ArrayList<Node>(candidates);
            for (Node candidate : candidates) {
                if (!node.neighbors.contains(candidate)) {
                    newCandidates.remove(candidate);
                    
                    // if there aren't enough candidates left to make a clique
                    // of the given size, stop checking
                    // if (newClique.size() + newCandidates.size() < size)
                        // continue mainLoop;
                }
            }
            
            if (newClique.size() + newCandidates.size() < size) continue;
            
            // now newCandidates only contains the still viable nodes
            ArrayList<Node> result = findClique(size, newClique, newCandidates);
            if (result != null) return result;
        }
        
        return null;
    }
}