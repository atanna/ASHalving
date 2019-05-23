package algo.graph;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class GraphTest {
    @Test
    public void testEdgeEquality() {
        assertEquals(new Graph.Edge(0, 1), new Graph.Edge(1, 0));
        assertNotEquals(new Graph.Edge(0, 1), new Graph.Edge(1, 0, 4));
    }

}