package phase3;

public interface Interruptable {
    public void interrupt();
    
    public interface WithLowerBoundUpdates extends Interruptable {
        public void newLowerBound(int lowerBound);
    }
    
    public interface WithUpperBoundUpdates extends Interruptable {
        public void newUpperBound(int upperBound);
    }
    
    public interface WithBothBoundUpdates extends WithLowerBoundUpdates, WithUpperBoundUpdates {}
}