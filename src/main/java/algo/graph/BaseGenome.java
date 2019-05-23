package algo.graph;


public class BaseGenome {

    protected Neighbours neighbours;

    public BaseGenome(Neighbours neighbours) {
        this.neighbours = neighbours;
    }

    public int getDegree() {
        return neighbours.getDegree();
    }

    public Neighbours getNeighbours() {
        return neighbours;
    }

//    public int getCyclesCount() {
//        return cyclesCount;
//    }

    public BaseGenome getCopy() throws GenomeException {
        return new BaseGenome(neighbours.getCopy());
    }


    @Override
    public String toString() {
        String result = String.valueOf(neighbours);
        return result;
    }
}

