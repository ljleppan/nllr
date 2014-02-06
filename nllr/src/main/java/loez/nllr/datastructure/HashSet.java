package loez.nllr.datastructure;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 * @author loezi
 */
public class HashSet<E> implements Iterable<E>{    
    private static final Object DUMMY = new Object();
    
    private HashMap<E, Object> map = new HashMap<>();
    private int size = 0;
    private int modCount = 0;

    
    public void add(E e){
        if (e != null){
            if (!map.containsKey(e)) {
                map.put(e, DUMMY);
                size++;
                modCount++;
            }
        }
    }
    
    public void remove(E e){
        if (e != null){
            if (map.containsKey(e)) {
                map.remove(e);
                size--;
                modCount++;
            }
        }
    }
    
    public boolean contains(E e){
        return map.containsKey(e);
    }
    
    public void addAll(ArrayList<E> elements){
        for (E e : elements){
            add(e);
        }
    }
    
    public void removeAll(ArrayList<E> elements){
        for (E e : elements){
            remove(e);
        }
    }
    
    public int size(){
        return size;
    }
    
    public boolean isEmpty(){
        return size == 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new HashSetIterator<>();
    }
    
    public class HashSetIterator<T> implements Iterator<T>{
        private ArrayList<T> keySet = (ArrayList<T>) map.keySet();
        private int expectedModCount = modCount;
        private int index = 0;
        
        @Override
        public boolean hasNext() {
            if (modCount != expectedModCount){
                throw new ConcurrentModificationException();
            }
            
            return index < (keySet.size());
        }

        @Override
        public T next() {
            if (index >= keySet.size()){
                throw new NoSuchElementException();
            }
            
            return (T) keySet.get(index++);
            
        }

        @Override
        public void remove() {
            if (modCount != expectedModCount){
                throw new ConcurrentModificationException();
            }

            HashSet.this.remove((E) keySet.get(index++));
            expectedModCount++;
        }
    }
}
