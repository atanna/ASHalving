package algo.ggap.graph;

import java.util.ArrayList;


public class BaseGenome {

    protected Neighbours neighbours;
    private int cyclesCount = 0;
    public ArrayList<Graph.Edge> result;

    public BaseGenome(Neighbours neighbours, int cyclesCount, ArrayList<Graph.Edge> result) {
        this.neighbours = neighbours;
        this.cyclesCount = cyclesCount;
        this.result = new ArrayList<>(result);
    }

    public int getDegree() {
        return neighbours.getDegree();
    }

    public Neighbours getNeighbours() {
        return neighbours;
    }

    public int getCyclesCount() {
        return cyclesCount;
    }


    @Override
    public String toString() {
        String result = "cyclesCount: " + String.valueOf(cyclesCount) + "\n";
        result += String.valueOf(neighbours);
        result += "result " + this.result;

        return result + "\n";
    }
}

