package algo.ugap.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;

public class TwoRegularNeighbours extends Neighbours {
    private HashMap<Integer, Integer> cycles;
    protected int cyclesCount = 0;
    protected int evenCyclesCount = 0;

    private HashSet<Integer> evenCycles;
    private HashSet<Integer> oddCycles;


    public TwoRegularNeighbours() {
        super(2);
        init();
    }

    public TwoRegularNeighbours(ArrayList<ArrayList<Integer>> neighbours) {
        super(neighbours, 2);
        init();
    }

    public TwoRegularNeighbours(ArrayList<ArrayList<Integer>> neighbours, ArrayList<Integer> indexes,
            HashSet<Integer> removedVertices) throws GenomeException
    {
        super(neighbours, 2, indexes, removedVertices);
//        init();
    }

    private void init() {
        labeledCycles();
    }

    public int getNextCycleVertex(int prevVertex, int vertex) {
        int nextVertex = vertex;
        for (Iterator<Integer> it = getVertexNeighbours(vertex).iterator(); it.hasNext(); ) {
            nextVertex = it.next();
            if (nextVertex != prevVertex) {
                return nextVertex;
            }
        }
        return nextVertex;
    }

    private void labeledCycles() {
        cycles = new HashMap<>();
        evenCycles = new HashSet<>();
        oddCycles = new HashSet<>();
        int label = 0;
        System.out.println("(for duplicated genome)");
        System.out.print("cycle sizes: ");
        for (Iterator<Integer> it = getVertices().iterator(); it.hasNext(); ) {
            int vertex = it.next();
            if (cycles.containsKey(vertex)) {
                continue;
            }
            int count = 0;
            Optional<Integer> nextVertex = Optional.of(vertex);
            while (nextVertex.isPresent()) {
                count++;
                vertex = nextVertex.get();
                cycles.put(vertex, label);
                nextVertex = Optional.empty();
                for (Iterator<Integer> iter = getVertexNeighbours(vertex).iterator(); iter.hasNext(); ) {
                    int neighbour = iter.next();
                    if (cycles.containsKey(neighbour)) {
                        continue;
                    }
                    nextVertex = Optional.of(neighbour);
                }


            }
            System.out.print(count + " ");
            if (count % 2 == 0) {
//                System.out.println(label + ": " + count);
                evenCycles.add(label);
            } else {
                oddCycles.add(label);
            }
            label++;
        }
        System.out.println();
        cyclesCount = label;
        evenCyclesCount = evenCycles.size();
        System.out.println("cycles count: " + cyclesCount + "   even: " + evenCyclesCount);
        System.out.println();
        System.out.println("n = " + neighbours.size());
    }

    public int recountEvenCyclesCount() {
        labeledCycles();
        return evenCyclesCount;
    }

    public int getEvenCyclesCount() {
        return evenCyclesCount;
    }

    public int getCyclesCount() {
        return cyclesCount;
    }

    public TwoRegularNeighbours getCopy() throws GenomeException {
        Neighbours nbrs = super.getCopy();
        return new TwoRegularNeighbours(nbrs.neighbours, nbrs.indexes, nbrs.removedVertices);
    }
}
