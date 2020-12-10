// Name: Zhantao Yang
// Email: zyang484@wisc.edu
// Team: IA
// TA: Mu Cai
// Lecturer: Florian Heimerl
// Notes to Grader: This is from project 1, slight modifications made to make the pair object fit in finalProject

/**
 * this is a Pair object that stores key and value, this Pair will be stored into the linkedlist in hashtable array
 *
 * @param <KeyType>   the type of key
 * @param <ValueType> the type of value
 */
public class Pair<KeyType, ValueType> {
    private final KeyType key; // key shall not change for hashTable, set to final
    private final ValueType value;

    /**
     * constructor that constructs a Pair Object with a key and a value
     *
     * @param key the key that correspond to the value we want to store
     * @param value the value we want to store
     */
    public Pair(KeyType key, ValueType value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Get keyType.
     *
     * @return private variable of keyType
     */
    public KeyType getKey() {
        return key;
    }

    /**
     * Get valueType.
     *
     * @return private variable of valueType
     */
    public ValueType getValue() {
        return value;
    }
}
