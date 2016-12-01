import java.util.*;

public class InMemDbImpl<K, V> implements InMemDb<K, V> {

    private final Map<K, V> commitLog = new HashMap<>();
    private final Map<V, Integer> valueCommitLog = new HashMap<>();

    private final Map<K, V> transactionLog = new HashMap<>();
    private final Map<V, Integer> valueTransactionLog = new HashMap<>();

    private final Deque<TransactionDelta<K, V>> transactionStack = new ArrayDeque<>();
    private final Deque<TransactionDelta<V, Integer>> valueTransactionStack = new ArrayDeque<>();

    private int transactionsStarted = 0;

    @Override
    public void set(K key, V value) {
        if (transactionLog.containsKey(key)) {
            V tempVal = transactionLog.get(key);
            removeOrDecrementCount(tempVal);
        }

        transactionLog.put(key, value);
        addOrIncrementCount(value);

        if (transactionsStarted == 0) {
            flushTransactionLogs();
        }
    }



    @Override
    public V get(K key) {
        if (transactionsStarted == 0) {
            return commitLog.getOrDefault(key, null);
        } else {
            return transactionLog.getOrDefault(key, null);
        }
    }

    @Override
    public void delete(K key) throws NoSuchElementException {
        V value;
        if (transactionLog.containsKey(key)) {
            value = transactionLog.get(key);
            transactionLog.remove(key);
            removeOrDecrementCount(value);
        } else {
            throw new NoSuchElementException();
        }

        if (transactionsStarted == 0) {
            flushTransactionLogs();
        }
    }

    @Override
    public int count(V value) {
        if (transactionsStarted == 0) {
            return valueCommitLog.getOrDefault(value, 0);
        } else {
            return valueTransactionLog.getOrDefault(value, 0);
        }
    }


    @Override
    public void rollback() throws NoSuchTransactionException {
        if (transactionsStarted == 0) {
            throw new NoSuchTransactionException();
        }

        transactionLog.clear();
        valueTransactionLog.clear();

        TransactionDelta<K, V> lastDelta = transactionStack.pop();
        lastDelta.getDelta().forEach(kvPair -> transactionLog.put(kvPair.key, kvPair.value));

        TransactionDelta<V, Integer> lastValueDelta = valueTransactionStack.pop();
        lastValueDelta.getDelta().forEach(vIntegerPair -> valueTransactionLog.put(vIntegerPair.key, vIntegerPair.value));

        transactionsStarted--;
    }

    @Override
    public void commit() throws NoSuchTransactionException {
        if (transactionsStarted == 0) {
            throw new NoSuchTransactionException();
        }

        flushTransactionLogs();
        transactionsStarted = 0;
    }

    @Override
    public void begin() {
        transactionsStarted++;
        transactionStack.push(createTransactionDelta());
        valueTransactionStack.push(createValueTransactionDelta());

    }

    private TransactionDelta<V, Integer> createValueTransactionDelta() {
        if (valueTransactionLog.isEmpty()) {
            return new TransactionDelta<>();
        }

        TransactionDelta<V, Integer> returnVal = new TransactionDelta<>();

        valueTransactionLog.forEach((v, count) -> {
            returnVal.addToDelta(new Pair<>(v, count));
        });

        return returnVal;
    }

    private TransactionDelta<K, V> createTransactionDelta() {
        if (transactionLog.isEmpty()) {
            return new TransactionDelta<>();
        }

        TransactionDelta<K, V> returnVal = new TransactionDelta<>();

        transactionLog.forEach((k, v) -> {
            returnVal.addToDelta(new Pair<>(k, v));
        });

        return returnVal;
    }


    private void flushTransactionLogs() {
        commitLog.clear();
        valueCommitLog.clear();
        transactionLog.forEach(commitLog::put);
        valueTransactionLog.forEach(valueCommitLog::put);
    }

    private void addOrIncrementCount(V value) {
        if (valueTransactionLog.containsKey(value)) {
            int tempV = valueTransactionLog.get(value);
            tempV++;
            valueTransactionLog.put(value, tempV);
        } else {
            valueTransactionLog.put(value, 1);
        }
    }

    private void removeOrDecrementCount(V value) {
        if (valueTransactionLog.containsKey(value)) {
            Integer tempVal = valueTransactionLog.get(value);
            tempVal--;
            valueTransactionLog.put(value, tempVal);
        } else {
            valueTransactionLog.remove(value);
        }
    }

}
