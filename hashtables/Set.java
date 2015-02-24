import java.util.*;

public class Set<Item> implements Iterable<Item> {
    private static final int DEFAULT_CAPACITY = 10;
    private int capacity;
    private int count;
    private LinkedList<Item>[] chains;

    public Set() {
        this(DEFAULT_CAPACITY);
    }

    public Set(int capacity) {
        if (capacity <= 0)
            throw new IllegalArgumentException();
        else if (capacity < DEFAULT_CAPACITY)
            this.capacity = DEFAULT_CAPACITY;
        else
            this.capacity = capacity;

        chains = (LinkedList<Item>[]) new LinkedList[this.capacity];
         count = 0;
    }

    public void put(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("item cannot be null");
        }

        if (contains(item)) {
            return;
        }

        if (count / capacity >= 10) {
            resize(capacity * 2);
        }

        final int index = calcHashCode(item);

        if (chains[index] == null) {
            chains[index] = new LinkedList<Item>();
        }

        chains[index].add(item);
        ++count;
    }

    public void delete(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("item cannot be null");
        }

        if (!contains(item)) {
            throw new NoSuchElementException("item does not exist");
        }

        final int index = calcHashCode(item);

        LinkedList<Item> chain = chains[index];

        for (Item i: chain) {
            if (i.equals(item)) {
                chain.remove(i);
                --count;
                if (capacity > DEFAULT_CAPACITY && count == capacity / 4) {
                    resize(capacity / 2);
                }
            }
        }
    }

    public boolean contains(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("item cannot be null");
        }

        final int index = calcHashCode(item);

        LinkedList<Item> chain = chains[index];
        if (chain == null) {
            return false;
        }

        for (Item i: chain) {
            if (i.equals(item)) {
                return true;
            }
        }

        return false;
    }

    private int calcHashCode(Item item) {
        return (item.hashCode() & 0x7fffffff) % capacity;
    }

    private void resize(int toCapacity) {
        Set<Item> tmp = new Set<Item>(toCapacity);

        for (int i = 0; i < capacity; ++i) {
            LinkedList<Item> chain = chains[i];

            if (chain == null) continue;

            for (Item item: chain) {
                tmp.put(item);
            }
        }
          chains = tmp.chains;
        capacity = tmp.capacity;
           count = tmp.count;
    }

    public Set<Item> intersect(Set<Item> that) {
        Set<Item> result = new Set<Item>();

        if (this.size() < that.size()) {
            for (int i = 0; i < capacity; ++i) {
                LinkedList<Item> chain = chains[i];
                
                if (chain == null) {
                    continue;
                }

                for (Item item: chain) {
                    if (that.contains(item)) {
                        result.put(item);
                    }
                }
            }
        } else {
            for (int i = 0; i < that.capacity; ++i) {
                LinkedList<Item> chain = that.chains[i];

                if (chain == null) {
                    continue;
                }

                for (Item item: chain) {
                    if (contains(item)) {
                        result.put(item);
                    }
                }
            }
        }

        return result;
    }

    public Set<Item> union(Set<Item> that) {
        Set<Item> result = new Set<Item>();

        for (int i = 0; i < this.capacity; ++i) {
            LinkedList<Item> chain = this.chains[i];

            if (chain == null) {
                continue;
            }

            for (Item item: chain) {
                result.put(item);
            }
        }

        for (int i = 0; i < that.capacity; ++i) {
            LinkedList<Item> chain = that.chains[i];

            if (chain == null) {
                continue;
            }

            for (Item item: chain) {
                result.put(item);
            }
        }

        return result;
    }

    public int size() {
        return count;
    }

    public Iterator<Item> iterator() {
        return new SetIterator();
    }

    private class SetIterator implements Iterator<Item>
    {
        private Item[] allItems;
        private int    numIterations;

        public SetIterator() {
            numIterations = 0;
                 allItems = (Item[]) new Object[count];

            for (int i = 0; i < capacity; ++i) {
                LinkedList<Item> chain = chains[i];

                if (chain == null) {
                    continue;
                }

                for (Item item: chain) {
                    allItems[numIterations++] = item;
                }
            }
            numIterations = 0; // reset it so that next() can make use of it
        }

        public boolean hasNext() {
            return numIterations < allItems.length;
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            return allItems[numIterations++];
        }
    }

    public static void main(String[] args) {
        Set<Integer> setA = new Set<Integer>();
        Set<Integer> setB = new Set<Integer>();

        setA.put(1);
        setA.put(2);
        setA.put(3);
        setB.put(3);
        setB.put(4);
        setB.put(5);

        Set<Integer> setI = setA.intersect(setB);
        Set<Integer> setU = setA.union(setB);

        for (Integer i: setI) {
            System.out.print(i + ",");
        }
        System.out.println("\n");

        for (Integer u: setU) {
            System.out.print(u + ",");
        }
        System.out.println("\n");
    }
}
