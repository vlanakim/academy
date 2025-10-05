import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class ConcurrentBank {
    private final ConcurrentHashMap<Long, BankAccount> accounts = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public BankAccount createAccount(long initialBalance) {
        long id = idGenerator.getAndIncrement();
        BankAccount account = new BankAccount(id, initialBalance);
        accounts.put(id, account);
        return account;
    }

    public boolean transfer(BankAccount from, BankAccount to, long amount) {
        if (from == null || to == null) throw new IllegalArgumentException("accounts must not be null");
        if (amount < 0) throw new IllegalArgumentException("amount must be >= 0");
        if (from.getId() == to.getId()) {
            return true;
        }

        BankAccount first = from.getId() < to.getId() ? from : to;
        BankAccount second = (first == from) ? to : from;

        first.lock();
        second.lock();
        try {
            boolean withdrawn = from.withdrawNoLock(amount);
            if (!withdrawn) {
                return false;
            }
            to.depositNoLock(amount);
            return true;
        } finally {
            second.unlock();
            first.unlock();
        }
    }

    public boolean transfer(long fromId, long toId, long amount) {
        BankAccount from = accounts.get(fromId);
        BankAccount to = accounts.get(toId);
        if (from == null || to == null) throw new IllegalArgumentException("account not found");
        return transfer(from, to, amount);
    }

    public long getTotalBalance() {
        List<BankAccount> list = new ArrayList<>(accounts.values());
        list.sort(Comparator.comparingLong(BankAccount::getId));
        for (BankAccount a : list) {
            a.lock();
        }
        try {
            long total = 0;
            for (BankAccount a : list) {
                total += a.getBalanceNoLock();
            }
            return total;
        } finally {
            for (int i = list.size() - 1; i >= 0; i--) {
                list.get(i).unlock();
            }
        }
    }

    public BankAccount getAccount(long accountId) {
        return accounts.get(accountId);
    }
}
