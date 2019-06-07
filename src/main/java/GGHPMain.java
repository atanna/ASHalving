import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;

import algo.graph.DuplicatedGenome;
import algo.graph.Graph;
import algo.graph.Neighbours;
import algo.graph.OrdinaryGenome;
import algo.graph.TwoRegularNeighbours;
import algo.guided_problems.GGHPGraph;
import algo.guided_problems.gghp.Solver;
import algo.solver.Solution;
import genome.Genome;
import graph.BreakpointGraph;
import io.Grimm;

import static io.Grimm.Reader.readFile;

public class GGHPMain {

    public static void solveProblem(String dirPath, int timeLimit) throws Exception {
        solveProblem(dirPath, dirPath + "/GGHPResult.gen", timeLimit);
    }

    public static void solveProblem(String dirPath, String resultPath, int timeLimit) throws Exception {
        String pathOrd = dirPath + "/ord.gen";
        String pathWGD = dirPath + "/wgd.gen";

        Genome ordGenome = readFile(pathOrd).get(0);
        Genome wgdGenome = readFile(pathWGD).get(0);
        String nameTest = "ord, wgd: " + ordGenome.getName() + ", " + wgdGenome.getName();

        BreakpointGraph graph = new BreakpointGraph();
        Neighbours ordNeigbours = graph.addGenome(ordGenome, "ord");
        Neighbours wgdNeigbours = graph.addGenome(wgdGenome, "wgd");


        Solver solver = tryToSolve(ordNeigbours, wgdNeigbours, timeLimit);
        Solution solution = solver.getCurrentSolution();

        System.out.println(solution);
        ArrayList<Graph.Edge> edges = solver.getCurrentSolution().getResultMatching();
        Genome result = graph.convertToGenome(edges, "GGHP_result");
        System.out.println(result);

        String comment =  String.join("\n# ", Arrays.asList(
                nameTest,
                "CyclesCount: " + solution.getCyclesCount(),
                "Distance: " + solution.getDistance(),
                "IsExact: " + solution.isExact()
        ));
        Grimm.Writer.writeToFile(resultPath, result, comment);

    }

    public static algo.guided_problems.gghp.Solver tryToSolve(Neighbours ordNbrs, Neighbours wgdNbrs, int timeLimit) throws Exception {
        DuplicatedGenome baseGenome = new DuplicatedGenome(new TwoRegularNeighbours(wgdNbrs.neighbours));
        OrdinaryGenome guidedGenome = new OrdinaryGenome(new Neighbours(ordNbrs.neighbours, 1));
        GGHPGraph graph = new GGHPGraph(baseGenome, guidedGenome);

        algo.guided_problems.gghp.Solver solver = new Solver(graph);
        if (timeLimit != -1) {
            solver.solveWithLimit(timeLimit);
        }
        solver.solve();
        return solver;
    }

    public static void solveAllFromDir(String dirProblems, int timeLimit) throws IOException {
        Files.list(new File(dirProblems).toPath())
                .forEach(problemPath -> {
                    try {
                        solveProblem(String.valueOf(problemPath), timeLimit);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    public static void main(String[] args) throws Exception {
        int timeLimit = 1000 * 60 * 60 * 2; // 2 hours
        if (args.length > 0) {
            if (args.length > 1) {
                solveProblem(args[0], args[1], timeLimit);
            } else {
                solveAllFromDir(args[0], timeLimit);
            }
        } else {
            solveAllFromDir("/home/atanna/GenomeMedian/yeasts_tannier", timeLimit);
        }
    }

}
