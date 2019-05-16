package algo.ggap.graph.shrink;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

import algo.ggap.graph.Graph;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TailsEnumeratorTest {

    @Test
    public void enumerate() {
        Integer[] sourcesArr = {4, 4};
        Integer[] targetsArr = {3, 3};
        Stream<Integer> sources = Arrays.stream(sourcesArr);
        Stream<Integer> targets = Arrays.stream(targetsArr);

        ArrayList<ArrayList<Graph.Edge>> enums = new TailsEnumerator(
                sources,
                targets,
                new Graph.OrderedEdge(4, 3)
        ).enumerate();

        assertEquals(enums.size(), 1);
        assertEquals(enums.get(0).size(), 1);
        assertEquals(enums.get(0).get(0), new Graph.Edge(3, 4));

    }

    @Test
    public void testRepeatedCases() {
        // we try to avoid duplicated branches
        // so we ignore the order of target '4' for this

        Integer[] sourcesArr = {1, 2, 3};
        Integer[] targetsArr = {4, 4, 5};

        ArrayList<ArrayList<Graph.Edge>> enums = new TailsEnumerator(
                Arrays.stream(sourcesArr),
                Arrays.stream(targetsArr),
                new Graph.OrderedEdge(6, 7)
        ).enumerate();

        assertEquals(enums.size(), 3);
    }

}