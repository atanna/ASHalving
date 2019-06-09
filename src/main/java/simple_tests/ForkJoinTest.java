package simple_tests;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ForkJoinTest {

    public static abstract class BaseState {
        abstract Collection<BaseState> getNextStates();

        abstract long getValue();
        
        abstract String getName();
    }


    public static class State extends BaseState {
        String name;
        public State(String name) {
            this.name = name;
        }

        @Override
        public Collection<BaseState> getNextStates() {
            if (name.length() > 10) {
                return List.of();
            } else {
                return Arrays.asList(new State(name + "0"), new State(name + "1"));
            }
        }

        @Override
        public long getValue() {
            return 0;
        }

        @Override
        public String getName() {
            return name;
        }
    }
    

    public static class ValueSumCounter extends RecursiveTask<Long> {
        private final BaseState state;

        public ValueSumCounter(BaseState state) {
            this.state = state;
        }

        @Override
        protected Long compute() {
            System.out.println(state.getName());
            long sum = state.getValue();
            List<ValueSumCounter> subTasks = new LinkedList<>();

            for(BaseState child : state.getNextStates()) {
                ValueSumCounter task = new ValueSumCounter(child);
                task.fork(); // запустим асинхронно
                subTasks.add(task);
            }

            for(ValueSumCounter task : subTasks) {
                sum += task.join(); // дождёмся выполнения задачи и прибавим результат
            }

            return sum;
        }

    }

    public static void main(String[] args) {
        State root = new State("");
        new ForkJoinPool().invoke(new ValueSumCounter(root));

    }
}
