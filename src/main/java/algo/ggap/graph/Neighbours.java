package algo.ggap.graph;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Neighbours implements Serializable {
    public ArrayList<ArrayList<Integer>> neighbours;
    public HashSet<Integer> removedVertices;
    public ArrayList<Integer> indexes;
    int degree;

    public Neighbours(int degree) {
        this.degree = degree;
        neighbours = new ArrayList<>();
        this.removedVertices = new HashSet<>();
        this.indexes = new ArrayList<>();
        postInit();
    }

    public Neighbours(ArrayList<ArrayList<Integer>> neighbours, int degree) {
        this.neighbours = neighbours;
        this.degree = degree;
        this.removedVertices = new HashSet<>();
        postInit();
    }

    public Neighbours(ArrayList<ArrayList<Integer>> neighbours, int degree, ArrayList<Integer> indexes,
            HashSet<Integer> removedVertices)
    {
        this(neighbours, degree);
        this.neighbours = neighbours;
        this.removedVertices = removedVertices;
        this.indexes = indexes;
    }

    private void postInit() {
        indexes = new ArrayList<>();
        for (int i = 0; i < neighbours.size(); ++i) {
            indexes.add(i);
        }
    }

    public Neighbours getCopy() {
        ArrayList<ArrayList<Integer>> newNeighbours = new ArrayList<>(neighbours.size());

        for (ArrayList<Integer> n : neighbours) {
            newNeighbours.add(new ArrayList<>(n));
        }

        return new Neighbours(newNeighbours, degree, new ArrayList<>(indexes), new HashSet<>(removedVertices));
    }

    public int getRealSize() {
        return indexes.size();
    }

    public Stream<Integer> getVertexNeighbours(int vertex) {
        if (!removedVertices.isEmpty()) {
            if (removedVertices.contains(vertex)) {
                return Stream.of();
            }
            return neighbours.get(vertex).stream().filter(v -> !removedVertices.contains(v));
        }
        return neighbours.get(vertex).stream();
    }

    public Stream<Integer> getSortedVertexNeighbours(int vertex) {
        return getVertexNeighbours(vertex).sorted();
    }

    public void lazyReconstruct(ArrayList<Integer> removedVertices, ArrayList<Graph.Edge> addedEdges, Graph.Edge edge) {
        ArrayList<Integer> sources = new ArrayList<>();
        ArrayList<Integer> targets = new ArrayList<>();
        for (Graph.Edge addedEdge : addedEdges) {
            if (addedEdge.source == edge.source && addedEdge.target != edge.target) {
                sources.add(addedEdge.target);
            }
            if (addedEdge.source != edge.source && addedEdge.target == edge.target) {
                targets.add(addedEdge.source);
            }
        }
        assert sources.size() != 0 || sources.size() != 2;
        assert targets.size() != 0 || targets.size() != 2;
        if (sources.size() == 2) {
            addedEdges.add(new Graph.Edge(sources.get(0), sources.get(1)));
        }
        if (targets.size() == 2) {
            addedEdges.add(new Graph.Edge(targets.get(0), targets.get(1)));
        }

        lazyReconstruct(removedVertices, addedEdges);
    }


    public void lazyReconstruct(ArrayList<Integer> removedVertices, ArrayList<Graph.Edge> addedEdges) {
        this.removedVertices.addAll(removedVertices);
        for (Graph.Edge edge : addedEdges) {
            neighbours.get(edge.source).add(edge.target);
            neighbours.get(edge.target).add(edge.source);
        }
    }

    public void forceReconstruct(ArrayList<Integer> removedVertices, ArrayList<Graph.Edge> addedEdges) {
        lazyReconstruct(removedVertices, addedEdges);
        applyForceReconstruction();
    }


    public void applyForceReconstruction() {
        ArrayList<Integer> indexes = new ArrayList<>(neighbours.size());
        int currentIndex = 0;
        int nullIndex = -1;
        for (int i = 0; i < neighbours.size(); ++i) {
            indexes.add(nullIndex);
            if (!removedVertices.contains(i)) {
                indexes.set(i, currentIndex);
                currentIndex++;
            }
        }
        int newSize = (int) (neighbours.size()) - (int) removedVertices.size();
        ArrayList<ArrayList<Integer>> newNeighbours = new ArrayList<>(newSize);
        for (int i = 0; i < newSize; ++i) {
            newNeighbours.add(new ArrayList<>());
        }


        for (int vertex = 0; vertex < neighbours.size(); ++vertex) {
            if (indexes.get(vertex) == nullIndex) {
                continue;
            }
            for (int neighbour : neighbours.get(vertex)) {
                if (vertex > neighbour) {
                    continue;
                }
                if (indexes.get(neighbour) == nullIndex) {
                    continue;
                }
                Graph.Edge edge = new Graph.Edge(indexes.get(vertex), indexes.get(neighbour));
                newNeighbours.get(edge.source).add(edge.target);
                if (edge.source != edge.target) {
                    newNeighbours.get(edge.target).add(edge.source);
                }
            }
        }

        this.removedVertices.clear();
        this.neighbours = newNeighbours;
        ArrayList<Integer> newIndexes = new ArrayList<>(newSize);
        for (int i = 0; i < newSize; ++i) {
            newIndexes.add(nullIndex);
        }

        for (int i = 0; i < indexes.size(); ++i) {
            if (indexes.get(i) != nullIndex) {
                newIndexes.set(indexes.get(i), this.indexes.get(i));
            }
        }
        this.indexes = newIndexes;
    }

    public int getDegree() {
        return degree;
    }

    @Override
    public String toString() {

        String neighbourString = "";
        for (ArrayList<Integer> n : neighbours) {
            neighbourString += "\n" + String.valueOf(n);
        }
        String result = String.join("\n", Arrays.asList(
                "degree: " + degree,
                "neighbours: " + neighbourString,
                "removed: " + removedVertices,
                "indexes: " + indexes
        ));
        return result;
    }
}


