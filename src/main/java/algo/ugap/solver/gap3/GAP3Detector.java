package algo.ugap.solver.gap3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import algo.ugap.graph.GenomeException;
import algo.ugap.graph.Graph;
import algo.ugap.graph.Neighbours;
import algo.ugap.graph.GAPGraph;
import algo.ugap.solver.Detector;

public class GAP3Detector extends Detector {
    public GAP3Detector(GAPGraph graph, boolean isRestricted) {
        super(graph, isRestricted);
    }

    @Override
    public Branch search2() {
        Branch resultedBranch = new Branch();
        Neighbours neighbours = graph.getBaseGenome().getNeighbours();
        HashSet<Integer> verticesWithLoops = new HashSet<>();
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
                int targetTarget = -1;
                if (vertex == targetDegree2) {
                    if (verticesWithLoops.contains(targetDegree1)) {
                        resultedBranch.merge(
                                getBranch(List.of(), Arrays.asList(new Graph.Edge(targetDegree1, vertex)), 2)
                        );
                    } else {
                        verticesWithLoops.add(vertex);
                    }
                } else {
                    for (Iterator<Integer> iter = neighbours.getVertexNeighbours(targetDegree2).iterator();
                            iter.hasNext(); ) {
                        targetTarget = iter.next();
                        if (targetTarget != vertex) {
                            break;
                        }
                    }
                    resultedBranch.merge(
                            getBranch(Arrays.asList(new Graph.Edge(targetDegree1, targetTarget)),
                                    Arrays.asList(new Graph.Edge(vertex, targetDegree2)), 2)
                    );
                }
            }
        }
        return resultedBranch;
    }

    @Override
    protected void updateWithExplicit4(Branch resultedBranch) {
        // There are 4 explicit not isomorphic cases: 1, 2, 4, 5
        // H = (V,E), V = {a, b, c, d}
        // 1. E = {aa, bb, cc, ad, bd, cd}
        // 2. E = {dd, cd, cb, ca, ab}
        // 3. E = {ab, bc, cd, ad}
        // 4. E = {ab, bc, cd, ad, cb}
        // 5. E = {ab, bc, cd, ad, cb, ab}
        Neighbours neighbours = graph.getBaseGenome().getNeighbours();
        for (Iterator<Integer> it = neighbours.getVertices().iterator(); it.hasNext(); ) {
            int vertex = it.next();
            HashSet<Integer> neighboursDepth2 = new HashSet<>();
            neighbours.getVertexNeighbours(vertex).forEach(neighboursDepth2::add);
            HashSet<Integer> verticesWithLoops = new HashSet<>();
            for (Iterator<Integer> itTarget = neighbours.getVertexNeighbours(vertex).iterator(); itTarget.hasNext();) {
                int target = itTarget.next();
                neighboursDepth2.add(target);
                neighbours.getVertexNeighbours(target).forEach(v->{
                    neighboursDepth2.add(v);
                    if (v == target) {
                        verticesWithLoops.add(v);
                    }
                });
            }
            if (neighboursDepth2.size() == 4) {
                // cases 1, 5
                int cyclesCount = 4;
                if (verticesWithLoops.size() > 0) {
                    // case 1
                    cyclesCount = 3;
                }
                Integer[] nbrs = neighboursDepth2.toArray(new Integer[0]);
                resultedBranch.merge(getBranch(
                        List.of(),
                        Arrays.asList(
                                new Graph.Edge(nbrs[0], nbrs[1]),
                                new Graph.Edge(nbrs[2], nbrs[3])),
                        cyclesCount));
            } else if (neighboursDepth2.size() == 6) {
                // cases 2, 4
                if (verticesWithLoops.size() == 1) {
                    // case 2
                    int loopVertex = verticesWithLoops.iterator().next();
                    Optional<Integer> vertexA = Optional.empty();
                    Optional<Integer> vertexB = Optional.empty();
                    for (Iterator<Integer> itTarget = neighbours.getVertexNeighbours(vertex).iterator(); itTarget.hasNext();) {
                        int target = itTarget.next();
                        if (target == loopVertex) {
                            continue;
                        }
                        if (!vertexA.isPresent()) {
                            vertexA = Optional.of(target);
                        } else {
                            vertexB = Optional.of(target);
                        }
                    }
                    if (!(vertexA.isPresent() && vertexB.isPresent())) {
                        continue;
                    }
                    if (neighbours.getVertexNeighbours(vertexA.get())
                            .anyMatch(vertexB.get()::equals)) {
                        neighboursDepth2.removeAll(
                                Arrays.asList(vertex, loopVertex, vertexA.get(), vertexB.get())
                        );
                        if (neighboursDepth2.size() != 2) {
                            continue;
                        }
                        // outer adequate graph neighbours
                        Integer[] outerVertices = neighboursDepth2.toArray(new Integer[0]);
                        resultedBranch.merge(getBranch(
                                Arrays.asList(
                                        new Graph.Edge(outerVertices[0], outerVertices[1])),
                                Arrays.asList(
                                        new Graph.Edge(vertex, loopVertex),
                                        new Graph.Edge(vertexA.get(), vertexB.get())),
                                3
                        ));
                    }

                }
            } else {
                continue;
            }
        }
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
