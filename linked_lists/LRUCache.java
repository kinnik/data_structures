/**
 * Implementation of LRU Cache
 * http://www.cs.princeton.edu/courses/archive/fall13/cos226/precepts/week4-handout.pdf
 * not using Key,Value but only Key.
 * Internally maintains a linked list and a hashtable
 */

import java.util.*;

public class LRUCache<Item>
{
    private LinkedList<Node>      list; // cached items are stored here
    private Hashtable<Item, Node> hash; // keeps track of the existence;
                                        // also stores the wrapper class of
                                        // the given Item

    private int count, capacity;

    /**
     * Creates the cache of given capacity
     */
    public LRUCache(int capacity)
    {
        assert capacity > 0;

        list = new LinkedList<Node>();
        hash = new Hashtable<Item, Node>(capacity);

        this.capacity = capacity;
                count = 0;
    }

    /**
     * Returns true if the given item is already in cache.
     */
    public boolean inCache(Item item) { return hash.containsKey(item); }

    /**
     * If the given item already exists in the cache, it is moved to front.
     * If the given item doesn't exist in the cache, i's inserted at front.
     * If the cache is already full, the last item is removed.
     */
    public void cache(Item item)
    {
        Node node = hash.get(item);

        if (node == null) // doesn't exist
        {
            if (count == capacity) // evict the last; the capacity is full
            {
                final Node last = list.removeLast();
                hash.remove(last.item); // also remove from the hash table
            }

            node = new Node(item);
            list.addFirst(node);
            hash.put(item, node);

            if (count < capacity) ++count; // only if not full
        }
        else
        {
            // already exists, so remove it and add it to the front
            list.remove(node);
            list.addFirst(node);
        }
    }

    /**
     * returns an Iterable items
     */
    public Iterable<Item> items()
    {
        final LinkedList<Item> items = new LinkedList<Item>();

        for (Node n: list) items.add(n.item);

        return items;
    }

    /**
     * prints what is in the cache; for debugging
     */
    private void printCache()
    {
        final StringBuilder sb = new StringBuilder();

        for (Item item: items()) sb.append(item.toString()).append(" ");

        System.out.println(sb.toString());
    }

    /**
     * Wrapper class to store the item
     */
    private class Node
    {
        private final Item item;
        
        private Node(Item item) { this.item = item; }
    }

    /**
     * Unit test the class
     */
    public static void main(String[] args)
    {
        LRUCache<String> lru = new LRUCache<String>(5);
        // LRU cache (in order of when last cached)
        lru.cache("A"); // A (add A to front)
        lru.printCache();
        lru.cache("B"); // B A (add B to front)
        lru.printCache();
        lru.cache("C"); // C B A (add C to front)
        lru.printCache();
        lru.cache("D"); // D C B A (add D to front)
        lru.printCache();
        lru.cache("E"); // E D C B A (add E to front)
        lru.printCache();
        lru.cache("F"); // F E D C B (remove A from back; add F to front)
        lru.printCache();
        boolean b1 = lru.inCache("C"); // F E D C B (true)
        System.out.println(b1);
        boolean b2 = lru.inCache("A"); // F E D C B (false)
        System.out.println(b2);
        lru.cache("D"); // D F E C B (move D to front)
        lru.printCache();
        lru.cache("C"); // C D F E B (move C to front)
        lru.printCache();
        lru.cache("G"); // G C D F E (remove B from back; add G to front)
        lru.printCache();
        lru.cache("H"); // H G C D F (remove E from back; add H to front)
        lru.printCache();
        boolean b3 = lru.inCache("D"); // H G C D F (true)
        System.out.println(b3);

    }
}
