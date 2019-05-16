package algo.ggap.graph.shrink;

import java.io.Serializable;
import java.util.ArrayList;

import algo.ggap.graph.BaseGenome;
import algo.ggap.graph.Graph;
import algo.ggap.graph.Neighbours;

public class ShrinkApplyer implements Serializable {

    public static ArrayList<BaseGenome> shrink(BaseGenome startGenome, ArrayList<Graph.Edge> edges) {
        // Add edges to result matching and reconstruct graph (remove adjacent vertices, and connect stumps as paths can go)
        // Return all available branches

        ArrayList<BaseGenome> results = new ArrayList<>();
        results.add(startGenome);

        for (Graph.Edge edge : edges) {
            ArrayList<BaseGenome> newResults = new ArrayList<>();
            for (BaseGenome genome : results) {
                newResults.addAll(shrinkWithSavingIndexes(genome, edge));
            }

            results = newResults;
        }

        for (BaseGenome result : results) {
            result.getNeighbours().applyForceReconstruction();
        }

        return results;
    }

    public static ArrayList<BaseGenome> shrink(BaseGenome startGenome, Graph.Edge edge) {
        ArrayList<BaseGenome> result = shrinkWithSavingIndexes(startGenome, edge);
        for (BaseGenome genome : result) {
            genome.getNeighbours().applyForceReconstruction();
        }
        return result;
    }

    public static ArrayList<BaseGenome> shrinkWithSavingIndexes(BaseGenome startGenome, Graph.Edge edge) {
        ArrayList<BaseGenome> result = new ArrayList<>();

        ArrayList<Integer> removedVertices = new ArrayList<>();
        removedVertices.add(edge.source);
        removedVertices.add(edge.target);

        Neighbours neighbours = startGenome.getNeighbours();

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
            ArrayList newResult = new ArrayList<Graph.Edge>(startGenome.result);
            newResult.add(new Graph.Edge(neighbours.indexes.get(edge.source), neighbours.indexes.get(edge.target)));
            BaseGenome genome = new BaseGenome(newNeighbours, startGenome.getCyclesCount() + cycles, newResult);

            result.add(genome);
        }


        return result;
    }

}
