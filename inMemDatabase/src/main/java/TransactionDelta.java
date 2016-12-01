import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


public class TransactionDelta <K, V> {
    @Getter
    private final List<Pair<K, V>> delta = new ArrayList<>();

    public void addToDelta(Pair<K, V> value) {
        delta.add(value);
    }
}
