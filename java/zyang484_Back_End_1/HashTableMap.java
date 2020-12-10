// Name: Zhantao Yang
// Email: zyang484@wisc.edu
// Team: IA
// TA: Mu Cai
// Lecturer: Florian Heimerl
// Notes to Grader: This is from project 1,
// I used Pair class (object) to store key-value pairs, several private helper are also used.

import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * a HashTable Class that implements MapADT and accepts two generics: KeyType and ValueType,
 * this class constructs a hashtable that can store key-value pairs with unique keys.
 *
 * @param <KeyType>   a generic that extends Object, this is the data type of the key corresponding to its value to store
 * @param <ValueType> a generic that extends Object, this is the data type of the value we want to store
 * @see MapADT implements interface MapADT<KeyType, ValueType>
 */
public class HashTableMap<KeyType, ValueType> implements MapADT<KeyType, ValueType> {
    private int tableSize; //the size of the array, double once full, this is the capacity of array, NOT number of pairs
    private double loadFactor; //can be but no need to set to final, although for the purpose of this assignment,
    // loadFactor will not change, but in case change is needed for future use, remain changeable
    private LinkedList<Pair<KeyType, ValueType>>[] hashTableArray;
    private int numPairs; //the actual amount of values stored in hashtable, the actual size

    /**
     * a constructor that requires an int as parameter to initialize the capacity of table (hashTableArray)
     * initialize a hashTableArray of LinkedList
     *
     * @param tableSize the initial capacity of table (hashTableArray)
     */
    @SuppressWarnings("unchecked") //creating array of LinkedList will cause unchecked warning, suppress
    public HashTableMap(int tableSize) {
        this.tableSize = tableSize;
        this.loadFactor = .8;
        this.hashTableArray = new LinkedList[tableSize];
        this.numPairs = 0;
    }

    /**
     * a default constructor that initialize the tableSize to 10, which is the initial capacity of table(hashTableArray)
     */
    public HashTableMap() { // with default capacity = 10
        this(10); //construct by calling the other constructor using 10 as tableSize
    }

    /**
     * the put method that stores a key-value pair to the hashtable, if the index after hashing already has a pair,
     * use LinkedList to avoid collision (chaining).
     * Resize and rehash if the table is "full" (numPairs >= loadFactor * tableSize)
     *
     * @param key   the key that correspond to the value we want to store
     * @param value the value we want to store
     * @return true if the pair is stored successfully, false if not (the key already exist in hashtable)
     * @see MapADT put(KeyType key, ValueType value) method
     */
    @Override
    public boolean put(KeyType key, ValueType value) {
        if (key == null || value == null) {
            return false;
        }
        int index = getIndex(key);
        if (containsKey(key)) { //check if the key is already stored in hashTable
            return false;
        }
        Pair<KeyType, ValueType> pair = new Pair<>(key, value); //create a key-value pair
        insert(pair, index);
        numPairs++;
        if (numPairs >= loadFactor * tableSize) { //check if resize is necessary
            expand();
        }
        return true; //key-value pair stored
    }

    /**
     * private helper method that helps to hash keys to index
     *
     * @param key the key that correspond the the value wants to be stored
     * @return the index of array where we hashed the key to
     */
    private int getIndex(KeyType key) {
        int hashCode = Math.abs(key.hashCode());
        return hashCode % tableSize; //index as int
    }

    /**
     * private helper method that helps to insert the pair object in the given index
     *
     * @param pair  the key-value object that we want to insert into the hashtable (array)
     * @param index the index we get after hashCode() and mod operations
     */
    private void insert(Pair<KeyType, ValueType> pair, int index) {
        if (hashTableArray[index] == null) {
            //if there is no any value stored in the index, the linkedList will be null
            hashTableArray[index] = new LinkedList<>();
        }
        hashTableArray[index].addLast(pair);

    }

