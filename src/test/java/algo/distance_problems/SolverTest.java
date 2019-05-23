package algo.distance_problems;

import java.util.ArrayList;
import java.util.Arrays;

import algo.graph.BaseGenome;
import algo.graph.Neighbours;
import algo.solver.Solution;
import org.junit.Test;

import static org.junit.Assert.*;

public class SolverTest {

    @Test
    public void solve() throws Exception {
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


        BaseGenome baseGenome = new BaseGenome(new Neighbours(neighbours, 2));
        Solver solver = new Solver(baseGenome);
        solver.solve(100);
        Solution result = solver.getCurrentSolution();

        assertEquals(6, result.getCyclesCount());
    }

    @Test
    public void solve3() throws Exception {
        ArrayList<ArrayList<Integer>> neighbours = new ArrayList<>();

        neighbours.add(new ArrayList<>(Arrays.asList(1, 4, 3)));
        neighbours.add(new ArrayList<>(Arrays.asList(0, 5, 2)));
        neighbours.add(new ArrayList<>(Arrays.asList(1, 4, 3)));
        neighbours.add(new ArrayList<>(Arrays.asList(0, 5, 2)));
        neighbours.add(new ArrayList<>(Arrays.asList(0, 5, 2)));
        neighbours.add(new ArrayList<>(Arrays.asList(1, 4, 3)));

        BaseGenome baseGenome = new BaseGenome(new Neighbours(neighbours, 3));
        Solver solver = new Solver(baseGenome);
        solver.solve(100);
        Solution result = solver.getCurrentSolution();

        assertEquals(6, result.getCyclesCount());
    }
}