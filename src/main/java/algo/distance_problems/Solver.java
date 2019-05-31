package algo.distance_problems;

import java.util.ArrayList;
import java.util.List;

import algo.distance_problems.detector.Adequate1;
import algo.distance_problems.detector.Adequate2;
import algo.distance_problems.detector.BaseDetector;
import algo.distance_problems.detector.BruteForce;
import algo.graph.BaseGenome;
import algo.graph.GenomeException;
import algo.distance_problems.shrink.ShrinkApplyer;
import algo.solver.BaseSolver;
import algo.solver.State;


public class Solver extends BaseSolver<BaseGenome> {

    public Solver(BaseGenome genome) {
        super(genome);
    }

    @Override
    public List<State<BaseGenome>> computeNextStates(State<BaseGenome> state)
            throws GenomeException
    {
        BaseGenome genome = state.data;

        BaseDetector.Result result = Adequate1.search(genome);
        if (result.getSize() > 0) {
            resultCounts.as1 += 1;
        }

        if (result.getSize() == 0) {
            result = Adequate2.search(genome);
            if (result.getSize() > 0) {
                resultCounts.as2 += 1;
            }
        }

        if (result.getSize() == 0) {
            result = BruteForce.search(genome);
            if (result.getSize() > 0) {
                resultCounts.bruteForceBranches += 1;
            }
        }

        if (result.getSize() == 0) {
            resultCounts.terminatedBranches += 1;
        }

        ArrayList<State<BaseGenome>> newStates = new ArrayList<>();
        for (BaseDetector.Branch branch : result.branches) {
            newStates.addAll(ShrinkApplyer.shrink(state, branch));
        }
        return newStates;
    }

    protected boolean isMissState(State<BaseGenome> state) {
        return getCurrentCyclesCount()
                > state.cyclesCount + state.data.getNeighbours().neighbours.size() * state.data.getDegree() / 2;
    }


}
