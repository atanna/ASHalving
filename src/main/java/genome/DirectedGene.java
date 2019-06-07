package genome;

import java.util.HashMap;

public class DirectedGene {
    private static final String HEAD = "h";
    private static final String TAIL = "t";

    public static class HalfGene {
        private final String geneName;
        private final String halfName;

        public HalfGene(String geneName, String halfName) {
            this.geneName = geneName;
            this.halfName = halfName;
        }

        public boolean isHead() {
            return halfName.equals(HEAD);
        }

        public String getName() {
            return geneName + "_" + halfName;
        }

        public String getGeneName() {
            return geneName;
        }

        public String toString() {
            return this.getName();
        }

        public  static HalfGene asHead(String geneName) {
            return new HalfGene(geneName, HEAD);
        }

        public static HalfGene asTail(String geneName) {
            return new HalfGene(geneName, TAIL);
        }

        public static HalfGene convertToHalf(String halfName) {
            return new HalfGene(halfName.substring(0, halfName.length() - 2), halfName.substring(halfName.length()-1));
        }

        public HalfGene getOppositeHalf() {
            return Gene.getOppositeHalf(this);
        }

        @Override
        public boolean equals(Object obj) {
            return getName().equals(((HalfGene)(obj)).getName());
        }
    }

    public static class Gene {

        private final String name;
        private final HalfGene head;
        private final HalfGene tail;

        public Gene(String name) {
            this.name = name;
            this.head = HalfGene.asHead(name);
            this.tail = HalfGene.asTail(name);
        }

        public String getName() {
            return name;
        }

        public HalfGene getHead() {
            return head;
        }

        public HalfGene getTail() {
            return tail;
        }


        public static HalfGene getOppositeHalf(HalfGene halfGene) {
            if (halfGene.isHead()) {
                return new HalfGene(halfGene.getGeneName(), TAIL);
            } else {
                return new HalfGene(halfGene.getGeneName(), HEAD);
            }
        }
    }

    private final Gene gene;
    private int sign = 1;

    public DirectedGene(String name, int sign) {
        this.gene = new Gene(name);
        this.sign = sign;
    }

    public DirectedGene(String name) {
        this(name,1);
    }

    public Gene getGene() {
        return gene;
    }

    public String getName() {
        return gene.getName();
    }

    public HalfGene getHead() {
        if (sign > 0) {
            return gene.getHead();
        }
        return gene.getTail();
    }

    public HalfGene getTail() {
        return  Gene.getOppositeHalf(getHead());
    }

    @Override
    public String toString() {
        String strSign = "";
        if (sign <= 0) {
            strSign = "-";
        }
        return strSign+getName();
    }
}
