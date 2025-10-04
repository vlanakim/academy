import java.util.Objects;

public class BlockingQueue<T> {
    private final Object[] buffer;
    private final int capacity;
    private int head = 0;
    private int tail = 0;
    private int count = 0;

    public BlockingQueue(int capacity) {
        if (capacity <= 0) throw new IllegalArgumentException("Capacity must be > 0");
        this.capacity = capacity;
        this.buffer = new Object[capacity];
    }

    public void enqueue(T item) throws InterruptedException {
        Objects.requireNonNull(item, "item must not be null");
        synchronized (this) {
            while (count == capacity) {
                wait();
            }
            buffer[tail] = item;
            tail = (tail + 1) % capacity;
            count++;
            notifyAll();
        }
    }

    @SuppressWarnings("unchecked")
    public T dequeue() throws InterruptedException {
        synchronized (this) {
            while (count == 0) {
                wait();
            }
            Object obj = buffer[head];
            buffer[head] = null;
            head = (head + 1) % capacity;
            count--;
            notifyAll();
            return (T) obj;
        }
    }

    public synchronized int size() {
        return count;
    }
}
