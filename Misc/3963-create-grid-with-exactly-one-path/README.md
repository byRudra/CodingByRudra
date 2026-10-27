# 3963. Create Grid With Exactly One Path

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/create-grid-with-exactly-one-path/)

## Intuition  
The only way to guarantee a single monotone path from the top‑left to the bottom‑right is to force the path to travel entirely across one row and then straight down the last column. If every cell in the first row is free ('.') and, for every later row, only the right‑most cell is free while all cells to its left are obstacles ('#'), the path has no alternative branches. A naïve approach might try to enumerate all possible obstacle placements or run a BFS to verify uniqueness, which would be exponential or at least O(m·n) per check. By constructing the grid directly with the described pattern we eliminate any extra passes, hash‑maps, or recursion. This is a classic **constructive greedy** pattern.

## Approach  
1. Allocate a `String[] grid` of length `m`.  
2. Iterate `i` from `0` to `m‑1` (loop exits when `i == m`).  
   * **Invariant:** rows `0 … i‑1` already satisfy the “first row all free, later rows only last cell free” property.  
3. Inside the loop:  
   * If `i == 0`, set `grid[i] = ".".repeat(n)`. The whole first row becomes free.  
   * Otherwise, set `grid[i] = "#".repeat(n‑1) + "."`. The first `n‑1` cells become obstacles and the last cell stays free.  
4. After the loop finishes, return `grid`.  

**Edge‑case handling**  
* `m == 1` → the loop runs once, hits the `i == 0` branch, and produces a single row of `n` dots, which is the only possible path.  
* `n == 1` → `".".repeat(1)` yields `"."` for the first row; for later rows `"#".repeat(0)` is an empty string, so `grid[i]` becomes `"."`. Every row contains exactly one free cell, forming a vertical straight line—again a unique path.  
* The code deliberately uses `repeat(n‑1)` (which may be zero) rather than a conditional to keep the implementation uniform and avoid off‑by‑one errors.

## Dry Run  
**Input:** `m = 2, n = 3`

| i | grid[i] after assignment | Note |
|---|--------------------------|------|
| 0 | `"..."`                  | First row all free (base case). |
| 1 | `"##."`                  | `"#".repeat(2)` → `"##"` then `+ "."`. Only last cell free. |

Loop ends (`i == 2`). The final grid is `["...", "##."]`. The only admissible moves are right across the first row (`(0,0) → (0,1) → (0,2)`) and then down to `(1,2)`, so exactly one valid path exists.

## Complexity  
- **Time:** **O(m · n)** – each iteration builds a string of length `n` via `repeat`, so the total character work is proportional to the grid size.  
- **Space:** **O(m · n)** – the returned `String[]` stores `m` rows of `n` characters; no extra auxiliary structures are allocated.

## Solution (Java)

```java
class Solution {
    public String[] createGrid(int m, int n) {
        String grid[] = new String[m];
        for(int i = 0; i < m; i++){
            if(i == 0) grid[i] = ".".repeat(n);
            else grid[i] = "#".repeat(n - 1) + ".";
        }
        return grid;
    }
}
```

---

**Runtime** 4 ms (beats 24.0%) · **Memory** 46 MB (beats 86.8%)

<sub>Synced by AILeetHub on 2026-10-27.</sub>
