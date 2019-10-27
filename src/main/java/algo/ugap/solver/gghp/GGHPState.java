package algo.ugap.solver.gghp;

import java.util.ArrayList;

import algo.graph.Graph;
import algo.guided_problems.GAPGraph;
import algo.guided_problems.GGAPGraph;
import algo.guided_problems.GGHPGraph;
import algo.ugap.solver.UGAPState;

public class GGHPState extends UGAPState {
    GGHPGraph data;

    public GGHPState(GGHPGraph data) {
        super(data);
        this.data = data;
    }

    public GGHPState(GGHPGraph data, int cyclesCount, ArrayList<Graph.Edge> resultMatching) {
        super(data, cyclesCount, resultMatching);
        this.data = data;
    }

    private void init(GGHPGraph graph) {
        this.data = graph;
    }

    protected int getFullDegree() {
        // ordinary degree + baseGenome degree  = 1 + 2
        return 3;
    }


    protected void setBounds() {
        int size = data.size();
        int upperBoundEvenCyclesCount = data.getCyclesCount();
        int lowerBoundEvenCyclesCount = Math.min(upperBoundEvenCyclesCount, data.getEvenCyclesCount());
        setUpperBound(cyclesCount + 3 * size / 2 + upperBoundEvenCyclesCount);
        setLowerBound(cyclesCount + size / 2 + lowerBoundEvenCyclesCount + 1);
    }

    @Override
    protected algo.ugap.solver.Detector getDetector(GAPGraph graph, boolean isRestricted) {
        return new GGHPDetector((GGHPGraph)(graph), isRestricted);
    }

    @Override
    protected UGAPState getNewState(GAPGraph data, int cyclesCount, ArrayList<Graph.Edge> resultMatching) {
        return new GGHPState((GGHPGraph)data, cyclesCount, resultMatching);
    }

}
