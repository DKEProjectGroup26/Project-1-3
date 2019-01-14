package phase3;

public interface Algorithm {
    public void run(Runner runner, Graph graph);
    
    // implement NotSplit to get only non-split graphs
    // implement Any to get any type of graph
    public interface Connected extends Algorithm {}
    public interface Any extends Algorithm {}
}