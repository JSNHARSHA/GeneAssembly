package assembler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * DeBruijn Graph Model
 */
public class DeBruijnGraph {
    //list of edges (src, dest)
    private List<Integer[]> edges;
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
        edges = new ArrayList<>();
        for (int i = 0; i < kmers.size(); i++) {
            String kmer1 = kmers.get(i);

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
                    Integer[] edge = new Integer[2];
                    edge[0] = i;//src
                    edge[1] = j;//dest
                    edges.add(edge);
                }
            }
        }
    }

    /**
     * To display the graph
     */
    public void print() {
        for (int i = 0; i < edges.size(); i++) {
            Integer[] edge = edges.get(i);
            System.out.println(edge[0] + " -> " + edge[1]);
        }
    }

    public int getNoOfVertices() {
        return kmers.size();
    }

    public int getNoOfEdges() {
        return edges.size();
    }

    /**
     * returns array of edges (src -> dest)
     * @return
     */
    public int[][] getEdges() {
        int[][] edgesArr = new int[edges.size()][2];
        for (int i = 0; i < edges.size(); i++) {
            edgesArr[i][0] = edges.get(i)[0];
            edgesArr[i][1] = edges.get(i)[1];
        }
        return edgesArr;
    }

    /**
     * returns kmers values of vertices
     * @return
     */
    public List<String> getVertices() {
        return kmers;
    }
}
