import java.util.*;

public class Stack<Item> implements Iterable<Item>{
    private Node<Item> first;
    private int count;

    public Stack() {
        first = null;
        count = 0;
    }

    public void push(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }

        Node<Item> oldFirst = first;

             first = new Node<Item>();
        first.item = item;
        first.next = oldFirst;
        
        ++count;
    }

    public Item pop() {
        if (first == null) {
            throw new NoSuchElementException();
        }

        Item item = first.item;
        first = first.next;

        --count;

        return item;
    }

    public boolean isEmpty() {
        return first == null;
    }

    public int size() {
        return count;
    }

    private class Node<Item> {
        private Item item;
        private Node<Item> next;
    }

    public Iterator<Item> iterator() {
        return new StackIterator<Item>(first);
    }

    private class StackIterator<Item> implements Iterator<Item> {
        private Node<Item> current;

        public StackIterator(Node<Item> first) {
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
              current = current.next;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public static void main(String[] args) {
        Stack<String> s = new Stack<String>();
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {
            String item = scanner.next();
            if (!item.equals("-")) {
                s.push(item);
            } else if (!s.isEmpty()) {
                System.out.println(s.pop() + " ");
            }
        }
        System.out.println("(" + s.size() + " left on stack)");

        for (String str: s) {
            System.out.print(str + " ");
        }
        System.out.println("");
    }
}
