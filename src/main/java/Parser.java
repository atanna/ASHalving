import java.io.BufferedWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;

import algo.graph.Graph;
import algo.graph.Neighbours;
import algo.guided_problems.gghp.Solver;
import genome.Genome;
import graph.BreakpointGraph;
import io.Grimm;

import static io.Grimm.Reader.readFile;

public class Parser {

    public static void main(String[] args) {
        String problemID = "36";
        String pathOrd = "/home/atanna/adequate/ILP-WGD-reconstructor/yeasts_tannier/" + problemID + "/ord.gen";
        String pathWGD = "/home/atanna/adequate/ILP-WGD-reconstructor/yeasts_tannier/" + problemID + "/wgd.gen";
        try {
            BreakpointGraph graph = new BreakpointGraph();
            Neighbours ordNeigbours = graph.addGenome(readFile(pathOrd).get(0), "ord");
            Neighbours wgdNeigbours = graph.addGenome(readFile(pathWGD).get(0), "wgd");


            Solver solver = GGHPMain.tryToSolve(ordNeigbours, wgdNeigbours, -1, false);
            System.out.println(solver.getCurrentSolution());
            ArrayList<Graph.Edge> edges = solver.getCurrentSolution().getResultMatching();
            Genome result = graph.convertToGenome(edges);
            System.out.println(result);

            StringWriter writer = new StringWriter();
            Grimm.Writer.write(new BufferedWriter(writer), Arrays.asList(result));
            System.out.println(writer.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
