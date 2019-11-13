package algo.ugap.solver.gghp;

import algo.graph.DuplicatedGenome;
import algo.graph.OrdinaryGenome;
import algo.guided_problems.GAPGraph;
import algo.guided_problems.GGHPGraph;
import algo.solver.BaseDetector;
import algo.ugap.solver.gap3.GAP3Detector;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class GAP3DetectorTest {
    @Test
    public void search2Test() throws Exception {
        ArrayList<ArrayList<Integer>> neighbours = new ArrayList<>();
        neighbours.add(new ArrayList<>(Arrays.asList(5, 3, 4)));
        neighbours.add(new ArrayList<>(Arrays.asList(1, 1, 2)));
        neighbours.add(new ArrayList<>(Arrays.asList(2, 2, 1)));
        neighbours.add(new ArrayList<>(Arrays.asList(0, 4, 5)));
        neighbours.add(new ArrayList<>(Arrays.asList(0, 3, 5)));
        neighbours.add(new ArrayList<>(Arrays.asList(0, 4, 3)));
        DuplicatedGenome baseGenome = new DuplicatedGenome(neighbours);
        GAPGraph graph = new GAPGraph(baseGenome);

        GAP3Detector detector = new GAP3Detector(graph, false);
        BaseDetector.Branch result = detector.search2();
        int cyclesCount = result.getCyclesCount();
        assertEquals(cyclesCount, 2);
    }
}
