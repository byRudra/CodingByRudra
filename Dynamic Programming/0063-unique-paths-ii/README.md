# 63. Unique Paths II

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/unique-paths-ii/)

`Array` · `Dynamic Programming` · `Matrix`

## Intuition  
When the robot moves only down or right, every cell’s reachable paths equal the sum of paths that reach the cell directly above it and the cell directly to its left—provided the cell itself is not an obstacle. The naïve way would be to enumerate all routes, which explodes combinatorially, or to store visited states in a hash set, which adds unnecessary overhead. Recognizing the additive relationship lets us fill a table in a single pass, updating each entry from its two predecessors while zero‑ing out cells that contain an obstacle. This is the classic **dynamic programming** pattern for grid‑path counting.

## Approach  
1. **Initialize dimensions** `m = obstacleGrid.length`, `n = obstacleGrid[0].length`.  
2. **Early exit**: if `obstacleGrid[0][0] == 1` return `0` because the start is blocked.  
3. **Create DP table** `int dp[][] = new int[m][n]` and set `dp[0][0] = 1` (one way to stand on the start).  
4. **Iterate rows** `i` from `0` to `m‑1`.  
   - **Iterate columns** `j` from `0` to `n‑1`.  
   - **Exit condition**: loops finish when `i == m` and `j == n`.  
   - **Invariant**: before processing `(i, j)`, `dp[x][y]` holds the exact number of obstacle‑free paths to every cell `(x, y)` with `x < i` or (`x == i` and `y < j`).  
   - **Obstacle handling**: if `obstacleGrid[i][j] == 1`, assign `dp[i][j] = 0` because no path can end on a blocked cell.  
   - **Transition** (when the cell is free):  
     - If `i > 0`, add paths from above: `dp[i][j] += dp[i - 1][j]`.  
     - If `j > 0`, add paths from the left: `dp[i][j] += dp[i][j - 1]`.  
   - These `+=` operations accumulate contributions; the initial value is `0` except for the start cell already set to `1`.  
5. **Result**: after the nested loops, `dp[m‑1][n‑1]` contains the total number of unique obstacle‑free routes to the bottom‑right corner, which is returned.

Key edge decisions:  
- The start cell is seeded before the loops, avoiding a special case inside the loops.  
- The `if (i > 0)` and `if (j > 0)` guards prevent out‑of‑bounds access; using `>` (not `>=`) matches zero‑based indexing.  
- Obstacles overwrite any accumulated value with `0`, ensuring they never contribute to downstream cells.

## Dry Run  
**Input**: `obstacleGrid = [[0,0,1],[0,0,0]]` (2 × 3 grid)

| i | j | obstacleGrid[i][j] | dp[i][j] before | dp[i][j] after | Change |
|---|---|--------------------|----------------|----------------|--------|
|0|0|0|0 (init) |1 (`dp[0][0]=1`) |seed start|
|0|1|0|0|`dp[0][1] += dp[0][0]` →1|from left|
|0|2|1|0|0 (obstacle) |blocked|
|1|0|0|0|`dp[1][0] += dp[0][0]` →1|from above|
|1|1|0|0|`+ dp[0][1]` (1) + `dp[1][0]` (1) →2|both directions|
|1|2|0|0|`+ dp[0][2]` (0) + `dp[1][1]` (2) →2|left only|

Final state: `dp[1][2] = 2`, which is the answer because there are exactly two obstacle‑free routes to the bottom‑right corner.

## Complexity  
- **Time:** O(m × n) – each cell is visited once and the inner updates are constant‑time.  
- **Space:** O(m × n) – the `dp` table stores one integer per grid cell; the output itself is not counted.

## Solution (Java)

```java
class Solution {
    public int uniquePathsWithObstacles(int[][] obstacleGrid) {
        int m = obstacleGrid.length;
        int n = obstacleGrid[0].length;
        int dp[][] = new int[m][n];
        if (obstacleGrid[0][0] == 1)
            return 0;
        dp[0][0] = 1;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (obstacleGrid[i][j] == 1) {
                    dp[i][j] = 0;
                } else {
                    if (i > 0) {
                        dp[i][j] += dp[i - 1][j];
                    }
                    if (j > 0)
                        dp[i][j] += dp[i][j - 1];
                }
            }
        }
        return dp[m - 1][n - 1];
    }
}
```

---

**Runtime** 0 ms (beats 100.0%) · **Memory** 43.5 MB (beats 63.1%)

<sub>Synced by AILeetHub on 2026-10-11.</sub>
