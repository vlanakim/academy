import java.util.concurrent.ThreadLocalRandom;

public class ComplexTask {
    private final int taskId;
    private final int complexity;

    public ComplexTask(int taskId, int complexity) {
        this.taskId = taskId;
        this.complexity = Math.max(1, complexity);
    }

    public int execute() {
        long acc = 0;
        int iterations = complexity * 50_000;
        for (int i = 1; i <= iterations; i++) {
            acc += (i * (taskId + 1)) & 0xFF;
        }

        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(20, 100));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return (int) (acc % 10_000);
    }

    @Override
    public String toString() {
        return "ComplexTask{id=" + taskId + ", complexity=" + complexity + "}";
    }
}
