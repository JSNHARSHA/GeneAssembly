package assembler;

import java.util.*;

/**
 * Sequence processor Util class
 */
public class SequenceProcessorUtil {

    /**
     * utility to generate set of Kmers from list of sequence reads
     * @param sequences
     * @param K
     * @return
     */
    public static List<String> generateKmers(List<String> sequences,  int K) {
        Set<String> kmerSet = new LinkedHashSet<String>();
        for (String sequence : sequences) {
            for (int i = 0; i < sequence.length() - K + 1; i++) {
                kmerSet.add(sequence.substring(i, i + K));
            }
        }
        return new ArrayList<>(kmerSet);
    }
}
