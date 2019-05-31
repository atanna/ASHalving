package algo.graph;

import java.util.HashMap;

public class Graph {

    public static class OrderedEdge {
        public int source = 0;
        public int target = 0;
        public int count = 0;

        public OrderedEdge(int source, int target, int count) {
            this.source = source;
            this.target = target;
            this.count = count;
        }

        public OrderedEdge(int source, int target) {
            this(source, target, 0);
        }

        @Override
        public String toString() {
            return "(" + String.valueOf(source) + " " + String.valueOf(target) + ")";
        }

        @Override
        public boolean equals(Object obj) {
            OrderedEdge edge = (OrderedEdge) obj;
            return edge.source == source && edge.target == target && edge.count == count;
        }
    }

    public static class Edge extends OrderedEdge {

        public Edge(int source, int target, int count) {
            super(source, target, count);

            if (source > target) {
                this.target = source;
                this.source = target;
            }
        }

        public Edge(int source, int target) {
            this(source, target, 0);
        }
    }


    protected HashMap<String, BaseGenome> genomes;


    public Graph(HashMap<String, BaseGenome> genomes) {
        this.genomes = genomes;
    }

    public Graph() {
        this.genomes = new HashMap<>();
    }

    public HashMap<String, BaseGenome> getGenomes() {
        return genomes;
    }

    @Override
    public String toString() {
        String result = String.join("\n",
                genomes.entrySet().stream().map(entry -> String.valueOf(entry.getKey() + ":\n" + String.valueOf(entry.getValue()))).toArray(String[]::new));
        return result;
    }
}
