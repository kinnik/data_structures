/* A special resizing array that supports the below operations in amortised O(1) time:
   void insert(Item item);
   void remove(Item item);
   boolean contains(Item item);
   Item random();

   Implementation
   ==============
   The resizing array supports insert and random in O(1) time.
   In order to support remove and contains, we use hash tables.

   insert – Everytime an item is inserted, we record that item's position in the hash table. The hash table thus stores the key value pair of (item, position).

   remove – To remove the given item in O(1) time, we need to know its position. This position is already recorded in the hash table.
   (i)  We therefore retrieve the position first from the hash table in O(1) amortised time.
   (ii) Removing the item from its current position means setting it to null. This will however create a gap in the array. To prevent this, we fill the gap with the last item in the array and take care of the last-index accordingly.
   
   The remove operation highlights the two limitations:
   (i)  duplicates are not allowed in the data structure
   (ii) ordered operations are not suppored (this is implied anyway since it is not listed in the API.

   contains – Since every insert adds the value of the item as a key in the hash table, we are able to take advantage of the hash table's O(1) amortised look-up operation to support this in O(1) (amortised).

   random – generate a random number to select the item from the array at O(1).
*/

import java.util.*;

public class SpecialResizingArray<Item> implements Iterable<Item>
{
    private static final int    DEFAULT_CAPACITY = 10;
    private static final Random random = new Random();

    private Item[] values; // stores the given value
    private Hashtable<Item, Integer> positions; // stores the position of the given value in the values array

    private int capacity;  // capacity of the values array
    private int nextItemIndex; // index at which the next value will be added; can also be used to query the number of elements, aka (size), since the array is zero-indexed.

    public SpecialResizingArray(int capacity)
    {
        this.capacity = capacity;
        nextItemIndex = 0;

           values = (Item[]) (new Object[capacity]);
        positions = new Hashtable<Item, Integer>(capacity);
    }

    // the default constructor sets the capacity to DEFAULT_CAPACITY
    public SpecialResizingArray()
    {
        this(DEFAULT_CAPACITY);
    }

    // throws new IllegalArgumentException if item is null.
    // stores the item in the values array.
    // records the position in the positions hashtable.
    // doubles the values array if the array is full.
    public void insert(Item item)
    {
        if (item == null)       throw new IllegalArgumentException();

        if (size() == capacity) resize(2 * capacity);

        values[nextItemIndex] = item;
        positions.put(item, nextItemIndex);

        ++nextItemIndex;
    }

    // returns true if there is a position recorded in the positions hashtable
    // for the given item.
    public boolean contains(Item item)
    {
        if (isEmpty()) return false;

        return positions.get(item) != null;
    }

    // throws IllegalArgumentException if item is null
    // throws NoSuchElementException if attempting to remove the item
    // that does not exist.
    public void remove(Item item)
    {
        if (item == null)    throw new IllegalArgumentException();
        if (!contains(item)) throw new NoSuchElementException();

        if (size() == 1)
        {
                values[0] = null;
            nextItemIndex = 0;

            positions.remove(item);
        }
        else
        {
            final  int    removingAtIndex = positions.get(item);
            
            final Item itemAtLastPosition = values[--nextItemIndex];

            values[removingAtIndex] = itemAtLastPosition;
            values[nextItemIndex]   = null;
            // no need to explicitly remove from the hash table.
            // put will replace the value of the given key
            positions.put(itemAtLastPosition, removingAtIndex);

            if (size() > 0 && size() == capacity / 4) resize(capacity / 2);
        }
    }

    // returns the number of items present in the values array.
    public     int size()    { return nextItemIndex; }

    // returns true if the call to size() returns zero.
    public boolean isEmpty() { return size() == 0;   }


    // copy the values array into a new one of the given capacity.
    // the positions hash table is not rehashed; not necessary.
    private void resize(int toCapacity)
    {
        assert toCapacity >= size();

        final Item[] newArray = (Item[]) new Object[toCapacity];

        for (int i = 0; i < size(); ++i) newArray[i] = values[i];

           values = newArray;
         capacity = toCapacity;
    }

    // returns null if the array is empty
    // otherwise returns a random element from the values array, based on 
    // the random number generated by java.util.Random.nextInt
    public Item random()
    {
        if (isEmpty())   return null;

        // get a random number between 0 and size() - 1 inclusive; hence size()
        final int randomIndex = random.nextInt(size());

        return values[randomIndex];
    }

    // returns an iterator that iterates over the items in the values array in
    // arbitrary order
    public Iterator<Item> iterator()
    {
        return new SpecialResizingArrayIterator();
    }

    // the iterator doesn't implement remove() since it's optional
    private class SpecialResizingArrayIterator implements Iterator<Item>
    {
        private int index;

        public SpecialResizingArrayIterator()
        {
            index = 0;    
        }

        public boolean hasNext()
        {
            return index < size();
        }

        public void remove() { throw new UnsupportedOperationException(); }

        public Item next()
        {
            if (!hasNext()) throw new NoSuchElementException();
            return values[index++];
        }
    }

    public static void main(String[] args)
    {
        SpecialResizingArray<String> array = new SpecialResizingArray<String>();

        array.insert("tinky winky");
        array.insert("dipsy");
        array.insert("lala");
        array.insert("po");

        for (String s: array) System.out.println(array.random());
    }
}

