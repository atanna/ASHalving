package io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
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
        assertEquals("Genome1", genomes.get(0).getName());
        assertEquals(5, genomes.get(1).getSize());
    }

    @Test
    public void testReader2() throws IOException {
        String test = ">Genome1\n"
                + "1 -2 3 @\n"
                + "4 5 @\n"
                + ">Genome2\n"
                + "1 2 3 4 5 @\n";
        BufferedReader in = new BufferedReader(new StringReader(test));
        ArrayList<Genome> genomes = Grimm.Reader.parse(in);

        assertEquals(2, genomes.size());
        assertEquals(5, genomes.get(0).getSize());
        assertEquals("Genome1", genomes.get(0).getName());
        assertEquals(5, genomes.get(1).getSize());
    }

    @Test
    public void testWriter() throws IOException {
        String test = ">Genome1\n"
                + "1 -2 3 @\n"
                + "4 5 @\n";
        BufferedReader in = new BufferedReader(new StringReader(test));
        ArrayList<Genome> genomes = Grimm.Reader.parse(in);
        Genome genome = genomes.get(0);
        assertEquals(1, genomes.size());
        assertEquals(5, genome.getSize());

        StringWriter writer = new StringWriter();
        Grimm.Writer.write(new BufferedWriter(writer), genomes);
        assertEquals(test, writer.toString());
    }
}