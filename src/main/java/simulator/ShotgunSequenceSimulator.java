package simulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Simulates the shotgun sequence reads
 */
public class ShotgunSequenceSimulator implements  SequenceSimulator{

    @Override
    public List<String> generateSequences(String referenceGenome, int readLength, int count) {
        List<String> sequences = new ArrayList<>();
        Random rand = new Random();
        //add the prefix sequence
        sequences.add(referenceGenome.substring(0, readLength));
        //add the suffix sequence
        sequences.add(referenceGenome.substring(referenceGenome.length() - readLength,
                referenceGenome.length()));
        int i = count - 2;
        while (i > 0) {
            int start = rand.nextInt(referenceGenome.length() - readLength + 1);
            String randSequence = referenceGenome.substring(start, start + readLength);
            sequences.add(randSequence);
            i--;
        }
        return sequences;
    }
}
