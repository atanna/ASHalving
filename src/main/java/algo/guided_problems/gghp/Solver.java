package algo.guided_problems.gghp;


import java.util.ArrayList;
import java.util.List;

import algo.distance_problems.detector.BaseAdequateSubgraph;
import algo.distance_problems.detector.DetectorException;
import algo.graph.GenomeException;
import algo.graph.Graph;
import algo.guided_problems.GGHPGraph;
import algo.guided_problems.gghp.detector.Detector;
import algo.solver.BaseSolver;
import algo.solver.State;


public class Solver extends BaseSolver<GGHPGraph> {


    public Solver(GGHPGraph data) {
        super(data);
    }

    @Override
    public List<State<GGHPGraph>> computeNextStates(State<GGHPGraph> state) throws Exception {
        ArrayList<State<GGHPGraph>> states = new ArrayList<>();
        ArrayList<BaseAdequateSubgraph.Branch> branches = new ArrayList<>();
        while (state.data.size() > 0) {
            Detector detector = new Detector(state.data);
            branches = detector.search();
            if (branches.size() == 1) {
                // explicit case
                BaseAdequateSubgraph.Branch branch = branches.get(0);
                state.cyclesCount += branches.get(0).getCyclesCount();
                state.resultMatching.addAll(state.data.convertToRealEdges(branch.getResultedEdges()));
                state.data.lazyReconstruct(branches.get(0));

            } else {
                break;
            }
            if (branches.size() == 0) {
                throw new DetectorException("Detector found nothing");
            }
        }
        if (state.data.size() == 0) {
            state.data.pushReconstruction();
            if (currentSolution.isBetter(state.cyclesCount)) {
                currentSolution.update(state);
            }
        }
        if (branches.size() > 1) {
            for (BaseAdequateSubgraph.Branch branch : branches) {
                GGHPGraph newGraph = state.data.getCopy();
                ArrayList<Graph.Edge> newResultMatching = new ArrayList<>(state.resultMatching);
                newResultMatching.addAll(newGraph.convertToRealEdges(branch.getResultedEdges()));
                newGraph.lazyReconstruct(branch);
                newGraph.pushReconstruction();
                State newState = new State(newGraph, state.cyclesCount + branch.getCyclesCount(), newResultMatching);
                states.add(newState);
            }
        }
        return states;
    }

    @Override
    public State<GGHPGraph> getFirstState() throws GenomeException {
        return new State<GGHPGraph>(data.getCopy(), 0, new ArrayList<>());
    }

}

