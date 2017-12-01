package app;

import aligner.NeedlemanWunsch;
import assembler.DeBruijnGraph;
import assembler.DenoSequenceAssembler;
import assembler.SequenceProcessorUtil;
import plotter.PlotGraph;
import simulator.SequenceSimulator;
import simulator.ShotgunSequenceSimulator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * Main class for application
 */
public class MainClass {

    //default data properties file
    private static String propFile = "data.properties";

    public static void main(String args[]) {
        //if the properties file is provided via args
        if (args.length > 0) {
            propFile = args[0];
        }
        //init properties
        PropertyReader.init(propFile);
        //Get reference genome from file
        String refGenomefile = PropertyReader.getPropertyValue(Constants.GENOME_FILE_PATH);
        String referenceGenome = null;
        try {
            referenceGenome = readReferenceGenome(refGenomefile);
        } catch (IOException e) {
            System.out.println("Error in reading ref genome from file");
            e.printStackTrace();
            return;
        }
        int refGenomeSubstrLength = Integer.parseInt(PropertyReader.getPropertyValue(Constants.GENOME_SUBSTR_LENGTH));
        referenceGenome = referenceGenome.substring(0, refGenomeSubstrLength);

        //simulate shotgun sequence reads
        SequenceSimulator sequenceSimulator = new ShotgunSequenceSimulator();
        int seqReadCount = Integer.parseInt(PropertyReader.getPropertyValue(Constants.SEQ_READ_LENGTH));
        int seqReadCoverage = Integer.parseInt(PropertyReader.getPropertyValue(Constants.SEQ_READ_COVERAGE));
        List<String> sequences = sequenceSimulator.generateSequences(referenceGenome, seqReadCount, seqReadCoverage);
        System.out.println("Shotgun Sequence Reads:");
        System.out.println(sequences);

        //generate Kmers
        int kmerLength = Integer.parseInt(PropertyReader.getPropertyValue(Constants.KMER_LENGTH));
        List<String> kmers = SequenceProcessorUtil.generateKmers(sequences, kmerLength);
        System.out.println("Set of Kmers:");
        System.out.println(kmers);
        DeBruijnGraph kmerGraph = new DeBruijnGraph(kmers);
        //System.out.println("Kmer graph:");
        //kmerGraph.print();
        //check Hamiltonian circuit

        //generate K - 1mers
        List<String> subkmers = SequenceProcessorUtil.generateKmers(kmers, kmerLength - 1);
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
            needlemanWunsch.geneticSimilarity();
            System.out.println("\nSequences after alignment: ");
            System.out.println("Original\t" + needlemanWunsch.getReference() + "\nComputed\t" + needlemanWunsch.getQuery());
            //System.out.println("Alignment score " + needlemanWunsch.getScore());

            //Plot the graph
            //int[][] points = {{0,0}, {1,1}, {2,2}, {3,2}, {4,2}, {5,3}, {6,4}, {6,5}, {7,6}};
            PlotGraph plotGraph = new PlotGraph();
            plotGraph.plot(needlemanWunsch.getPoints(needlemanWunsch.getQuery(), needlemanWunsch.getReference()), needlemanWunsch.getSimilarity());
        }
    }

    private static String readReferenceGenome(String fileName) throws IOException {
        String inputSequence = "";
        FileReader fileReader =
                new FileReader(fileName);
        BufferedReader bufferedReader =
                new BufferedReader(fileReader);

        String sequenceStr;
        while((sequenceStr = bufferedReader.readLine()) != null) {
            inputSequence += sequenceStr;
        }
        bufferedReader.close();
        return inputSequence;
    }
}
