# 3619. Count Islands With Total Value Divisible by K

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/count-islands-with-total-value-divisible-by-k/)

`Array` · `Depth-First Search` · `Breadth-First Search` · `Union-Find` · `Matrix`

## Intuition  
The key observation is that every island can be discovered and its total value accumulated in a single depth‑first walk that simultaneously marks the visited cells. Because each land cell belongs to exactly one island, we never need a second pass, a hash map, or sorting to group cells. The only extra work is a constant‑time modulo check after the walk finishes. This pattern is a classic **flood‑fill / two‑pointer** style traversal where the recursion acts as the “pointer” that expands outward from a seed cell.

## Approach  
1. **Initialize dimensions.** Store `rows = grid.length` and `cols = grid[0].length` for bounds checks.  
2. **Iterate over every cell.**  
   - Loop `i` from `0` to `rows‑1` and `j` from `0` to `cols‑1`.  
   - **Exit condition:** loops finish when `i == rows` and `j == cols`.  
   - **Invariant:** all cells with indices `< i` or (`i` equal and `< j`) have already been processed and, if they were land, have been turned to `0`.  
3. **Start a new island when a positive cell is found.**  
   - Condition `grid[i][j] > 0` guarantees we are on unvisited land.  
   - Call `dfs(grid, i, j)` which returns the sum of the whole connected component.  
4. **Depth‑first accumulation (`dfs`).**  
   - Base case returns `0` if the coordinates are out of bounds or the cell value is `≤ 0`. This prevents revisiting water or already‑cleared land.  
   - Store `value = grid[r][c]`, then immediately set `grid[r][c] = 0` to mark it visited.  
   - Recursively explore the four orthogonal neighbours and return `value +` the four recursive results.  
   - **Invariant:** at the start of each call, the current cell is positive and unmarked; after the call returns, the entire reachable region from that cell is zeroed.  
5. **Check divisibility.** After `dfs` returns, compute `sum % k`. If the remainder is `0`, increment `count`.  
6. **Return the final count** after the double loop finishes.

Edge‑case handling:  
- Empty grid is impossible by constraints, but a single‑cell grid works because the loops still run once.  
- Islands of size 1 are correctly summed because `dfs` returns the cell’s own value before recursing.  
- The modulo uses `%` on a `long` sum to avoid overflow when individual cell values are up to 10⁶ and the island size can be 10⁵.

## Dry Run  
Input: `grid = [[2,0,1],[0,3,0],[4,0,5]]`, `k = 3`  

| Step | (i,j) | Action                               | sum (returned by dfs) | grid after step (relevant cells) | Note                         |
|------|-------|--------------------------------------|-----------------------|----------------------------------|------------------------------|
| 1    | (0,0) | `grid[0][0] > 0` → start dfs         | 2                     | (0,0)=0                          | Island of single cell 2      |
| 2    | (0,2) | `grid[0][2] > 0` → start dfs         | 1                     | (0,2)=0                          | Island of single cell 1      |
| 3    | (1,1) | `grid[1][1] > 0` → start dfs         | 3                     | (1,1)=0                          | Island of single cell 3 (divisible) |
| 4    | (2,0) | `grid[2][0] > 0` → start dfs         | 4                     | (2,0)=0                          | Island of single cell 4      |
| 5    | (2,2) | `grid[2][2] > 0` → start dfs         | 5                     | (2,2)=0                          | Island of single cell 5      |

After the loop, only the island with sum 3 satisfied `sum % 3 == 0`, so `count = 1`.

## Complexity  
- **Time:** `O(m·n)` – each cell is visited at most once; the outer loops scan all `m·n` cells and the recursive flood‑fill touches each land cell exactly one time.  
- **Space:** `O(1)` extra besides the recursion stack, because we mark visited cells in‑place. The recursion depth can reach `O(m·n)` in the worst‑case (a single huge island), which is the only additional memory usage.

## Solution (Java)

```java
class Solution {
    private int cols, rows;
    public int countIslands(int[][] grid, int k) {
        rows = grid.length;
        cols = grid[0].length;
        int count = 0;
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                if(grid[i][j] > 0){
                    long sum = dfs(grid, i, j);
                    if(sum % k == 0) count++;
                }
            }
        }
        return count;
    }
    private long dfs(int[][] grid, int r, int c){
        if(r < 0 || c < 0 || r >= rows || c >= cols || grid[r][c] <= 0) return 0;
        long value = grid[r][c];
        grid[r][c] = 0;
        return value + dfs(grid, r + 1, c) + dfs(grid, r - 1, c) + dfs(grid, r, c + 1) + dfs(grid, r, c - 1); 
    }
}
```

---

**Runtime** 21 ms (beats 66.7%) · **Memory** 118.1 MB (beats 20.0%)

<sub>Synced by AILeetHub on 2026-10-26.</sub>
