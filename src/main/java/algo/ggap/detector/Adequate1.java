package algo.ggap.detector;

import java.util.HashSet;

import algo.ggap.graph.BaseGenome;
import algo.ggap.graph.Neighbours;

public class Adequate1 extends BaseDetector {

    public static Result search(BaseGenome genome) {
        Result result = new Result();
        Branch branch = new Branch();
        Neighbours neighbours = genome.getNeighbours();
        HashSet<Integer> used = new HashSet<>();
        for (int vertex = 0; vertex < neighbours.neighbours.size(); ++vertex) {
            int previousTarget = -1;
            if (used.contains(vertex)) {
                continue;
            }
            for (Object objTarget : neighbours.getSortedVertexNeighbours(vertex).toArray()) {
                int target = (int) (objTarget);
                if (target == previousTarget && target > vertex && !used.contains(target)) {
                    branch.add(vertex, target);
                    used.add(vertex);
                    used.add(target);
                    break;
                }
                previousTarget = target;
            }
        }
        if (branch.edges.size() > 0) {
            result.addBranch(branch);
        }
        return result;
    }

}
