package algo.guided_problems;

import java.util.ArrayList;
import java.util.List;

import algo.graph.BaseGenome;
import algo.graph.DuplicatedGenome;
import algo.graph.GenomeException;
import algo.graph.OrdinaryGenome;

public class GGHPGraph extends GGAPGraph {
    protected DuplicatedGenome baseGenome;

    public GGHPGraph(DuplicatedGenome baseGenome, OrdinaryGenome guidedGenome) throws GenomeException {
        super(baseGenome, guidedGenome);
        this.baseGenome = baseGenome;
    }

    public GGHPGraph(DuplicatedGenome baseGenome, OrdinaryGenome guidedGenome, List<Integer> indexes) throws GenomeException {
        super(baseGenome, guidedGenome, indexes);
        this.baseGenome = baseGenome;
    }

    public GGHPGraph getCopy() throws GenomeException {
        GGHPGraph graph = new GGHPGraph(baseGenome.getCopy(), guidedGenome.getCopy(), new ArrayList<>(indexes));
        return graph;
    }

    public int getEvenCyclesCount() {
        return baseGenome.getEvenCyclesCount();
    }

    public int recountEvenCyclesCount() {
        return baseGenome.recountEvenCyclesCount();
    }

    public int getCyclesCount() {
        return baseGenome.getNeighbours().getCyclesCount();
    }

    public DuplicatedGenome getBaseGenome() {
        return baseGenome;
    }
}
