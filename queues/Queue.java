package data_structures.queues;

/**
 * A basic queue implementation using a singly-linked list with
 * two pointers – one to the front-most element and another to the last
 * element.
 */


import java.util.*;

public class Queue<Item> implements Iterable<Item> {
    private Node<Item> first, last;
    private int count;

    public Queue() {
        first = null;
        last  = null;
        count = 0;
    }

    public void enqueue(Item item) {
        Node<Item> oldlast = last;

        last = new Node<Item>();
        last.item = item;
        last.next = null;

        if (isEmpty())         first = last;
        else            oldlast.next = last;

        ++count;
    }

    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");

        Item item = first.item;

        first = first.next;

        --count;

        if (isEmpty()) last = null;

        return item;
    }

    public boolean isEmpty() {
        return first == null;
    }

    public int size() {
        return count;
    }

    public Iterator<Item> iterator() {
        return new QueueIterator<Item>(first);
    }

    private class QueueIterator<Item> implements Iterator<Item> {
        private Node<Item> current;

        public QueueIterator(Node<Item> first) {
            current = first;
        }

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Item item = current.item;
            current   = current.next;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private class Node<Item> {
        Item item;
        Node<Item> next;
    }

    public static void main(String[] args) {
        Queue<String> q = new Queue<String>();
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {
            String item = scanner.next();
            if (!item.equals("-")) {
                q.enqueue(item);
            } else if (!q.isEmpty()) {
                System.out.println(q.dequeue() + " ");
            }
            System.out.println("(" + q.size() + " left on queue)");
        }

        for (String s: q) {
            System.out.print(s + " ");
        }
        System.out.println("");
    }
}
