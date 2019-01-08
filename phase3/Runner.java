package phase3;

public class Runner {
    private Graph graph;
    private int currentLowerBound = 1;
    private int currentUpperBound;
    
    public Runner(Graph g) {
        graph = g;
        currentUpperBound = g.nodes.size();
        
        run();
    }
    
    private void run() {
        // call all other classes
        
        Algorithm algorithms[] = {
            new BrooksTheorem(),
            new Tree(),
            new Clique()
        };
        
        for (Algorithm algorithm : algorithms) {
            // make a thread
            algorithm.run(this, graph);
        }
    }
    
    public void chromaticNumberFound(int chromaticNumber) {
        System.out.println("CHROMATIC NUMBER = " + chromaticNumber);
        System.exit(0);
    }
    
    private void boundCheck() {
        if (currentLowerBound == currentUpperBound)
            chromaticNumberFound(currentLowerBound);
    }
        
    public void upperBound(int newUpperBound) {
        if (newUpperBound < currentUpperBound) {
            System.out.println("NEW BEST UPPER BOUND = " + newUpperBound);
            currentUpperBound = newUpperBound;
            
            boundCheck();
        }
    }
    
    public void lowerBound(int newLowerBound) {
        if (newLowerBound > currentLowerBound) {
            System.out.println("NEW BEST LOWER BOUND = " + newLowerBound);
            currentLowerBound = newLowerBound;
            
            boundCheck();
        }
    }
}