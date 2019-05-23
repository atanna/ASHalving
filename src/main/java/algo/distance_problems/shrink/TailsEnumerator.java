package algo.distance_problems.shrink;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import algo.graph.Graph;

public class TailsEnumerator {
    private ArrayList<Integer> sources;
    private ArrayList<Integer> targets;
    private ArrayList<Integer> used;
    private ArrayList<ArrayList<Graph.Edge>> result;
    private int parallelEdgesCount;
    private ArrayList<Integer> currentArray;
    private Graph.OrderedEdge edge;

    public TailsEnumerator(
            Stream<Integer> streamSources,
            Stream<Integer> streamTargets,
            Graph.OrderedEdge edge)
    {

        final AtomicInteger parallelEdgesCount = new AtomicInteger(0);
        final AtomicInteger sourceLoopsCount = new AtomicInteger(0);
        sources = streamSources.peek(source -> {
            if (source == edge.source) {
                sourceLoopsCount.getAndIncrement();
            }
            if (source == edge.target) {
                parallelEdgesCount.getAndIncrement();
            }
        }).filter(source -> source != edge.target).sorted().collect(Collectors.toCollection(ArrayList::new));

        final AtomicInteger targetParallelEdgesCount = new AtomicInteger(0);
        final AtomicInteger targetLoopsCount = new AtomicInteger(0);
        targets = streamTargets.peek(target -> {
            if (target == edge.target) {
                targetLoopsCount.getAndIncrement();
            }
            if (target == edge.source) {
                targetParallelEdgesCount.getAndIncrement();
            }
        }).filter(target -> target != edge.source).sorted().collect(Collectors.toCollection(ArrayList::new));

        assert sourceLoopsCount.get() % 2 == 0;
        assert targetLoopsCount.get() % 2 == 0;

        this.parallelEdgesCount = parallelEdgesCount.get();
        this.edge = edge;

        assert sources.size() == targets.size();
        assert parallelEdgesCount.get() == targetParallelEdgesCount.get();

    }

    public ArrayList<ArrayList<Graph.Edge>> enumerate() {
        // return all available cases to connect sources with targets without repetition
        // every case coded as permutation of targets indexes for sources

        result = new ArrayList<>();
        used = new ArrayList<>(sources.size());
        currentArray = new ArrayList<>(sources.size());
        for (int i = 0; i < sources.size(); i++) {
            used.add(0);
            currentArray.add(-1);
        }

        enumerate(0);
        return result;
    }

    private void collectCurrentResult() {
        ArrayList<Graph.Edge> edges = new ArrayList<>(sources.size());
        for (int i = 0; i < parallelEdgesCount; ++i) {
            edges.add(new Graph.Edge(edge.source, edge.target));
        }

        ArrayList<Integer> loopConnectedSources = new ArrayList<>();
        ArrayList<Integer> loopConnectedTargets = new ArrayList<>();

        for (int i = 0; i < sources.size(); ++i) {
            int source = sources.get(i);
            int target = targets.get(currentArray.get(i));
            boolean skipEdge = false;

            if (source == this.edge.source) {
                loopConnectedTargets.add(target);
                skipEdge = true;
            }
            if (target == this.edge.target) {
                loopConnectedSources.add(source);
                skipEdge = true;
            }
            if (!skipEdge) {
                edges.add(new Graph.Edge(source, target));
            }
        }
        assert loopConnectedSources.size() % 2 == 0;
        assert loopConnectedTargets.size() % 2 == 0;

        for (int i = 0; i + 1 < loopConnectedSources.size(); i += 2) {
            int source = loopConnectedSources.get(i);
            int target = loopConnectedSources.get(i + 1);
            if (source == edge.source && target == edge.source) {
                target = edge.target;
            } else if (source == edge.source || target == edge.source) {
                // invalid case, false branch
                return;
            }
            edges.add(new Graph.Edge(source, target));
        }

        for (int i = 0; i + 1 < loopConnectedTargets.size(); i += 2) {
            int source = loopConnectedTargets.get(i);
            int target = loopConnectedTargets.get(i + 1);
            if (source == edge.target && target == edge.target) {
                source = edge.source;
                // this loop we added early
                continue;
            } else if (source == edge.target || target == edge.target) {
                // invalid case, false branch
                return;
            }
            edges.add(new Graph.Edge(source, target));
        }

        result.add(edges);
    }

    private void enumerate(int index) {
        if (index == sources.size()) {
            collectCurrentResult();
            return;
        }
        int previousTarget = -1;
        int startIndex = 0;
        if (index > 0 && sources.get(index - 1).equals(sources.get(index))) {
            startIndex = currentArray.get(index - 1) + 1;
        }
        for (int i = startIndex; i < targets.size(); ++i) {
            int target = targets.get(i);
            if (used.get(i) > 0 || previousTarget == target) {
                continue;
            }
            used.set(i, 1);
            currentArray.set(index, i);
            enumerate(index + 1);

            used.set(i, 0);
            previousTarget = target;
        }
    }
}
