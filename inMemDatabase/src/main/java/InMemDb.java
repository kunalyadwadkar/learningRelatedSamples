import java.util.NoSuchElementException;

public interface InMemDb<K, V> {

    void set(K key, V value);

    V get(K key);

    void delete(K key) throws NoSuchElementException;

    int count(V value);

    void rollback() throws NoSuchTransactionException;

    void commit() throws NoSuchTransactionException;

    void begin();
}
