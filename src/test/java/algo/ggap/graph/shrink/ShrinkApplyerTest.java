package algo.ggap.graph.shrink;

import java.util.ArrayList;
import java.util.Arrays;

import algo.ggap.graph.BaseGenome;
import algo.ggap.graph.Graph;
import algo.ggap.graph.Neighbours;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ShrinkApplyerTest {

    @Test
    public void shrink() {
        ArrayList<ArrayList<Integer>> neighbours = new ArrayList<>();
        neighbours.add(new ArrayList<>(Arrays.asList(1, 2)));
        neighbours.add(new ArrayList<>(Arrays.asList(0, 2)));
        neighbours.add(new ArrayList<>(Arrays.asList(0, 1)));
        neighbours.add(new ArrayList<>(Arrays.asList(3, 3)));
        BaseGenome baseGenome = new BaseGenome(new Neighbours(neighbours, 2), 0, new ArrayList<>());
        ArrayList<BaseGenome> genomes = ShrinkApplyer.shrink(
                baseGenome,
                new ArrayList<>(Arrays.asList(new Graph.Edge(2, 3), new Graph.Edge(0, 1)))
        );
        assertEquals(genomes.size(), 1);
        assertEquals(genomes.get(0).getCyclesCount(), 2);
    }

}