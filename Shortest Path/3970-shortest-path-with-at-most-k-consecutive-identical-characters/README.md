# 3970. Shortest Path With At Most K Consecutive Identical Characters

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/shortest-path-with-at-most-k-consecutive-identical-characters/)

`String` · `Graph Theory` · `Heap (Priority Queue)` · `Shortest Path`

## Intuition  
The only obstacle to a standard shortest‑path search is the *run* of identical labels: after each step we must know how many consecutive equal characters have appeared so far. If we treat “(node, current run length)” as a single state, the constraint becomes a simple feasibility check on transitions. This observation lets us reuse Dijkstra’s algorithm without any extra passes, hash maps, or sorting of the label string. The pattern is a **state‑expanded Dijkstra** (two‑dimensional distance array).

## Approach  
1. **Build adjacency list** `adj`. For every edge `[u,v,w]` store `{v,w}` in `adj[u]`.  
2. **Initialize distance matrix** `dist[n][k+1]` with `INF`. `dist[u][r]` holds the smallest total weight to reach node `u` while the current run of identical labels has length `r`. Set `dist[0][1] = 0` because we start at node 0 with a run of length 1.  
3. **Priority queue** `pq` stores triples `{cost, node, run}` ordered by `cost`. Insert the start triple `{0,0,1}`.  
4. **Main loop** – pop the smallest‑cost triple `cur`.  
   - If `cur.cost` is larger than the recorded `dist[node][run]`, skip (stale entry).  
   - If `node == n‑1`, return `cur.cost`; the first time the target is popped is optimal because Dijkstra processes states in non‑decreasing order of cost.  
5. **Relax outgoing edges** of `node`. For each `{v,w}` in `adj[node]`:  
   - Compute the next run length `nr`. If `labels[v] == labels[node]` then `nr = run + 1`; otherwise `nr = 1`.  
   - If `nr > k` the transition violates the constraint → ignore.  
   - New distance `nd = cur.cost + w`. If `nd < dist[v][nr]`, update `dist[v][nr] = nd` and push `{nd, v, nr}` into `pq`.  
6. If the queue empties without reaching `n‑1`, return `-1`.

Key invariants:  
- The priority queue always contains the cheapest undiscovered state.  
- `dist[u][r]` is the best known cost for that exact `(node,run)` pair.  
- The loop terminates when the smallest remaining cost belongs to the target, guaranteeing optimality.

Edge cases handled explicitly:  
- Single‑node graph (`n==1`) works because `dist[0][1]=0` and the first pop returns 0.  
- Paths that would exceed `k` are filtered by the `nr > k` guard.  
- The run length is stored from 1 to k (inclusive); using `k+1` columns avoids off‑by‑one errors.

## Dry Run  
Input: `n = 3`, `edges = [[0,1,1],[1,2,1],[0,2,3]]`, `labels = "aab"`, `k = 1`.

| step | pop (cost,node,run) | explore → (v, w) | nr | nd | dist after update | note |
|------|---------------------|------------------|----|----|-------------------|------|
| 1 | (0,0,1) | (1,1) → nr=2 (skip, >k) | – | – | unchanged | edge 0→1 invalid because run would be 2 |
|   |                     | (2,3) → nr=1 | 1 | 3 | `dist[2][1]=3` | push (3,2,1) |
| 2 | (3,2,1) | – | – | – | – | node 2 is target → return 3 |

The algorithm returns `3`, matching the optimal path that uses the direct edge `0→2`.

## Complexity  
- **Time:** `O((n·k + m·k) log (n·k))` → each state `(node,run)` is extracted at most once, and each outgoing edge is relaxed at most `k` times; the priority queue operations dominate with a logarithmic factor.  
- **Space:** `O(n·k + m)` → the distance matrix stores `n·(k+1)` longs and the adjacency list holds `m` edges; the heap stores at most the same number of states. (The output integer does not affect the bound.)

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

**Runtime** 66 ms (beats 53.8%) · **Memory** 274.2 MB (beats 21.2%)

<sub>Synced by AILeetHub on 2026-10-30.</sub>
