import java.util.*;

public class SeparateChainingHashTable<Key, Value>
{
  private static final int DEFAULT_TABLE_SIZE = 10;
  private int TABLE_SIZE;
  private LinkedList<Node<Key, Value>>[] chains;
  private int count;

  /*
   * creates a separate chaining hash table of the given capacity
   */
  public SeparateChainingHashTable(int capacity)
  {
    TABLE_SIZE = capacity;
    chains = (LinkedList<Node<Key, Value>>[]) new LinkedList[TABLE_SIZE];    
  }

  /*
   * creates a separate chaining hash table of the default size (10)
   */
  public SeparateChainingHashTable()
  {
    this(DEFAULT_TABLE_SIZE);
  }

  /* 
   * value = null means deletion
   * if the key already exists, the value of that key is replaced
   */
  public void put(Key key, Value value)
  {
    if (value == null)
    {
      delete(key);
      return;
    }

    // double the table size if the average length of the list >= 10
    if (count >= 10*TABLE_SIZE) resize(2*TABLE_SIZE);

    final int hashCode = calculateHashcode(key);
    if (chains[hashCode] == null)
    {
      chains[hashCode] = new LinkedList<Node<Key, Value>>();
    }
    else
    {
      delete(key);  // if it exists already, it will be deleted
    }

    chains[hashCode].add(new Node<Key, Value>(key, value));
    ++count;
  }

  /*
   *  returns null if the given key is not present in the hash table
   */
  public Value get(Key key)
  {
    final int hashCode = calculateHashcode(key);
    final LinkedList<Node<Key, Value>> chain = chains[hashCode];

    for (Node<Key, Value> node: chain)
      if (node.getKey().equals(key)) return node.getValue();

    return null;  // not found
  }

  /*
   *  returns true if the given key already exists in the table;
   *  false otherwise.
   */
  public boolean contains(Key key)
  {
    return get(key) != null;
  }

  /*
   *  deletes the object having the given key from the table.
   */
  public boolean delete(Key key)
  {
    final int hashCode = calculateHashcode(key);
    final LinkedList<Node<Key, Value>> chain = chains[hashCode];

    if (chain != null)
    {
      for (Node<Key, Value> node: chain)
      {
        if (node.getKey().equals(key))
        {
          chain.remove(node);
          --count;

          // halve the table size if the average length of the chain <= 2*TABLE_SIZE
          if (TABLE_SIZE > DEFAULT_TABLE_SIZE && count <= 2*TABLE_SIZE)
            resize(TABLE_SIZE/2);

          return true;
        }
      }
    }
    return false;
  }

  /*
   *  calculates hash code of the given key by masking the most significant
   *  bit (i.e., the sign bit) of the hashCode() returned by the given key
   *  and applying the modulus operator using the TABLE_SIZE as an operand.
   */
  public int calculateHashcode(Key key)
  {
    // Mask the sign bit first because the % operator may return a
    // negative integer for very large numbers
    return (key.hashCode() & 0x7fffffff) % TABLE_SIZE;
  }

  /*
   * returns true if the given hash table does not contain any key-value pairs
   */
  public boolean isEmpty()
  {
    return size() == 0;
  }

  /*
   * returns the number of key-value pairs present in the hash table
   */
  public int size()
  {
    return count;
  }

  /*
   * returns keys in the hash table as an Iterable object
   */
  public Iterable<Key> keys()
  {
    ArrayList<Key> list = new ArrayList<Key>(count);

    for (int i = 0; i < TABLE_SIZE; ++i)
    {
      LinkedList<Node<Key, Value>> chain = chains[i];
      for (Node<Key, Value> node: chain)
        list.add(node.getKey());
    }

    return list;
  }

  /*
   * the Node class is used to store a key-value pair
   */
  private class Node<Key, Value>
  {
    private Key key;
    private Value value;

    private Node(Key key, Value value)
    {
      this.key   = key;
      this.value = value;
    }

    private Key   getKey()    { return key;   }
    private Value getValue()  { return value; }
  }

  /*
   * 
   */
  private void resize(int numChains)
  {
    SeparateChainingHashTable<Key, Value> newTable = new SeparateChainingHashTable<Key, Value>(numChains);
    for (int i = 0; i < TABLE_SIZE; ++i)
    {
      final LinkedList<Node<Key, Value>> chain = chains[i];
      for (Node<Key, Value> node: chain)
      {
        newTable.put(node.getKey(), node.getValue());
      }
    }
    TABLE_SIZE = newTable.TABLE_SIZE;
    count  = newTable.count;
    chains = newTable.chains;
  }

  public static void main(String[] args)
  {
    SeparateChainingHashTable<Integer, Integer> hashtable = new SeparateChainingHashTable<Integer, Integer>();

    System.out.println("adding squares");
    for (int i = 0; i < 50; ++i)
    {
      Integer integer = new Integer(i);
      hashtable.put(integer, new Integer(i*i));
    }
    System.out.println("====================");

    System.out.println("retrieving what is there. count = " + hashtable.size());
    for (int i = 0; i < 50; ++i)
    {
      Integer integer = new Integer(i);
      System.out.println("key: " + i + "\tvalue: " + hashtable.get(integer));
    }
    System.out.println("====================");    

    System.out.println("deleting squares of even numbers");
    for (int i = 0; i < 50; ++i)
    {
      Integer integer = new Integer(i);
      if (i % 2 == 0) hashtable.delete(integer);
    }
    System.out.println("====================");

    System.out.println("printing what's left. count = " + hashtable.size());
    for (int i = 0; i < 50; ++i)
    {
      Integer integer = new Integer(i);      
      System.out.println("key: " + i + "\tvalue: " + hashtable.get(integer));
    }
    System.out.println("====================");
  }
}
