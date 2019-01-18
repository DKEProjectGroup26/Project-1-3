package phase3.everything;

/**
 * The general interface for algorithms, all algorithms must implement this interface
 * but only through {@link Algorithm.Connected} or {@link Algorithm.Any}, otherwise an error will be
 * thrown at runtime.
 */
public interface Algorithm {
    /**
     * Used to pass a graph to an algorithm
     *
     * @param runner 
     * @param graph
     */
    public void run(Runner runner, Graph graph);
    
    /**
     * The interface to be implemented by algorithms which only accept connected graphs
     */
    public interface Connected extends Algorithm {}
    
    /**
     * The interface to be implemented by algorithms which can accept both connected and
     * non-connected graphs
     */
    public interface Any extends Algorithm {}
}