package algo.ugap.solver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import algo.distance_problems.detector.DetectorException;

import algo.graph.Graph;
import algo.guided_problems.GAPGraph;
import algo.solver.BaseDetector;

public abstract class UGAPState extends State {
    protected GAPGraph data;

    public UGAPState(GAPGraph data) {
        super();
        init(data);
    }

    public UGAPState(GAPGraph data, int cyclesCount, ArrayList<Graph.Edge> resultMatching) {
        super(cyclesCount, resultMatching);
        init(data);
    }

    private void init(GAPGraph graph) {
        this.data = graph;
    }

    protected boolean isMissState(int bestCyclesCount, int lowerBound, int upperBound) {
        boolean result = (cyclesCount + data.size() * getFullDegree() / 2 < bestCyclesCount);
        return result;
    }

    public int getSize() {
        return data.size();
    }

    protected int getFullDegree() {
        return data.getFullDegree();
    }

    @Override
    public List<State> computeNextStates(boolean isRestricted) {
        try {


            ArrayList<State> states = new ArrayList<>();
            List<algo.solver.BaseDetector.Branch> branches = new ArrayList<>();
            if (data.size() == 0) {
                return List.of();
            }
            while (data.size() > 0) {
                Detector detector = getDetector(data, isRestricted);
                branches = detector.search();
                if (branches.size() == 1) {
                    // explicit case
                    System.out.println("Explicit case" + branches.get(0).getResultedEdges());
                    algo.solver.BaseDetector.Branch branch = branches.get(0);

                    cyclesCount += branches.get(0).getCyclesCount();
                    resultMatching.addAll(data.convertToRealEdges(branch.getResultedEdges()));
                    data.lazyReconstruct(branches.get(0));

                } else if (branches.size() == 0) {
                    throw new DetectorException("Detector found nothing");
                } else {
                    // branches size > 1
                    break;
                }
            }
            if (data.size() == 0) {
                data.pushReconstruction();
                return Arrays.asList(this);
            }
            if (branches.size() > 1) {
                data.pushReconstruction();
                Detector detector = getDetector(data, isRestricted);
                branches = detector.search();

                for (BaseDetector.Branch branch : branches) {
                    GAPGraph newGraph = data.getCopy();
                    ArrayList<Graph.Edge> newResultMatching = new ArrayList<>(resultMatching);
                    newResultMatching.addAll(newGraph.convertToRealEdges(branch.getResultedEdges()));
                    newGraph.lazyReconstruct(branch);
                    newGraph.pushReconstruction();
                    UGAPState newState = getNewState(newGraph, cyclesCount + branch.getCyclesCount(), newResultMatching);
                    newState.setBounds();
                    states.add(newState);
                }
            }

            return states;
        } catch ( Exception e) {
            e.printStackTrace();
        }
        return List.of();
    }

    protected abstract void setBounds();

    protected abstract Detector getDetector(GAPGraph graph, boolean isRestricted);

    protected abstract UGAPState getNewState(GAPGraph data, int cyclesCount, ArrayList<Graph.Edge> resultMatching);

}