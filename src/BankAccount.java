import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class BankAccount {
    private final long id;
    private long balance;
    private final ReentrantLock lock = new ReentrantLock();

    public BankAccount(long id, long initialBalance) {
        this.id = id;
        this.balance = initialBalance;
    }

    public long getId() {
        return id;
    }

    public void lock() {
        lock.lock();
    }

    public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException {
        return lock.tryLock(timeout, unit);
    }

    public void unlock() {
        lock.unlock();
    }

    public void deposit(long amount) {
        if (amount < 0) throw new IllegalArgumentException("amount must be >= 0");
        lock.lock();
        try {
            balance += amount;
        } finally {
            lock.unlock();
        }
    }

    public boolean withdraw(long amount) {
        if (amount < 0) throw new IllegalArgumentException("amount must be >= 0");
        lock.lock();
        try {
            if (balance >= amount) {
                balance -= amount;
                return true;
            } else {
                return false;
            }
        } finally {
            lock.unlock();
        }
    }

    public long getBalance() {
        lock.lock();
        try {
            return balance;
        } finally {
            lock.unlock();
        }
    }

    boolean withdrawNoLock(long amount) {
        if (amount < 0) throw new IllegalArgumentException("amount must be >= 0");
        if (balance >= amount) {
            balance -= amount;
            return true;
        } else {
            return false;
        }
    }

    void depositNoLock(long amount) {
        if (amount < 0) throw new IllegalArgumentException("amount must be >= 0");
        balance += amount;
    }

    long getBalanceNoLock() {
        return balance;
    }
}
