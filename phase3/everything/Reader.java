package phase3.everything;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

import java.util.ArrayList;

/**
 * The class used to read graphs from file
 */
final public class Reader {
    /**
     * Reads a graph from file and converts it into a {@link Graph} object
     *
     * @param  filePath  the relative or full path to the file from which a graph should be read
     * @return the {@link Graph} object extracted from the file
     * @throws IOException if the graph couldn't be read
     */
    public static Graph readGraph(String filePath) throws IOException {
        int nNodes = 0;
        int nEdges = 0;
        int seenEdges = 0;
        
        ArrayList<int[]> edges = new ArrayList<int[]>();
        
        FileReader fileReader = new FileReader(filePath);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        
        String line;
        
        // assuming no comment (//) lines
        
        // get nNodes
        line = bufferedReader.readLine();
        if (!line.startsWith("VERTICES = ")) throw new RuntimeException("first line is not VERTICES");
        nNodes = Integer.parseInt(line.substring(11));
        
        // get nEdges
        line = bufferedReader.readLine();
        if (!line.startsWith("EDGES = ")) throw new RuntimeException("second line is not EDGES");
        nEdges = Integer.parseInt(line.substring(8));
        
        // get edges
        while ((line = bufferedReader.readLine()) != null) {
            String[] edgeStr = line.split(" ");
            // node numbering starts from 0
            edges.add(new int[] {
                Integer.parseInt(edgeStr[0]) - 1,
                Integer.parseInt(edgeStr[1]) - 1
            });
            
            seenEdges++;
        }
        
        if (seenEdges != nEdges)
            throw new RuntimeException("seen edges don't match declared edges");
        
        return new Graph(nNodes, edges);
    }
}