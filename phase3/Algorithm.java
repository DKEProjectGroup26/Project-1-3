package phase3;

public interface Algorithm {
    public void run(Runner runner, Graph graph);
    
    public interface Connected extends Algorithm {}
    public interface Any extends Algorithm {}
}