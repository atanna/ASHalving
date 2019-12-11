package bio.genome;

import bio.genome.DirectedGene;
import org.junit.Test;

import static org.junit.Assert.*;

public class DirectedGeneTest {

    @Test
    public void getHead() {
        DirectedGene gene = new DirectedGene("gene", 1);
        DirectedGene oppositeGene = new DirectedGene("gene", -1);

        assertEquals(gene.getHead().getName(), oppositeGene.getTail().getName());
    }

    @Test
    public void getTail() {
        DirectedGene gene = new DirectedGene("gene", 1);
        assertEquals(gene.getHead(), gene.getTail().getOppositeHalf());
    }
}