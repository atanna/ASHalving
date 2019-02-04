package graph;

import java.util.HashMap;

import genome.Genome;

public class BreakpointGraph {
    private HashMap<String, GenomeGraph> graphs = new HashMap<>();

    public void addGenome(Genome genome) throws GenomeGraphException {
        if (graphs.containsKey(genome.getName())) {
            throw new GenomeGraphException("Genomes must have different names");
        }
        graphs.put(genome.getName(), new GenomeGraph(genome));
    }
}
