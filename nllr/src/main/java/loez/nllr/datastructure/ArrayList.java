package loez.nllr.datastructure;

import java.util.Iterator;

/**
 *
 * @author ljleppan
 * @param <T>
 */
public class ArrayList<T>  implements Iterable<T>{
    private final static int DEFAULT_SIZE = 8;
    private final static int MIN_SIZE = 8;
    private final static double SCALING_TRESHOLD = 0.75;
    private final static int SCALING_CONSTANT = 2;
    
    private int limit = DEFAULT_SIZE;
    private int size = 0;
    
    private Object[] array = new Object[DEFAULT_SIZE];
    
    public void add(T value) {
        array[size] = value;
        size++;
        checkCapacity();
    }
    
    public T get(int index){
        if (validIndex(index)){
            return (T) array[index];
        }
        throw new IndexOutOfBoundsException();
    }
    
    public int size(){
        return this.size;
    }
    
    public boolean contains(T value){
        return indexOf(value) != -1;
    }
    
    public int indexOf(T value){
        for (int i = 0; i < size; i++) {
            if ((value == null && array[i] == null) || (value != null && value.equals(array[i]))){
                return i;
            }
        }
        return -1;
    }
    
    public boolean isEmpty(){
        return size == 0;
    }
    
    public void clear(){
        array = new Object[DEFAULT_SIZE];
        limit = DEFAULT_SIZE;
        size = 0;
    }
    
    public void remove(int index){
        if (!validIndex(index)){
            throw new IndexOutOfBoundsException();
        }
        
        while(validIndex(index)){
            array[index] = array[index+1];
            index++;
        }
        size--;
        checkCapacity();
    }
    
    public void remove(T value){
        int index = indexOf(value);
        if (index != -1){
            remove(index);
        }
    }
    
    private boolean validIndex(int index){
        return (index >= 0 && index < size);
    }

    private void checkCapacity() {
        if (size > limit * SCALING_TRESHOLD){
            int newLimit = limit * SCALING_CONSTANT;
            changeCapacity(newLimit);
        } else if (size < (limit * (1.0 - SCALING_TRESHOLD))) {
            int newLimit = limit / SCALING_CONSTANT;
            if (newLimit >= MIN_SIZE){
                changeCapacity(newLimit);
            }
        }
    }
    
    private void changeCapacity(int newLimit){
        Object[] newArray = new Object[newLimit];
        int smallerLimit = Math.min(newLimit, limit);
        System.arraycopy(array, 0, newArray, 0, smallerLimit);
        array = newArray;
        limit = newLimit;
    }
    
    int limit(){
        return limit;
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayListIterator<>();
    }
    
    public class ArrayListIterator<T> implements Iterator<T>{

        private int index;
        
        @Override
        public boolean hasNext() {
            return index < (size);
        }

        @Override
        public T next() {
            return (T) array[index++];
        }

        @Override
        public void remove() {
            ArrayList.this.remove(index);
        }
    
}
}
