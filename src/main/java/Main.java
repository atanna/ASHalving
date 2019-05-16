import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import algo.ggap.Solver;
import algo.ggap.graph.BaseGenome;
import algo.ggap.graph.Neighbours;


public class Main {

    public static void testBaseGenome() {
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


        BaseGenome baseGenome = new BaseGenome(new Neighbours(neighbours, 2), 0, new ArrayList<>());
        Solver.Solution result = new Solver(baseGenome).solve(10);
        System.out.println(result.getCyclesCount());
        System.out.println(result.getResultMatching());
        System.out.println(result.getCounts());
    }

    public static void main(String[] args) throws IOException {
        testBaseGenome();
    }

}
