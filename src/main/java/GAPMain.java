

import algo.distance_problems.Solver;
import algo.graph.BaseGenome;
import algo.graph.Neighbours;
import algo.solver.Solution;
import genome.Genome;
import graph.BreakpointGraph;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import static io.Grimm.Reader.readFile;

public class GAPMain {


    public static void solveProblem(String sourcePath, String resultedPath, int timeLimit, boolean isRestricted) throws Exception {

        Genome genome = readFile(sourcePath).get(0);
        String nameTest = "genome: " + genome.getName();

        BreakpointGraph graph = new BreakpointGraph();
        Neighbours neighbours = graph.addGenome(genome);


        Solver solver = tryToSolve(neighbours, timeLimit, isRestricted);

        Solution solution = solver.getCurrentSolution();
        Common.writeResult(graph, solution, solver.getDistance(), resultedPath, isRestricted, "GAP", nameTest);

    }

    public static algo.distance_problems.Solver tryToSolve(Neighbours nbrs, int timeLimit, boolean isRestricted) throws Exception {
        BaseGenome baseGenome = new BaseGenome(nbrs);


        algo.distance_problems.Solver solver = new Solver(baseGenome);
        if (timeLimit != -1) {
            solver.solveWithLimit(timeLimit, isRestricted);
        }
        solver.solve();
        return solver;
    }


    public static void main(String[] args) throws Exception {
        int timeLimit = 60 * 60 * 2; // 2 hours
        boolean isRestricted = false;

        Options options = new Options();

        options
                .addOption("g", "genome", true, "Path to source genome")
                .addOption("r", "result", true, "Resulted path")
                .addOption("t", "time", true, "Time limit for solving problem (in seconds), default 60*60*2")
                .addOption("p", "restricted",false, "Flag for using restricted model, default = false");

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        try {
            CommandLine cmd = parser.parse(options, args);

            String source = cmd.getOptionValue("genome");
            String resultedPath = cmd.getOptionValue("result");
            timeLimit = (int) Long.parseLong(cmd.getOptionValue("time", String.valueOf(timeLimit))) * 1000;
            isRestricted = cmd.hasOption("restricted");

            if (source != null) {
                solveProblem(source, resultedPath, timeLimit, isRestricted);
            } else {
                throw new ParseException("Cannot parse paths");
            }

        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("GAPMain", options);

            System.exit(1);
        }
    }

}
