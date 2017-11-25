package assembler;

import java.util.ArrayList;
import java.util.List;

/**
 * DeBruijn Graph Model
 */
public class DeBruijnGraph {
    //total vertices
    private int noOfVertices;
    //adjacency list representation of edges (vertex -> edges list)
    private ArrayList<Integer>[] adjacencyList;
    //kmer values of each vertex
    private List<String> kmers;

    public DeBruijnGraph(List<String> kmers) {
        this.kmers = kmers;
        this.noOfVertices = kmers.size();
        buildAdjacencyList();
    }

    //build adjacency list from list of kmers
    private void buildAdjacencyList() {
        adjacencyList = new ArrayList[noOfVertices];
        for (int i = 0; i < kmers.size(); i++) {
            String kmer1 = kmers.get(i);
            adjacencyList[i] = new ArrayList<>();
            for (int j = 0; j < kmers.size(); j++) {
                //skip same vertex
                if (i == j) {
                    continue;
                }
                String kmer2 = kmers.get(j);
                //add an edge when vertex 1 suffix (length k-1) is matching vertex 2 prefix
                if(kmer1.endsWith(kmer2.substring(0, kmer2.length() - 1))) {
                    adjacencyList[i].add(j);
                }
            }
        }
    }

    public void print() {
        for (int i = 0; i < noOfVertices; i++) {
            System.out.println(i + " -> " + adjacencyList[i]);
        }
    }

    public int getNoOfVertices() {
        return noOfVertices;
    }

    /**
     * get list of connected vertices from source vertex
     * @param vertex
     * @return
     */
    public ArrayList<Integer> getEdges(int vertex) {
        if (vertex < noOfVertices) {
            return adjacencyList[vertex];
        }
        return null;
    }

    /**
     * get kmer value of a vertex
     * @param vertex
     * @return
     */
    public String getKmerValue(int vertex) {
        if (vertex < noOfVertices) {
            return kmers.get(vertex);
        }
        return null;
    }
}
