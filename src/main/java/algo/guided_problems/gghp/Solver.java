package algo.guided_problems.gghp;


import java.util.ArrayList;
import java.util.List;

import algo.solver.BaseDetector;
import algo.distance_problems.detector.DetectorException;
import algo.graph.GenomeException;
import algo.graph.Graph;
import algo.guided_problems.GGHPGraph;
import algo.guided_problems.gghp.detector.Detector;
import algo.solver.ParallelSolver;
import algo.solver.State;


//public class Solver extends BaseSolver<GGHPGraph> {
public class Solver extends ParallelSolver<GGHPGraph> {
    public int firstBruteForce = 0;

    public Solver(GGHPGraph data) {
        super(data);
    }

    @Override
    protected void setBoundsToState(State<GGHPGraph> state) {

    }

    @Override
    protected boolean isMissState(State<GGHPGraph> state) {
        if (super.isMissState(state)) {
//            System.out.println(" -- missed " + state.data.size());
            return true;
        }
        boolean result = state.cyclesCount + state.data.size() * 3 / 2 < getCurrentSolution().getCyclesCount();
        if (result) {
//            System.out.println(" -- strange option");
        }
        return result;
    }

    @Override
    protected int getSize() {
        return data.size();
    }

    @Override
    protected int getFullDegree() {
        // ordinary degree + baseGenome degree  = 1 + 2
        return 3;
    }

    @Override
    public List<State<GGHPGraph>> computeNextStates(State<GGHPGraph> state) throws Exception {
        ArrayList<State<GGHPGraph>> states = new ArrayList<>();
        List<BaseDetector.Branch> branches = new ArrayList<>();
        while (state.data.size() > 0) {
            Detector detector = new Detector(state.data, isRestricted);
            branches = detector.search();
            if (branches.size() == 1) {
                // explicit case
                BaseDetector.Branch branch = branches.get(0);
//                if (state.data.size() > 20) {
//                    System.out.println(
//                            "explicit " + state.data.size() + "  - " + (branch.getResultedEdges().size() * 2) + " +"
//                                    + branch.getCyclesCount() + " cycles");
//                }
                state.cyclesCount += branches.get(0).getCyclesCount();
                state.resultMatching.addAll(state.data.convertToRealEdges(branch.getResultedEdges()));
                state.data.lazyReconstruct(branches.get(0));

            } else if (branches.size() == 0) {
                throw new DetectorException("Detector found nothing");
            } else {
                // branches size > 1
                break;
            }
        }
        if (state.data.size() == 0) {
            state.data.pushReconstruction();
            if (currentSolution.isBetter(state.cyclesCount)) {
                currentSolution.update(state);
            }
        }
        if (branches.size() > 1) {
            if (firstBruteForce == 0 || firstBruteForce < state.data.size()) {
                firstBruteForce = state.data.size();
                System.out.println("Size = " + state.data.size() + " first brute force");
            }
            state.data.pushReconstruction();
            Detector detector = new Detector(state.data, isRestricted);
            branches = detector.search();

            for (BaseDetector.Branch branch : branches) {
                GGHPGraph newGraph = state.data.getCopy();
                ArrayList<Graph.Edge> newResultMatching = new ArrayList<>(state.resultMatching);
                newResultMatching.addAll(newGraph.convertToRealEdges(branch.getResultedEdges()));
                newGraph.lazyReconstruct(branch);
                newGraph.pushReconstruction();
                State newState = new State(newGraph, state.cyclesCount + branch.getCyclesCount(), newResultMatching);
                setBounds(newState);
                states.add(newState);
            }
        }

//        System.out.println();
//        for (State st : states) {
//            System.out.println(state.resultMatching.size() + " " + st.resultMatching.size());
//        }

//        if (states.size() > 1) {
//            branchesCounter.getAndIncrement(state.resultMatching.size());
//        }
        return states;
    }

    @Override
    public State<GGHPGraph> getFirstState() throws GenomeException {
        State<GGHPGraph> state = new State<>(data.getCopy(), 0 ,new ArrayList<>());
        setBounds(state);
        return state;
    }

    private void setBounds(State<GGHPGraph> state) throws GenomeException {
        int size = state.data.size();
//        int evenCyclesCount = state.data.recountEvenCyclesCount();
        int upperBoundEvenCyclesCount = state.data.getCyclesCount();
        int lowerBoundEvenCyclesCount = Math.min(upperBoundEvenCyclesCount, state.data.getEvenCyclesCount());
        state.setUpperBound(state.cyclesCount + 3 * size / 2 + upperBoundEvenCyclesCount);
        state.setLowerBound(state.cyclesCount + size / 2 + lowerBoundEvenCyclesCount + 1);


//        state.setUpperBound(state.cyclesCount + 3 * size / 2 + evenCyclesCount);
//        state.setLowerBound(state.cyclesCount + size / 2 + evenCyclesCount + 1);
    }

}

