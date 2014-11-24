import java.util.*;

public class MinPriorityQueue<Key extends Comparable>
{
    private Key[] pq;
    private int   count;
    private int   capacity;

    private static int DEFAULT_CAPACITY = 10;

    public MinPriorityQueue(int capacity)
    {
        this.capacity = capacity;

        count = 0;
           pq = (Key[]) (new Comparable[capacity+1]);
    }

    public MinPriorityQueue() { this(DEFAULT_CAPACITY+1); }

    public void insert(Key k)
    {
        if (count == capacity)  resize(capacity * 2);

        pq[++count] = k;
        swim(count);
    }

    public boolean isEmpty() { return count == 0; }
    public int     size()    { return count; }

    private void swim(int k)
    {
        // while the parent is greater than the child and the parent is
        // not at the top most position
        while (k > 1 && greater(k/2, k))
        {
            exchange(k, k/2);
            k = k / 2;
        }
    }

    private void sink(int k)
    {
        while (2*k <= count)
        {
            int childIndex = 2*k;
            if (childIndex < count && greater(childIndex, childIndex+1))
                ++childIndex;

            if (!greater(k, childIndex)) break;
            
            exchange(k, childIndex);
            k = childIndex;
        }
    }

    public Key deleteMinimum()
    {
        if (isEmpty()) throw new NoSuchElementException("Priority Queue Empty");
        final Key minimum = pq[1];
        exchange(1, count--);
        sink(1);
        pq[count+1] = null;

        if (count == capacity / 4) resize(capacity/2);

        return minimum;
    }

    private boolean greater(int i, int j)
    {
        return pq[i].compareTo(pq[j]) > 0;
    }

    private void exchange(int i, int j)
    {
        final Key tmp = pq[i];
        pq[i] = pq[j];
        pq[j] = tmp;
    }

    private void resize(int toCapacity)
    {
        assert toCapacity > count;

        Key[] tmp = (Key[]) new Comparable[toCapacity+1];

        for (int i = 1; i <= count; ++i) tmp[i] = pq[i];

        pq = tmp;
        capacity = toCapacity;
    }

    public static void main(String args[])
    {
        MinPriorityQueue<String> pq = new MinPriorityQueue<String>();
        pq.insert("P");
        pq.insert("Q");
        pq.insert("E");
        System.out.print(pq.deleteMinimum());
        pq.insert("X");
        pq.insert("A");
        pq.insert("M");
        System.out.print(pq.deleteMinimum());
        pq.insert("P");
        pq.insert("L");
        pq.insert("E");
        System.out.print(pq.deleteMinimum());

        System.out.println("(" + pq.size() + " left on the queue)");
    }
}

