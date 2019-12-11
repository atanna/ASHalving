package algo.ugap.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import algo.ugap.solver.BaseDetector;


public class GGAPGraph extends GAPGraph {
    protected OrdinaryGenome guidedGenome;
    final static String GUIDED_NAME = "guided";

    public GGAPGraph(BaseGenome baseGenome, OrdinaryGenome guidedGenome) throws GenomeException {
        super();
        this.baseGenome = baseGenome;
        this.guidedGenome = guidedGenome;
//        validate();

        genomes.put(BASE_NAME, baseGenome);
        genomes.put(GUIDED_NAME, guidedGenome);

        postInit();
    }

    public GGAPGraph(BaseGenome baseGenome, OrdinaryGenome guidedGenome, List<Integer> indexes) throws GenomeException {
        this(baseGenome, guidedGenome);
        this.indexes = indexes;
    }

    private void validate() throws GenomeException {
        if (this.baseGenome.getNeighbours().size() != this.guidedGenome.getNeighbours().size()) {
            throw new GGAPException("Sizes of genomes are not equal");
        }
    }

    private void postInit() {
        int size = baseGenome.getNeighbours().getRealSize();
        indexes = new ArrayList<>(size);
        for (int i = 0; i < size; ++i) {
            indexes.add(i);
        }
        fullDegree = 0;
//        for (BaseGenome genome : genomes.values()) {
//            fullDegree += genome.getDegree();
//        }
    }

    public BaseGenome getBaseGenome() {
        return baseGenome;
    }

    public OrdinaryGenome getGuidedGenome() {
        return guidedGenome;
    }

    public static HashMap<String, List<Edge>> convertToEdgesMap(List<Edge> baseEdges, List<Edge> guidedEdges) {
        HashMap<String, List<Edge>> edges = new HashMap<>();
        edges.put(BASE_NAME, baseEdges);
        edges.put(GUIDED_NAME, guidedEdges);
        return edges;
    }

    public static HashMap<String, List<Edge>> convertToEdgesMap(List<Edge> baseEdges) {
        HashMap<String, List<Edge>> edges = new HashMap<>();
        edges.put(BASE_NAME, baseEdges);
        return edges;
    }

    public void lazyReconstruct(BaseDetector.Branch branch) {
        ArrayList<Integer> removed = new ArrayList<>(branch.getRemovedVertices());

        baseGenome.getNeighbours().lazyReconstruct(removed, branch.getAddedEdges().getOrDefault(BASE_NAME, new ArrayList<>()));
        guidedGenome.getNeighbours().lazyReconstruct(removed, branch.getAddedEdges().getOrDefault(GUIDED_NAME, new ArrayList<>()));
    }

    public void pushReconstruction() {
        baseGenome.getNeighbours().applyForceReconstruction();
        guidedGenome.getNeighbours().applyForceReconstruction();
        indexes = baseGenome.getNeighbours().indexes;
    }

    public GGAPGraph getCopy() throws GenomeException {
        GGAPGraph
                graph = new GGAPGraph(baseGenome.getCopy(), guidedGenome.getCopy(), new ArrayList<>(indexes));
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
