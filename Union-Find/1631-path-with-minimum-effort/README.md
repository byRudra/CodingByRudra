# 1631. Path With Minimum Effort

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/path-with-minimum-effort/)

`Array` · `Binary Search` · `Depth-First Search` · `Breadth-First Search` · `Union-Find` · `Heap (Priority Queue)` · `Matrix` · `Dijkstra's Algorithm`

## Intuition  
The key observation is that the effort of a path is the **maximum** edge weight seen so far, and this value can never decrease when we extend the path. Consequently, if we always expand the frontier cell whose current effort is smallest, we are guaranteed to settle the optimal effort for that cell—exactly the invariant behind Dijkstra’s algorithm. A naïve solution would try every path (exponential) or binary‑search a threshold and run a BFS for each guess (extra log factor). By treating the maximum‑difference as a monotone “cost” and using a min‑heap, we eliminate the repeated searches and obtain a single pass.

## Approach  
1. **Initialisation** – Compute `rows` and `cols`. Create `effort[rows][cols]` filled with `Integer.MAX_VALUE` and set `effort[0][0] = 0`. Initialise a priority queue `pq` that orders triples `[e, r, c]` by `e` (the current path effort). Push `[0, 0, 0]`.  
2. **Direction array** – `dirs = {{1,0},{0,1},{-1,0},{0,-1}}` enumerates the four moves.  
3. **Main loop** – While `pq` is not empty:  
   - Poll the smallest triple `cur`. Let `e = cur[0]`, `r = cur[1]`, `c = cur[2]`.  
   - **Exit condition**: if `(r,c)` is the bottom‑right cell, return `e`; this is the minimal possible effort because any later poll would have `e` ≥ current `e`.  
   - **Stale entry guard**: if `e > effort[r][c]` skip, because a better effort for this cell has already been recorded.  
   - **Relax neighbours** – For each `dir` compute `nr = r + dir[0]`, `nc = c + dir[1]`. If inside bounds, compute the edge weight `diff = Math.abs(heights[r][c] - heights[nr][nc])` and the candidate effort `ne = Math.max(e, diff)`.  
   - If `ne < effort[nr][nc]`, update `effort[nr][nc] = ne` and push `[ne, nr, nc]` into `pq`. This respects the invariant that `effort[x][y]` always stores the smallest known maximum‑difference to reach `(x,y)`.  
4. The loop is guaranteed to terminate because each cell is relaxed at most when a strictly smaller effort is found, and there are finitely many cells. The final `return 0` is unreachable but satisfies the compiler.

## Dry Run  
Input  

```
heights = [
  [1, 2],
  [3, 4]
]
```

| step | e | r | c | effort matrix (partial) | change |
|------|---|---|---|--------------------------|--------|
| 1    | 0 | 0 | 0 | [[0, INF],[INF, INF]]   | start |
| 2    | 0 | 0 | 0 | [[0, 1],[1, INF]]       | relax (0,1) diff=1 → ne=1, (1,0) diff=2 → ne=2 (but 1 is smaller) |
| 3    | 1 | 0 | 1 | [[0, 1],[1, INF]]       | poll (0,1) |
| 4    | 1 | 0 | 1 | [[0, 1],[1, 1]]         | relax (1,1) diff=2 → ne=max(1,2)=2 (no update), relax (1,0) diff=1 → ne=1 updates (1,0) |
| 5    | 1 | 1 | 0 | [[0, 1],[1, INF]]       | poll (1,0) |
| 6    | 1 | 1 | 0 | [[0, 1],[1, 1]]         | relax (1,1) diff=1 → ne=1 updates (1,1) |
| 7    | 1 | 1 | 1 | [[0, 1],[1, 1]]         | poll target → return 1 |

The algorithm stops at step 7 with effort 1, which is the minimal possible maximum height difference.

## Complexity  
- **Time:** `O(rows * cols * log(rows * cols))` – each cell may be inserted into the heap a few times, and the heap operations cost `log` of the number of stored entries.  
- **Space:** `O(rows * cols)` – the `effort` matrix and the priority queue together store at most one entry per cell (output array excluded).

## Solution (Java)

```java
class Solution {
    public int minimumEffortPath(int[][] heights) {
        int rows = heights.length;
        int cols = heights[0].length;
        int effort [][] = new int[rows][cols];
        for(int[] row : effort){
            Arrays.fill(row, Integer.MAX_VALUE);
        }

        effort[0][0] = 0;
        PriorityQueue<int []> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);
        pq.offer(new int[]{0, 0, 0});

        int dirs[][] = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};

        while(!pq.isEmpty()){
            int []cur = pq.poll();
            int e = cur[0], r = cur[1], c = cur[2];

            if(r == rows - 1 && c == cols - 1){
                return e;
            }
            if(e > effort[r][c]) continue;

            for(int dir[] : dirs){
                int nr = r + dir[0];
                int nc = c + dir[1];
                if(nr >= 0 && nr < rows && nc >=0 && nc < cols){
                    int ne = Math.max(e, Math.abs(heights[r][c] - heights[nr][nc]));
                    if(ne < effort[nr][nc]){
                        effort[nr][nc] = ne;
                        pq.offer(new int[] {ne, nr, nc});
                    }
                } 
            }
        }
        return 0;
    }
}
```

---

**Runtime** 40 ms (beats 80.0%) · **Memory** 47.5 MB (beats 95.6%)

<sub>Synced by AILeetHub on 2026-10-14.</sub>
