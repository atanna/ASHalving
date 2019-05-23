package algo.guided_problems.gghp.detector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import algo.distance_problems.detector.BaseAdequateSubgraph;
import algo.distance_problems.detector.DetectorException;
import algo.graph.GenomeException;
import algo.graph.Graph;
import algo.graph.Neighbours;
import algo.graph.OrdinaryGenome;
import algo.guided_problems.GGHPGraph;


public class Detector extends BaseAdequateSubgraph {
    private GGHPGraph graph;


    public Detector(GGHPGraph graph) {
        this.graph = graph;
    }

    @Override
    public Branch searchExplicitBranch() throws DetectorException {
        Branch branch = search2();
        updateWithExplicit4(branch);
        return branch;
    }

    public ArrayList<Branch> search() throws DetectorException, GenomeException {
        ArrayList<Branch> branches = new ArrayList<>();
        Branch branch = searchExplicitBranch();
        if (branch.isNotEmpty()) {
            branches.add(branch);
        } else {
            branches = searchNotExplicit4();
            if (branches.size() == 0) {
                for (int vertex = 0; vertex < graph.size(); ++vertex) {
                    if (graph.getGuidedGenome().getNeighbours().removedVertices.contains(vertex)) {
                        continue;
                    }
                    branches = bruteForce(vertex);
                    break;
                }
            }
        }
        return branches;
    }

    private Branch search2() throws DetectorException {
        // Search non-intersecting adequate subgraphs H with |V(H)| = 2 and merge its in one branch.
        // H = (V,E join E_guideed), V = {a,b}
        // There are 4 cases:
        // 1. E = {(a,b)},  E_guided = {(a, b)}
        // 2. E = {(a,b), (a,b)}, E_guided = {}
        // 3. E = {(a,b), (a,b)}, E_guided = {(a,b)}
        // 4. E = {(a,a), {b,b}}. E_guided = {(a,b)}
        // (All cases are symmetric by a, b)

        Branch resultedBranch = new Branch();
        Neighbours neighbours = graph.getBaseGenome().getNeighbours();
        OrdinaryGenome guidedGenome = graph.getGuidedGenome();
        for (int vertex = 0; vertex < neighbours.neighbours.size(); vertex++) {
            if (neighbours.removedVertices.contains(vertex)) {
                continue;
            }
            Map<Integer, Long> counts =
                    neighbours.getSortedVertexNeighbours(vertex).collect(Collectors.groupingBy(e -> e, Collectors.counting()));

            int guidedTarget = guidedGenome.getNeighbour(vertex);;

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
                                .merge(new Branch(new HashMap<>(), Arrays.asList(new Graph.Edge(vertex, guidedTarget)),
                                        2));
                        continue;
                    }

                } else if (target == guidedTarget) {
                    // 3
                    Branch branch = new Branch(new HashMap<>(), Arrays.asList(new Graph.Edge(vertex, target)), 3);
                    resultedBranch.merge(branch);
                    continue;
                } else {
                    // 2
                    int guidedTargetTarget = guidedGenome.getNeighbour(target);
                    HashMap<String, List<Graph.Edge>> addedEdges = GGHPGraph.convertToEdgesMap(List.of(), Arrays.asList(new Graph.Edge(guidedTarget, guidedTargetTarget)));
                    Branch branch = new Branch(addedEdges, Arrays.asList(new Graph.Edge(vertex, target)), 2);
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
                    HashMap<String, List<Graph.Edge>> addedEdges = GGHPGraph.convertToEdgesMap(Arrays.asList(edge), List.of());
                    Branch branch = new Branch(addedEdges, Arrays.asList(new Graph.Edge(vertex, firstTarget)), 2);
                    resultedBranch.merge(branch);
                    continue;
                }

            } else {
                throw new DetectorException("Every vertex must have two neighbours");
            }
        }
        return resultedBranch;
    }

    private void updateWithExplicit4(Branch resultedBranch) {
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

        // TODO : write this
    }

    private ArrayList<Branch> searchNotExplicit4() {
        // 1, 3 from previous description
        ArrayList<Branch> result = new ArrayList<>();
        // TODO : wirte this
        return result;
    }

    private ArrayList<Branch> bruteForce(int vertex) throws DetectorException {
        // collects all possible resulted matching for vertex (n-1 branches)
        // in case other cases are not worked
        ArrayList<Branch> result = new ArrayList<>();
        OrdinaryGenome guidedGenome = graph.getGuidedGenome();
        Neighbours neighbours = graph.getBaseGenome().getNeighbours();
        int guidedTarget = guidedGenome.getNeighbour(vertex);


        Integer[] targets = neighbours.getSortedVertexNeighbours(vertex).toArray(Integer[]::new);


        for (Iterator<Integer> it = neighbours.getVertices().iterator(); it.hasNext(); ) {
            int matchingVertex = it.next();
            if (vertex == matchingVertex) {
                continue;
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
//                throw new DetectorException("We must process this case early. Double edge -- adequate subgraph.");
            } else if (targets[1] == matchingVertex) {
                cyclesCount++;
                int matchingTarget = matchingTargets[0];
                if (matchingTarget == vertex) {
                    matchingTarget = matchingTargets[1];
                }
                baseEdgesAdded.add(new Graph.Edge(targets[0], matchingTarget));
//                throw new DetectorException("We must process this case early. Double edge -- adequate subgraph.");
            } else {
                ArrayList<Graph.Edge> edges = new ArrayList<>();
                edges.add(new Graph.Edge(targets[0], matchingTargets[1]));
                edges.add(new Graph.Edge(targets[1], matchingTargets[0]));

                result.add(new Branch(GGHPGraph.convertToEdgesMap(edges, new ArrayList<>(guideEdgesAdded)), Arrays.asList(new Graph.Edge(vertex, matchingVertex)), cyclesCount));

                baseEdgesAdded.add(new Graph.Edge(targets[0], matchingTargets[0]));
                baseEdgesAdded.add(new Graph.Edge(targets[1], matchingTargets[1]));
            }


            result.add(new Branch(GGHPGraph.convertToEdgesMap(baseEdgesAdded, guideEdgesAdded), Arrays.asList(new Graph.Edge(vertex, matchingVertex)), cyclesCount));

        }
        return result;
    }
}
