import java.util.*;

public class ResizingArrayQueue<Item> implements Iterable<Item> {
    private static final int DEFAULT_CAPACITY = 10;
    private Item[] items;
    private int head, tail, count;

    public ResizingArrayQueue(int capacity) {
        if (capacity <=0) {
            throw new IllegalArgumentException();
        }

        items = (Item[]) new Object[capacity];
        head  = 0;
        tail  = 0;
        count = 0;
    }

    public ResizingArrayQueue() {
        this(DEFAULT_CAPACITY);
    }

    public void enqueue(Item item) {
        if (count == items.length) {
            resize(count * 2);
        }

        items[tail++] = item;
        if (tail == items.length) {
            tail = 0;
        }

        ++count;
    }

    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        Item item = items[head];
        items[head] = null;

        ++head;

        if (head == items.length) {
            head = 0;
        }
 
        --count;

        if (count > 0 && count == items.length / 4) {
            resize(items.length / 2);
        }

        return item;
    }

    public int size() {
        return count;
    }

    private void resize(int toCapacity) {
        Item[] newItems = (Item[]) new Object[toCapacity];

        for (int i=0; i < count; ++i) {
            newItems[i] = items[(head + i) % items.length];
        }

        items = newItems;
        head  = 0;
        tail  = count;
    }

    public boolean isEmpty() {
        return count == 0;
    }

    public Iterator<Item> iterator() {
        return new QueueIterator();
    }

    private class QueueIterator implements Iterator<Item> {
        private int current = 0;

        public boolean hasNext() {
            return current < count;
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Item item = items[(head + current) % items.length];
            ++current;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public static void main(String[] args) {
        ResizingArrayQueue<String> q = new ResizingArrayQueue<String>();
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {
            String item = scanner.next();
            if (!item.equals("-")) {
                q.enqueue(item);
            } else if (!q.isEmpty()) {
                System.out.println(q.dequeue() + " ");
            }
        }
        System.out.println("(" + q.size() + " left on queue)");

        for (String s: q) {
            System.out.print(s + " ");
        }
        System.out.println("");
    }


}
