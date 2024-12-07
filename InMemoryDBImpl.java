import java.util.HashMap;
import java.util.Map;
public class InMemoryDBImpl implements InMemoryDB {
    private final Map<String, Integer> mainStore;
    private Map<String, Integer> transactionStore;
    private boolean inTransaction;
    public InMemoryDBImpl() {
        this.mainStore = new HashMap<>();
        this.inTransaction = false;
    }

    @Override
    public Integer get(String key) {
        // check if the key is in the transaction store first
        if (inTransaction && transactionStore.containsKey(key)) {
            return transactionStore.get(key);
        }
        // return from main store or null if does not exist
        return mainStore.get(key);
    }

    @Override
    public void put(String key, int val) {
        if (!inTransaction) { throw new IllegalStateException(" Cannot perform 'put', NO active transaction");}
        transactionStore.put(key, val);
    }

    @Override
    public void begin_transaction() {
        if (inTransaction) {
            throw new IllegalStateException("transaction already in progress");
        }
        inTransaction = true;
        transactionStore = new HashMap<>();
    }

    @Override
    public void commit() {
        if (!inTransaction) {
            throw new IllegalStateException("transaction not active,  cannot commit");
        }

        // combine changes
        for (Map.Entry<String, Integer> entry : transactionStore.entrySet()) {
            mainStore.put(entry.getKey(), entry.getValue());
        }
        transactionStore = null;
        inTransaction = false;
    }

    @Override
    public void rollback() {
        if (!inTransaction) {
            throw new IllegalStateException("Cannot rollback, no active transaction. ");
        }
        // reset
        transactionStore = null;
        inTransaction = false;
    }
}
