package io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import genome.Genome;
import org.junit.Test;


import static org.junit.Assert.*;

public class GrimmTest {
    @Test
    public void testReader() throws IOException {
        String test = ">Genome1\n"
                + "C: 1 -2 3 @\n"
                + "C: 4 5 @\n"
                + ">Genome2\n"
                + "C: 1 2 3 4 5 @\n";
        BufferedReader in = new BufferedReader(new StringReader(test));
        ArrayList<Genome> genomes = Grimm.Reader.parse(in);

        assertEquals(2, genomes.size());
        assertEquals(5, genomes.get(0).getSize());
        assertEquals(5, genomes.get(1).getSize());
    }
}