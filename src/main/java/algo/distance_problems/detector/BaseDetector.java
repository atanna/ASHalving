package algo.distance_problems.detector;

import java.util.ArrayList;

import algo.graph.Graph;


public class BaseDetector {

    public static class Branch {
        public ArrayList<Graph.Edge> edges = new ArrayList<>();

        public Branch() {
        }

        public void add(int source, int target) {
            add(new Graph.Edge(source, target));
        }

        public void add(Graph.Edge edge) {
            edges.add(edge);
        }

        @Override
        public String toString() {
            return String.valueOf(edges);
        }
    }

    public static class Result {
        public ArrayList<Branch> branches = new ArrayList<>();

        public Result() {
        }

        public Branch addNewBranch() {
            return addBranch(new Branch());
        }

        public Branch addBranch(Branch branch) {
            branches.add(branch);
            return branch;
        }

        public int getSize() {
            return branches.size();
        }
    }


}
