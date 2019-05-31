package algo.guided_problems.gghp;

import java.util.ArrayList;
import java.util.Arrays;

import algo.graph.BaseGenome;
import algo.graph.DuplicatedGenome;
import algo.graph.Neighbours;
import algo.graph.OrdinaryGenome;
import algo.guided_problems.GGHPGraph;
import org.junit.Test;

import static org.junit.Assert.*;

public class SolverTest {

    @Test
    public void solve() throws Exception {
        ArrayList<ArrayList<Integer>> neighbours = new ArrayList<>();
        neighbours.add(new ArrayList<>(Arrays.asList(5, 3)));
        neighbours.add(new ArrayList<>(Arrays.asList(1, 1)));
        neighbours.add(new ArrayList<>(Arrays.asList(2, 2)));
        neighbours.add(new ArrayList<>(Arrays.asList(0, 4)));
        neighbours.add(new ArrayList<>(Arrays.asList(3, 5)));
        neighbours.add(new ArrayList<>(Arrays.asList(0, 4)));

        neighbours.add(new ArrayList<>(Arrays.asList(7, 7)));
        neighbours.add(new ArrayList<>(Arrays.asList(6, 6)));

        neighbours.add(new ArrayList<>(Arrays.asList(9, 13)));
        neighbours.add(new ArrayList<>(Arrays.asList(8, 10)));
        neighbours.add(new ArrayList<>(Arrays.asList(9, 11)));
        neighbours.add(new ArrayList<>(Arrays.asList(10, 12)));
        neighbours.add(new ArrayList<>(Arrays.asList(11, 13)));
        neighbours.add(new ArrayList<>(Arrays.asList(12, 8)));
        DuplicatedGenome baseGenome = new DuplicatedGenome(neighbours);


        OrdinaryGenome guidedGenome = new OrdinaryGenome(Arrays.asList(4, 2, 1, 5, 0, 3, 7, 6, 9, 8, 11, 10, 13, 12));
        GGHPGraph graph = new GGHPGraph(baseGenome, guidedGenome);

        Solver solver = new Solver(graph);
        solver.solve();

        int cyclesCount = solver.getCurrentSolution().getCyclesCount();

        assertEquals(16, cyclesCount);
    }
}