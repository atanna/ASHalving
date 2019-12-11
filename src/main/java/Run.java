import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import algo.ugap.graph.BaseGenome;
import algo.ugap.graph.DuplicatedGenome;
import algo.ugap.graph.GAPGraph;
import algo.ugap.graph.Graph;
import algo.ugap.graph.Neighbours;
import algo.ugap.graph.OrdinaryGenome;
import algo.ugap.graph.TwoRegularNeighbours;
import algo.ugap.solver.BaseSolver;
import algo.ugap.solver.gap3.GAP3State;
import algo.ugap.solver.gghp.GGHPGraph;
import algo.ugap.solver.ParallelSolver;
import algo.ugap.solver.Solution;
import algo.ugap.solver.gghp.GGHPState;
import bio.genome.Genome;
import bio.graph.BreakpointGraph;
import bio.io.Grimm;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import static bio.io.Grimm.Reader.readFile;

public class Run {

    public static class GGHP {
        public static void solveProblem(String dirPath, int timeLimit, boolean isRestricted) throws Exception {
            String resultedName = "GGHPResult";
            if (isRestricted) {
                resultedName = "C" + resultedName;
            }
            solveProblem(dirPath, dirPath + "/" + resultedName + ".gen", timeLimit, isRestricted);
        }

        public static void solveProblem(String dirPath, String resultedPath, int timeLimit, boolean isRestricted) throws Exception {
            String pathOrd = dirPath + "/ord.gen";
            String pathWGD = dirPath + "/wgd.gen";
            solveProblem(pathOrd, pathWGD, resultedPath, timeLimit, isRestricted);
        }

        public static void solveProblem(String pathOrd, String pathWGD, String resultedPath, int timeLimit, boolean isRestricted) throws Exception {

            Genome ordGenome = readFile(pathOrd).get(0);
            Genome wgdGenome = readFile(pathWGD).get(0);
            String nameTest = "ord, wgd: " + ordGenome.getName() + ", " + wgdGenome.getName();

            BreakpointGraph graph = new BreakpointGraph();
            Neighbours ordNeigbours = graph.addGenome(ordGenome, "ord");
            Neighbours wgdNeigbours = graph.addGenome(wgdGenome, "wgd");
            int size = graph.getSize();

            BaseSolver solver = tryToSolve(ordNeigbours, wgdNeigbours, timeLimit, isRestricted);

            Solution solution = solver.getCurrentSolution();
            System.out.println("Time: " + solver.getSolutionTime());
            writeResult(graph, solution, solution.getDistance(3, size), resultedPath, isRestricted, "gghp", nameTest);
        }

        public static BaseSolver tryToSolve(Neighbours ordNbrs, Neighbours wgdNbrs, int timeLimit, boolean isRestricted) throws Exception {
            DuplicatedGenome baseGenome = new DuplicatedGenome(new TwoRegularNeighbours(wgdNbrs.neighbours));
            OrdinaryGenome guidedGenome = new OrdinaryGenome(new Neighbours(ordNbrs.neighbours, 1));
            GGHPGraph graph = new GGHPGraph(baseGenome, guidedGenome);

            GGHPState firstState = new GGHPState(graph);
            BaseSolver solver = new ParallelSolver(firstState);
            if (timeLimit != -1) {
                solver.solveWithLimit(timeLimit, isRestricted);
            }
            solver.solve();
            return solver;
        }

