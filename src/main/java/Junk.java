import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Stream;

import algo.graph.DuplicatedGenome;
import algo.graph.GenomeException;
import algo.graph.OrdinaryGenome;
import algo.graph.TwoRegularNeighbours;
import algo.guided_problems.GGHPGraph;
import algo.guided_problems.gghp.Solver;
import algo.solver.Solution;
import algo.graph.BaseGenome;
import algo.graph.Neighbours;


public class Junk {


    public static GGHPGraph generateGGHPGraph(int n) throws Exception {
        ArrayList<ArrayList<Integer>> neighbours = new ArrayList<>(n);
        Random randomSeed = new Random(23);
        randomSeed = new Random(35);
        randomSeed = new Random();
        for (int degree = 0; degree < 2; degree++) {
            Generate.addMatching(neighbours, n, randomSeed.nextInt());
        }
        ArrayList<ArrayList<Integer>> guidedNeighbours = new ArrayList<>(n);
        Generate.addMatching(guidedNeighbours, n, randomSeed.nextInt());
        DuplicatedGenome baseGenome = new DuplicatedGenome(new TwoRegularNeighbours(neighbours));
        OrdinaryGenome guidedGenome = new OrdinaryGenome(new Neighbours(guidedNeighbours, 1));
        GGHPGraph graph = new GGHPGraph(baseGenome, guidedGenome);

        return graph;
    }

    public static void testGGHPSolverOnSimulatedDate() throws Exception {
        GGHPGraph graph = generateGGHPGraph(100/*40*/);

//        System.out.println(graph);
        algo.guided_problems.gghp.Solver solver = new Solver(graph);
        solver.solve();

        System.out.println(solver.getCurrentSolution());
    }

    public static Neighbours readFromFile(String fileName) throws Exception {
        StringBuilder sb = new StringBuilder();

        try (BufferedReader br = Files.newBufferedReader(Paths.get(fileName))) {

            // read line by line
            String line;
            Scanner lineScanner = new Scanner(br.readLine());
            int size = lineScanner.nextInt();
            int degree = lineScanner.nextInt();
            ArrayList<ArrayList<Integer>> neighbours = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                lineScanner = new Scanner(line);
                ArrayList<Integer> arr = new ArrayList<>();
                while (lineScanner.hasNextInt()) {
                    arr.add(lineScanner.nextInt());
                }
                if (arr.size() == 0) {
                    continue;
                }
                neighbours.add(arr);
            }

            System.out.println(neighbours);
            return new Neighbours(neighbours, degree);

        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }

        return null;

    }

    public static void tryToSolve(String problemID) throws Exception {
        Neighbours ordNbrs = readFromFile("/home/atanna/GenomeMedian/dir/" + problemID + "/ord.gen");
        Neighbours wgdNbrs = readFromFile("/home/atanna/GenomeMedian/dir/" + problemID + "/wgd.gen");

        algo.guided_problems.gghp.Solver solver = GGHPMain.tryToSolve(ordNbrs, wgdNbrs, 100 * 60 * 1000, false);

        System.out.println(solver.getCurrentSolution());
        ;

        String result  = solver.getCurrentSolution().toString();

        BufferedWriter writer = new BufferedWriter(new FileWriter("/home/atanna/GenomeMedian/dir/" + problemID +"/res"));
        writer.write(result);

        writer.close();
    }

    public static void test() throws Exception {
        for (int i = 30; i < 60; ++i) {
            tryToSolve(String.valueOf(i));
        }
    }

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

        neighbours.add((new ArrayList<>(Arrays.asList(15, 16))));
        neighbours.add((new ArrayList<>(Arrays.asList(14, 16))));
        neighbours.add((new ArrayList<>(Arrays.asList(14, 15))));
        neighbours.add((new ArrayList<>(Arrays.asList(17, 17))));
        DuplicatedGenome baseGenome = new DuplicatedGenome(new TwoRegularNeighbours(neighbours));


        OrdinaryGenome guidedGenome = new OrdinaryGenome(Arrays.asList(4, 2, 1, 5, 0, 3, 7, 6, 9, 8, 11, 10, 13, 12, 16, 17, 14, 15));
        GGHPGraph graph = new GGHPGraph(baseGenome, guidedGenome);

        algo.guided_problems.gghp.Solver solver = new Solver(graph);
        solver.solve();

        System.out.println(solver.getCurrentSolution());
    }

    public static void testParallelSolver() throws Exception {
        ArrayList<ArrayList<Integer>> neighbours = new ArrayList<>();
        neighbours.add(new ArrayList<>(Arrays.asList(1, 2)));
        neighbours.add(new ArrayList<>(Arrays.asList(0, 2)));
        neighbours.add(new ArrayList<>(Arrays.asList(0, 1)));

        neighbours.add(new ArrayList<>(Arrays.asList(3, 3)));

//        neighbours.add(new ArrayList<>(Arrays.asList(5, 5)));
//        neighbours.add(new ArrayList<>(Arrays.asList(4, 4)));

        DuplicatedGenome baseGenome = new DuplicatedGenome(new TwoRegularNeighbours(neighbours));


        OrdinaryGenome guidedGenome = new OrdinaryGenome(Arrays.asList(2, 3, 0, 1));
        GGHPGraph graph = new GGHPGraph(baseGenome, guidedGenome);

        algo.guided_problems.gghp.Solver solver = new Solver(graph);
        solver.solve();

        System.out.println(solver.getCurrentSolution());
    }

    public static void main(String[] args) throws Exception {
        long startTime = System.currentTimeMillis();
//        testGGHPSolverOnSimulatedDate();
//        testGGHPSolver();
//        testParallelSolver();
//        if (args.length > 0) {
//            tryToSolve(args[0]);
//        } else {
//            test();
//        }

        tryToSolve("36");

        long finishTime = System.currentTimeMillis();
        System.out.println((finishTime +0. - startTime) / 1000/ 60  + " " + "min");
        System.out.println((finishTime + 0. - startTime) / 1000/ 60 / 60 + " "  + "hours");
//        testGGHPSolver();
    }

}
