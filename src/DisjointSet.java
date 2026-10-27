/**
 * Mission 0. Disjoint Set Union (DSU).
 *
 * Each cell starts in its own set. The parent array stores the tree,
 * and size stores the size of each root's set.
 */
public class DisjointSet {
    private final int[] parent;
    private final int[] size;
    private int setCount;

    public DisjointSet(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Number of elements cannot be negative");
        }

        parent = new int[n];
        size = new int[n];
        setCount = n;

        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }
    }

    /** Returns the root of x's set and compresses the path. */
    public int find(int x) {
        // Path halving: on each step, skip one parent in the tree.
        while (parent[x] != x) {
            parent[x] = parent[parent[x]];
            x = parent[x];
        }
        return x;
    }

    /** Joins two sets. Returns false if they were already connected. */
    public boolean union(int a, int b) {
        int rootA = find(a);
        int rootB = find(b);

        if (rootA == rootB) {
            return false;
        }

        // Keep the larger tree as the root.
        if (size[rootA] < size[rootB]) {
            int temp = rootA;
            rootA = rootB;
            rootB = temp;
        }

        parent[rootB] = rootA;
        size[rootA] += size[rootB];
        setCount--;
        return true;
    }

    public boolean connected(int a, int b) {
        return find(a) == find(b);
    }

    public int count() {
        return setCount;
    }
}
