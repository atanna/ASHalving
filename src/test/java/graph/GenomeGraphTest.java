package graph;

import java.util.ArrayList;
import java.util.Arrays;

import genome.Chromosome;
import genome.DirectedGene;
import genome.Genome;
import org.junit.Test;

import static org.junit.Assert.*;

public class GenomeGraphTest {
    @Test
    public void testConstructor() {
        Genome genome = new Genome();
        genome.addChromosome(new Chromosome(new ArrayList<DirectedGene>(Arrays.asList(
                new DirectedGene("1"),
                new DirectedGene("3", -1),
                new DirectedGene("3"))
        ), true));
        GenomeGraph graph = new GenomeGraph(genome);
        assertEquals(6, graph.getSize());

        ArrayList<GenomeGraph.Vertex> vertices =  graph.getAdjacentVertices("1_h");

        assertEquals(1, vertices.size());
        assertEquals(vertices.get(0).getName(), "3_t");
    }
}
