# 1254. Number of Closed Islands

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/number-of-closed-islands/)

`Array` · `Depth-First Search` · `Breadth-First Search` · `Union-Find` · `Matrix`

## Intuition  
Any island that touches the outer border can never be “closed” because the border itself is water (value 1). Therefore, if we first erase every land cell (0) that is reachable from the boundary, the remaining 0‑cells form exactly the closed islands. The naïve way would be to examine each island twice—once to see if it reaches the edge and again to count it—but the boundary‑first flood‑fill removes the need for a second reachability test. This is a classic two‑phase flood‑fill (depth‑first search) pattern.

## Approach  
1. **Initialize dimensions** – `rows = grid.length`, `cols = grid[0].length`.  
2. **Erase boundary‑connected land** –  
   - Loop `r = 0 … rows‑1`, `c = 0 … cols‑1`.  
   - **Exit condition:** loop ends after the last cell.  
   - **Invariant:** before each iteration, all previously visited boundary cells have already been turned into 1.  
   - If the current cell lies on the outer frame (`r == 0 || c == 0 || r == rows‑1 || c == cols‑1`) **and** `grid[r][c] == 0`, invoke `dfs(grid, r, c)`.  
   - The DFS recursively flips every reachable 0 to 1, guaranteeing that no land touching the border survives this phase.  
   - Edge handling: the check uses `||` (inclusive) because a corner belongs to both a row and a column border; using `<` would miss the last index.  
3. **Count remaining islands** –  
   - Initialise `count = 0`.  
   - Iterate again over every cell with the same double loop.  
   - **Invariant:** at the start of each iteration, all cells belonging to previously counted islands are already 1.  
   - When `grid[r][c] == 0`, we have discovered a new closed island: increment `count` and call `dfs(grid, r, c)` to flood‑fill it to 1, preventing double counting.  
   - The loop naturally handles empty or single‑row/column grids because the boundary phase will turn any existing 0 into 1, leaving `count` at 0.  
4. **Depth‑first search (`dfs`)** –  
   - Base case returns if `(r < 0 || c < 0 || r >= rows || c >= cols || grid[r][c] != 0)`. This guards against out‑of‑bounds and stops when water or already‑visited land is hit.  
   - Mark the current cell as water: `grid[r][c] = 1`.  
   - Recurse in the four cardinal directions.  
   - The recursion depth can reach the size of an island; the algorithm chooses recursion over an explicit stack for brevity.

## Dry Run  
Input  

```
[[1,1,1,1],
 [1,0,0,1],
 [1,1,0,1],
 [1,0,1,1]]
```

| Step | r | c | grid[r][c] before | Action                               | Note                              |
|------|---|---|-------------------|--------------------------------------|-----------------------------------|
| B1   | 0 | 0 | 1                 | skip (border but water)              | –                                 |
| B2   | 1 | 0 | 1                 | skip (border water)                  | –                                 |
| B3   | 3 | 1 | 0                 | `dfs` flips (3,1) → 1; recurses none | removes boundary‑connected land   |
| C1   | 1 | 1 | 0                 | `count=1`; `dfs` flips (1,1),(1,2),(2,2) → 1 | first closed island discovered |
| C2   | 1 | 2 | 1                 | skip (already water)                 |                                   |
| C3   | 2 | 2 | 1                 | skip                                 |                                   |

After the loops finish, `count = 1`, which is the number of closed islands because the only remaining 0‑region was completely surrounded by 1s.

## Complexity  
- **Time:** `O(rows * cols)` – each cell is visited at most twice: once during the boundary flood‑fill and once during the counting flood‑fill.  
- **Space:** `O(rows * cols)` in the worst case for the recursion stack (when a single island occupies the whole grid); the algorithm uses no extra data structures beyond the input grid.

## Solution (Java)

```java
class Solution {
    private int rows, cols;

    public int closedIsland(int[][] grid) {
        rows = grid.length;
        cols = grid[0].length;

        // checking if any island connects with the boundary if it does making the whole island 1 so we know it isnt the one we need to count 
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if ((r == 0 || c == 0 || r == rows - 1 || c == cols - 1) && grid[r][c] == 0) {
                    dfs(grid, r, c);
                }
            }
        }

        // now checking for the left overs
        // we start with 1 because 0 is the boundary
        int count = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] == 0) {
                    count++;
                    // dfs from that point will make every 0 to a 1 and hence this will lead to conversion of the whole island that was 0 to 1
                    dfs(grid, r, c);
                }
            }
        }
        return count;
    }
    public void dfs(int[][] grid, int r, int c){
        if(r < 0 || c < 0 || r >= rows || c >= cols || grid[r][c] != 0) return;
        grid[r][c] = 1;
        dfs(grid, r - 1, c);
        dfs(grid, r + 1, c);
        dfs(grid, r, c + 1);
        dfs(grid, r, c - 1);
    }
}
```

---

**Runtime** 2 ms (beats 99.8%) · **Memory** 45.9 MB (beats 95.9%)

<sub>Synced by AILeetHub on 2026-10-25.</sub>
