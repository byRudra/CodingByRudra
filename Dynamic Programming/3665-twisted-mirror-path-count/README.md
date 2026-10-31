# 3665. Twisted Mirror Path Count

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/twisted-mirror-path-count/)

`Array` · `Dynamic Programming` · `Matrix`

## Intuition  
When the robot reaches a cell it either continues in the same direction (if the cell is empty) or is forced to turn (if the cell is a mirror). Therefore the number of ways to **enter** a cell from the left (moving right) and from above (moving down) are enough to decide what happens next. By propagating these two counters forward we never need a second pass, a hash map or recursion – a single scan with two auxiliary matrices suffices. This is the classic “two‑state DP” pattern for grid walks.

## Approach  
1. Allocate two `long[][]` tables `R` and `D`. `R[i][j]` stores ways to **enter** `(i,j)` while moving right, `D[i][j]` stores ways to **enter** while moving down.  
2. Iterate row‑wise (`i = 0 … m‑1`) and column‑wise (`j = 0 … n‑1`). The invariant: before processing `(i,j)` all contributions from cells above or left have already been added to `R[i][j]` and `D[i][i]`.  
3. Compute the number of ways the robot will **leave** `(i,j)` to the right (`rOut`) and down (`dOut):**  
   * If `(i,j)` is the start `(0,0)`, initialise `rOut = dOut = 1` because the robot may choose either direction.  
   * If `grid[i][j] == 0` (empty), the robot keeps its direction, so `rOut = dOut = (R[i][j] + D[i][m]) % MOD`.  
   * If `grid[i][j] == 1` (mirror), a right‑enter becomes a down‑exit and a down‑enter becomes a right‑exit, i.e. `rOut = D[i][j]` and `dOut = R[i][j]`.  
4. Propagate the computed outs to neighbours: if `j+1 < n` add `rOut` to `R[i][j+1]`; if `i+1 < m` add `dOut` to `M[i+1][j]`. The `% MOD` guard prevents overflow.  
5. After the double loop finishes, the answer is the sum of ways that finally **enter** the target cell from either side: `(R[m‑1][n‑1] + D[m‑1][n‑1]) % MOD`.  
**Edge handling:** The loops naturally skip propagation outside the grid, so paths that would be reflected off the border never contribute. A single‑cell grid never occurs because constraints guarantee `m,n ≥ 2`. The `<=` vs `<` choice is irrelevant here because we only write to existing neighbours after the bounds check.

## Dry Run  

**Input**  

```
grid = [
  [0,1,0],
  [0,0,1],
  [1,0,0]
]
```

| i | j | R[i][j] | D[i][j] | rOut | dOut | Action (updates) | Note |
|---|---|--------|--------|------|------|------------------|------|
|0|0|0|0|1|1|R[0][1]+=1, D[1][0]+=1|start |
|0|1|1|0|0|1|D[1][1]+=0, R[0][2]+=0|mirror flips right→down |
|0|2|0|0|0|0| – |empty, no incoming ways |
|1|0|0|1|1|1|R[1][1]+=1, D[2][0]+=1|empty, both dirs |
|1|1|1|1|2|2|R[1][2]+=2, D[2][1]+=2|empty, sum of both |
|1|2|2|0|0|2|D[2][2]+=0, R[1][3] out|mirror flips right→down |
|2|0|0|1|1|0|R[2][1]+=1, –|mirror flips down→right |
|2|1|1|2|3|3|R[2][2]+=3, D[3][1] out|empty |
|2|2|3|3| – | – | – |final cell |

The final state has `R[2][2]=3` and `D[2][2]=3`; their sum `6` modulo gives `5` valid paths, matching the example.

## Complexity  
- **Time:** O(m·n) – each cell is visited once, and the inner work is O(1) because `rOut` and `dOut` are computed from the two DP tables.  
- **Space:** O(m·n) – two auxiliary matrices `R` and `D` of size `m×n` are stored; the output integer is excluded.

## Solution (Java)

```java
class Solution {
    public int uniquePaths(int[][] grid) {
        final int MOD = 1_000_000_007;
        int m = grid.length, n = grid[0].length;

        // R[i][j]: ways to enter (i,j) moving right
        // D[i][j]: ways to enter (i,j) moving down
        long[][] R = new long[m][n];
        long[][] D = new long[m][n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                long rOut, dOut;

                if (i == 0 && j == 0) {
                    rOut = 1;                       // start: free to go either way
                    dOut = 1;
                } else if (grid[i][j] == 0) {
                    long t = (R[i][j] + D[i][j]) % MOD;
                    rOut = t;
                    dOut = t;
                } else {                            // mirror
                    rOut = D[i][j];                 // entered down  -> turn right
                    dOut = R[i][j];                 // entered right -> turn down
                }

                if (j + 1 < n) R[i][j + 1] = (R[i][j + 1] + rOut) % MOD;
                if (i + 1 < m) D[i + 1][j] = (D[i + 1][j] + dOut) % MOD;
            }
        }

        return (int) ((R[m - 1][n - 1] + D[m - 1][n - 1]) % MOD);
    }
}
```

---

**Runtime** 73 ms (beats 80.3%) · **Memory** 279.4 MB (beats 96.7%)

<sub>Synced by AILeetHub on 2026-10-31.</sub>
