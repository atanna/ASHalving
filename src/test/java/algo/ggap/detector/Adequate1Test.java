package algo.ggap.detector;

import java.util.ArrayList;
import java.util.Arrays;

import algo.ggap.graph.BaseGenome;
import algo.ggap.graph.Graph;
import algo.ggap.graph.Neighbours;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Adequate1Test {

    @Test
    public void search() {
        ArrayList<ArrayList<Integer>> neighbours = new ArrayList<>();
        neighbours.add(new ArrayList<>(Arrays.asList(1, 1)));
        neighbours.add(new ArrayList<>(Arrays.asList(0, 0)));
        neighbours.add(new ArrayList<>(Arrays.asList(3, 3)));
        neighbours.add(new ArrayList<>(Arrays.asList(2, 2)));
        BaseGenome genome = new BaseGenome(new Neighbours(neighbours, 2), 0, new ArrayList<>());

        BaseDetector.Result result = Adequate1.search(genome);
        assertEquals(result.getSize(), 1);

        ArrayList<Graph.Edge> edges = result.branches.get(0).edges;
        assertEquals(edges.size(), 2);
        assertTrue(edges.contains(new Graph.Edge(0, 1)));
        assertTrue(edges.contains(new Graph.Edge(2, 3)));

    }
}