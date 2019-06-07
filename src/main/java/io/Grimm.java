package io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import genome.Chromosome;
import genome.DirectedGene;
import genome.Genome;

public class Grimm {

    public static class Reader {

        public static ArrayList<Genome> parse(BufferedReader in) throws IOException {
            ArrayList<Genome> genomes = new ArrayList<Genome>();
            Genome currGenome = null;


            for (String line; (line = in.readLine()) != null; ) {

                if (line.length() == 0 || line.startsWith("#")) {
                    continue;
                } else if (line.startsWith(">")) {
                    if (currGenome != null) {
                        if (currGenome.isEmpty()) throw new IOException("Cannot parse empty genomes");
                        else genomes.add(currGenome);
                    }
                    currGenome = new Genome(line.substring(1));
                } else {
                    ArrayList<DirectedGene> genes = new ArrayList<>();
                    for (String label: line.split(" ")) {
                        if (label.equals("C:")) {
                            continue;
                        }
                        if (label.equals("$") || label.equals("@")) {
                            if (!genes.isEmpty()) {
                                currGenome.addChromosome(new Chromosome(genes, label.equals("@")));
                                genes = null;
                            } else {
                                throw new IOException("Cannot parse empty chromosomes");
                            }
                            break;
                        }
                        if (label.startsWith("-")) {
                            genes.add(new DirectedGene(label.substring(1),-1));
                        } else {
                            genes.add(new DirectedGene(label));
                        }
                    }

                    if (genes != null) {
                        if (genes.isEmpty()) throw new IOException("Cannot parse empty genomes");
                        else throw new IOException("Chromosomes must end in '@' or '$'");
                    }

                }

            }
            if (currGenome != null) {
                if (currGenome.getSize() > 0) genomes.add(currGenome);
                else throw new IOException("Cannot parse empty genomes");

                return genomes;
            } else {
                throw new IOException("No genomes found in file");
            }
        }

        public static ArrayList<Genome> readFile(String fileName) throws IOException {
            try (BufferedReader in = new BufferedReader(new FileReader(fileName))) {
                return Reader.parse(in);
            }
        }

    }

    public static class Writer {

        public static void write(BufferedWriter writer, List<Genome> genomes) {
            write(writer, genomes, "");
        }

        public static void write(BufferedWriter writer, List<Genome> genomes, String comment) {
            try {
                if (comment.length() > 0) {
                    writer.append("# ").append(comment).append("\n\n");
                }
                for (Genome genome : genomes) {
                    writer.append(">").append(genome.getName()).append("\n");
                    for (Chromosome chromosome : genome.getChromosomes()) {
                        for (DirectedGene gene : chromosome.getGenes()) {
                            writer.append(gene.toString()).append(" ");
                        }
                        if (chromosome.isCyclical()) {
                            writer.append("@\n");
                        } else {
                            writer.append("$\n");
                        }
                    }
                }
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        public static void writeToFile(String fileName, Genome genome) {
            writeToFile(fileName, genome, "");
        }

        public static void writeToFile(String fileName, List<Genome> genomes) {
            writeToFile(fileName, genomes, "");
        }

        public static void writeToFile(String fileName, Genome genome, String comment) {
            writeToFile(fileName, Arrays.asList(genome), comment);

        }

        public static void writeToFile(String fileName, List<Genome> genomes, String comment) {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
                write(writer, genomes, comment);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

}