/*
 Hash Table implementation using the Linear Probing algorithm.
 Closely follows the http://algs4.cs.princeton.edu/34hash/LinearProbingHashST.java.html taught as part of the Algorithms course on Coursera.
*/

public class LinearProbingHashTable<Key, Value>
{
    private static final int DEFAULT_CAPACITY = 10;

    private   Key[] keys;
    private Value[] values;

    private   int   capacity;
    private   int   count;

    public LinearProbingHashTable(int capacity)
    {
        this.capacity = capacity;

          keys =   (Key[]) new Object[capacity];
        values = (Value[]) new Object[capacity];

         count = 0;
    }

    public LinearProbingHashTable()
    {
        this(DEFAULT_CAPACITY);
    }

    // if the value is null, delete the existing entry
    public void put(Key key, Value value)
    {
        if (value == null)
        {
            delete(key);
            return;
        }

        // resize: double the array size if the array is half-full
        if (size() >= capacity / 2) resize(2 * capacity);

        int index = calculateHashCode(key);

        while (keys[index] != null)
        {
            // if the key already exists, replace the value
            if (keys[index].equals(key))
            {
                values[index] = value;
                ++count;
                return;
            }
            index = ++index % capacity;
        }

        // a null entry has been found at this point
          keys[index] = key;
        values[index] = value;
        ++count;
    }

    public Value get(Key key)
    {
        int index = calculateHashCode(key);

        while (keys[index] != null)
        {
            if (keys[index].equals(key)) return values[index];

            index = ++index % capacity;
        }

        return null;
    }

    public     int     size() { return count; }
    public boolean  isEmpty() { return size() == 0; }

    public boolean contains(Key key) { return get(key) != null; }

    public void delete(Key key)
    {
        if (!contains(key)) return;

        int index = calculateHashCode(key);

        while (!keys[index].equals(key)) index = ++index % capacity;

          keys[index] = null;
        values[index] = null;
        --count;

        index = ++index % capacity;
        while (keys[index] != null)
        {
              Key   keyToRehash =   keys[index];
            Value valueToRehash = values[index];

              keys[index] = null;
            values[index] = null;
            --count;

            put(keyToRehash, valueToRehash);
            index = ++index % capacity;
        }
        // resize the array to half the capacity if its size is less
        // than or equal to 1/8 of the capacity.
        if (count > 0 && count <= capacity/8) resize (capacity/2);
    }

    private void resize(int toCapacity)
    {
        final LinearProbingHashTable<Key, Value> temp = new LinearProbingHashTable<Key, Value>(toCapacity);

        for (int i = 0; i < capacity; ++i)
        {
            if (keys[i] != null)
            {
                temp.put(keys[i], values[i]);    
            }
        }

            keys = temp.keys;
          values = temp.values;
        capacity = temp.capacity;
    }

    // In Java, % may return negative.
    // Math.abs() returns a negative result for the largest negative number.
    // For example, s.hashCode() is -2^31 for the Java String value "polygenelubricants"
    // We mark the sign bit (to turn the 32-bit number into a 31-bit non-negative integer) and then compute the remainder when dividing by M, as in modular hashing.
    private int calculateHashCode(Key k)
    {
        return (k.hashCode() & 0x7fffffff) % capacity;
    }

}
