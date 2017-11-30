package app;

import assembler.DeBruijnGraph;
import assembler.DenoSequenceAssembler;
import assembler.SequenceProcessorUtil;
import plotter.PlotGraph;
import simulator.SequenceSimulator;
import simulator.ShotgunSequenceSimulator;
import aligner.NeedlemanWunsch;

import java.util.List;

/**
 * Main class for application
 */
public class MainClass {
    public static void main(String args[]) {
        //Get all the input from user
        String referenceGenome = "GTCAGGTC";

        //simulate shotgun sequence reads
        SequenceSimulator sequenceSimulator = new ShotgunSequenceSimulator();
        List<String> sequences = sequenceSimulator.generateSequences(referenceGenome, 5, 4);
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

        DenoSequenceAssembler assembler = new DenoSequenceAssembler(subkmerGraph);
        String eulerPath = assembler.EulerianPath(assembler.getGraph().getEdges(), assembler.getGraph().getVertices());

        if(eulerPath.equals("no"))
        {
            System.out.println("No Euler Path exists for the above graph");
        }

        else {
            System.out.println("The Euler Path is:\t" + eulerPath);
            NeedlemanWunsch needlemanWunsch = new NeedlemanWunsch();
            //pass query as first param, reference as second param
            needlemanWunsch.align(eulerPath, referenceGenome);
            System.out.println("\nSequences after alignment: ");
            System.out.println("Original\t" + needlemanWunsch.getReference() + "\nComputed\t" + needlemanWunsch.getQuery());
            System.out.println("Alignment score " + needlemanWunsch.getScore());

            //Plot the graph
            int[][] points = {{0,0}, {1,1}, {2,2}, {3,2}, {4,2}, {5,3}, {6,4}, {6,5}, {7,6}};
            PlotGraph plotGraph = new PlotGraph();
            plotGraph.plot(needlemanWunsch.getPoints());
        }
    }
}
