/* *****************************************************************************
 *  Created by Bingyue Bei, 2020/09/29
 *
 **************************************************************************** */
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
public class Permutation {
    public static void main(String[] args) {
        int k = 0;
        if (args.length > 0) {
            k = Integer.parseInt(args[0]);
        }

        RandomizedQueue<String> q = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            q.enqueue(item);
        }
        while (k > 0) {
            StdOut.println(q.dequeue());
            k--;
        }

    }
}
