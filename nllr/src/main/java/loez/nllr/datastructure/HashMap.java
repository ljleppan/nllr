package loez.nllr.datastructure;



/**
 * An implementation of a Hash map
 * @author ljleppan
 * @param <Key>     Class of keys
 * @param <Value>   Class of values
 */
public class HashMap<Key, Value> {
    /**
     * The default size of the backing Array.
     * Must be a power of two.
     */
    private final static int DEFAULT_SIZE = 16;
    
    /**
     * The minimum allowed size of the backing Array.
     */
    private final static int MIN_SIZE = DEFAULT_SIZE;
    
    /**
     * Determines when the capacity should be increased.
     */
    private final static double INCREASE_CAPACITY_LIMIT = 0.75;
    
    /**
     * Determines when the capacity should be decreased.
     */
    private final static double DECREASE_CAPACITY_LIMIT = 0.25;
    
    /**
     * Determines the amount of change in the backing array's size when capacity is changed.
     * When increasing, is used to multiply current size. When decreasing, is used to divide the current size.
     * Must be a power of two.
     */
    private final static double CHANGE_CAPACITY_FACTOR = 2;
    
    private int size = DEFAULT_SIZE;
    private int entries = 0;
    private Entry[] array = new Entry[DEFAULT_SIZE];
    
    /**
     * A single entry (key-value pair) in the HashMap. Also a linked list for easy bucketing.
     * @param <Key>     Class of key
     * @param <Value>   Class of value
     */
    class Entry<Key, Value> {
        private final Key key;
        private Value value;
        private Entry next;

        /**
         * Creates a new Key-Value pair
         * @param key   Key
         * @param value Value
         */
        public Entry(Key key, Value value){
            this.key = key;
            this.value = value;
        }

        /**
         * @return The key
         */
        public Key getKey() {
            return this.key;
        }

        /**
         * @return The value
         */
        public Value getValue() {
            return this.value;
        }

        /**
         * @param value The new value
         */
        public void setValue(Value value) {
            this.value = value;
        }

        /**
         * @return Next Entry in the linked list
         */
        public Entry getNext(){
            return this.next;
        }

        /**
         * @param next New next entry in the linked list
         */
        public void setNext(Entry next){
            this.next = next;
        }
    }
    
    /**
     * PACKAGE VISIBLE, FOR UNIT TESTS
     * @return The size of the HashMap's backing array (NOT number of entries).
     */
    int getSize(){
        return this.size;
    }
    
    /**
     * Return the value associated with the key within the HashMap.
     * @param key   The key
     * @return      Value associated with the key, or null if the key is not present in the HashMap
     */
    public Value get(Key key){
        // Determine in which bucket the Entry should be in
        int index = getIndex(key, size);
        Entry entry = this.array[index];
        
        //Go thru that bucket, looking for the key
        while(entry != null) {
            if (entry.getKey().equals(key)){
                return (Value) entry.getValue();
            }
            entry = entry.getNext();
        }
        
        //If bucket is empty or no key found in bucket, return null
        return null;
    }
    
    /**
     * Adds a new Key-Value pair to the HashMap.
     * If the key is already present, the value associated with the key is overwritten.
     * Key is considered present if key.equals(someKeyInHashMap).
     * @param key   The key
     * @param value The value
     */
    public void put(Key key, Value value){   
        //Determine which bucket the key belongs to
        int index = getIndex(key, size);
        
        //If the bucket is empty, add the Entry as a new bucket
        if (this.array[index] == null){
            Entry e = new Entry<>(key, value);
            this.array[index] = e;    
        } else {
            //An existing bucket was found, go thru the bucket looking for the key
            Entry entry = this.array[index];
            while (entry != null){
                if (entry.getKey().equals(key)){
                    //Found key, overwrite current value and exit
                    entry.setValue(value);
                    return;
                }
                entry = entry.getNext();
            }
            
            //Didn't find key in bucket, add a new entry to start of bucket
            Entry newEntry = new Entry<>(key, value);
            newEntry.setNext(array[index]);
            array[index] = newEntry;
        }
        entries++;
        checkCapacity();
    }
    
