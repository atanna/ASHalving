package bio.graph;

import java.util.ArrayList;
import java.util.HashMap;

import algo.ugap.graph.Neighbours;
import bio.genome.Chromosome;
import bio.genome.Genome;
import bio.genome.DirectedGene;

public class GenomeGraph {
    public static class Vertex {
        private String name = "";

        public Vertex(String name) {
            this.name = name;
        }

        public Vertex(DirectedGene.HalfGene vertex) {
            this(vertex.getName());
        }

        public String getName() {
            return name;
        }

        boolean equals(Vertex vertex) {
            return vertex.getName().equals(getName());
        }
    }

    public static class Indexes {
        HashMap<Integer, String> idToVertex;
        HashMap<String, Integer> vertexToId;

        public Indexes(HashMap<Integer, String> idToVertex, HashMap<String, Integer> vertexToId) {
            this.idToVertex = idToVertex;
            this.vertexToId = vertexToId;
        }

        public int getSize() {
            return idToVertex.size() / 2;
        }
    }

    private HashMap<String, Vertex> vertices;
    private HashMap<Vertex, ArrayList<Vertex>> edges;


    public Vertex getVertex(String name) {
        if (!vertices.containsKey(name)) {
            vertices.put(name, new Vertex(name));
        }
        return vertices.get(name);
    }

    public Vertex getVertex(DirectedGene.HalfGene gene) {
        return getVertex(gene.getName());
    }

    public GenomeGraph(Genome genome) {
        edges = new HashMap<>();
        vertices = new HashMap<>();

        for (Chromosome chromosome : genome.getChromosomes()) {
            Vertex previousVertex = null;
            Vertex firstVertex = null;
            for (DirectedGene gene : chromosome.getGenes()) {
                Vertex vertexFrom = getVertex(gene.getHead());
                Vertex vertexTo = getVertex(gene.getTail());
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

    public Indexes createIndexes() {
        HashMap<Integer, String> idToVertex = new HashMap<>();
        HashMap<String, Integer> vertexToId = new HashMap<>();
        int maxCurrentID = 0;
        for (Vertex vertex : vertices.values()) {
            int id = vertexToId.getOrDefault(vertex.name, maxCurrentID);
            if (id == maxCurrentID) {
                maxCurrentID++;
            }
            vertexToId.put(vertex.name, id);
            idToVertex.put(id, vertex.name);
        }
        return new Indexes(idToVertex, vertexToId);
    }

    public Neighbours getNeighbours(Indexes indexes) {
        ArrayList<ArrayList<Integer>> nbrs = new ArrayList<>(vertices.size());
        int degree = 0;
        for (int i = 0; i < vertices.size(); ++i) {
            ArrayList<Integer> arr = new ArrayList<>();
            String vertex = indexes.idToVertex.get(i);
            degree = 0;
            for (Vertex target : getAdjacentVertices(vertex)) {
                arr.add(indexes.vertexToId.get(target.name));
                degree++;
            }
            nbrs.add(arr);
        }
        return new Neighbours(nbrs, degree);
    }

    private void addEdge(String first, String second) {
        addEdge(getVertex(first), getVertex(second));
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

    public ArrayList<Vertex> getAdjacentVertices(String name) {
        return edges.get(getVertex(name));
    }

}
