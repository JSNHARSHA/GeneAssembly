package simulator;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Simulates the shotgun sequence reads
 */
public class ShotgunSequenceSimulator implements  SequenceSimulator{

    private Random rand;
    private Random sequenceErrorRand;
    private Random baseRand;
    private final char[] BASES = {'A', 'C', 'G', 'T'};
    public ShotgunSequenceSimulator() {
        rand = new Random();
        sequenceErrorRand = new Random();
        baseRand = new Random();
    }

    @Override
    public List<String> generateSequences(String referenceGenome, int readLength, int coverage, double error) {
        int count = getReadCount(referenceGenome.length(), readLength, coverage);
        List<String> sequences = new ArrayList<>();
        //add the prefix sequence
        sequences.add(referenceGenome.substring(0, readLength));
        //add the suffix sequence
        sequences.add(referenceGenome.substring(referenceGenome.length() - readLength,
                referenceGenome.length()));
        int i = count - 2;
        //multiplier for base error
        int errorMultiplier = getMultiplier(error);
        while (i > 0) {
            int start = rand.nextInt(referenceGenome.length() - readLength + 1);
            String randSequence = referenceGenome.substring(start, start + readLength);
            String impureRandSequence = addBaseErrorToSequence(randSequence, error, errorMultiplier);
            sequences.add(impureRandSequence);
            i--;
        }
        return sequences;
    }

    //add base error based on defined probability
    private String addBaseErrorToSequence(String randSequence, double error, int multiplier) {
        StringBuilder impureString = new StringBuilder(randSequence);
        for (int i = 0; i < impureString.length(); i++) {
            //check if base error is possible based on probability
            if(isErrorPossible(error, multiplier)) {
                char randBaseValue = BASES[baseRand.nextInt(4)];
                while (randSequence.charAt(i) == randBaseValue) {
                    randBaseValue = BASES[baseRand.nextInt(4)];
                }
                //update to another random base
                impureString.setCharAt(i, randBaseValue);
            }
        }
        return impureString.toString();
    }

    private boolean isErrorPossible(double error, int multiplier) {
        int value = (int) Math.round(error * Math.pow(10, multiplier));
        int random = sequenceErrorRand.nextInt((int)(1.0 * Math.pow(10, multiplier))) + 1;
        if (random > value) {
            return false;
        }
        return true;
    }

    //returns the precision of probability value
    private int getMultiplier(double probability) {
        //formatting the double value to decimal format
        String str = new DecimalFormat("#.####").format(probability);
        int decimalStartIndex = str.indexOf('.');
        if (decimalStartIndex >= 0) {
            return str.substring(str.indexOf('.') + 1, str.length()).length();
        }
        return 0;
    }

    /**
     * computing read count
     * @param refGenomelength
     * @param readLength
     * @param coverage
     * @return
     */
    private int getReadCount(int refGenomelength, int readLength, int coverage) {
        return (refGenomelength * coverage) / readLength;
    }
}
