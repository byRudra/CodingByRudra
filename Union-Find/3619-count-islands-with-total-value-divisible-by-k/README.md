# 3619. Count Islands With Total Value Divisible by K

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/count-islands-with-total-value-divisible-by-k/)

`Array` · `Depth-First Search` · `Breadth-First Search` · `Union-Find` · `Matrix`

## Intuition  
When we treat every positive cell as land, an island is exactly the set of cells reachable by repeatedly moving up, down, left or right. If we “erase” a cell by setting it to 0 the moment we first touch it, we guarantee we never revisit it and we also avoid a separate visited matrix. Thus a single depth‑first traversal can both compute the island’s total value and mark the whole island as processed. The naive approach would scan the grid twice—once to locate islands and again to sum them—or would keep an auxiliary boolean array, both of which waste O(m·n) space. The insight that mutating the input grid eliminates the extra pass and extra storage leads directly to the two‑pointer‑free flood‑fill used here.

## Approach  
1. **Initialize dimensions** – `rows = grid.length`, `cols = grid[0].length`.  
2. **Iterate over every cell** (`r` from 0 to `rows‑1`, `c` from 0 to `cols‑1`).  
   - *Invariant*: all cells with index `< (r,c)` have already been examined and any island they belonged to is completely zeroed.  
3. **Start a new island** when `grid[r][c] > 0`.  
   - Push `[r,c]` onto `stack`, add `grid[r][c]` to `sum`, then set `grid[r][c] = 0` to mark visited.  
4. **Process the stack** while it is not empty.  
   - Pop `cur = stack.pop()`.  
   - For each direction `d` in `{0,1,2,3}` compute neighbor `nr = cur[0] + dr[d]`, `nc = cur[1] + dc[d]`.  
   - *Exit condition*: the `while` loop ends when `stack` becomes empty, meaning every reachable positive cell has been visited.  
   - If the neighbor is inside the grid bounds (`nr >= 0 && nr < rows && nc >= 0 && nc < cols`) **and** `grid[nr][nc] > 0`, then:  
     * add its value to `sum`,  
     * zero it (`grid[nr][nc] = 0`),  
     * push `[nr,nc]` onto `stack`.  
   - This invariant guarantees that each cell is pushed at most once, preventing duplicate work and off‑by‑one errors on the borders.  
5. **Finish the island** – after the stack empties, `sum` holds the total value of the just‑explored island. If `sum % k == 0`, increment `count`.  
6. **Return** `count` after the double loop finishes.  
   - Edge cases such as an empty grid or a single‑cell island are naturally handled because the outer loops simply never enter the inner block when `grid[r][c] <= 0`.

## Dry Run  
**Input**

```
grid = [
  [0,2,0],
  [3,1,4],
  [0,5,0]
], k = 3
```

| Step | r | c | cur (popped) | stack (size) | sum | Action |
|------|---|---|--------------|--------------|-----|--------|
| 1 | 0 | 0 | – | – | 0 | cell ≤0, skip |
| 2 | 0 | 1 | – | – | 0 | start island: push (0,1), sum=2, zero it |
| 3 | – | – | (0,1) | 0 | 2 | explore neighbors → (1,1) added (value 1) |
| 4 | – | – | (1,1) | 0 | 3 | neighbors → (1,0) added (3), (1,2) added (4) |
| 5 | – | – | (1,2) | 1 | 7 | neighbor (2,1) added (5) |
| 6 | – | – | (2,1) | 1 | 12 | no new positives |
| 7 | – | – | (1,0) | 0 | 15 | no new positives |
| 8 | 1 | 0 … | – | – | – | remaining cells are 0, loop ends |

After step 7 the stack is empty, `sum = 15`, and `15 % 3 == 0`, so `count` becomes 1. No other positive cells remain, so the final answer is 1.

## Complexity  
- **Time:** O(m · n) – each cell is visited at most once; the inner `while` loop runs a total of `#positive cells` times, and `fast`‑like pointer advances are not used.  
- **Space:** O(min(m·n, s)) – the explicit `stack` holds at most the size `s` of the current island; in the worst case (one giant island) this is O(m·n), otherwise it is bounded by the island’s area. The output integer `count` is O(1) extra space.

## Solution (Java)

```java
// class Solution {
//     private int cols, rows;
//     public int countIslands(int[][] grid, int k) {
//         rows = grid.length;
//         cols = grid[0].length;
//         int count = 0;
//         for(int i = 0; i < rows; i++){
//             for(int j = 0; j < cols; j++){
//                 if(grid[i][j] > 0){
//                     long sum = dfs(grid, i, j);
//                     if(sum % k == 0) count++;
//                 }
//             }
//         }
//         return count;
//     }
//     private long dfs(int[][] grid, int r, int c){
//         if(r < 0 || c < 0 || r >= rows || c >= cols || grid[r][c] <= 0) return 0;
//         long value = grid[r][c];
//         grid[r][c] = 0;
//         return value + dfs(grid, r + 1, c) + dfs(grid, r - 1, c) + dfs(grid, r, c + 1) + dfs(grid, r, c - 1); 
//     }
// }

// Iterative DFA
class Solution {
    private int cols, rows;
    public int countIslands(int[][] grid, int k) {
        rows = grid.length;
        cols = grid[0].length;
        int[] dr = {-1, 1, 0, 0};
        int[] dc = {0, 0, 1, -1};
        int count = 0;
        for(int r = 0; r < rows; r++){
            for(int c = 0; c < cols; c++){
                if (grid[r][c] > 0) {
                    long sum = 0;
                    java.util.ArrayDeque<int[]> stack = new java.util.ArrayDeque<>();
                    stack.push(new int[]{r, c});
                    sum += grid[r][c];
                    grid[r][c] = 0;

                    while (!stack.isEmpty()) {
                        int[] cur = stack.pop();
                        for (int d = 0; d < 4; d++) {
                            int nr = cur[0] + dr[d], nc = cur[1] + dc[d];
                            if (nr >= 0 && nc >= 0 && nr < rows && nc < cols && grid[nr][nc] > 0) {
                                sum += grid[nr][nc];
                                grid[nr][nc] = 0;
                                stack.push(new int[]{nr, nc});
                            }
                        }
                    }
                    if (sum % k == 0) count++;
                }
            }
        }
        return count;
    }
}
```

---

**Runtime** 21 ms (beats 66.7%) · **Memory** 107.8 MB (beats 80.0%)

<sub>Synced by AILeetHub on 2026-10-26.</sub>
