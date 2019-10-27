package algo.distance_problems.shrink;

import java.io.Serializable;
import java.util.ArrayList;

import algo.distance_problems.detector.BaseDetector;
import algo.graph.BaseGenome;
import algo.graph.GenomeException;
import algo.graph.Graph;
import algo.graph.Neighbours;
import algo.solver.State;

public class ShrinkApplyer implements Serializable {
    

    public static ArrayList<State<BaseGenome>> shrink(State<BaseGenome> state, BaseDetector.Branch branch)
            throws GenomeException
    {
        return shrink(state, branch.edges);
    }

    public static ArrayList<State<BaseGenome>> shrink(State<BaseGenome> baseState, ArrayList<Graph.Edge> edges)
            throws GenomeException
    {
        // Add edges to result matching and reconstruct graph (remove adjacent vertices, and connect stumps as paths can go)
        // Return all available branches

        ArrayList<State<BaseGenome>> results = new ArrayList<>();
        results.add(baseState);

        for (Graph.Edge edge : edges) {
            ArrayList<State<BaseGenome>> newResults = new ArrayList<>();
            for (State<BaseGenome> state : results) {
                newResults.addAll(shrinkWithSavingIndexes(state, edge));
            }

            results = newResults;
        }

        for (State<BaseGenome> result : results) {
            result.data.getNeighbours().applyForceReconstruction();
        }

        return results;
    }

    public static ArrayList<State<BaseGenome>> shrinkWithSavingIndexes(State<BaseGenome> baseState, Graph.Edge edge)
            throws GenomeException
    {
        ArrayList<State<BaseGenome>> result = new ArrayList<>();

        ArrayList<Integer> removedVertices = new ArrayList<>();
        removedVertices.add(edge.source);
        removedVertices.add(edge.target);

        Neighbours neighbours = baseState.data.getNeighbours();

        TailsEnumerator enumerator = new TailsEnumerator(
                neighbours.getVertexNeighbours(edge.source),
                neighbours.getVertexNeighbours(edge.target),
                edge
        );
        for (ArrayList<Graph.Edge> edges : enumerator.enumerate()) {
            int cycles = 0;
            for (Graph.Edge addedEdge : edges) {
                if (edge.equals(addedEdge)) {
                    cycles += 1;
                }
            }

            Neighbours newNeighbours = neighbours.getCopy();
            newNeighbours.lazyReconstruct(removedVertices, edges, edge);
            ArrayList newResult = new ArrayList<Graph.Edge>(baseState.resultMatching);
            newResult.add(new Graph.Edge(neighbours.indexes.get(edge.source), neighbours.indexes.get(edge.target)));

            BaseGenome genome = new BaseGenome(newNeighbours);

            result.add(new State<BaseGenome>(genome, baseState.cyclesCount + cycles, newResult));
        }
        return result;
    }




}
