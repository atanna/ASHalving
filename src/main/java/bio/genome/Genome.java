package bio.genome;

import java.util.ArrayList;

public class Genome {
    private String name = "";
    private ArrayList<Chromosome> chromosomes = new ArrayList<Chromosome>();

    public Genome() {}

    public Genome(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isEmpty() {
        return chromosomes.isEmpty();
    }

    public void addChromosome(Chromosome chromosome) {
        chromosomes.add(chromosome);
    }

    public int getSize() {
        return chromosomes.stream().mapToInt(Chromosome::getSize).sum();
    }

    public ArrayList<Chromosome> getChromosomes() {
        return chromosomes;
    }

    @Override
    public String toString() {
        return name + ":\n" + chromosomes;
    }
}
