package genome;

public class DirectedGene {

    public class Gene {
        public abstract class HalfGene {
            private final String halfName;
            public HalfGene(String halfName) {
                this.halfName = halfName;
            }

            public abstract HalfGene getOppositeHalf();

            public String getName() {
                return name + "_" + halfName;
            }

            public String toString() {
                return this.getName();
            }
        }

        private class Head extends HalfGene {
            public Head() {
                super("h");
            }


            @Override
            public HalfGene getOppositeHalf() {
                return tail;
            }
        }

        private class Tail extends HalfGene {
            public Tail() {
                super("t");
            }

            @Override
            public HalfGene getOppositeHalf() {
                return head;
            }
        }

        private final String name;
        private final HalfGene head;
        private final HalfGene tail;

        public Gene(String name) {
            this.name = name;
            this.head = new Head();
            this.tail = new Tail();
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

    public Gene.HalfGene getHead() {
        if (sign > 0) {
            return gene.getHead();
        }
        return gene.getTail();
    }

    public Gene.HalfGene getTail() {
        return getHead().getOppositeHalf();
    }

}
