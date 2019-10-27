package algo.guided_problems;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import algo.solver.BaseDetector;
import algo.graph.BaseGenome;
import algo.graph.GenomeException;
import algo.graph.Graph;


public class GAPGraph extends Graph {
    protected List<Integer> indexes;
    protected BaseGenome baseGenome;
    protected int fullDegree = 0;

    final static String BASE_NAME = "base";

    public int getFullDegree() {
        return fullDegree;
    }

    public GAPGraph() {}


    public GAPGraph(BaseGenome baseGenome) {
        super();
        this.baseGenome = baseGenome;
        genomes.put(BASE_NAME, baseGenome);

        postInit();
    }

    public GAPGraph(BaseGenome baseGenome, List<Integer> indexes) throws GenomeException {
        this(baseGenome);
        this.indexes = indexes;
    }


    private void postInit() {
        int size = baseGenome.getNeighbours().getRealSize();
        indexes = new ArrayList<>(size);
        for (int i = 0; i < size; ++i) {
            indexes.add(i);
        }
        fullDegree = 0;
        for (BaseGenome genome : genomes.values()) {
            fullDegree += genome.getDegree();
        }
    }

    public BaseGenome getBaseGenome() {
        return baseGenome;
    }


    public static HashMap<String, List<Edge>> convertToEdgesMap(List<Edge> baseEdges) {
        HashMap<String, List<Edge>> edges = new HashMap<>();
        edges.put(BASE_NAME, baseEdges);
        return edges;
    }

    public void lazyReconstruct(BaseDetector.Branch branch) {
        ArrayList<Integer> removed = new ArrayList<>(branch.getRemovedVertices());

        baseGenome.getNeighbours().lazyReconstruct(removed, branch.getAddedEdges().getOrDefault(BASE_NAME, new ArrayList<>()));
    }

    public void pushReconstruction() {
        baseGenome.getNeighbours().applyForceReconstruction();
        indexes = baseGenome.getNeighbours().indexes;
    }

    public GAPGraph getCopy() throws GenomeException {
        GAPGraph graph = new GAPGraph(baseGenome.getCopy(), new ArrayList<Integer>(indexes));
        return graph;
    }

    public int size() {
        return baseGenome.getNeighbours().size();
    }

    public List<Edge> convertToRealEdges(List<Edge> edges) {
        List<Edge> realEdges = new ArrayList<>(edges.size());
        for (Edge edge: edges) {
            realEdges.add(new Edge(getRealVertexIndex(edge.source), getRealVertexIndex(edge.target)));
        }
        return realEdges;
    }

    public int getRealVertexIndex(int vertex) {
        return indexes.get(vertex);
    }

    public List<Integer> getIndexes() {
        return indexes;
    }
}
