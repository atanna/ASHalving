package algo.ugap.graph;

import java.util.ArrayList;

public class DuplicatedGenome extends BaseGenome {
    protected TwoRegularNeighbours neighbours;

    public DuplicatedGenome(ArrayList<ArrayList<Integer>> nbrs) {
        this(new TwoRegularNeighbours(nbrs));
    }

    public DuplicatedGenome(TwoRegularNeighbours neighbours) {
        super(neighbours);
        this.neighbours = neighbours;
    }

    public TwoRegularNeighbours getNeighbours() {
        return neighbours;
    }

    public int getEvenCyclesCount() {
        return neighbours.getEvenCyclesCount();
    }

    public int recountEvenCyclesCount() {
        return neighbours.recountEvenCyclesCount();
    }

    public DuplicatedGenome getCopy() throws GenomeException {
        return new DuplicatedGenome(neighbours.getCopy());
    }
}
