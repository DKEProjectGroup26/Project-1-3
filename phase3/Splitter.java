package phase3;

import java.util.ArrayList;

public class Splitter {
    public static ArrayList<Graph> split(Graph graph) {
        ArrayList<Graph> sections = new ArrayList<Graph>();
        
        boolean firstIteration = true;
        
        while (graph.nodes.size() > 0) {
            ArrayList<Node> section = splitOff(graph);
            
            if (firstIteration) {
                firstIteration = false;

                if (graph.nodes.isEmpty()) {
                    // the first section is the whole graph
                    graph.nodes = section;
                    
                    sections.add(graph);
                    
                    return sections;
                }
            }
            
            sections.add(new Graph(section));
        }
        
        return sections;
    }
    
    private static ArrayList<Node> splitOff(Graph graph) {
        ArrayList<Node> nodes = new ArrayList<Node>();
        Node firstNode = graph.nodes.get(0);
        graph.nodes.remove(firstNode);
        nodes.add(firstNode);
        
        ArrayList<Node> holding = new ArrayList<Node>();
        while (true) {
            for (Node node : nodes) {
                for (Node neighbor : node.neighbors) {
                    // if remove returns true, neighbor was still in the graph
                    if (graph.nodes.remove(neighbor)) {
                        holding.add(neighbor);
                        
                        // check safety later
                        // if (graph.nodes.isEmpty()) {
                        //     // no nodes left, early finish
                        //     for (Node held : holding) nodes.add(held);
                        //
                        //     System.out.println("return 0");
                        //     System.out.println(holding.size());
                        //     System.out.println(nodes.size());
                        //     return nodes;
                        // }
                    }
                }
            }
            
            if (holding.isEmpty()) {
                return nodes;
            }
            
            for (Node held : holding) nodes.add(held);

            // if no nodes left, no point in doing another loop
            if (graph.nodes.isEmpty()) {
                return nodes;
            }
            
            holding.clear();
        }
    }
}