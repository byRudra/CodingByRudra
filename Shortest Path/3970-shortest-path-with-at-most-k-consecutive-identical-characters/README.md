# 3970. Shortest Path With At Most K Consecutive Identical Characters

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/shortest-path-with-at-most-k-consecutive-identical-characters/)

`String` · `Graph Theory` · `Heap (Priority Queue)` · `Shortest Path`

## Intuition  
The only obstacle to a classic shortest‑path search is the extra “run” constraint: we must remember how many identical labels we have seen consecutively at the current node. If we treat each pair (node, run) as a distinct state, the graph becomes a layered DAG where moving along an edge either resets the run to 1 (different label) or increments it (same label). This observation eliminates the need for a second pass, a hash‑map of visited substrings, or any exponential enumeration. The solution is therefore a standard Dijkstra on the expanded state space, i.e. the **two‑pointer (state‑augmented) shortest‑path** pattern.

## Approach  
1. **Build adjacency list** `adj`. For every edge `[u,v,w]` we store `{v,w}` in `adj[u]`.  
2. **Initialize distance matrix** `dist[n][k+1]` with `INF`. `dist[u][r]` holds the smallest total weight to reach node `u` with a current run length `r`. Set `dist[0][1]=0` because we start at node 0 with a run of 1 (its own label).  
3. **Priority queue** `pq` stores triples `{cost, node, run}` ordered by `cost`. Insert the start triple `{0,0,1}`.  
4. **Main loop** – while `pq` is not empty:  
   - Pop the smallest triple `cur`. Let `d, u, run` be its components.  
   - **Skip stale entry** if `d > dist[u][run]`. This preserves Dijkstra’s invariant that the first time we pop a state we have its optimal distance.  
   - **Early exit**: if `u == n‑1` we can return `d` immediately because any later pop would be larger.  
   - **Relax outgoing edges**: for each `{v,w}` in `adj[u]` compute the new run `nr = (labels.charAt(v) == labels.charAt(u)) ? run+1 : 1`. If `nr > k` the edge violates the constraint and is ignored. Otherwise compute `nd = d + w`. If `nd < dist[v][nr]`, update `dist[v][nr] = nd` and push `{nd, v, nr}` into `pq`.  
5. If the loop finishes without reaching node `n‑1`, return `-1`.

Key invariants:  
- At the start of each iteration, `dist[u][run]` equals the shortest known weight for that state.  
- The priority queue always extracts the currently smallest unreached state, guaranteeing optimality when a target state is popped.  
- The run length never exceeds `k`, so the state space is bounded by `n·(k+1)`.

## Dry Run  
Input: `n=3, edges=[[0,1,1],[1,2,1],[0,2,3]], labels="aab", k=1`

| step | pop `(u,run,d)` | edge examined | `nr` | `nd` | action | note |
|------|-----------------|---------------|-----|------|--------|------|
| 1 | `(0,1,0)` | 0→1 (w=1) | `nr=2` → >k, skip | – | – | same label “a”, run would be 2 |
|   |             | 0→2 (w=3) | `nr=1` (different) | 3 | `dist[2][1]=3`, push `(2,1,3)` | valid transition |
| 2 | `(2,1,3)` | – | – | – | target reached (`u==2`) | return 3 |

The algorithm terminates after two pops; the shortest valid path weight is 3, matching the expected answer.

## Complexity  
- **Time:** `O((n·k + m) log (n·k))` – each state `(node,run)` is processed at most once; relaxing an edge costs a heap operation, and there are `m` original edges.  
- **Space:** `O(n·k + m)` – adjacency list stores `m` edges, distance matrix stores `n·(k+1)` longs, and the priority queue holds at most the same number of states. (Output is a single integer, not counted.)

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

**Runtime** 66 ms (beats 53.9%) · **Memory** 274.2 MB (beats 19.5%)

<sub>Synced by AILeetHub on 2026-10-30.</sub>
