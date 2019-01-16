package phase3.everything;

/**
 * The interface to be implemented by any algorithm that accepts an interrupt (stop calculating)
 * message, this functionality is used to stop processor-intensive algorithms when a chromatic number
 * has been found and they're just using up cpu time
 */
public interface Interruptable {
    /**
     * This method should be overridden by algorithms implementing their own stopping functionality
     */
    public void interrupt();
    
    /**
     * An additional interface for algorithms that accept new lower bound messages, this is used for
     * slower lower bound algorithms such as {@link Clique} to fast-forward them to the new lower bound
     * found by another algorithm.
     */
    public interface WithLowerBoundUpdates extends Interruptable {
        public void newLowerBound(int lowerBound);
    }
    
    /**
     * An additional interface for algorithms that accept new upper bound messages, this is currently unused
     */
    public interface WithUpperBoundUpdates extends Interruptable {
        public void newUpperBound(int upperBound);
    }
    
    /**
     * For when an algorithm wishes to implement both {@link Interruptable#WithLowerBoundUpdates} and
     * {@link Interruptable#WithUpperBoundUpdates}, currently unused
     */
    public interface WithBothBoundUpdates extends WithLowerBoundUpdates, WithUpperBoundUpdates {}
}