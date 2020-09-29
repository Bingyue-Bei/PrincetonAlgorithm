/* *****************************************************************************
 *  Created by Bingyue Bei, 2020/09/29
 *  Description: This is an implementation of RandomizedQueue with resizing Array.
 *  Each time we just remove the last item/ add the last item.
 *  To achieve randomness, we swap the added item with a randomly chosen item in the array.
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private int size = 0;
    private Item[] queue;

    // construct an empty randomized queue
    public RandomizedQueue() {
        queue = (Item[]) new Object[size];
    }

    // resize the array
    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < size; i++) {
            copy[i] = queue[i];
        }
        queue = copy;
    }

    // swap item after insertion.
    private void swap(int index) {
        int target = StdRandom.uniform(size);
        Item temp = queue[target];
        queue[target] = queue[index];
        queue[index] = temp;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (size == queue.length) {
            resize(2 * queue.length + 1);
        }
        queue[size] = item;
        size++;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        swap(size - 1);
        size--;
        Item item = queue[size];
        queue[size] = null;
        if (size > 0 && size < queue.length / 4) {
            resize(queue.length / 2);
        }
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        int index = StdRandom.uniform(size);
        return queue[index];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        class RandomizedQueueIterator implements Iterator<Item> {
            private int index = 0;
            Item[] q = (Item[]) new Object[size];

            private RandomizedQueueIterator() {
                for (int i = 0; i < size; i ++) {
                    q[i] = queue[i];
                }
                StdRandom.shuffle(q);
            }

            public boolean hasNext() {
                return index < size;
            }

            public Item next() {
                if (index >= size) {
                    throw new NoSuchElementException();
                }
                Item item = q[index];
                index++;
                return item;
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
        return new RandomizedQueueIterator();
    }

    // unit testing (required)
    public static void main(String[] args) {
        /** Test all the method. */
        RandomizedQueue<Character> testMethod = new RandomizedQueue<Character>();
        testMethod.enqueue('a');
        testMethod.enqueue('b');
        testMethod.enqueue('c');
        testMethod.enqueue('d');
        testMethod.enqueue('e');
        testMethod.enqueue('f');
        testMethod.enqueue('g');
        testMethod.enqueue('h');
        for (int i = 0; i < 8; i++) {
            System.out.println(testMethod.dequeue());
        }
        System.out.println();
        /** Test the iterator. */
        RandomizedQueue<Integer> testIterator = new RandomizedQueue<Integer>();
        for (int i = 0; i < 10; i++) {
            testIterator.enqueue(i);
        }
        Iterator<Integer> iter = testIterator.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next());
        }
    }
}