package algo.ggap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.PriorityQueue;

import algo.ggap.detector.Adequate1;
import algo.ggap.detector.Adequate2;
import algo.ggap.detector.BaseDetector;
import algo.ggap.detector.BruteForce;
import algo.ggap.graph.BaseGenome;
import algo.ggap.graph.Graph;
import algo.ggap.graph.shrink.ShrinkApplyer;


public class Solver {

    public class Solution {
        private BaseGenome genome;
        private int cyclesCount;
        private ArrayList<Graph.Edge> resultMatching;
        private Counts counts;

        public class Counts {
            public int iterations = 0;
            public int bruteForceBranches = 0;
            public int terminatedBranches = 0;
            public int as1 = 0;
            public int as2 = 0;

            public Counts() {
            }

            @Override
            public String toString() {
                String result = String.join("\n", Arrays.asList(
                        "iterations: " + iterations,
                        "bruteForceBranches: " + bruteForceBranches,
                        "terminatedBranches: " + terminatedBranches,
                        "as1: " + as1,
                        "as2: " + as2
                ));
                return result;
            }
        }

        public Solution(BaseGenome genome) {
            this.genome = genome;
            cyclesCount = 0;
            resultMatching = new ArrayList<>();
            counts = new Counts();
        }

        public boolean isBetter(Solution solution) {
            return isBetter(solution.getCyclesCount());
        }

        public boolean isBetter(BaseGenome genome) {
            return isBetter(genome.getCyclesCount());
        }

        public boolean isBetter(int cyclesCount) {
            return this.cyclesCount < cyclesCount;
        }

        public int getCyclesCount() {
            return cyclesCount;
        }

        public ArrayList<Graph.Edge> getResultMatching() {
            return resultMatching;
        }

        public BaseGenome getGenome() {
            return genome;
        }

        public void update(BaseGenome genome) {
            cyclesCount = genome.getCyclesCount();
            resultMatching = new ArrayList<>(genome.result);
        }

        public Counts getCounts() {
            return counts;
        }
    }

    private Solution currentSolution;

    public Solver(BaseGenome genome) {
        currentSolution = new Solution(genome);
    }

    public int getCurrentCyclesCount() {
        return currentSolution.getCyclesCount();
    }

    public Solution solve() {
        return _solve(-1);
    }

    public Solution solve(int maxIteration) {
        return _solve(maxIteration);
    }

    private Solution _solve(int maxIteration) {

        Solution.Counts resultCounts = currentSolution.getCounts();
        PriorityQueue<BaseGenome> pq =
                new PriorityQueue<BaseGenome>(5, (a, b) -> b.getCyclesCount() - a.getCyclesCount());
        pq.add(currentSolution.getGenome());

        HashMap<Integer, Integer> counts = new HashMap<>();
        while (pq.size() > 0 && (maxIteration < 0 || resultCounts.iterations < maxIteration)) {
            resultCounts.iterations++;

            BaseGenome genome = pq.remove();

            if (currentSolution.isBetter(genome)) {
                currentSolution.update(genome);
            }

            if (getCurrentCyclesCount()
                    > genome.getCyclesCount() + genome.getNeighbours().neighbours.size() * genome.getDegree() / 2)
            {
                continue;
            }

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
                counts.put(genome.result.size(), counts.getOrDefault(genome.result.size(), 0) + 1);
                resultCounts.terminatedBranches += 1;
            }

            for (BaseDetector.Branch branch : result.branches) {
                pq.addAll(ShrinkApplyer.shrink(genome, branch.edges));
            }
        }

        return currentSolution;
    }

}
