import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

import algo.graph.GenomeException;
import algo.graph.OrdinaryGenome;
import algo.guided_problems.GGHPGraph;
import algo.guided_problems.gghp.Solver;
import algo.solver.Solution;
import algo.graph.BaseGenome;
import algo.graph.Neighbours;


public class Main {

    public static void testGGHPSolver() throws Exception {
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
        BaseGenome baseGenome = new BaseGenome(new Neighbours(neighbours, 2));


        OrdinaryGenome guidedGenome = new OrdinaryGenome(Arrays.asList(4, 2, 1, 5, 0, 3, 7, 6, 9, 8, 11, 10, 13, 12));
        GGHPGraph graph = new GGHPGraph(baseGenome, guidedGenome);

        algo.guided_problems.gghp.Solver solver = new Solver(graph);
        solver.solve();

        System.out.println(solver.getCurrentSolution());
    }

    public static void main(String[] args) throws Exception {
        testGGHPSolver();
    }

}