import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import algo.ugap.graph.Graph;
import algo.ugap.solver.Solution;
import genome.Genome;
import graph.BreakpointGraph;
import io.Grimm;


public class Common {
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

}
