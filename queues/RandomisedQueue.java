/*
 * Exercise 1.3.35 & 1.3.36 from Algorithms (4th Edition) by 
   Robert Sedgewick & Kevin Wayne

   An implementation of a random queue which stores a collection of items.
 */

import java.util.*;

public class RandomisedQueue<Item> implements Iterable<Item>
{
    private static final int    DEFAULT_CAPACITY = 10;
    private static final Random random = new Random();

    private int count;      // keeps track of the number of items
    private int capacity;   // the capacity of the allocated array

    private Item[] items;   // the array stores the given items

    /**
     * Creates an empty random queue of the given capacity
     * throws IllegalArgumentException if capacity is <= 0
     */
    public RandomisedQueue(int capacity)
    {
        if (capacity <= 0) throw new IllegalArgumentException("Capacity must be greater than zero");
        this.capacity = capacity;

        items = (Item[]) new Object[capacity];
        count = 0;
    }

    /**
     * Creates an empty random queue of size 10
     */
    public RandomisedQueue() { this(DEFAULT_CAPACITY); }

    /**
     * Returns true if there are no items in the queue
     */
    public boolean isEmpty() { return size() == 0; }

    /**
     * Returns the number of items in the queue
     */
    public int size() { return count; }

    /**
     * Enqueues the given item
     * throws IllegalArgumentException if item is null
     */
    public void enqueue(Item item)
    {
        if (item == null)
            throw new IllegalArgumentException("Item must not be null");

        if (count == capacity) resize(2*capacity);

        items[count++] = item;
    }

    /**
     * Dequeues an item from the queue
     * throws NoSuchElementException if the queue is empty
     */
    public Item dequeue()
    {
        if (isEmpty())
            throw new NoSuchElementException("The queue is empty");

        final int randomPosition = random.nextInt(size());

        exchange(items, randomPosition, count-1);

        final Item randomItem = items[count-1];

        items[--count] = null;

        if (count > 0 && count == capacity / 4) resize(capacity / 2);

        return randomItem;
    }

    /**
     * Returns a random item from the queue; the item is not removed
     * throws NoSuchElementException if the queue is empty
     */
    public Item sample()
    {
        if (isEmpty())
            throw new NoSuchElementException("The queue is empty");

        return items[random.nextInt(size())];
    }

    /**
     * fatal error if toCapacity is not greater than the size of the queue
     * copies all the elements in the queue to a new queue of given capacity
     * reasigns the new queue to the queue instance
     */
    private void resize(int toCapacity)
    {
        assert toCapacity > size();

        final Item[] tmp = (Item[]) new Object[toCapacity];

        for (int i = 0; i < size(); ++i) tmp[i] = items[i];

        capacity = toCapacity;
           items = tmp;
    }

    /**
     * Returns a randomised iterator
     */
    public Iterator<Item> iterator()
    { 
        return new RandomisedQueueIterator();
    }

    /**
     * Implementation of RandomisedQueueIterator
     */
    private class RandomisedQueueIterator implements Iterator<Item>
    {
        private int[] indices;          // randomised indices
        private int   iteratorPosition; // keeps track of the iterator

        /**
         * initialises the indices array with random indices
         */
        public RandomisedQueueIterator()
        {
            indices = new int[size()];
            
            for (int i = 0; i < indices.length; ++i) indices[i] = i;

            shuffle(indices);
            iteratorPosition = 0;
        }

        /**
         * returns true if there are more items to be iterated
         */
        public boolean hasNext()
        {
            return iteratorPosition < indices.length;
        }

        /**
         * returns the next item in the iterator
         * throws NoSuchElementException if the iterator is empty
         */
        public Item next()
        {
            if (!hasNext()) throw new NoSuchElementException("The queue is empty");

            return items[indices[iteratorPosition++]];
        }

        /**
         * not supported
         * throws UnsupportedOperationException
         */
        public void remove() { throw new UnsupportedOperationException(); }
    }

    /**
     * shuffle the elements in the given array using Knuth Shuffle
     */
    private static void shuffle(int[] a)
    {
        for (int i = a.length-1; i >= 1; --i)
        {
            // get a random number between 0 and i inclusive (hence i+1)
            final int r = random.nextInt(i+1);
            exchange(a, i, r);
        }
    }

    // swaps the position of the two elements at the given indices in the array a
    private static void exchange(Object[] a, int i, int j)
    {
         Object tmp = a[i];
               a[i] = a[j];
               a[j] = tmp;
    }

    // swaps the position of the two ints at the given indices in the int array a
    private static void exchange(int[] a, int i, int j)
    {
        final int tmp = a[i];
                 a[i] = a[j];
                 a[j] = tmp;
    }

    public static void main(String args[])
    {
        final RandomisedQueue<String> randoq = new RandomisedQueue<String>();
        randoq.enqueue("Tinky Winky");
        randoq.enqueue("Dipsy");
        randoq.enqueue("Lala");
        randoq.enqueue("Po");

        for (String s: randoq) { System.out.println(s); }
    }
}
