package algo.guided_problems;

import java.util.ArrayList;
import java.util.List;

import algo.graph.BaseGenome;
import algo.graph.GenomeException;
import algo.graph.OrdinaryGenome;

public class GGHPGraph extends GGAPGraph {
    public GGHPGraph(BaseGenome baseGenome, OrdinaryGenome guidedGenome) throws GenomeException {
        super(baseGenome, guidedGenome);
    }

    public GGHPGraph(BaseGenome baseGenome, OrdinaryGenome guidedGenome, List<Integer> indexes) throws GenomeException {
        super(baseGenome, guidedGenome, indexes);
    }

    public GGHPGraph getCopy() throws GenomeException {
        GGHPGraph graph = new GGHPGraph(baseGenome.getCopy(), guidedGenome.getCopy(), new ArrayList<>(indexes));
        return graph;
    }
}
