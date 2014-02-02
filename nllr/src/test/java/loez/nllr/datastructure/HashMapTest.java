package loez.nllr.datastructure;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ljleppan
 */
public class HashMapTest {
    private HashMap<String, Integer> hm;
    
    public class Collider{
        int value;
        
        public Collider(int value){
            this.value = value;
        }
        
        @Override
        public int hashCode(){
            return 1;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Collider other = (Collider) obj;
            return this.value == other.value;
        }


    }
    
    @Before
    public void setUp() {
        hm = new HashMap<>();
    }

    @Test
    public void emptyHashMapHasNoKeys() {
        assertTrue("An empty hashmap should have no keys in its keyset",
                hm.keySet().isEmpty());
    }
    
    @Test
    public void addingKeysWords() {
        hm.put("a", 1);
        assertFalse("After adding a key, the keyset should be non-empty",
                hm.keySet().isEmpty());
        assertTrue("After adding a key, containsKey should return true for that key",
                hm.containsKey("a"));
        assertTrue("Adding two keys to an empty map should not change the size",
                hm.getSize() == 16);
        
    }
    
    @Test
    public void removingKeysWorks() {
        hm.put("a", 1);
        hm.put("b", 2);
        assertTrue("After adding two keys, two keys should be present in the keyset",
                hm.keySet().size() == 2);
        
        hm.remove("c");
        assertTrue("Removing a non-existant key should not do anything",
                hm.keySet().size() == 2);
        
        hm.remove("a");
        assertTrue("Keyset should not include a key after that key has been removed",
                hm.keySet().size() == 1);
        
        assertTrue("Removing one of two entries should not change size",
                hm.getSize() == 16);
    }
    
    @Test
    public void gettingKeysWorks() {
        assertTrue("Getting a non-existant key should return null",
                hm.get("NOPE") == null);
    }
    
    @Test
    public void collisionsWork() {
        HashMap<Collider, Integer> h = new HashMap<>();
        Collider c1 = new Collider(1);
        Collider c2 = new Collider(2);
        Collider c3 = new Collider(3);
        Collider c4 = new Collider(4);
        h.put(c1, 1);
        h.put(c2, 2);
        h.put(c3, 3);
        h.put(c4, 4);
        assertTrue("Adding colliding entries should keep all entries in keyset",
                h.keySet().size() == 4);
        assertTrue("Searching for colliding key should return corrent key",
                h.get(c1) == 1 && h.get(c2) == 2 && h.get(c3) == 3 && h.get(c4) == 4);

        h.remove(c3);
        assertTrue("Removing one of colliding keys should leave the others and remove the correct one",
                h.get(c1) == 1 && h.get(c2) == 2 && h.get(c3) == null && h.get(c4) == 4);
        h.remove(c4);
        assertTrue("Removing one of colliding keys should leave the others and remove the correct one",
                h.get(c1) == 1 && h.get(c2) == 2 && h.get(c3) == null && h.get(c4) == null);
        assertTrue("Adding 3 colliding keys to an empty hashmap should not increase the size",
                h.getSize() == 16);
    }
    
    @Test
    public void capacityChangesWork(){
        System.out.println("1");
        addMultiple(12);
        assertTrue("Adding 12 entries to an empty hashmap should not result in a size increase",
                hm.getSize() == 16);
        
        hm = new HashMap<>();
        addMultiple(13);
        assertTrue("Adding a 13th element to a hashmap with size 16 should increase the size",
                hm.getSize() == 32);        
        
        removeMultiple(9, 13);
        assertTrue("Leaving 8 keys in a hashmap with size 32 should not decrease the size",
                hm.getSize() == 32);
        
        hm.remove("8");
        assertTrue("Leaving 7 keys in a a hashmap with size 32 should decrease the size ",
                hm.getSize() == 16);
    }

    private void addMultiple(int amount) {
        for (int i = 1; i <= amount; i++) {
            hm.put(""+i, i);
        }
    }
    
    private void removeMultiple(int start, int end){
        for (int i = start; i <= end; i++) {
            hm.remove(""+i);
        }
    }
}
