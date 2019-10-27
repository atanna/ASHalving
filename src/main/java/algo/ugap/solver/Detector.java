package algo.ugap.solver;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import algo.graph.GenomeException;
import algo.guided_problems.GAPGraph;
import algo.solver.BaseDetector;
import algo.graph.Graph;
import algo.guided_problems.GGHPGraph;


public abstract class Detector extends BaseDetector {
    protected GAPGraph graph;
    protected boolean isRestricted = false;

    public Detector(GAPGraph graph, boolean isRestricted) {
        this.graph = graph;
        this.isRestricted = isRestricted;
    }

    @Override
    public Branch searchExplicitBranch() {
        Branch branch = search2();
        updateWithExplicit4(branch);
        return branch;
    }

    public List<Branch> search() throws GenomeException {
        List<Branch> branches = new ArrayList<>();
        Branch branch = searchExplicitBranch();
        if (branch.isNotEmpty()) {
            branches.add(branch);
        } else {
            branches = searchNotExplicit4();
            if (branches.size() == 0) {
                for (Iterator<Integer> it = graph.getBaseGenome().getNeighbours().getVertices().iterator(); it
                        .hasNext(); ) {
                    int vertex = it.next();
                    branches = bruteForce(vertex);
                    break;
                }
            }
        }
        return branches;
    }

    protected abstract Branch search2();

    protected abstract void updateWithExplicit4(Branch resultedBranch);

    protected abstract List<Branch> searchNotExplicit4();

    protected ArrayList<Branch> bruteForce(int vertex) throws GenomeException {
        ArrayList<Branch> result = new ArrayList<>();
        addBranchesAlongSelfEdges(vertex, result);
        if (!isRestricted) {
            addBranchesAlongNotSelfEdges(vertex, result);
        }
        return result;
    }

    protected abstract void addBranchesAlongSelfEdges(int vertex, ArrayList<Branch> result) throws GenomeException;

    protected abstract void addBranchesAlongNotSelfEdges(int vertex, ArrayList<Branch> result) throws GenomeException;


    private Branch getBranch(List<Graph.Edge> baseEdgesAdded, List<Graph.Edge> guideEdgesAdded, List<Graph.Edge> resultedEdges, int cyclesCount) {
        return new Branch(GGHPGraph.convertToEdgesMap(baseEdgesAdded, guideEdgesAdded), resultedEdges, cyclesCount);
    }
}
