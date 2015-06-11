import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

/*
 * Circular blocking queue implementation using an array and a lock.
 * If the queue is full when enqueue() is called, it waits until the 
 * 'queueNotFull' signal is received. On the other hand, if the queue is
 * not full, it enqueus the given item and sends a signal to 
 * 'queueNotEmpty' so that the thread awaiting that signal is able to consume.

 * Similarly, if the queue is empty when dequeue() is called, it waits until
 * the 'queueNotEmpty' signal is received. On the other hand, If the queue 
 * is not empty, it dequeues an item and sends a signal to 'queueNotFull' 
 * so that the thread awaiting that signal is able to add an additional item.

 * enqueue: O(1) when queue is not full
 * dequeue: O(1) when queue is not empty
 */

public class CircularBlockingQueueConditions<T> implements Iterable<T> {
    private final int CAPACITY = 10;

    private int beginIndex, endIndex, count;
    private final T[] queue;

    private final Lock lock;
    private final Condition queueNotEmpty, queueNotFull;

    public CircularBlockingQueueConditions() {
        beginIndex = 0;
          endIndex = 0;
             count = 0;
             queue = (T[]) new Object[CAPACITY];

                 lock = new ReentrantLock();
        queueNotEmpty = lock.newCondition();
         queueNotFull = lock.newCondition();
    }

    public int size() {
        return count;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public void enqueue(T item) throws InterruptedException {
        lock.lock();
        while (true) {
            if (count < CAPACITY) {
                System.out.println("Enqueuing: " + item);
                queue[endIndex] = item;
                endIndex = (endIndex + 1) % CAPACITY;
                ++count;
                queueNotEmpty.signal();
                break;
            } else {
                try {
                    queueNotFull.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    throw new InterruptedException();
                }
            }
        }
        lock.unlock();
    }

    public T dequeue() throws InterruptedException {
        lock.lock();
        T item;
        while (true) {
            if (count > 0) {
                item = queue[beginIndex];
                System.out.println("Dequeuing: " + item);
                queue[beginIndex] = null; // avoid dangling pointer
                beginIndex = (beginIndex + 1) % CAPACITY;
                --count;
                queueNotFull.signal();
                break;
            } else {
                try {
                    queueNotEmpty.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    throw new InterruptedException();
                }
            }
        }
        lock.unlock();
        return item;
    }

    public Iterator<T> iterator() {
        return new QueueIterator();
    }

    private class QueueIterator implements Iterator<T> {
        private int iterIndex, iterCount;

        public QueueIterator() {
            iterIndex = beginIndex;
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
            T item = queue[iterIndex];
            iterIndex = (iterIndex + 1) % CAPACITY;
            --iterCount;
            return item;
        }
    }

    public static void main(String[] args) {
        CircularBlockingQueue<Integer> queue = new CircularBlockingQueue<>();

        // producer-consumer
        final ExecutorService producerThreadPool = Executors.newCachedThreadPool();
        final ExecutorService consumerThreadPool = Executors.newCachedThreadPool();
        
        producerThreadPool.submit( () -> {
            for (int i = 0; i < 10000; ++i) {
                try {
                    queue.enqueue(i);
                    System.out.println("Enqueued: " + i);
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

        producerThreadPool.shutdown();
        consumerThreadPool.shutdown();
    }

}
