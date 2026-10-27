import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/** Missions 1 and 2: build a maze and find its minimum spanning tree. */
public class Maze {

    /** Mission 1: process the walls in the same order as walls.txt. */
    public static int[] breakWalls(Input.Walls walls) {
        DisjointSet dsu = new DisjointSet(walls.cells());
        List<Integer> broken = new ArrayList<>();

        for (int i = 0; i < walls.count(); i++) {
            if (dsu.union(walls.a()[i], walls.b()[i])) {
                broken.add(i);
            }
        }

        return toIntArray(broken);
    }

    /** Mission 2: Kruskal's algorithm, processing the cheapest walls first. */
    public static int[] cheapestMaze(Input.Walls walls) {
        Integer[] order = new Integer[walls.count()];
        for (int i = 0; i < order.length; i++) {
            order[i] = i;
        }

        // The input gives every wall a different cost, so no tie-break is needed.
        Arrays.sort(order, Comparator.comparingInt(i -> walls.cost()[i]));

        DisjointSet dsu = new DisjointSet(walls.cells());
        List<Integer> chosen = new ArrayList<>();

        for (int wall : order) {
            if (dsu.union(walls.a()[wall], walls.b()[wall])) {
                chosen.add(wall);
                if (chosen.size() == walls.cells() - 1) {
                    break;
                }
            }
        }

        return toIntArray(chosen);
    }

    /** Mission 2: Prim's algorithm with the cheapest available wall first. */
    public static long primCost(Input.Walls walls) {
        int cellCount = walls.cells();
        int wallCount = walls.count();
        if (cellCount == 0) {
            return 0;
        }

        // Each cell remembers the indexes of its touching walls.
        int[] degree = new int[cellCount];
        for (int wall = 0; wall < wallCount; wall++) {
            degree[walls.a()[wall]]++;
            degree[walls.b()[wall]]++;
        }
        int[][] incident = new int[cellCount][];
        for (int cell = 0; cell < cellCount; cell++) {
            incident[cell] = new int[degree[cell]];
        }
        Arrays.fill(degree, 0);
        for (int wall = 0; wall < wallCount; wall++) {
            int a = walls.a()[wall];
            int b = walls.b()[wall];
            incident[a][degree[a]++] = wall;
            incident[b][degree[b]++] = wall;
        }

        boolean[] inTree = new boolean[cellCount];
        PriorityQueue<Integer> queue = new PriorityQueue<>(
                Comparator.comparingInt(wall -> walls.cost()[wall]));
        inTree[0] = true;
        for (int wall : incident[0]) {
            queue.add(wall);
        }

        long totalCost = 0;
        int added = 1;
        while (!queue.isEmpty() && added < cellCount) {
            int wall = queue.poll();
            int a = walls.a()[wall];
            int b = walls.b()[wall];
            if (inTree[a] == inTree[b]) {
                continue;
            }
            int newCell = inTree[a] ? b : a;
            inTree[newCell] = true;
            added++;
            totalCost += walls.cost()[wall];
            for (int nextWall : incident[newCell]) {
                int other = walls.a()[nextWall] == newCell
                        ? walls.b()[nextWall] : walls.a()[nextWall];
                if (!inTree[other]) {
                    queue.add(nextWall);
                }
            }
        }

        if (added != cellCount) {
            throw new IllegalStateException("The maze graph is disconnected");
        }
        return totalCost;
    }

    private static int[] toIntArray(List<Integer> values) {
        int[] result = new int[values.size()];
        for (int i = 0; i < values.size(); i++) {
            result[i] = values.get(i);
        }
        return result;
    }

}
