package algo.distance_problems.shrink;

import java.util.ArrayList;
import java.util.Arrays;

import algo.graph.BaseGenome;
import algo.graph.GenomeException;
import algo.graph.Graph;
import algo.graph.Neighbours;
import algo.solver.State;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ShrinkApplyerTest {

    @Test
    public void shrink() throws GenomeException {
        ArrayList<ArrayList<Integer>> neighbours = new ArrayList<>();
        neighbours.add(new ArrayList<>(Arrays.asList(1, 2)));
        neighbours.add(new ArrayList<>(Arrays.asList(0, 2)));
        neighbours.add(new ArrayList<>(Arrays.asList(0, 1)));
        neighbours.add(new ArrayList<>(Arrays.asList(3, 3)));
        BaseGenome baseGenome = new BaseGenome(new Neighbours(neighbours, 2));
        ArrayList<State<BaseGenome>> states = ShrinkApplyer.shrink(
                new State<BaseGenome>(baseGenome, 0 , new ArrayList<>()),
                new ArrayList<>(Arrays.asList(new Graph.Edge(2, 3), new Graph.Edge(0, 1)))
        );
        assertEquals(states.size(), 1);
        assertEquals(states.get(0).cyclesCount, 2);
    }

}