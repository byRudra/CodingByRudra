# 1254. Number of Closed Islands

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/number-of-closed-islands/)

`Array` · `Depth-First Search` · `Breadth-First Search` · `Union-Find` · `Matrix`

## Intuition  
If a land cell (0) can reach the matrix border by moving only up, down, left or right, then the whole island it belongs to is open and must not be counted. Conversely, an island is closed exactly when **every** DFS branch starting from any of its cells stays inside the grid and never steps out of bounds. The naive way would be to flood‑fill each island, then separately scan its border cells, costing an extra pass or extra bookkeeping. By letting the recursive DFS itself return a boolean that is `false` the moment it steps out of the grid, we collapse both the flood‑fill and the “is it closed?” check into a single traversal. This is the classic *two‑state DFS* pattern.

## Approach  
1. **Initialize dimensions** `rows = grid.length`, `cols = grid[0].length`.  
2. **Iterate over every cell** (`r = 0 … rows‑1`, `c = 0 … cols‑1`).  
   - *Exit condition*: loops finish when `r == rows` and `c == cols`.  
   - *Invariant*: all cells with indices `< r` or (`== r` and `< c`) have already been examined and, if they were land, are recorded in `visited`.  
   - If `grid[r][c] == 0` **and** the cell key `r*cols + c` is not in `visited`, start a DFS.  
3. **DFS** `dfs(grid, r, c)` returns `true` iff the component reachable from `(r,c)` never leaves the matrix.  
   - **Base cases**:  
     * Out‑of‑bounds → return `false` (the island touches the border).  
     * Water (`grid[r][c] == 1`) → return `true` (this direction is safely closed).  
     * Already visited → return `true` (we have already proven this part closed).  
   - Mark the current cell as visited: `visited.put(r*cols + c, true)`.  
   - Recurse in the four directions, storing the results in `up`, `down`, `right`, `left`.  
   - Return `up && down && right && left`. The logical AND guarantees that a single `false` (border contact) propagates up, marking the whole island as open.  
4. Back in the outer loops, if `dfs(grid, r, c)` returned `true`, increment `count`.  
5. After scanning the whole grid, return `count`.

**Edge handling**:  
- Empty grid is impossible by constraints, but the loops naturally handle a single‑cell grid.  
- The algorithm treats even‑ and odd‑sized islands uniformly because the boolean aggregation does not depend on island size.  
- Using a `Map<Integer, Boolean>` avoids mutating the input; the key `r*cols + c` uniquely identifies a cell.

## Dry Run  
Input  

```
[[0,1,0],
 [0,0,1],
 [1,1,0]]
```

| step | r | c | visited keys after step | dfs return | note |
|------|---|---|--------------------------|------------|------|
| 1 | 0 | 0 | {0} | false | out‑of‑bounds reached via up → open island |
| 2 | 0 | 2 | {0,2} | true | all recursive calls hit water → closed |
| 3 | 1 | 0 | {0,2,3} | false | down recursion reaches border (row 2, col 0 is water, but up from (1,0) goes out) |
| 4 | 1 | 1 | {0,2,3,4} | false | left recursion hits (1,0) already visited → true, but up goes out → false |
| 5 | 2 | 2 | {0,2,3,4,8} | true | isolated land surrounded by water |
| 6 | – | – | – | – | loops finish |

`count` increments on steps 2 and 5, so the final answer is **2** closed islands.

## Complexity  
- **Time:** O(rows × cols) – each cell is visited at most once; the DFS explores each neighbor a constant number of times, and the outer loops run `rows*cols` iterations.  
- **Space:** O(rows × cols) in the worst case for the `visited` map (the recursion stack is bounded by the size of the current island, which is ≤ rows × cols). The output integer is excluded from the space count.

## Solution (Java)

```java
// class Solution {
//     private int rows, cols;

//     public int closedIsland(int[][] grid) {
//         rows = grid.length;
//         cols = grid[0].length;

//         // checking if any island connects with the boundary if it does making the whole island 1 so we know it isnt the one we need to count 
//         for (int r = 0; r < rows; r++) {
//             for (int c = 0; c < cols; c++) {
//                 if ((r == 0 || c == 0 || r == rows - 1 || c == cols - 1) && grid[r][c] == 0) {
//                     dfs(grid, r, c);
//                 }
//             }
//         }

//         // now checking for the left overs
//         // we start with 1 because 0 is the boundary
//         int count = 0;
//         for (int r = 0; r < rows; r++) {
//             for (int c = 0; c < cols; c++) {
//                 if (grid[r][c] == 0) {
//                     count++;
//                     // dfs from that point will make every 0 to a 1 and hence this will lead to conversion of the whole island that was 0 to 1
//                     dfs(grid, r, c);
//                 }
//             }
//         }
//         return count;
//     }
//     public void dfs(int[][] grid, int r, int c){
//         if(r < 0 || c < 0 || r >= rows || c >= cols || grid[r][c] != 0) return;
//         grid[r][c] = 1;
//         dfs(grid, r - 1, c);
//         dfs(grid, r + 1, c);
//         dfs(grid, r, c + 1);
//         dfs(grid, r, c - 1);
//     }
// }

// Now with an approach in which direct manipulation of the grid is not allowed

class Solution {
    private int rows, cols;

    private Map<Integer, Boolean> visited = new HashMap<>();

    public int closedIsland(int[][] grid) {
        rows = grid.length;
        cols = grid[0].length;

        int count = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] == 0 && !visited.containsKey(r * cols + c)) {
                    if(dfs(grid, r, c))
                        count++;
                }
            }
        }
        return count;
    }

    public boolean dfs(int[][] grid, int r, int c) {
        if (r < 0 || c < 0 || r >= rows || c >= cols)
            return false;
        if(grid[r][c] == 1) return true;
        int key = r * cols + c;
        if(visited.containsKey(key)) return true;
        visited.put(key, true);

            boolean up    = dfs(grid, r - 1, c);
    boolean down  = dfs(grid, r + 1, c);
    boolean right = dfs(grid, r, c + 1);
    boolean left  = dfs(grid, r, c - 1);

    return up && down && right && left;
    }
}
```

---

**Runtime** 7 ms (beats 5.6%) · **Memory** 47 MB (beats 6.4%)

<sub>Synced by AILeetHub on 2026-10-25.</sub>
