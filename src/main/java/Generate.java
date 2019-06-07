import java.util.*;
import java.util.stream.Collectors;

import algo.graph.DuplicatedGenome;
import algo.graph.OrdinaryGenome;
import algo.guided_problems.GGHPGraph;
import algo.guided_problems.gghp.Solver;

public class Generate {

    public static void addMatching(ArrayList<ArrayList<Integer>> nbrs, int size, int seed) {
        HashSet<Integer> used = new HashSet<>();
        Random random = new Random(seed);
        if (nbrs.size() == 0) {
            for (int i = 0; i < size; ++ i) {
                nbrs.add(new ArrayList<>());
            }
        }
        for (int vertex = 0; vertex < size; vertex++) {
            if (used.contains(vertex)) {
                continue;
            }
            int target = random.nextInt(size);
            for (; target < size; target++) {
                if (target == vertex || used.contains(target)) {
                    if (target == size - 1) {
                        target = 0;
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
    }

    public static ArrayList<ArrayList<Integer>> addMatching(int size, int seed) {
        ArrayList<ArrayList<Integer>> nbrs = new ArrayList<>(size);
        addMatching(nbrs, size, seed);
        return nbrs;
    }

    public static ArrayList<ArrayList<Integer>> generate(int n, int seed) {
        Random randomSeed = new Random(seed);
        return addMatching(n, randomSeed.nextInt());

    }

    private static ArrayList<ArrayList<Integer>> getDoubleGenome(ArrayList<ArrayList<Integer>> genome) {

        return genome.stream().map( al -> {
            ArrayList<Integer> a2 = new ArrayList<>();
            a2.add(al.get(0));
            a2.add(al.get(0));
            return a2;
        }).collect(Collectors.toCollection(ArrayList::new));
    }

    private static boolean check(ArrayList<ArrayList<Integer>> genome) {
        for (int i = 0; i < genome.size(); ++i) {
            for (int v : genome.get(i)) {
                if (!genome.get(v).contains(i)) {
                    System.out.println("         invalid case");
                    return false;
                }
            }
        }
        return true;
    }

    private static void mutateGenome(ArrayList<ArrayList<Integer>> genome, Integer mutatePercent, int seed) {
        if (mutatePercent.equals(0) || genome.size() == 0) {
            return;
        }

        int genomeSize = genome.size();
        int x = (int)Math.ceil((genomeSize*mutatePercent/100f));
        if (!check(genome) ) {
            System.out.println("pfff");
        }

        Random random = new Random(seed);
        for (int i = 0; i < mutatePercent; i++) {
            int firstDot = random.nextInt(genomeSize);

            int secondDot = random.nextInt(genomeSize);
            while (secondDot == firstDot) {
                secondDot = random.nextInt(genomeSize);
            }

            ArrayList<Integer> sub1 = genome.get(firstDot);
            ArrayList<Integer> sub2 = genome.get(secondDot);

            int ind1 = 0;
            int ind2 = 0;

            int firstDotNbr = sub1.get(ind1);
            int secondDotNbr = sub2.get(ind2);

            ArrayList<Integer> sub1Nbr = genome.get(firstDotNbr);
            ArrayList<Integer> sub2Nbr = genome.get(secondDotNbr);

            int ind1Nbr = 0;
            if (firstDot == firstDotNbr) {
                ind1Nbr = 1;
            }
            while (sub1Nbr.get(ind1Nbr) != firstDot) {
                ind1Nbr++;
                if (ind1Nbr == 2) {
                    System.out.println(firstDotNbr+ " " + sub1Nbr + " " + firstDot + " " + sub1);
                }
            }

            int ind2Nbr = 0;
            if (secondDot == secondDotNbr) {
                ind2Nbr = 1;
            }
            while (sub2Nbr.get(ind2Nbr) != secondDot) {
                ind2Nbr++;
                if (ind2Nbr == 2) {
                    System.out.println(secondDotNbr + " " + sub2Nbr + " " + secondDot + " " + sub2);
                }
            }

            if (firstDot == secondDotNbr || firstDot == firstDotNbr || secondDot == firstDotNbr) {
                i--;
                continue;
            }
            sub1.set(ind1, secondDotNbr);
            sub2.set(ind2, firstDotNbr);
            sub1Nbr.set(ind1Nbr, secondDot);
            sub2Nbr.set(ind2Nbr, firstDot);
            if (!check(genome)) {
                System.out.println(firstDot + " " + firstDotNbr + " " + secondDot + " " + secondDotNbr);
                System.out.println(i);
            }


            //Integer subDot3 = sub3.remove(random.nextInt(sub3.size() != 0 ? sub3.size() : 1));
            //Integer subDot4 = sub4.remove(random.nextInt(sub4.size() != 0 ? sub4.size() : 1));

//            int firstDot_i = sub3.indexOf(firstDot);
//            Integer subDot3 = sub3.remove(firstDot_i);
//
//            int secondDot_i = sub4.indexOf(secondDot);
//            Integer subDot4 = sub4.remove(secondDot_i);
//
//            sub1.add(subDot4);
//            sub4.add(subDot1);
//            sub2.add(subDot3);
//            sub3.add(subDot2);

        }

    }

    public static void main(String[] args) throws Exception {
        int seed = 5;

        seed = (new Random()).nextInt();
        seed = -1253887205;
//        Random random = new Random(seed);

        System.out.println(seed);
        Random random = new Random(seed);

        ArrayList<ArrayList<Integer>> genome = generate(28, random.nextInt());

        ArrayList<ArrayList<Integer>> doubleGenome = getDoubleGenome(genome);


        int seed1 = random.nextInt();
        int seed2 = random.nextInt();
        mutateGenome(doubleGenome, 100, seed1);
        mutateGenome(genome, 100, seed2);

        System.out.println(genome);
        System.out.println(doubleGenome);

        GGHPGraph graph = new GGHPGraph(new DuplicatedGenome(doubleGenome), new OrdinaryGenome(genome));

        algo.guided_problems.gghp.Solver solver = new Solver(graph);
        solver.solve();

        System.out.println(solver.getCurrentSolution());

        String result  = solver.getCurrentSolution().toString();
    }

}