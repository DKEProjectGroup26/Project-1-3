package phase3;

public class StephansAlgorithm implements Algorithm {
    public void run(Runner runner, Graph graph) {
        int upperbound;
        int currentMax = 0;
        for (int i=0; i<graph.nodes.size(); i++) {
            tryColor(graph.nodes.get(i));
            if (graph.nodes.get(i).index > currentMax) {
                currentMax = graph.nodes.get(i).index;
            }
        }
        upperbound = currentMax;
        runner.upperBound(upperbound);
    }

    public int tryColor(Node node) {
        int nodeValue = 1;
        for (int i = 0; i<node.neighbors.size(); i++) {
            if (node.neighbors.get(i).index == nodeValue) {
                nodeValue++;
            }
        }
        node.index = nodeValue;
        return node.index;
    }
}
