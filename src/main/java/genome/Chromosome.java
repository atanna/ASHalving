package genome;

import java.util.ArrayList;
import java.util.List;


public class Chromosome {
    final private List<DirectedGene> genes;
    final private boolean cyclical;

    public Chromosome(ArrayList<DirectedGene> genes, boolean cyclical) {
        this.genes = genes;
        this.cyclical = cyclical;
    }

    public int getSize() {
        return genes.size();
    }

    public List<DirectedGene> getGenes() {
        return genes;
    }

    public boolean isCyclical() {
        return cyclical;
    }
}
