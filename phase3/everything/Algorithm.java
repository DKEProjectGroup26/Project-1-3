package phase3.everything;

/**
 * The general interface for algorithms, all algorithms must implement this interface
 * but only through {@link Algorithm.Connected} or {@link Algorithm.Any}, otherwise an error will be
 * thrown at runtime.
 */
public interface Algorithm {
    /**
     * The method which every algorithm must have, this is used by {@link Runner} to call
     * the algorithm passing itself and the graph to be processed
     *
     * @param  runner  the object of type {@link Runner} or {@link OGRunner} to which the algorithm should
     *  send new lower/upper bound messages
     * @param  graph   the graph object, given by runner on which the algorithm should operate
     */
    public void run(Runner runner, Graph graph);
    
    /**
     * The interface to be implemented by algorithms which only accept connected graphs
     * or graphs in which a path can be found from any node to any other node
     */
    public interface Connected extends Algorithm {}
    
    /**
     * The interface to be implemented by algorithms which can accept both connected and
     * non-connected graphs or graphs consisting of multiple, completely disconnected sections
     */
    public interface Any extends Algorithm {}
}