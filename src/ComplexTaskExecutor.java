import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class ComplexTaskExecutor {
    private final int taskComplexity;

    public ComplexTaskExecutor(int taskComplexity) {
        this.taskComplexity = Math.max(1, taskComplexity);
    }

    public void executeTasks(int numberOfTasks) {
        if (numberOfTasks <= 0) throw new IllegalArgumentException("numberOfTasks must be > 0");

        final Queue<Integer> results = new ConcurrentLinkedQueue<>();

        Runnable barrierAction = () -> {
            List<Integer> snapshot = results.stream().collect(Collectors.toList());
            int combined = snapshot.stream().mapToInt(Integer::intValue).sum();
            System.out.printf("[%s] Barrier action: combined result = %d, partials=%s%n",
                    Thread.currentThread().getName(), combined, snapshot);
        };

        final CyclicBarrier barrier = new CyclicBarrier(numberOfTasks, barrierAction);
        ExecutorService executor = Executors.newFixedThreadPool(numberOfTasks);
        CountDownLatch doneLatch = new CountDownLatch(numberOfTasks);

        System.out.printf("[%s] Starting executeTasks: numberOfTasks=%d, complexity=%d%n",
                Thread.currentThread().getName(), numberOfTasks, taskComplexity);

        for (int i = 0; i < numberOfTasks; i++) {
            final int taskId = i;
            executor.submit(() -> {
                try {
                    System.out.printf("[%s] Task-%d started%n", Thread.currentThread().getName(), taskId);
                    ComplexTask task = new ComplexTask(taskId, taskComplexity);
                    int result = task.execute();
                    System.out.printf("[%s] Task-%d produced result=%d%n", Thread.currentThread().getName(), taskId, result);

                    results.add(result);

                    try {
                        barrier.await();
                        System.out.printf("[%s] Task-%d passed barrier%n", Thread.currentThread().getName(), taskId);
                    } catch (BrokenBarrierException e) {
                        System.err.printf("[%s] Task-%d: barrier broken%n", Thread.currentThread().getName(), taskId);
                    }

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.printf("[%s] Task-%d interrupted%n", Thread.currentThread().getName(), taskId);
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        try {
            boolean finished = doneLatch.await(2, TimeUnit.MINUTES);
            if (!finished) {
                System.err.printf("[%s] Warning: tasks didn't finish in time%n", Thread.currentThread().getName());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.printf("[%s] executeTasks interrupted%n", Thread.currentThread().getName());
        } finally {
            executor.shutdownNow();
            try {
                if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                    System.err.printf("[%s] Executor did not terminate cleanly%n", Thread.currentThread().getName());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.printf("[%s] executeTasks completed for %d tasks%n", Thread.currentThread().getName(), numberOfTasks);
    }
}
