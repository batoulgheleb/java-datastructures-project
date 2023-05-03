package structures;

public class HashMap<K, V> {
 
    private int capacity;
    private int size;
    private CustomList<Entry<K, V>>[] array;
    private CustomList<K> keys;
 
    //-------------------------------------nested entry class 
    public static class Entry<K, V> {
        private K key;
        private V value;
 
        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
 
        public K getKey() {
            return key;
        }
 
        public V getValue() {
            return value;
        }
 
        public void setValue(V value) {
            this.value = value;
        }
    }
    //---------------------------------end of nested entry class 
 
    // Constructor for HashMap
    @SuppressWarnings("unchecked")
    public HashMap(int capacity) {
      
        if (capacity == 0 ) {
            this.capacity = 1;
        } else {
            this.capacity = capacity;
       
        }
      
        this.size = 0;
        
        this.array = new CustomList[this.capacity];
        
        for (int i = 0; i < this.capacity; i++) {
            array[i] = new CustomList<Entry<K, V>>();
        }
        
        keys = new CustomList<>();
    }
 
    public int size() {
        return size;
    }
 
    public boolean isEmpty() {
        return size == 0;
    }
 
    public void clear() {
        for (int i = 0; i < capacity; i++) {
            array[i].clear();
        }
        size = 0;
    }
 
    public V get(K key) {
        int index = (int) key % capacity;
        CustomList<Entry<K, V>> list = array[index];
 
        for (Entry<K, V> entry : list) {
            if (entry.getKey().equals(key)) {
                return entry.getValue();
            }
        }
 
        return null;
    }
 
    public boolean put(K key, V value) {
        int index = (int) key % capacity;
 
        for (Entry<K, V> entry : array[index]) {
            if (entry.getKey().equals(key)) {
                return false;
            }
        }
 
        array[index].add(new Entry<K, V>(key, value));
        keys.add(key);
        size++;
        return true;
    }
 
    public boolean replace(K key, V value) {
        int index = (int) key % capacity;
 
        for (Entry<K, V> entry : array[index]) {
            if (entry.getKey().equals(key)) {
                entry.setValue(value);
                return true;
            }
        }
        
        return false;
    }
 
    public boolean containsKey(K key) {
      
        int index = (int) key % capacity;
 
        for (Entry<K, V> entry : array[index]) {
            if (entry.getKey().equals(key)) {
                return true;
            }
        }
 
        return false;
    }
 
    public boolean remove(K key) {
        int index = (int) key % capacity;
        CustomList<Entry<K, V>> list = array[index];
        
        for (Entry<K, V> entry : list) {
            if (entry.getKey().equals(key)) {
                list.remove(entry);
                keys.remove(key);
                size--;
                return true;
            }
        }
 
        return false;
    }
 
    @Override
    public String toString() {
      System.out.println(" [ ");
      for (int i = 0; i < capacity; i++) {
 
              for (Entry<K, V> entry : array[i]) {
                  System.out.print(" " + entry.getKey() + " : rating = " + entry.getValue());
              }
              System.out.println(" ");
      }
      System.out.print(" ] ");
      return " ";
    }


    public CustomList<K> getKeySet() {
        return keys;
    }

 
}