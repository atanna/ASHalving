package algo.ugap.graph;


import java.io.Serializable;
import java.util.ArrayList;

public class BaseGenome implements Serializable {

    protected Neighbours neighbours;

    public BaseGenome(ArrayList<ArrayList<Integer>> nbrs, int degree) {
        this.neighbours = new Neighbours(nbrs, degree);
    }

    public BaseGenome(Neighbours neighbours) {
        this.neighbours = neighbours;
    }

    public int getDegree() {
        return neighbours.getDegree();
    }

    public Neighbours getNeighbours() {
        return neighbours;
    }

    public BaseGenome getCopy() throws GenomeException {
        return new BaseGenome(neighbours.getCopy());
    }

    @Override
    public String toString() {
        String result = String.valueOf(neighbours);
        return result;
    }
}

