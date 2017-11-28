package assembler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * DeBruijn Graph Model
 */
public class DeBruijnGraph {
    //adjacency list representation of edges (vertex -> edges list)
    private ArrayList<Integer>[] adjacencyList;
    //kmer values
    private List<String> kmers;

    //constructing Debruijn graph from kmers
    public DeBruijnGraph(List<String> kmers) {
        this(kmers, Collections.emptyList());
    }

    //constructing Debruijn graph from k-1mers and ref kmers
    public DeBruijnGraph(List<String> kmers, List<String> refEdges) {
        this.kmers = kmers;
        buildAdjacencyList(refEdges);
    }

    //build adjacency list from list of kmers
    private void buildAdjacencyList(List<String> refEdges) {
        adjacencyList = new ArrayList[kmers.size()];
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
                //check for existence of edge in ref kmers for k-1mer case
                if (kmer1.endsWith(kmer2.substring(0, kmer2.length() - 1))
                        && (refEdges.isEmpty() || refEdges.contains(kmer1.concat(kmer2.substring(kmer2.length() - 1))))) {
                    adjacencyList[i].add(j);
                }
            }
        }
    }

    public void print() {
        for (int i = 0; i < adjacencyList.length; i++) {
            System.out.println(i + " -> " + adjacencyList[i]);
        }
    }

    public int getNoOfVertices() {
        return adjacencyList.length;
    }

    /**
     * get list of connected vertices from source vertex
     * @param vertex
     * @return
     */
    public ArrayList<Integer> getEdges(int vertex) {
        if (vertex < adjacencyList.length) {
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
        if (vertex < kmers.size()) {
            return kmers.get(vertex);
        }
        return null;
    }
}