        public static void solveAllFromDir(String dirProblems, int timeLimit, boolean isRetricted) throws IOException {
            Files.list(new File(dirProblems).toPath()).sorted()
                    .forEach(problemPath -> {
                        try {
                            solveProblem(String.valueOf(problemPath), timeLimit, isRetricted);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
        }
    };

    public static class GAP3 {
        public static void solveProblem(String sourcePath, String resultedPath, int timeLimit, boolean isRestricted) throws Exception {

            Genome genome = readFile(sourcePath).get(0);
            String nameTest = "genome: " + genome.getName();

            BreakpointGraph graph = new BreakpointGraph();
            Neighbours neighbours = graph.addGenome(genome);
            int size = neighbours.size();

            BaseSolver solver = tryToSolve(neighbours, timeLimit, isRestricted);

            Solution solution = solver.getCurrentSolution();
            writeResult(graph, solution, solution.getDistance(3, size), resultedPath, isRestricted, "GAP", nameTest);

        }

        public static BaseSolver tryToSolve(Neighbours nbrs, int timeLimit, boolean isRestricted) throws Exception {
            BaseGenome baseGenome = new BaseGenome(nbrs);

            GAPGraph graph = new GAPGraph(baseGenome);

            GAP3State firstState = new GAP3State(graph);
            BaseSolver solver = new ParallelSolver(firstState);
            if (timeLimit != -1) {
                solver.solveWithLimit(timeLimit, isRestricted);
            }
            solver.solve();
            return solver;
        }
    }



    public static void writeResult(BreakpointGraph graph, Solution solution, int distance, String problemName, boolean isRestricted, String resultedPath, String testName) {
        List<String> info = Arrays.asList(
                testName,
                "Size: " + graph.getSize(),
                "CyclesCount: " + solution.getCyclesCount(),
                "Distance: " + distance,
                "IsExact: " + solution.isExact(),
                "IsRestricted: " + isRestricted
        );

        System.out.println("\n" + String.join("\n", info));
        String comment =  String.join("\n# ", info);

        if (resultedPath != null) {
            ArrayList<Graph.Edge> edges = solution.getResultMatching();
            String resultedName = problemName;
            if (isRestricted) {
                resultedName = "C" + resultedName;
            }
            Genome result = graph.convertToGenome(edges, resultedName);
            Grimm.Writer.writeToFile(resultedPath, result, comment);
        }
    }


    public static void main(String[] args) throws Exception {
        int timeLimit = 60 * 60 * 2; // 2 hours
        boolean isRestricted = false;

        Options options = new Options();

        options
                .addOption("g", "genome", true, "Path to source (main) genome")
                .addOption("b", "ord", true, "Path to ordinary genome B (for GGHP only)")
                .addOption("o", "output", true, "Resulted path")
                .addOption("t", "time", true, "Time limit for solving problem (in seconds), default 60*60*2")
                .addOption("r", "restricted",false, "Flag for using restricted model, default = false")
                .addOption("w", "dir", true,"Solve all problems from dir (use instead g, [b], o) (for GGHP only)");


        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        try {
            CommandLine cmd = parser.parse(options, args);

            String mainGenomePath = cmd.getOptionValue("genome");
            String ordPath = cmd.getOptionValue("ord");
            String resultedPath = cmd.getOptionValue("output");
            isRestricted = cmd.hasOption("restricted");
            timeLimit = (int) Long.parseLong(cmd.getOptionValue("time", String.valueOf(timeLimit))) * 1000;

            if (ordPath != null ) {
                // gghp problem
                System.out.println("GGHP problem is parsed");
                String dir = cmd.getOptionValue("dir");
                if (ordPath != null && mainGenomePath  != null && resultedPath != null) {
                    System.out.println(ordPath + " " + mainGenomePath  + " " + resultedPath);
                    GGHP.solveProblem(ordPath, mainGenomePath, resultedPath, timeLimit, isRestricted);
                } else if (dir != null) {
                    GGHP.solveAllFromDir(dir, timeLimit, isRestricted);
                } else {
                    throw new ParseException("Cannot parse paths");
                }
            } else {
                // gap3 problem
                System.out.println("GAP3 problem is parsed");
                if (mainGenomePath != null) {
                    GAP3.solveProblem(mainGenomePath, resultedPath, timeLimit, isRestricted);
                } else {
                    throw new ParseException("Cannot parse paths");
                }
            }

        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("Run", options);

            System.exit(1);
        }
    }

}
