import java.util.*;
import java.util.concurrent.*;

/*
 * Circular blocking queue implementation using an array and thread
 * synchronisation. If the queue is full when enqueue() is called,
 * the calling thread is asked to wait until the queue has space again.
 * If the queue is empty when dequeue() is called, the calling thread
 * is asked to wait until there is something in the queue to be dequeued.
 * enqueue: O(1) when queue is not full
 * dequeue: O(1) when queue is not empty
 */

public class CircularBlockingQueue<T> implements Iterable<T> {
    private final int CAPACITY = 10;

    private int beginIndex, endIndex, count;
    private final T[] queue;
    
    public CircularBlockingQueue() {
        beginIndex = 0;
          endIndex = 0;
             count = 0;
             queue = (T[]) new Object[CAPACITY];
    }

    public int size() {
        return count;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public synchronized void enqueue(T item) throws InterruptedException {
        while (true) {
            if (count < CAPACITY) {
                System.out.println("Enqueuing " + item);
                queue[endIndex] = item;
                endIndex = (endIndex + 1) % CAPACITY;
                ++count;
                notify();
                break;
            } else {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    throw new InterruptedException();
                }
            }
        }
    }

    public synchronized T dequeue() throws InterruptedException {
        T item;
        while (true) {
            if (count > 0) {
                item = queue[beginIndex];
                System.out.println("Dequeuing " + item);
                queue[beginIndex] = null; // avoid dangling pointer
                beginIndex = (beginIndex + 1) % CAPACITY;
                --count;
                notify();
                break;
            } else {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    throw new InterruptedException();
                }
            }
        }
        return item;
    }

    public Iterator<T> iterator() {
        return new QueueIterator();
    }

    private class QueueIterator implements Iterator<T> {
        private int beginIterIndex, endIterIndex, iterCount;

        public QueueIterator() {
            beginIterIndex = beginIndex;
              endIterIndex = endIndex;
                  iterCount = count;
        }

        public boolean hasNext() {
            return iterCount > 0;
                   
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            T item = queue[beginIterIndex];
            beginIterIndex = (beginIterIndex + 1) % CAPACITY;
            --iterCount;
            return item;
        }
    }

    public static void main(String[] args) {
        CircularBlockingQueue<Integer> queue = new CircularBlockingQueue<>();

        // single thread scenario
        // 1. enqueue
        for (int i = 0; i < 10; ++i) {
            try {
                queue.enqueue(i);
            } catch (InterruptedException e) {
                System.err.println("Interrupted while queuing: " + i);
                e.printStackTrace();
            }
        }
        // 1.5 iterator
        System.out.print("Contents: ");
        for (Integer i: queue) {
            System.out.print(i + "\t");
        }
        System.out.println();

        // 2. dequeue
        for (int i = 0; i < 10; ++i) {
            try {
                queue.dequeue();
            } catch (InterruptedException e) {
                System.err.println("Interrupted while dequeuing: " + i);
                e.printStackTrace();
            }
        }
        // 2.5 iterator
        System.out.print("Contents: ");
        for (Integer i: queue) {
            System.out.print(i + "\t");
        }
        System.out.println();

        // producer-consumer scenario
        final ExecutorService producerThreadPool, consumerThreadPool;
        producerThreadPool = Executors.newCachedThreadPool();
        consumerThreadPool = Executors.newCachedThreadPool();
        producerThreadPool.submit( () -> {
            for (int i = 0; i < 10000; ++i) {
                try {
                    queue.enqueue(i);
                    System.out.println("Queued: " + i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        consumerThreadPool.submit( () -> {
            for (int i = 0; i < 10000; ++i) {
                try {
                    Thread.sleep(3000);
                    System.out.println("Dequeued: " + queue.dequeue());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
