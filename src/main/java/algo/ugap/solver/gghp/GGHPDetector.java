package algo.ugap.solver.gghp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import algo.ugap.graph.GenomeException;
import algo.ugap.graph.Graph;
import algo.ugap.graph.Neighbours;
import algo.ugap.graph.OrdinaryGenome;
import algo.ugap.graph.TwoRegularNeighbours;
import algo.ugap.solver.Detector;


public class GGHPDetector extends Detector {
    private GGHPGraph graph;

    public GGHPDetector(GGHPGraph graph, boolean isRestricted) {
        super(graph, isRestricted);
        this.graph = graph;
    }

    @Override
    protected Branch search2() {
        // Search non-intersecting adequate subgraphs H with |V(H)| = 2 and merge its in one branch.
        // H = (V,E join E_guideed), V = {a,b}
        // There are 4 cases:
        // 1. E = {(a,b)},  E_guided = {(a, b)}
        // 2. E = {(a,b), (a,b)}, E_guided = {}
        // 3. E = {(a,b), (a,b)}, E_guided = {(a,b)}
        // 4. E = {(a,a), {b,b}}. E_guided = {(a,b)}
        // (All cases are symmetric by a, b)

        try {
            Branch resultedBranch = new Branch();
            Neighbours neighbours = graph.getBaseGenome().getNeighbours();
            OrdinaryGenome guidedGenome = graph.getGuidedGenome();
            for (int vertex = 0; vertex < neighbours.neighbours.size(); vertex++) {
                if (neighbours.removedVertices.contains(vertex)) {
                    continue;
                }
                Map<Integer, Long> counts =
                        neighbours.getSortedVertexNeighbours(vertex)
                                .collect(Collectors.groupingBy(e -> e, Collectors.counting()));

                int guidedTarget = guidedGenome.getNeighbour(vertex);
                ;

                if (counts.size() == 1) {
                    // 2, 3, 4
                    int target = -1;
                    for (Map.Entry<Integer, Long> item : counts.entrySet()) {
                        target = item.getKey();
                    }
                    if (target == vertex) {
                        Integer[] targetsGuidedTarget =
                                neighbours.getSortedVertexNeighbours(guidedTarget).toArray(Integer[]::new);
                        if (targetsGuidedTarget[0] == guidedTarget) {
                            // 4
                            resultedBranch
                                    .merge(getBranch(List.of(), List.of(),
                                            Arrays.asList(new Graph.Edge(vertex, guidedTarget)),
                                            2));
                            continue;
                        }

                    } else if (target == guidedTarget) {
                        // 3
                        Branch branch =
                                getBranch(List.of(), List.of(), Arrays.asList(new Graph.Edge(vertex, target)), 3);
                        resultedBranch.merge(branch);
                        continue;
                    } else {
                        // 2
                        int guidedTargetTarget = guidedGenome.getNeighbour(target);
                        Branch branch =
                                getBranch(List.of(), Arrays.asList(new Graph.Edge(guidedTarget, guidedTargetTarget)),
                                        Arrays.asList(new Graph.Edge(vertex, target)), 2);
                        resultedBranch.merge(branch);
                        continue;
                    }
                } else if (counts.size() == 2) {
                    // 1
                    int firstTarget = -1;
                    int secondTarget = -1;
                    for (Map.Entry<Integer, Long> item : counts.entrySet()) {
                        if (firstTarget == -1) {
                            firstTarget = item.getKey();
                        } else {
                            secondTarget = item.getKey();
                        }
                    }
                    if (firstTarget > secondTarget) {
                        int tmp = firstTarget;
                        firstTarget = secondTarget;
                        secondTarget = tmp;
                    }

                    if (guidedTarget == firstTarget || guidedTarget == secondTarget) {
                        // 1
                        if (guidedTarget != firstTarget) {
                            int tmp = firstTarget;
                            firstTarget = secondTarget;
                            secondTarget = tmp;
                        }
                        AtomicInteger targetTarget = new AtomicInteger(-1);
                        int finalVertex = vertex;
                        neighbours.getVertexNeighbours(guidedTarget).forEach(
                                v -> {
                                    if (v != finalVertex) {
                                        targetTarget.set(v);
                                    }
                                }
                        );
                        Graph.Edge edge = new Graph.Edge(secondTarget, targetTarget.get());
                        Branch branch = getBranch(Arrays.asList(edge), List.of(),
                                Arrays.asList(new Graph.Edge(vertex, firstTarget)), 2);
                        resultedBranch.merge(branch);
                        continue;
                    }

                } else {
    //                    throw new DetectorException("Every vertex must have two neighbours");
                }
            }
            return resultedBranch;
        }  catch (GenomeException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void updateWithExplicit4(Branch resultedBranch) {
        // There are 7 explicit not isomorphic cases: 2, 4, 5, 6, 7, 8, 9
        // H = (V,E join E_guideed), V = {a, b, c, d}
        // 1. E = {ab, bd, dc, ca}, E_guided = {}
        // 2. E = {ab, bd, dc, ca}, E_guided = {bc}
        // 3. E = {ac, bd}, E_guided = {ab, cd}
        // 4. E = {ab, bc,ac}, E_guided = {cd}
        // 5. E = {ab, bd, dc, ca}, E_guided = {cb, ad}
        // 6. E = {ac, cb, bd}, E_guided = {ab, cd}
        // 7. E = {aa, bb, cd}, E_guided = {ac, bd}
        // 8. E = {ac, ba, db}, E_guided = {cd}
        // 9. E = {ac, bc}, E_guided = {ab, cd}

        try {
            OrdinaryGenome guidedGenome = graph.getGuidedGenome();
            TwoRegularNeighbours neighbours = graph.getBaseGenome().getNeighbours();
            HashSet<Integer> processed = new HashSet<>();

            for (Iterator<Integer> it = neighbours.getVertices().iterator(); it.hasNext(); ) {
                int vertex = it.next();
                if (processed.contains(vertex)) {
                    continue;
                }

                int guidedVertex = guidedGenome.getNeighbour(vertex);

                int prevVertex = neighbours.getNextCycleVertex(vertex, vertex);
                ;
                int guidedPrev = guidedGenome.getNeighbour(prevVertex);
                int prevPrevVertex = neighbours.getNextCycleVertex(vertex, prevVertex);
                int guidedPrevPrev = guidedGenome.getNeighbour(prevPrevVertex);

                int nextVertex = neighbours.getNextCycleVertex(prevVertex, vertex);
                int guidedNext = guidedGenome.getNeighbour(nextVertex);

                int nextNextVertex = neighbours.getNextCycleVertex(vertex, nextVertex);
                int guidedNextNext = guidedGenome.getNeighbour(nextNextVertex);

                if (nextVertex == vertex || nextNextVertex == vertex) {
                    // cycle size = 1 || 2
                    processed.add(vertex);
                    processed.add(nextVertex);
                    continue;
                }
                if (prevPrevVertex == nextVertex) {
                    // cycle size = 3
                    processed.add(prevVertex);
                    processed.add(vertex);
                    processed.add(nextVertex);
                    // check 4
                    if (guidedNext == neighbours.getNextCycleVertex(guidedNext, guidedNext)) {
                        // 4
                        Graph.Edge edge = new Graph.Edge(guidedPrev, guidedVertex);
                        Branch branch = getBranch(List.of(), List.of(edge),
                                Arrays.asList(new Graph.Edge(prevVertex, vertex),
                                        new Graph.Edge(prevPrevVertex, guidedPrevPrev)), 3);
                        resultedBranch.merge(branch);
                    }
                    continue;
                }
                if (prevPrevVertex == nextNextVertex) {
                    // cycle size = 4
                    processed.add(prevPrevVertex);
                    processed.add(prevVertex);
                    processed.add(vertex);
                    processed.add(nextVertex);
                    // check 2, 5
                    if (guidedPrevPrev == vertex && guidedPrev == nextVertex) {
                        // 5
                        Branch branch = getBranch(List.of(), List.of(),
                                Arrays.asList(new Graph.Edge(prevVertex, vertex),
                                        new Graph.Edge(prevPrevVertex, nextVertex)), 4);
                        resultedBranch.merge(branch);
                    } else if (guidedPrevPrev == vertex) {
                        // 2
                        Branch branch =
                                getBranch(List.of(), Arrays.asList(new Graph.Edge(guidedVertex, guidedPrevPrev)),
                                        Arrays.asList(new Graph.Edge(prevVertex, vertex),
                                                new Graph.Edge(prevPrevVertex, nextVertex)), 3);
                        resultedBranch.merge(branch);
                    } else if (guidedPrev == nextVertex) {
                        // sim 2
                        Branch branch = getBranch(List.of(), Arrays.asList(new Graph.Edge(guidedPrev, guidedNext)),
                                Arrays.asList(new Graph.Edge(prevVertex, vertex),
                                        new Graph.Edge(prevPrevVertex, nextVertex)), 3);
                        resultedBranch.merge(branch);
                    }
                    continue;
                }

                while (!processed.contains(vertex)) {
                    processed.add(vertex);


                    if (guidedPrev == nextNextVertex) {
                        // 8
                        Graph.Edge newBaseEdge = new Graph.Edge(prevPrevVertex,
                                neighbours.getNextCycleVertex(nextVertex, nextNextVertex));
                        Graph.Edge newGuidedEdge = new Graph.Edge(guidedVertex, guidedNext);
                        Branch branch = getBranch(Arrays.asList(newBaseEdge), Arrays.asList(newGuidedEdge),
                                Arrays.asList(new Graph.Edge(prevVertex, vertex),
                                        new Graph.Edge(nextVertex, nextNextVertex)), 3);
                        resultedBranch.merge(branch);
                    } else if (guidedNext == prevVertex) {
                        // check 6, 9
                        if (guidedNextNext == vertex) {
                            // 6
                            Graph.Edge newEdge = new Graph.Edge(prevPrevVertex,
                                    neighbours.getNextCycleVertex(nextVertex, nextNextVertex));
                            Branch branch = getBranch(Arrays.asList(newEdge), List.of(),
                                    Arrays.asList(new Graph.Edge(prevVertex, vertex),
                                            new Graph.Edge(nextVertex, nextNextVertex)), 3);
                            resultedBranch.merge(branch);
                        } else {
                            int v = neighbours.getNextCycleVertex(guidedVertex, guidedVertex);
                            if (v == guidedVertex) {
                                // 9
                                Graph.Edge newEdge = new Graph.Edge(prevPrevVertex, nextNextVertex);
                                Branch branch = getBranch(Arrays.asList(newEdge), List.of(),
                                        Arrays.asList(new Graph.Edge(prevVertex, nextVertex),
                                                new Graph.Edge(vertex, guidedVertex)), 3);
                                resultedBranch.merge(branch);
                            }
                        }
                    }

                    prevPrevVertex = prevVertex;
                    guidedPrevPrev = guidedPrev;
                    prevVertex = vertex;
                    guidedPrev = guidedVertex;
                    vertex = nextVertex;
                    guidedVertex = guidedNext;
                    nextVertex = nextNextVertex;
                    guidedNext = guidedNextNext;
                    nextNextVertex = neighbours.getNextCycleVertex(vertex, nextVertex);
                    guidedNextNext = guidedGenome.getNeighbour(nextNextVertex);
                }
            }
        } catch (GenomeException e) {
            e.printStackTrace();
        }


        // TODO : write this
    }


    protected List<Branch> searchNotExplicit4() {
        // 1, 3 from previous description
        try {
            ArrayList<Branch> result = new ArrayList<>();
            OrdinaryGenome guidedGenome = graph.getGuidedGenome();
            TwoRegularNeighbours neighbours = graph.getBaseGenome().getNeighbours();
            HashSet<Integer> processed = new HashSet<>();

            for (Iterator<Integer> it = neighbours.getVertices().iterator(); it.hasNext(); ) {
                int vertex = it.next();
                if (processed.contains(vertex)) {
                    continue;
                }

                int guidedVertex = guidedGenome.getNeighbour(vertex);

                int prevVertex = neighbours.getNextCycleVertex(vertex, vertex);
                ;
                int guidedPrev = guidedGenome.getNeighbour(prevVertex);
                int prevPrevVertex = neighbours.getNextCycleVertex(vertex, prevVertex);
                int guidedPrevPrev = guidedGenome.getNeighbour(prevPrevVertex);

                int nextVertex = neighbours.getNextCycleVertex(prevVertex, vertex);
                int guidedNext = guidedGenome.getNeighbour(nextVertex);

                int nextNextVertex = neighbours.getNextCycleVertex(vertex, nextVertex);
                int guidedNextNext = guidedGenome.getNeighbour(nextNextVertex);

                if (prevPrevVertex == nextNextVertex) {
                    processed.add(prevVertex);
                    processed.add(vertex);
                    processed.add(nextVertex);
                    processed.add(nextNextVertex);
                    continue;
                    // cycle size = 4
                    // 1
    //                List<Graph.Edge> newGuidedEdges = Arrays.asList(new Graph.Edge(guidedPrevPrev, guidedNext), new Graph.Edge(guidedPrev, guidedVertex));
    //                result.add(getBranch(List.of(), newGuidedEdges, Arrays.asList(new Graph.Edge(prevPrevVertex, nextVertex), new Graph.Edge(prevVertex, vertex)), 3));
    //
    //                List<Graph.Edge> newGuidedEdges2 = Arrays.asList(new Graph.Edge(guidedPrevPrev, guidedPrev), new Graph.Edge(guidedNext, guidedVertex));
    //                result.add(getBranch(List.of(), newGuidedEdges2, Arrays.asList(new Graph.Edge(prevPrevVertex, prevVertex), new Graph.Edge(nextVertex, vertex)), 3));
    //
    //                return result;
                }

                while (!processed.contains(vertex)) {
                    processed.add(vertex);


                    if (neighbours.hasEdge(guidedPrev, guidedVertex)) {

                        // 3
                        int x = prevPrevVertex;
                        int z = nextVertex;
                        int y = neighbours.getNextCycleVertex(guidedPrev, guidedVertex);
                        int w = neighbours.getNextCycleVertex(guidedVertex, guidedPrev);
                        List<Graph.Edge> newBaseEddes = Arrays.asList(new Graph.Edge(x, z), new Graph.Edge(y, w));
                        result.add(getBranch(newBaseEddes, List.of(), Arrays.asList(new Graph.Edge(prevVertex, vertex),
                                new Graph.Edge(guidedPrev, guidedVertex)), 3));
                        List<Graph.Edge> newBaseEddes2 = Arrays.asList(new Graph.Edge(x, y), new Graph.Edge(z, w));
                        result.add(getBranch(newBaseEddes2, List.of(),
                                Arrays.asList(new Graph.Edge(prevVertex, guidedPrev),
                                        new Graph.Edge(vertex, guidedVertex)), 3));
                        return result;
                    }
                    prevPrevVertex = prevVertex;
                    prevVertex = vertex;
                    guidedPrev = guidedVertex;
                    vertex = nextVertex;
                    guidedVertex = guidedNext;
                    nextVertex = nextNextVertex;
                    guidedNext = guidedNextNext;
                    nextNextVertex = neighbours.getNextCycleVertex(vertex, nextVertex);
                    guidedNextNext = guidedGenome.getNeighbour(nextNextVertex);
                }
            }
            // TODO : wirte this
            return result;
        } catch (GenomeException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void addBranchesAlongSelfEdges(int vertex, ArrayList<Branch> result) throws GenomeException {
        OrdinaryGenome guidedGenome = graph.getGuidedGenome();
        Neighbours neighbours = graph.getBaseGenome().getNeighbours();
        int guidedTarget = guidedGenome.getNeighbour(vertex);

        Integer[] targets = neighbours.getSortedVertexNeighbours(vertex).toArray(Integer[]::new);

        for (int target : targets) {
            addBranchAlongEdgeToResult(vertex, target, result, guidedGenome, neighbours, guidedTarget, targets);
        }
        if (!neighbours.hasEdge(vertex, guidedTarget)) {
            addBranchAlongEdgeToResult(vertex, guidedTarget, result, guidedGenome, neighbours, guidedTarget, targets);
        }
    }

    @Override
    protected void addBranchesAlongNotSelfEdges(int vertex, ArrayList<Branch> result) throws GenomeException {
        OrdinaryGenome guidedGenome = graph.getGuidedGenome();
        Neighbours neighbours = graph.getBaseGenome().getNeighbours();
        int guidedTarget = guidedGenome.getNeighbour(vertex);
        Integer[] targets = neighbours.getSortedVertexNeighbours(vertex).toArray(Integer[]::new);

        for (Iterator<Integer> it = neighbours.getVertices().iterator(); it.hasNext(); ) {
            int matchingVertex = it.next();
            if (neighbours.hasEdge(vertex, matchingVertex) || vertex == matchingVertex) {
                continue;
            }

            addBranchAlongEdgeToResult(
                    vertex, matchingVertex,
                    result, guidedGenome, neighbours, guidedTarget, targets);
        }
    }

    private void addBranchAlongEdgeToResult(int vertex, int matchingVertex, ArrayList<Branch> result, OrdinaryGenome guidedGenome, Neighbours neighbours, int guidedTarget, Integer[] targets)
    {
        try {
            if (!(neighbours.hasEdge(vertex, matchingVertex) || guidedTarget == matchingVertex)) {
                // along edges only
                if (isRestricted) {
                    return;
                }
            }
            if (vertex == matchingVertex) {
                return;
            }
            int guidedMatchingVertexTarget = guidedGenome.getNeighbour(matchingVertex);

            int cyclesCount = 0;
            ArrayList<Graph.Edge> baseEdgesAdded = new ArrayList<>();
            ArrayList<Graph.Edge> guideEdgesAdded = new ArrayList<>();
            if (guidedTarget != matchingVertex) {
                guideEdgesAdded.add(new Graph.Edge(guidedTarget, guidedMatchingVertexTarget));
            } else {
                cyclesCount++;
            }

            Integer[] matchingTargets = neighbours.getSortedVertexNeighbours(matchingVertex).toArray(Integer[]::new);

            if (targets[0] == vertex) {
                if (matchingTargets[0] == matchingVertex) {
                    cyclesCount++;
                } else {
                    baseEdgesAdded.add(new Graph.Edge(matchingTargets[0], matchingTargets[1]));
                }
            } else if (matchingTargets[0] == matchingVertex) {
                baseEdgesAdded.add(new Graph.Edge(targets[0], targets[1]));
            } else if (targets[0] == matchingVertex) {
                cyclesCount++;
                if (targets[1] == matchingVertex) {
                    cyclesCount++;
                } else {
                    int matchingTarget = matchingTargets[0];
                    if (matchingTarget == vertex) {
                        matchingTarget = matchingTargets[1];
                    }
                    baseEdgesAdded.add(new Graph.Edge(targets[1], matchingTarget));
                }
            } else if (targets[1] == matchingVertex) {
                cyclesCount++;
                int matchingTarget = matchingTargets[0];
                if (matchingTarget == vertex) {
                    matchingTarget = matchingTargets[1];
                }
                baseEdgesAdded.add(new Graph.Edge(targets[0], matchingTarget));
            } else {
                ArrayList<Graph.Edge> edges = new ArrayList<>();
                edges.add(new Graph.Edge(targets[0], matchingTargets[1]));
                edges.add(new Graph.Edge(targets[1], matchingTargets[0]));

                result.add(getBranch(edges, guideEdgesAdded, Arrays.asList(new Graph.Edge(vertex, matchingVertex)),
                        cyclesCount));

                baseEdgesAdded.add(new Graph.Edge(targets[0], matchingTargets[0]));
                baseEdgesAdded.add(new Graph.Edge(targets[1], matchingTargets[1]));
            }


            result.add(getBranch(baseEdgesAdded, guideEdgesAdded, Arrays.asList(new Graph.Edge(vertex, matchingVertex)),
                    cyclesCount));
        } catch (GenomeException e) {
            e.printStackTrace();
        }
    }

    private Branch getBranch(List<Graph.Edge> baseEdgesAdded, List<Graph.Edge> guideEdgesAdded, List<Graph.Edge> resultedEdges, int cyclesCount) {
        return new Branch(GGHPGraph.convertToEdgesMap(baseEdgesAdded, guideEdgesAdded), resultedEdges, cyclesCount);
    }
}
