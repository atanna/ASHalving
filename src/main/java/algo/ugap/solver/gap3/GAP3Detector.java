package algo.ugap.solver.gap3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import algo.distance_problems.shrink.TailsEnumerator;
import algo.graph.GenomeException;
import algo.graph.Graph;
import algo.graph.Neighbours;
import algo.guided_problems.GAPGraph;
import algo.ugap.solver.Detector;

public class GAP3Detector extends Detector {
    public GAP3Detector(GAPGraph graph, boolean isRestricted) {
        super(graph, isRestricted);
    }

    @Override
    protected Branch search2() {
        Branch resultedBranch = new Branch();
        Neighbours neighbours = graph.getBaseGenome().getNeighbours();
        for (Iterator<Integer> it = neighbours.getVertices().iterator(); it.hasNext(); ) {
            int vertex = it.next();
            Map<Integer, Long> counts =
                    neighbours.getSortedVertexNeighbours(vertex)
                            .collect(Collectors.groupingBy(e -> e, Collectors.counting()));

            int targetDegree3 = -1;
            int targetDegree2 = -1;
            int targetDegree1 = -1;
            for (Map.Entry<Integer, Long> item : counts.entrySet()) {
                if (item.getValue() == 3) {
                    targetDegree3 = item.getKey();
                }
                if (item.getValue() == 2) {
                    targetDegree2 = item.getKey();
                }
                if (item.getValue() == 1) {
                    targetDegree1 = item.getKey();
                }
            }
            if (counts.size() == 1) {
                int target = targetDegree3;
                resultedBranch.merge(
                        getBranch(List.of(), Arrays.asList(new Graph.Edge(vertex, target)), 3)
                );

            }
            if (counts.size() == 2) {
                int target = targetDegree2;
                int targetTarget = -1;
                for (Iterator<Integer> iter = neighbours.getVertexNeighbours(target).iterator(); iter.hasNext(); ) {
                    targetTarget = iter.next();
//                    targetTarget = it2.next();
                    if (targetTarget != vertex) {
                        break;
                    }
                }
                resultedBranch.merge(
                        getBranch(Arrays.asList(new Graph.Edge(targetDegree1, targetTarget)), Arrays.asList(new Graph.Edge(vertex, target)), 2)
                );
            }
        }
        return resultedBranch;
    }

    @Override
    protected void updateWithExplicit4(Branch resultedBranch) {

    }

    @Override
    protected List<Branch> searchNotExplicit4() {
        return List.of();
    }

    @Override
    protected void addBranchesAlongSelfEdges(int vertex, ArrayList<Branch> result) throws GenomeException {
        for (Iterator<Integer> it = graph.getBaseGenome().getNeighbours().getVertexNeighbours(vertex).iterator(); it
                .hasNext(); ) {
            int target = it.next();
            addBranchAlongEdge(vertex, target, result);
        }
    }

    @Override
    protected void addBranchesAlongNotSelfEdges(int vertex, ArrayList<Branch> result) throws GenomeException {
        Neighbours neighbours = graph.getBaseGenome().getNeighbours();
        for (Iterator<Integer> it = neighbours.getVertices().iterator(); it.hasNext(); ) {
            int target = it.next();
            if (neighbours.hasEdge(vertex, target)) {
                continue;
            }
            addBranchAlongEdge(vertex, target, result);
        }
    }

    void addBranchAlongEdge(int vertex, int target, ArrayList<Branch> result) {
        if (vertex == target) {
            return;
        }
        Neighbours neighbours = graph.getBaseGenome().getNeighbours();
        Graph.Edge edge = new Graph.Edge(vertex, target);
        TailsEnumerator enumerator = new TailsEnumerator(
                neighbours.getVertexNeighbours(edge.source),
                neighbours.getVertexNeighbours(edge.target),
                edge
        );
        for (ArrayList<Graph.Edge> edges : enumerator.enumerate()) {
            int cycles = 0;
            List<Graph.Edge> addedEdges = new ArrayList<>();
            for (Graph.Edge addedEdge : edges) {
                if (edge.equals(addedEdge)) {
                    cycles += 1;
                } else if (addedEdge.source != vertex && addedEdge.source != target) {
                    addedEdges.add(addedEdge);
                }
            }
            result.add(getBranch(addedEdges, Collections.singletonList(edge), cycles));
        }
    }

    private Branch getBranch(List<Graph.Edge> addedEdges, List<Graph.Edge> resultedEdges, int cyclesCount)
    {
        return new Branch(GAPGraph.convertToEdgesMap(addedEdges), resultedEdges, cyclesCount);
    }

}
