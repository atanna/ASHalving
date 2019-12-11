package algo.ugap.solver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import algo.ugap.graph.GenomeException;
import algo.ugap.graph.Graph;


public abstract class BaseDetector {

    public class Branch {
        private HashSet<Integer> removedVertices;
        private HashMap<String, List<Graph.Edge>> addedEdges;
        private List<Graph.Edge> resultedEdges;
        private int cyclesCount;


        public Branch() {
            removedVertices = new HashSet<>();
            addedEdges = new HashMap<>();
            resultedEdges = new ArrayList<>();
            cyclesCount = 0;
        }

        public Branch(HashMap<String, List<Graph.Edge>> addedEdges, List<Graph.Edge> resultedEdges, int cyclesCount) {
            this.addedEdges = addedEdges;
            this.resultedEdges = resultedEdges;
            ArrayList<Integer> removed = new ArrayList<>();
            for (Graph.Edge edge : resultedEdges) {
                removed.add(edge.source);
                removed.add(edge.target);
            }
            removedVertices = new HashSet<>(removed);
            this.cyclesCount = cyclesCount;
        }

        public boolean merge(Branch branch) {
            if (!isMergeable(branch)) {
                return false;
            }
            removedVertices.addAll(branch.getRemovedVertices());
            for (Map.Entry<String, List<Graph.Edge>> entry : branch.getAddedEdges().entrySet()) {
                if (!addedEdges.containsKey(entry.getKey())) {
                    addedEdges.put(entry.getKey(), new ArrayList<>());
                }
                addedEdges.get(entry.getKey()).addAll(entry.getValue());
            }
            resultedEdges.addAll(branch.getResultedEdges());
            cyclesCount += branch.getCyclesCount();
            return true;
        }

        public boolean isMergeable(Branch branch) {
            HashSet<Integer> removed = new HashSet<>(removedVertices);
            removed.addAll(branch.getRemovedVertices());
            if (removedVertices.size() + branch.getRemovedVertices().size() != removed.size()) {
                return false;
            }
            for (HashMap<String, List<Graph.Edge>> edges : Arrays.asList(addedEdges, branch.getAddedEdges())) {
                for (Map.Entry<String, List<Graph.Edge>> entry : edges.entrySet()) {
                    for (Graph.Edge edge : entry.getValue()) {
                        if (removed.contains(edge.source) || removed.contains(edge.target)) {
                            return false;
                        }
                    }
                }
            }
            return true;
        }

        public HashSet<Integer> getRemovedVertices() {
            return removedVertices;
        }

        public HashMap<String, List<Graph.Edge>> getAddedEdges() {
            return addedEdges;
        }

        public List<Graph.Edge> getResultedEdges() {
            return resultedEdges;
        }

        public void setResultedEdges(List<Graph.Edge> resultedEdges) {
            this.resultedEdges = resultedEdges;
        }

        public int getCyclesCount() {
            return cyclesCount;
        }

        public boolean isNotEmpty() {
            return resultedEdges.size() > 0;
        }

        public boolean isEmpty() {
            return !isNotEmpty();
        }
    }


    public abstract Branch searchExplicitBranch() throws DetectorException, GenomeException;

    public abstract List<Branch> search() throws Exception;

}
