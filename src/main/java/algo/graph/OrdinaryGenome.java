package algo.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OrdinaryGenome extends BaseGenome {

    public OrdinaryGenome(Neighbours neighbours) {
        super(neighbours);
    }

    public OrdinaryGenome(List<Integer> matching) {
        super(convertToNeighbours(matching));
    }

    public int getNeighbour(int vertex) {
        return neighbours.neighbours.get(vertex).get(0);
    }

    private static Neighbours convertToNeighbours(List<Integer> matching) {
        ArrayList<ArrayList<Integer>> neighbours = new ArrayList<>();
        for (int vertex : matching) {
            neighbours.add(new ArrayList<>(Arrays.asList(vertex)));
        }
        return new Neighbours(neighbours, 1);
    }

    public OrdinaryGenome getCopy() throws GenomeException {
        return new OrdinaryGenome(neighbours.getCopy());
    }
}