    /**
     * Removes a Key-Value pair from the HashMap.
     * If Key is not present in the HashMap, nothing happens.
     * @param key   The key identifying the entry to delete.
     */
    public void remove(Key key){
        //Find the correct bucket
        int index = getIndex(key, size);
        Entry e = array[index];
        
        //If no bucket present, just exit
        if (e == null){
            return;
        }
        
        //Check if our entry is the first in the bucket
        if (e.getKey().equals(key)){
            //Just set the following entry (null is fine) to be the first entry in the bucket and exit
            array[index] = e.getNext();
            entries--;
            checkCapacity();
            return;
        }
        
        //Search the bucket for the key, starting from the second entry
        Entry previous = e;
        Entry current = e.getNext();
        while (current != null){
            Entry next = current.getNext();
            if (current.getKey().equals(key)){
                //Found our entry!
                //Just set the previous's next to be current's next. It'll automatically be null if current is last in bucket
                previous.setNext(next);
                entries--;
                checkCapacity();
                return;
            }
        }
    }
    
    /**
     * Checks whether a key is present in the HashMap.
     * @param key   The key
     * @return      True, if key is present; false otherwise
     */
    public boolean containsKey(Key key){
        if (key == null){
            return false;
        }
        
        int index = getIndex(key, size);
        Entry e = array[index];
        while (e != null){
            if (e.getKey().equals(key)){
                return true;
            }
            e = e.getNext();
        }
        
        return false;
    }
    
    /**
     * Lists all the Keys present in the HashMap.
     * @return  ArrayList containing all Keys
     */
    public ArrayList<Key> keySet(){
        ArrayList<Key> keySet = new ArrayList<>();
        for (int bucket = 0; bucket < this.size; bucket++) {
            Entry e = array[bucket];
            while (e != null){
                keySet.add((Key) e.getKey());
                e = e.getNext();
            }
        }
        return keySet;
    }
    
    /**
     * Calculates a more evenly distributed index based on the original hash of the key.
     * This is same approach the Java standard library implements.
     * 
     * The problem: Ordinarily, only X least significant bits are used to determine the
     * hash of the key. This is especially troublesome if the original hash
     * calculation is guaranteed to have an even component.
     * 
     * The solution: XOR the least significant bits to the most significant bits. This ensures
     * a more even distribution of indices over the map, despite any possible least-significant-bit-bias
     * in the original hash.
     * 
     * After the calculation of the improved hash, the hash is then AND'ed to the (size-of-array - 1). In
     * our case, this is more efficient than just using the % -operator, because we can guarantee that the
     * size is a power of two (Starts at 16, only doubled or halved).
     * 
     * @param key
     * @return 
     */
    private int getIndex(Key key, int size){
        int hash = key.hashCode();
        hash ^= (hash >>> 20) ^ (hash >>> 12) ^ (hash >>> 7) ^ (hash >>> 4); 
        
        int index = hash & (size - 1);
        return index;
    }
    
    /**
     * Checks whether the backing Array's capacity should be changed.
     */
    private void checkCapacity(){
        double fillRate = (double) this.entries / this.size;
        if (fillRate < DECREASE_CAPACITY_LIMIT){
            decreaseCapacity();
        } else if (fillRate > INCREASE_CAPACITY_LIMIT){
            increaseCapacity();
        }
    }
    
    /**
     * Exchanges the backing array for a smaller one.
     */
    private void decreaseCapacity(){
        int newSize = (int) (this.size / CHANGE_CAPACITY_FACTOR);
        if (newSize >= MIN_SIZE){
            Entry[] newArray = new Entry[newSize];
            reHash(newArray);
        }
    }
    
    /**
     * Exchanges the backing array for a larger one.
     */
    private void increaseCapacity(){
        if (this.size < Integer.MAX_VALUE){  //Ensure that new size will fit in an int.
            int newSize = (int) (this.size * CHANGE_CAPACITY_FACTOR);
            Entry[] newArray = new Entry[newSize];
            reHash(newArray);
        }
        
    }
    
    /**
     * Adds all the Entries within the currently used backing array to the new backing array.
     * @param newArray The new backing array
     */
    private void reHash(Entry[] newArray){
        int newSize = newArray.length;
        
        //Loop thru all buckets
        for (int i = 0; i < this.size; i++) {
            //Loop thru all entries
            Entry e = array[i];
            while (e != null){
                cloneEntry(e, newArray);
                e = e.getNext();
            }
        }
        
        //Update size and insert the new array
        this.size = newSize;
        this.array = newArray;
    }

    /**
     * Clones an entry from the current backing array to another array, recalculating the hash for said entry.
     * @param e         Entry to clone
     * @param newArray  The new array
     */
    private void cloneEntry(Entry e, Entry[] newArray) {
        int newIndex = getIndex((Key) e.getKey(), newArray.length);
        Entry<Key, Value> newEntry = new Entry<>((Key) e.getKey(), (Value) e.getValue());
        Entry bucketHead = newArray[newIndex];
        newEntry.setNext(bucketHead);
        newArray[newIndex] = newEntry;
    }
}