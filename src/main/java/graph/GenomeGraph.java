package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.SplittableRandom;

import genome.Chromosome;
import genome.DirectedGene;
import genome.Genome;

public class GenomeGraph {
    public static class Vertex {
        private String name = "";

        public Vertex(String name) {
            this.name = name;
        }

        public Vertex(DirectedGene.Gene.HalfGene vertex) {
            this(vertex.getName());
        }

        public String getName() {
            return name;
        }

        boolean equals(Vertex vertex) {
            return vertex.getName().equals(getName());
        }
    }

    private HashMap<String, Vertex> vertices;
    private HashMap<Vertex, ArrayList<Vertex>> edges;

    public GenomeGraph(Genome genome) {
        edges = new HashMap<>();
        vertices = new HashMap<>();
        for (Chromosome chromosome : genome.getChromosomes()) {
            Vertex previousVertex = null;
            Vertex firstVertex = null;
            for (DirectedGene gene : chromosome.getGenes()) {
                Vertex vertexFrom = new Vertex(gene.getHead());
                Vertex vertexTo = new Vertex(gene.getTail());
                addVertex(vertexFrom);
                addVertex(vertexTo);
                if (previousVertex != null) {
                    addEdge(previousVertex, vertexFrom);
                } else {
                    firstVertex = vertexFrom;
                }
                previousVertex = vertexTo;
            }
            if (chromosome.isCyclical()) {
                addEdge(firstVertex, previousVertex);
            }
         }
    }

    private void addVertex(Vertex vertex) {
        if (!vertices.containsKey(vertex.getName())) {
            vertices.put(vertex.getName(), vertex);
        }
    }

    private void addEdge(Vertex first, Vertex second) {
        addDirectedEdge(first, second);
        addDirectedEdge(second, first);
    }

    private void addDirectedEdge(Vertex first, Vertex second) {
        if (!edges.containsKey(first)) {
            edges.put(first, new ArrayList<>());
        }
        edges.get(first).add(second);
    }

    public int getSize() {
        return edges.size();
    }

    public HashMap<Vertex, ArrayList<Vertex>> getEdges() {
        return edges;
    }

    public HashMap<String, Vertex> getVertices() {
        return vertices;
    }

    public Vertex getVertex(String name) {
        return vertices.get(name);
    }

    public ArrayList<Vertex> getAdjacentVertices(String name) {
        return edges.get(getVertex(name));
    }

}
