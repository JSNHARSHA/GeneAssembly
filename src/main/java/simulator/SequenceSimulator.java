package simulator;

import java.util.List;

public interface SequenceSimulator {
    /**
     * Generate random sequence reads of fixed length k
     * @param referenceGenome
     * @param readLength
     * @param coverage
     * @return
     */
    List<String> generateSequences(String referenceGenome, int readLength, int coverage);
}
