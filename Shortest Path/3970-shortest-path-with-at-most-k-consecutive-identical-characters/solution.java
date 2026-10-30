import java.util.*;

class Solution {
    public int shortestPath(int n, int[][] edges, String labels, int k) {
        List<int[]>[] adj = new ArrayList[n];
        for (int i = 0; i < n; i++) adj[i] = new ArrayList<>();
        for (int[] e : edges) adj[e[0]].add(new int[]{e[1], e[2]});

        long INF = Long.MAX_VALUE / 2;
        long[][] dist = new long[n][k + 1];
        for (long[] row : dist) Arrays.fill(row, INF);

        // {cost, node, run}
        PriorityQueue<long[]> pq = new PriorityQueue<>((a, b) -> Long.compare(a[0], b[0]));
        dist[0][1] = 0;
        pq.offer(new long[]{0, 0, 1});

        while (!pq.isEmpty()) {
            long[] cur = pq.poll();
            long d = cur[0];
            int u = (int) cur[1], run = (int) cur[2];

            if (d > dist[u][run]) continue;
            if (u == n - 1) return (int) d; // first pop of target is optimal

            for (int[] edge : adj[u]) {
                int v = edge[0], w = edge[1];
                int nr = labels.charAt(v) == labels.charAt(u) ? run + 1 : 1;
                if (nr > k) continue;
                long nd = d + w;
                if (nd < dist[v][nr]) {
                    dist[v][nr] = nd;
                    pq.offer(new long[]{nd, v, nr});
                }
            }
        }
        return -1;
    }
}