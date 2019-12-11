package bio.graph;

import java.util.ArrayList;
import java.util.Arrays;

import bio.genome.Chromosome;
import bio.genome.DirectedGene;
import bio.genome.Genome;
import bio.graph.GenomeGraph;
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
        assertEquals(4, graph.getSize());

        ArrayList<GenomeGraph.Vertex> vertices =  graph.getAdjacentVertices("1_h");

        assertEquals(1, vertices.size());
        assertEquals("3_t", vertices.get(0).getName());
    }
}
