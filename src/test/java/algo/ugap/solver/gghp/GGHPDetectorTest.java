package algo.ugap.solver.gghp;

import java.util.ArrayList;
import java.util.Arrays;


import algo.ugap.graph.DuplicatedGenome;
import algo.ugap.graph.GenomeException;
import algo.ugap.graph.OrdinaryGenome;
import algo.ugap.solver.BaseSolver;
import algo.ugap.solver.ParallelSolver;
import algo.ugap.solver.SeqSolver;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GGHPDetectorTest {

    GGHPGraph getTestGraph() throws GenomeException {
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
        return graph;
    }

    @Test
    public void testWithParallelSolver() throws Exception {

        GGHPGraph graph = getTestGraph();
        GGHPState firstState = new GGHPState(graph);
        BaseSolver solver = new ParallelSolver(firstState);
        solver.solve();

        int cyclesCount = solver.getCurrentSolution().getCyclesCount();
        assertEquals(cyclesCount, 16);
    }

    @Test
    public void testWithSeqSolver() throws Exception {

        GGHPGraph graph = getTestGraph();
        GGHPState firstState = new GGHPState(graph);
        SeqSolver solver = new SeqSolver(firstState);
        solver.solve();

        int cyclesCount = solver.getCurrentSolution().getCyclesCount();
        assertEquals(cyclesCount, 16);
    }

}