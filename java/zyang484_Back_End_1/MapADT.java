// Name: Zhantao Yang
// Email: zyang484@wisc.edu
// Team: IA
// TA: Mu Cai
// Lecturer: Florian Heimerl
// Notes to Grader: None

import java.util.NoSuchElementException;

public interface MapADT<KeyType,ValueType> {
    public boolean put(KeyType key, ValueType value);
    public ValueType get(KeyType key) throws NoSuchElementException;
    public int size();
    public boolean containsKey(KeyType key);
    public ValueType remove(KeyType key);
    public void clear();
}
