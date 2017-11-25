package assembler;

public interface SequenceAssembler {
    /**
     *  generate assembled sequences from graph
     * @param graph
     * @return
     */
    String assemble(DeBruijnGraph graph);
}
