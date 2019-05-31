package algo.graph;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import static org.junit.Assert.*;

public class TwoRegularNeighboursTest {

    @Test
    public void getEvenCyclesCount() {
        ArrayList<ArrayList<Integer>> neighbours = new ArrayList<>();

        neighbours.add(new ArrayList<>(Arrays.asList(1, 3)));
        neighbours.add(new ArrayList<>(Arrays.asList(0, 2)));
        neighbours.add(new ArrayList<>(Arrays.asList(1, 3)));
        neighbours.add(new ArrayList<>(Arrays.asList(0, 2)));
        neighbours.add(new ArrayList<>(Arrays.asList(5, 6)));
        neighbours.add(new ArrayList<>(Arrays.asList(4, 6)));
        neighbours.add(new ArrayList<>(Arrays.asList(4, 5)));
        neighbours.add(new ArrayList<>(Arrays.asList(8, 9)));
        neighbours.add(new ArrayList<>(Arrays.asList(7, 9)));
        neighbours.add(new ArrayList<>(Arrays.asList(7, 8)));

        TwoRegularNeighbours nbrs = new TwoRegularNeighbours(neighbours);
        assertEquals(1, nbrs.getEvenCyclesCount());

    }
}