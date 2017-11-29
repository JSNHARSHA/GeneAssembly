package app;

import assembler.DeBruijnGraph;
import assembler.DenoSequenceAssembler;
import assembler.SequenceProcessorUtil;
import simulator.SequenceSimulator;
import simulator.ShotgunSequenceSimulator;

import java.util.Arrays;
import java.util.List;

/**
 * Main class for application
 */
public class MainClass {
    public static void main(String args[]) {
        //simulate shotgun sequence reads
        SequenceSimulator sequenceSimulator = new ShotgunSequenceSimulator();
        List<String> sequences = sequenceSimulator.generateSequences("ACGTCGGTT", 5, 4);
        System.out.println("Shotgun Sequence Reads:");
        System.out.println(sequences);

        //generate Kmers
        List<String> kmers = SequenceProcessorUtil.generateKmers(sequences, 3);
        System.out.println("Set of Kmers:");
        System.out.println(kmers);
        DeBruijnGraph kmerGraph = new DeBruijnGraph(kmers);
        System.out.println("Kmer graph:");
        kmerGraph.print();
        //check Hamiltonian circuit

        //generate K - 1mers
        List<String> subkmers = SequenceProcessorUtil.generateKmers(kmers, 2);
        System.out.println("Set of K-1mers:");
        System.out.println(subkmers);
        DeBruijnGraph subkmerGraph = new DeBruijnGraph(subkmers, kmers);
        System.out.println("K-1mer graph:");
        subkmerGraph.print();
        //check Eulerian path*/

        DenoSequenceAssembler assembler = new DenoSequenceAssembler();

        //sample graph usage:
        //get edges array
        int[][] edges = subkmerGraph.getEdges();

        //get vertices (kmers)
        List<String> vertices = subkmerGraph.getVertices();
    }
}
