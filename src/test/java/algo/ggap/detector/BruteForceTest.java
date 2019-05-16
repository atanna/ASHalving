package algo.ggap.detector;

import java.util.ArrayList;
import java.util.Arrays;

import algo.ggap.graph.BaseGenome;
import algo.ggap.graph.Graph;
import algo.ggap.graph.Neighbours;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BruteForceTest {

    @Test
    public void search() {
        ArrayList<ArrayList<Integer>> neighbours = new ArrayList<>();
        neighbours.add(new ArrayList<>(Arrays.asList(1, 2)));
        neighbours.add(new ArrayList<>(Arrays.asList(0, 2)));
        neighbours.add(new ArrayList<>(Arrays.asList(0, 1)));
        neighbours.add(new ArrayList<>(Arrays.asList(3, 3)));
        BaseGenome genome = new BaseGenome(new Neighbours(neighbours, 2), 0, new ArrayList<>());

        BaseDetector.Result result = BruteForce.search(genome);
        assertEquals(result.getSize(), 3);
        assertEquals(result.branches.get(0).edges.size(), 1);
        assertEquals(result.branches.get(1).edges.size(), 1);
        assertEquals(result.branches.get(2).edges.size(), 1);

    }

}