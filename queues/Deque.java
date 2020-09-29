/* *****************************************************************************
 *  Created by Bingyue Bei, 2020/09/29
 *  Description: This is an implementation of Deque using linked list.
 *  This implementation is likely to fail the memory requirements,
 *  which restricted that A deque containing n items must use at most
 *  48n + 192 bytes of memory.
 **************************************************************************** */

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private class Node {
        Item item;
        Node next;
        Node prev;
    }
    private Node head = null;
    private Node tail = null;
    private int size = 0;

    // construct an empty deque
    public Deque() {
        return;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        Node oldHead = head;
        head = new Node();
        head.item = item;
        head.prev = null;
        if (size > 0) {
            head.next = oldHead;
            oldHead.prev = head;
        } else {
            tail = head;
        }
        size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        Node oldTail = tail;
        tail = new Node();
        tail.item = item;
        tail.next = null;
        if (size > 0) {
            tail.prev = oldTail;
            oldTail.next = tail;
        } else {
            head = tail;
        }
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Node remove = head;
        head = remove.next;
        if (head != null) {
            head.prev = null;
        }
        remove.next = null;
        size--;
        return remove.item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Node remove = tail;
        tail = remove.prev;
        if (tail != null) {
            tail.next = null;
        }
        remove.prev = null;
        size--;
        return remove.item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        Iterator<Item> it = new Iterator<Item>() {
            private Node current = head;
            public boolean hasNext() {
                return current != null;
            }

            public Item next() {
                if (current == null) {
                    throw new NoSuchElementException();
                }
                Item item = current.item;
                current = current.next;
                return item;
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
        return it;
    }

    // unit testing (required)
    public static void main(String[] args) {
        /** Test cases when size == 1. */
        Deque<Character> testSingle = new Deque<Character>();
        testSingle.addFirst('a');
        System.out.println(testSingle.removeFirst());
        testSingle.addFirst('b');
        System.out.println(testSingle.removeLast());
        testSingle.addLast('c');
        System.out.println(testSingle.removeFirst());
        testSingle.addLast('d');
        System.out.println(testSingle.removeLast());
        testSingle.addLast('e');
        testSingle.addLast('f');
        System.out.println(testSingle.removeFirst());
        System.out.println(testSingle.removeFirst());
        /** Test iterator. */
        Deque<Integer> testIterator = new Deque<Integer>();
        for (int i = 0; i < 10; i++) {
            testIterator.addFirst(i);
        }
        Iterator<Integer> iter = testIterator.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next());
        }
    }
}