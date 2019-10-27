package algo.guided_problems.gghp.detector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import algo.solver.BaseDetector;
import algo.graph.DuplicatedGenome;
import algo.graph.Graph;
import algo.graph.OrdinaryGenome;
import algo.graph.TwoRegularNeighbours;
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
        DuplicatedGenome baseGenome = new DuplicatedGenome(new TwoRegularNeighbours(neighbours));


        OrdinaryGenome guidedGenome = new OrdinaryGenome(Arrays.asList(4, 2, 1, 5, 0, 3, 7, 6));
        GGHPGraph graph = new GGHPGraph(baseGenome, guidedGenome);

        Detector detector = new Detector(graph, false);
        List<BaseDetector.Branch> branches = detector.search();
        assertEquals(1, branches.size());
        BaseDetector.Branch branch = branches.get(0);
        assertEquals(4, branch.getResultedEdges().size());
        // 4
        assertEquals(new Graph.Edge(1, 2), branch.getResultedEdges().get(0));

        // 3
        assertEquals(new Graph.Edge(6, 7), branch.getResultedEdges().get(1));
        assertEquals(9, branch.getCyclesCount());
    }

    @Test
    public void searchExplicit4Branch() throws Exception {
        ArrayList<ArrayList<Integer>> neighbours = new ArrayList<>();
        neighbours.add(new ArrayList<>(Arrays.asList(1, 2)));
        neighbours.add(new ArrayList<>(Arrays.asList(2, 0)));
        neighbours.add(new ArrayList<>(Arrays.asList(0, 1)));

        neighbours.add(new ArrayList<>(Arrays.asList(3, 3)));

        neighbours.add(new ArrayList<>(Arrays.asList(5, 11)));
        neighbours.add(new ArrayList<>(Arrays.asList(4, 6)));
        neighbours.add(new ArrayList<>(Arrays.asList(5, 7)));
        neighbours.add(new ArrayList<>(Arrays.asList(6, 8)));
        neighbours.add(new ArrayList<>(Arrays.asList(7, 9)));
        neighbours.add(new ArrayList<>(Arrays.asList(8, 10)));
        neighbours.add(new ArrayList<>(Arrays.asList(9, 11)));
        neighbours.add(new ArrayList<>(Arrays.asList(10, 4)));

        DuplicatedGenome baseGenome = new DuplicatedGenome(new TwoRegularNeighbours(neighbours));

        OrdinaryGenome guidedGenome = new OrdinaryGenome(Arrays.asList(5, 4, 3, 2, 1, 0, 11, 9, 10, 7, 8, 6));
        GGHPGraph graph = new GGHPGraph(baseGenome, guidedGenome);

        Detector detector = new Detector(graph, false);
        List<BaseDetector.Branch> branches = detector.search();
        assertEquals(1, branches.size());
        assertEquals(new HashSet<Integer>(Arrays.asList(0, 1, 2, 3, 7, 8, 9, 10)), branches.get(0).getRemovedVertices());
    }

    @Test
    public void searchNotExplicit4Branches() throws Exception {
        ArrayList<ArrayList<Integer>> neighbours = new ArrayList<>();
        neighbours.add(new ArrayList<>(Arrays.asList(1, 3)));
        neighbours.add(new ArrayList<>(Arrays.asList(0, 2)));
        neighbours.add(new ArrayList<>(Arrays.asList(1, 3)));
        neighbours.add(new ArrayList<>(Arrays.asList(0, 2)));

        neighbours.add(new ArrayList<>(Arrays.asList(5, 7)));
        neighbours.add(new ArrayList<>(Arrays.asList(4, 6)));
        neighbours.add(new ArrayList<>(Arrays.asList(5, 7)));
        neighbours.add(new ArrayList<>(Arrays.asList(4, 6)));

        DuplicatedGenome baseGenome = new DuplicatedGenome(new TwoRegularNeighbours(neighbours));

        OrdinaryGenome guidedGenome = new OrdinaryGenome(Arrays.asList(5, 6,7,4,3,0,1,2));
        GGHPGraph graph = new GGHPGraph(baseGenome, guidedGenome);

        Detector detector = new Detector(graph, false);
        List<BaseDetector.Branch> branches = detector.search();
        assertEquals(2, branches.size());
    }
}