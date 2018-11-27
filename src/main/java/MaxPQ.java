public class MaxPQ<Key extends Comparable<Key>> {

    private boolean less(int i, int j) {
        return pq[i].compareTo(pq[j]) < 0;
    }

    private void exch(int i, int j) {
        Key t = pq[i];
        pq[i] = pq[j];
        pq[j] = t;
    }

    private Key[] pq;
    private int N = 0;

    private void swim(int k) {
        while (k > 1 && less(k/2, k))
        {
            exch(k/2, k);
            k /= 2;
        }
    }

    private void sink(int k)
    {
        while (2*k <= N)
        {
            int j = 2*k;
            if (j < N && less(j, j+1)) j++;
            if (!less(k,j)) break;
            exch(k,j);
            k = j;
        }
    }

    MaxPQ() {

    }

    MaxPQ(int maxN) {
        pq = (Key[]) new Comparable[maxN+1];
    }

    MaxPQ(Key[] a) {

    }

    void insert(Key v) {
        pq[++N] = v;
        swim(N);
    }

    Key max() {
        return pq[1];
    }

    Key delMax() {
        Key max = pq[1];
        exch(1, N--);
        pq[N+1] = null;
        sink(1);
        return max;
    }

    boolean isEmpty() {
        return N == 0;
    }

    int size() {
        return N;
    }

}