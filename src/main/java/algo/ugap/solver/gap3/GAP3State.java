package algo.ugap.solver.gap3;

import java.util.ArrayList;

import algo.graph.Graph;
import algo.guided_problems.GAPGraph;
import algo.ugap.solver.UGAPState;

public class GAP3State extends UGAPState {
    public GAP3State(GAPGraph data) {
        super(data);
    }

    public GAP3State(GAPGraph data, int cyclesCount, ArrayList<Graph.Edge> resultMatching) {
        super(data, cyclesCount, resultMatching);
    }

    protected int getFullDegree() {
        return 3;
    }


    protected void setBounds() {
        int size = data.size();
        setLowerBound(cyclesCount + 3 * size / 4 );
        setUpperBound(cyclesCount + 3 * size / 2);
    }

    @Override
    protected algo.ugap.solver.Detector getDetector(GAPGraph graph, boolean isRestricted) {
        return new GAP3Detector(graph, isRestricted);
    }

    @Override
    protected UGAPState getNewState(GAPGraph data, int cyclesCount, ArrayList<Graph.Edge> resultMatching) {
        return new GAP3State(data, cyclesCount, resultMatching);
    }

}
