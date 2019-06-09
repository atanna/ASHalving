package algo.distance_problems.detector;

import algo.graph.BaseGenome;

public class BruteForce extends BaseDetector {

    public static Result search(BaseGenome genome) {
        int verticesCount = genome.getNeighbours().neighbours.size();
        Result result = new Result();
        for (int i = 1; i < verticesCount; ++i) {
            if (!genome.getNeighbours().hasEdge(0, i)) {
//                continue;
            }
            result.addNewBranch().add(0, i);
        }

        return result;
//        return search(genome.getNeighbours().neighbours.size());
    }

    private static Result search(int verticesCount) {
        Result result = new Result();
        for (int i = 1; i < verticesCount; ++i) {
            result.addNewBranch().add(0, i);
        }

        return result;
    }

}
