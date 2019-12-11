package algo.ugap.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class OrdinaryGenome extends BaseGenome {

    public OrdinaryGenome(ArrayList<ArrayList<Integer>> neighbours) {
        super(neighbours, 1);
    }

    public OrdinaryGenome(Neighbours neighbours) {
        super(neighbours);
    }

    public OrdinaryGenome(List<Integer> matching) {
        super(convertToNeighbours(matching));
    }

    public int getNeighbour(int vertex) throws GenomeException {
        int neighbour = -1;
        for (Iterator<Integer> it = neighbours.getVertexNeighbours(vertex).iterator(); it.hasNext(); ) {
            int target = it.next();
            if (neighbour != -1) {
                throw new GenomeException("Ordinary genome cannot have more than 1 neighbour");
            }
            neighbour = target;
        }
        return neighbour;
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
