package phase3.everything;

/**
 * The interface to be implemented by any algorithm that can be interrupted
 */
public interface Interruptible {
    /**
     * This method should be overridden by algorithms implementing their own stopping functionality
     */
    public void interrupt();
    
    /**
     * An additional interface for algorithms that accept lower bound updates
     */
    public interface WithLowerBoundUpdates extends Interruptible {
        public void newLowerBound(int lowerBound);
    }
}