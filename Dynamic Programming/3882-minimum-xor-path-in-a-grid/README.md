# 3882. Minimum XOR Path in a Grid

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/minimum-xor-path-in-a-grid/)

`Array` · `Dynamic Programming` · `Bit Manipulation` · `Matrix`

## Intuition  
The XOR of a path can be built incrementally: when we step into a new cell we simply XOR the current accumulated value with the cell’s number. Therefore the set of all possible XOR results that can reach a cell depends only on the sets from its two predecessors (above and left). The naïve idea of enumerating every path would be exponential, but the observation that the XOR domain is bounded (0 … 1023) lets us store **all** reachable values per cell in a constant‑size container. This eliminates the need for a second pass, a hash‑map keyed by whole paths, or any recursion.

## Approach  
1. **Initialisation** – Create a 2‑D array `dp` where each entry is a `HashSet<Integer>`.  
   * `dp[0][0]` receives the start value: `dp[0][0].add(grid[0][0])`.  
2. **Iterate cells row‑wise** (`for i = 0 … m‑1`, `for j = 0 … n‑1`).  
   * Skip the start cell (`if i==0 && j==0 continue`).  
3. **Merge from the cell above** (`if i > 0`).  
   * For every XOR `x` already stored in `dp[i‑1][j]` insert `x ^ grid[i][j]` into `dp[i][j]`.  
   * Invariant: after processing the “above” merge, `dp[i][j]` contains exactly the XORs of all paths that end at `(i,j)` and whose last step came from `(i‑1,j)`.  
4. **Merge from the cell on the left** (`if j > 0`).  
   * For every XOR `x` in `dp[i][j‑1]` insert `x ^ grid[i][j]` into `dp[i][j]`.  
   * Invariant extends to include paths whose last step came from `(i,j‑1)`.  
5. **Final answer** – The destination set `dp[m‑1][n‑1]` now holds every XOR achievable at the bottom‑right corner; the minimum is obtained with `Collections.min(dp[m‑1][n‑1])`.  

Edge handling:  
* A single‑cell grid never enters the loops; the answer is the sole element added in step 1.  
* For a one‑row or one‑column grid the “above” or “left” condition is never true, so only the existing predecessor contributes, matching the unique monotone path.  
* The XOR result space is limited to 10 bits, so each `HashSet` never exceeds 1024 entries; the code relies on this bound rather than explicit size checks.

## Dry Run  
Input: `grid = [[1,2],[3,4]]`

| i | j | dp[i][j] (set)                | note                                   |
|---|---|------------------------------|----------------------------------------|
|0|0|{1}|initial cell|
|0|1|{1 ^ 2 = 3}|only left predecessor (0,0)|
|1|0|{1 ^ 3 = 2}|only above predecessor (0,0)|
|1|1|{3 ^ 4 = 7, 2 ^ 4 = 6}|merge from (0,1) gives 7, from (1,0) gives 6|

After the loops finish, `dp[1][1] = {7,6}` and `Collections.min` returns **6**, which is the optimal XOR path.

## Complexity  
- **Time:** O(m · n · B) where B = 1024 is the maximum number of distinct XOR values; each cell iterates over at most B elements from its two neighbours, so effectively O(m · n).  
- **Space:** O(m · n · B) for the `dp` table of hash sets; B is a constant, thus the extra space is O(m · n) beyond the input grid. (The output integer itself is not counted.)

## Solution (Java)

```java
class Solution {
    public int minCost(int[][] grid) {
         int m = grid.length, n = grid[0].length;

        // dp[i][j] = all XOR values possible at cell (i, j)
        Set<Integer>[][] dp = new HashSet[m][n];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                dp[i][j] = new HashSet<>();

        dp[0][0].add(grid[0][0]);

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (i == 0 && j == 0) continue;

                // from above
                if (i > 0)
                    for (int x : dp[i - 1][j])
                        dp[i][j].add(x ^ grid[i][j]);

                // from left
                if (j > 0)
                    for (int x : dp[i][j - 1])
                        dp[i][j].add(x ^ grid[i][j]);
            }
        }

        return Collections.min(dp[m - 1][n - 1]);
    }
}
```

---

**Runtime** 827 ms (beats 5.2%) · **Memory** 281.3 MB (beats 6.9%)

<sub>Synced by AILeetHub on 2026-10-28.</sub>
