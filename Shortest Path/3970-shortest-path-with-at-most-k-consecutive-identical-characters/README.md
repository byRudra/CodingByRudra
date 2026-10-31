# 3970. Shortest Path With At Most K Consecutive Identical Characters

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/shortest-path-with-at-most-k-consecutive-identical-characters/)

`String` · `Graph Theory` · `Heap (Priority Queue)` · `Shortest Path`

## Intuition  
The only thing that can invalidate a path is a *run* of identical labels that exceeds k. If we know, for the current node, how many identical characters have appeared consecutively up to that point, we can decide whether an outgoing edge is admissible and how the run length changes. Thus the problem reduces to finding the cheapest way to reach the target **while carrying the run length as part of the state**. A naïve solution would enumerate all paths or keep a separate DP table per node, which either blows up exponentially or requires an extra full‑graph scan for each possible run length. By treating each pair (node, run) as a vertex in an expanded graph, we can apply Dijkstra’s algorithm directly. The pattern is “Dijkstra on an expanded state graph”.

## Approach  
1. **Build adjacency list** `adj`. For each original edge `[u,v,w]` we store `{v,w}` in `adj[u]`.  
2. **Initialize distance matrix** `dist[n][k+1]` with `INF`. `dist[u][r]` will hold the smallest known total weight to reach node `u` with a current run length `r`. Set `dist[0][1] = 0` because we start at node 0 with a run of length 1 (its own label).  
3. **Priority queue** `pq` stores triples `{cost, node, run}` ordered by `cost`. Insert the start triple `{0,0,1}`.  
4. **Main loop** `while (!pq.isEmpty())`:  
   - Pop the smallest‑cost triple `cur`. Let `d, u, run` be its components.  
   - **Invariant:** `d` equals `dist[u][run]`; any later entry for the same state will have `d ≥ dist[u][run]`.  
   - If `d > dist[u][run]` skip (stale entry).  
   - If `u == n‑1` return `d`; the first time the target is extracted it is optimal because Dijkstra processes states in non‑decreasing cost order.  
   - **Relax edges**: for each `{v,w}` in `adj[u]` compute the new run length `nr = (labels.charAt(v) == labels.charAt(u)) ? run+1 : 1`. If `nr > k` the edge would violate the constraint, so continue. Compute new distance `nd = d + w`. If `nd < dist[v][nr]`, update `dist[v][nr] = nd` and push `{nd, v, nr}` into `pq`.  
5. If the queue empties without reaching node `n‑1`, return `-1`.  

**Edge cases handled:**  
- Single‑node graph: `dist[0][1]` is set, the loop immediately returns `0` because `u == n‑1`.  
- `k` = 1 forces a run reset on any label change; the `nr` computation respects this.  
- The run index starts at 1 (not 0) to avoid off‑by‑one confusion when the first node itself forms a run.

## Dry Run  
Input: `n = 3`, `edges = [[0,1,1],[1,2,1],[0,2,3]]`, `labels = "aab"`, `k = 1`.

| step | popped `(d,u,run)` | edge examined → `(v,w)` | `nr` | `nd` | action / note |
|------|-------------------|--------------------------|------|------|----------------|
| 1 | (0, 0, 1) | (0→1, 1) | labels[1]==labels[0] → 2 → **skip** (`nr>k`) | – | edge discarded |
|   |               | (0→2, 3) | labels[2]!=labels[0] → 1 | 3 | `dist[2][1]=3`, push (3,2,1) |
| 2 | (3, 2, 1) | – | – | – | `u == n‑1`, return **3** |

The algorithm never explores the 0→1→2 route because the first edge would create a run of length 2, exceeding `k=1`. The direct edge yields cost 3, which is returned as the optimal answer.

## Complexity  
- **Time:** `O((n·k + m·k) · log(n·k))` – each state `(node,run)` is extracted at most once, and each outgoing edge is relaxed for every feasible run length, with heap operations costing `log(n·k)`.  
- **Space:** `O(n·k)` – the `dist` matrix stores a distance for every node‑run pair; the priority queue holds at most the same number of states. (Output is a single integer, not counted.)

## Solution (Java)

```java
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
```

---

**Runtime** 66 ms (beats 31.4%) · **Memory** 274.2 MB (beats 28.3%)

<sub>Synced by AILeetHub on 2026-10-30.</sub>
