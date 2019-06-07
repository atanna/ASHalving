package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import algo.graph.Graph;
import algo.graph.Neighbours;
import genome.Chromosome;
import genome.DirectedGene;
import genome.Genome;

public class BreakpointGraph {
    private HashMap<String, GenomeGraph> graphs = new HashMap<>();
    private GenomeGraph.Indexes indexes;
    private HashMap<String, Neighbours> nbrs = new HashMap<>();


    public Neighbours addGenome(Genome genome) throws GenomeGraphException {
        return addGenome(genome, genome.getName());
    }

    public Neighbours addGenome(Genome genome, String name) throws GenomeGraphException {
        if (graphs.containsKey(name)) {
            throw new GenomeGraphException("Genomes must have different names");
        }
        GenomeGraph genomeGraph = new GenomeGraph(genome);
        graphs.put(name, genomeGraph);

        if (indexes == null) {
            indexes = genomeGraph.createIndexes();
            nbrs = new HashMap<>();
        }

        nbrs.put(name, genomeGraph.getNeighbours(indexes));
        return getNeighbours(name);
    }

    public Neighbours getNeighbours(String name) {
        return nbrs.get(name);
    }

    public Genome convertToGenome(ArrayList<Graph.Edge> edges) {
        return convertToGenome(edges, "Genome");
    }

    public Genome convertToGenome(ArrayList<Graph.Edge> edges, String name) {
        Genome genome = new Genome(name);

        HashMap<String, String> next = new HashMap<>();
        for (Graph.Edge edge : edges) {
            String source = indexes.idToVertex.get(edge.source);
            String target = indexes.idToVertex.get(edge.target);

            next.put(source, target);
            next.put(target, source);
        }
        HashSet<String> used = new HashSet<>();
        ArrayList<DirectedGene> arr = new ArrayList<>();
        for (Map.Entry<String, String> edge : next.entrySet()) {

            String half = edge.getKey();

            while (!used.contains(half)) {
                used.add(half);
                String target = next.get(half);
                DirectedGene.HalfGene targetHalfGene = DirectedGene.HalfGene.convertToHalf(target);
                int sign = 1;
                if (targetHalfGene.isHead()) {
                    sign = -1;
                }
                arr.add(new DirectedGene(targetHalfGene.getGeneName(), sign));
                half = targetHalfGene.getOppositeHalf().getName();
            }
            if (arr.size() > 0) {
                genome.addChromosome(new Chromosome(arr, true));
                arr = new ArrayList<>();
            }
        }

        return genome;
    }
}
