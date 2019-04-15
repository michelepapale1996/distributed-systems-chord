package chord;

public class Ring {
    private int id;
    private boolean simpleLookupAlgorithm;
    private int num_bits_identifiers;

    public Ring(boolean simpleLookupAlgorithm, int num_bits_identifiers) {
        this.simpleLookupAlgorithm = simpleLookupAlgorithm;
        this.num_bits_identifiers = num_bits_identifiers;
    }

    public int getNum_bits_identifiers() {
        return num_bits_identifiers;
    }

    public boolean isSimpleLookupAlgorithm() {
        return simpleLookupAlgorithm;
    }
}
