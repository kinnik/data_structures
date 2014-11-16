package data_structures.queues;

/**
 * A basic queue implementation using a singly-linked list with
 * two pointers – one to the front-most element and another to the last
 * element.
 */


import java.util.*;

public class Queue<Item>
{
    private Node first, last;
    private int count;

    public Queue()
    {
        first = null;
        last  = null;
        count = 0;
    }

    public void enqueue(Item item)
    {
        Node oldlast = last;

        last = new Node();
        last.item = item;
        last.next = null;

        if (isEmpty())         first = last;
        else            oldlast.next = last;

        ++count;
    }

    public Item dequeue()
    {
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");

        Item item = first.item;

        first = first.next;

        if (isEmpty()) last = null;

        --count;

        return item;
    }

    public boolean isEmpty() { return first == null; }

    public int size() { return count; }

    private class Node
    {
        Item item;
        Node next;
    }
}
