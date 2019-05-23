package algo.distance_problems.detector;

import java.util.Arrays;
import java.util.HashSet;

import algo.graph.BaseGenome;
import algo.graph.Neighbours;

public class Adequate2 extends BaseDetector {

    public static Result search(BaseGenome genome) {
        return new Result();
    }

    public static Result search_(BaseGenome genome) {
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
                if (used.contains(target)) {
                    continue;
                }
                for (Object objTarget2 : neighbours.getSortedVertexNeighbours(vertex).toArray()) {
                    int target2 = (int) (objTarget2);
                    if (used.contains(target2)) {
                        continue;
                    }
                    for (Object objVertex3 : neighbours.getSortedVertexNeighbours(target).toArray()) {
                        int vertex2 = (int)(objVertex3);
                        if (used.contains(vertex2)) {
                            continue;
                        }
                        if (neighbours.hasEdge(vertex2, target2)) {
                            used.addAll(Arrays.asList(vertex, vertex2, target, target2));
                            Branch newBranch = result.addNewBranch();
                            newBranch.add(vertex, target);
                            newBranch.add(target2, vertex2);
                        }
                    }
                }
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