    /**
     * private helper method that expands(double) the hashtable and rehash all the pairs while "full"
     */
    @SuppressWarnings("unchecked") //creating array of LinkedList will cause unchecked warning, suppress
    private void expand() {
        tableSize *= 2; //double the array capacity
        LinkedList<Pair<KeyType, ValueType>>[] tempHashTableArray = this.hashTableArray.clone();
        this.hashTableArray = new LinkedList[tableSize];
        for (LinkedList<Pair<KeyType, ValueType>> element : tempHashTableArray) { //rehashing
            if (element != null) { //skip if the element is null (nothing stored)
                for (Pair<KeyType, ValueType> pair : element) {
                    int index = getIndex(pair.getKey());
                    insert(pair, index);
                }
            }
        }
    }

    /**
     * the get method that stores a key-value pair to the hashtable, if the key could not be found (not exist in table),
     * java.util.NoSuchElementException will be thrown.
     *
     * @param key the key that correspond the the value wants to be stored
     * @return the value that the given key correspond to stored in hashTable
     * @throws NoSuchElementException if the key could not be found (not exist in table)
     * @see MapADT get(KeyType key) method
     */
    @Override
    public ValueType get(KeyType key) throws NoSuchElementException {
        int index = getIndex(key);
        if (!containsKey(key)) { //check if this key-value pair exists in hashtable
            //if there is no any value stored in the index, the linkedList will be null
            throw new NoSuchElementException();
        } else {
            for (Pair<KeyType, ValueType> pair : hashTableArray[index]) { //iterate through linkedList
                if (pair.getKey().equals(key)) { //check if the key is same
                    return pair.getValue();
                }
            }
        }
        throw new NoSuchElementException();
        //if not found in LinkedList, however, it is not expected to reach here since already checked with containsKey()
    }

    /**
     * return the number of total pairs stored in hashtable
     *
     * @return numPairs, the number of total pairs stored in hashtable
     * @see MapADT size() method
     */
    @Override
    public int size() {
        return numPairs;
    }

    /**
     * check if the key has a corresponding key-value pair stored in the hashtable
     *
     * @param key the key that correspond the the value wants to be stored
     * @return true if the key and its corresponding pair is found in hashTable, false otherwise
     * @see MapADT containsKey(KeyType key) method
     */
    @Override
    public boolean containsKey(KeyType key) {
        if (key == null) {
            return false;
        }
        int index = getIndex(key);
        if (hashTableArray[index] == null) {
            return false; //directly return false if there is no any pairs stored at index in array
        } else { //if there is some pairs in index, iterate through the linkedList
            for (Pair<KeyType, ValueType> pair : hashTableArray[index]) { //iterate through linkedList
                if (pair.getKey().equals(key)) { //check if the key is same
                    return true; //if we find the key in index return true
                }
            }
        }
        return false; //if not found in linkedList, return false
    }

    /**
     * removes a key-value type from the hashTable by using the given key.
     * If the key cannot be found (does not exist in hashtable), null will be returned, nothing will be changed
     *
     * @param key the key that correspond the the value wants to be stored
     * @return the value that the given key corresponds to, the value of the pair been removed, null if doesn't exist
     * @see MapADT remove(KeyType key) method
     */
    @Override
    public ValueType remove(KeyType key) {
        if (key == null) {
            return null;
        }
        int index = getIndex(key);
        if (hashTableArray[index] == null) {
            return null; //return null if no any pair at the index position
        }
        int linkedListSize = hashTableArray[index].size();
        if (linkedListSize == 1) { //if only contains 1 in index, store and return
            ValueType tempValue = hashTableArray[index].getFirst().getValue();
            hashTableArray[index] = null;
            numPairs--; //both fields that keep tracks of items should be reduced
            return tempValue;
        }
        //if there are more than 1 pair stored in index
        for (int i = 0; i < linkedListSize; i++) { //since we are sure the key exist, no need to check with if
            if (hashTableArray[index].get(i).getKey().equals(key)) {
                numPairs--; //this removal will not change filledElements
                return hashTableArray[index].remove(i).getValue(); //get the value to return
            }
        }
        return null; //if the key does not exist return null
    }

    /**
     * clear the hashTable without changing capacity. Removes all key-value pairs from the table
     *
     * @see MapADT clear() method
     */
    @Override
    public void clear() {
        for (int i = 0; i < tableSize; i++) {
            hashTableArray[i] = null;
        }//set all element to null
        numPairs = 0; //reset number of pairs stored to 0
    }
}
