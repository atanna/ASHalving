package algo.guided_problems.gghp.detector;

import java.util.ArrayList;
import java.util.Arrays;

import algo.distance_problems.detector.BaseAdequateSubgraph;
import algo.graph.BaseGenome;
import algo.graph.Graph;
import algo.graph.Neighbours;
import algo.graph.OrdinaryGenome;
import algo.guided_problems.GGHPGraph;
import org.junit.Test;

import static org.junit.Assert.*;

public class DetectorTest {

    @Test
    public void searchExplicitBranch() throws Exception {
        ArrayList<ArrayList<Integer>> neighbours = new ArrayList<>();
        neighbours.add(new ArrayList<>(Arrays.asList(5, 3)));
        neighbours.add(new ArrayList<>(Arrays.asList(1, 1)));
        neighbours.add(new ArrayList<>(Arrays.asList(2, 2)));
        neighbours.add(new ArrayList<>(Arrays.asList(0, 4)));
        neighbours.add(new ArrayList<>(Arrays.asList(3, 5)));
        neighbours.add(new ArrayList<>(Arrays.asList(0, 4)));
        neighbours.add(new ArrayList<>(Arrays.asList(7, 7)));
        neighbours.add(new ArrayList<>(Arrays.asList(6, 6)));
        BaseGenome baseGenome = new BaseGenome(new Neighbours(neighbours, 2));


        OrdinaryGenome guidedGenome = new OrdinaryGenome(Arrays.asList(4, 2, 1, 5, 0, 3, 7, 6));
        GGHPGraph graph = new GGHPGraph(baseGenome, guidedGenome);

        Detector detector = new Detector(graph);
        ArrayList<BaseAdequateSubgraph.Branch> branches = detector.search();
        assertEquals(1, branches.size());
        BaseAdequateSubgraph.Branch branch = branches.get(0);
        assertEquals(2, branch.getResultedEdges().size());
        // 4
        assertEquals(new Graph.Edge(1, 2), branch.getResultedEdges().get(0));

        // 3
        assertEquals(new Graph.Edge(6, 7), branch.getResultedEdges().get(1));
        assertEquals(5, branch.getCyclesCount());
    }
}