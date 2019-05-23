package algo.guided_problems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import algo.distance_problems.detector.BaseAdequateSubgraph;
import algo.graph.BaseGenome;
import algo.graph.GenomeException;
import algo.graph.Graph;
import algo.graph.OrdinaryGenome;

public class GGAPGraph extends Graph {
    protected BaseGenome baseGenome;
    protected OrdinaryGenome guidedGenome;
    protected List<Integer> indexes;

    final static String BASE_NAME = "base";
    final static String GUIDED_NAME = "guided";


    public GGAPGraph(BaseGenome baseGenome, OrdinaryGenome guidedGenome) throws GenomeException {
        super();
        this.baseGenome = baseGenome;
        this.guidedGenome = guidedGenome;
        validate();

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

    public void lazyReconstruct(BaseAdequateSubgraph.Branch branch) {
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
        GGAPGraph graph = new GGAPGraph(baseGenome.getCopy(), guidedGenome.getCopy(), new ArrayList<>(indexes));
        return graph;
    }

    public int size() throws GenomeException {
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
