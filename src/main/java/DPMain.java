import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

import algo.distance_problems.Solver;
import algo.graph.BaseGenome;
import algo.graph.DuplicatedGenome;
import algo.graph.Graph;
import algo.graph.Neighbours;
import algo.graph.OrdinaryGenome;
import algo.graph.TwoRegularNeighbours;
import algo.solver.Solution;

public class DPName {
    // Distance Problem

    public static void addMatching(ArrayList<ArrayList<Integer>> nbrs, int size, int seed, List<Integer> borders) {
        HashSet<Integer> used = new HashSet<>();
        Random random = new Random(seed);
        if (nbrs.size() == 0) {
            for (int i = 0; i < size; ++ i) {
                nbrs.add(new ArrayList<>());
            }
        }
        int lowerBound = 0;
        for (int upperBound : borders) {
            for (int vertex = lowerBound; vertex < upperBound; vertex++) {
                if (used.contains(vertex)) {
                    continue;
                }
                int target = lowerBound + random.nextInt(upperBound - lowerBound);
                for (; target < upperBound; target++) {
                    if (target == vertex || used.contains(target)) {
                        if (target == upperBound - 1) {
                            target = lowerBound;
                        }
                        continue;
                    }
                    nbrs.get(vertex).add(target);
                    nbrs.get(target).add(vertex);
                    used.add(target);
                    used.add(vertex);
                    break;
                }
                if (!used.contains(vertex)) {
                    System.out.println("Bad news");
                }
            }
            lowerBound = upperBound;
        }
    }

    public static ArrayList<ArrayList<Integer>> generateNeighbours(int n, int seed, int componentSize) {
        ArrayList<ArrayList<Integer>> neighbours = new ArrayList<>(n);
//        seed = 1924038335;
        System.out.println(seed);
        Random randomSeed = new Random(seed);
//        randomSeed = new Random(35);
//        randomSeed = new Random();
        int degree = 3;
        List<Integer> borders = new ArrayList<>();
        int lastBorder = 0;
        int addition = n / componentSize;
        if (addition % 2 != 0) {
            addition++;
        }
        for (int i = 0; i < componentSize; ++i) {
//            int addition = randomSeed.nextInt(n - lastBorder - 2*(componentSize - i));
//            if (addition % 2 != 0) {
//                addition++;
//            }
//            if (addition < 1) {
//                addition = 2;
//            }


            int border = lastBorder + addition;
            if (border >= n-1) {
                break;
            }
            borders.add(border);
            lastBorder = border;
        }
        borders.add(n);
        System.out.println(borders);

        for (int i = 0; i < degree; i++) {
            addMatching(neighbours, n, randomSeed.nextInt(), borders);
        }
        return neighbours;
    }

    public static void main(String[] args) throws Exception {

        ArrayList<ArrayList<Integer>> neighbours = new ArrayList<>();
//        int n = 0;

        int n = 16;
        int k = n/ 2;
        for (int i = 0; i < n; ++i) {
            neighbours.add(new ArrayList<>());
        }
        neighbours.get(0).addAll(Arrays.asList(1, k, n-1));
        neighbours.get(k).addAll(Arrays.asList(k-1, 0, k+1));
        for (int i = 1; i < n; i++) {
            if (i != k) {
                neighbours.get(i).addAll(Arrays.asList(i - 1, (i + 1) % n, n - i));
            }
        }

//        for (int i = 0; i < n; ++i) {
//            ArrayList<Integer> targets = new ArrayList<>();
//            for (int target : neighbours.get(i)) {
//                targets.add(n + target);
//            }
//            neighbours.add(targets);
//        }
//
//        System.out.println(neighbours);


        ArrayList<Integer> answers = new ArrayList<>();
        n = 16;

//        for (int j = 0; j < 100; ++j) {
        int seed = (new Random()).nextInt();
//        neighbours = generateNeighbours(n, seed, 1);
        n = neighbours.size();

            ArrayList<Integer> used = new ArrayList<>(neighbours.size());
            for (int i = 0; i < neighbours.size(); ++i) {
                used.add(0);
            }
            Queue<Integer> queue = new PriorityQueue<>();
            int label = 0;
            int count = 0;
            int evenCount = 0;
            HashMap<Integer, Integer> counts = new HashMap<>();
            HashMap<Integer, Integer> labels = new HashMap<>();
            for (int i = 0; i < neighbours.size(); ++i) {
                if (used.get(i) == 0) {
//                    System.out.println(i);
                    queue.add(i);
                    count = 0;
                    while (!queue.isEmpty()) {
                        count++;
                        int vertex = queue.remove();
//                        System.out.println(vertex + " " + label);
                        labels.put(vertex, label);
                        if (used.get(vertex) < 2) {
                            for (int target : neighbours.get(vertex)) {
                                if (used.get(target) == 0) {
//                                    System.out.println(vertex + " " + target + neighbours.get(vertex));
                                    queue.add(target);
                                    used.set(target, 1);
                                }
                            }
                        }
                        used.set(vertex, 2);
                    }
                    System.out.print(count + " ");
                    counts.put(label, count);
                    label++;
                    if (count % 2 == 0) {
                        evenCount++;
                    }

                } else if (used.get(i) == 1) {
                    System.out.println("Something wrong!");
                }
            }
//            if (label > 1) {
//                continue;
//            }
            System.out.println();
            System.out.println(label + " " + "components are found");
            System.out.println(evenCount + " " + "even components");
            int degree = 3;
            BaseGenome baseGenome = new BaseGenome(new Neighbours(neighbours, degree));

//        DuplicatedGenome baseGenome = new DuplicatedGenome(new TwoRegularNeighbours(neighbours));
//        System.out.println(baseGenome.getNeighbours().size() / 2 + baseGenome.getEvenCyclesCount());
            Solver solver = new Solver(baseGenome);
            solver.solve();
            Solution result = solver.getCurrentSolution();
//        System.out.println(result);

            for (Graph.Edge edge : result.getResultMatching()) {
                if (!labels.get(edge.source).equals(labels.get(edge.target))) {
                    System.out.println(
                            " -----" + counts.get(labels.get(edge.source)) + " " + counts.get(labels.get(edge.target)));
                    System.out.println(seed);
                    return;
                }
//            if (!baseGenome.getNeighbours().hasEdge(edge.source, edge.target)) {
//                System.out.println(" another edge");
//                for (int vertex = 0; vertex < neighbours.size(); vertex++) {
//                    System.out.println(vertex + " " + neighbours.get(vertex));
//                }
//                System.out.println(result);
//                return;
//            }
            }
            System.out.println(result.getCyclesCount());
            System.out.println(result);
//        if (n %4 != 0) {
//            System.out.println((3*n +1)/ 4);
//        }
            answers.add(result.getCyclesCount());
//        }
        Collections.sort(answers);
        System.out.println(answers);
        System.out.println(answers.get(0) + "\n" + answers.get(answers.size()-1));
        System.out.println((3*n + 2)/4);
        System.out.println(neighbours);
    }

}