// an implementation of a symbol table using a linked-list,
// as part of the algorithms course from Princeton on Coursera.

public class LinkedListSymbolTable<Key, Value>
{
  private Node first;

  private class Node
  {
    private Key   key;
    private Value value;
    private Node  next;

    private Node(Key key, Value value, Node next)
    {
      this.key   = key;
      this.value = value;
      this.next  = next;
    }
  }

  public void add(Key key, Value value)
  {
    // if the keys are the same, replace the value
    for (Node n = first; n != null; n = n.next)
    {
      if (n.key.equals(key))
      {
        n.value = value;
        return;
      }
    }

    first = new Node(key, value, first);
  }

  public Value get(Key key)
  {
    for (Node n = first; n != null; n = n.next)
      if (n.key.equals(key))  return n.value;

    return null;
  }

  public void delete(Key key)
  {
    Node prev    = null;
    Node current = first;

    while (current != null)
    {
      if (current.key.equals(key))
      {
        if (prev != null) prev.next = current.next;
        else              first     = current.next;
        break;
      }
         prev = current;
      current = current.next;
    }
  }

}
