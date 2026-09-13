# 766. Toeplitz Matrix

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/toeplitz-matrix/)

`Array` · `Matrix`

## Intuition  
The only thing a Toeplitz matrix requires is that every element matches the one directly down‑right on the same diagonal. If we compare each cell with its immediate diagonal neighbour, any mismatch instantly disproves the property, so we never need to examine an entire diagonal or keep extra state. A naïve solution might collect all diagonals in a map or perform a second pass for each diagonal, which would add O(m·n) space or extra O(m·n) time. By observing that a single pass comparing `matrix[i][j]` with `matrix[i+1][j+1]` suffices, we eliminate all auxiliary structures. This is the classic **two‑pointer on a grid** pattern, but here the pointers are implicit in the nested loops.

## Approach  
1. **Outer loop (`i`)** – iterate rows from `0` to `matrix.length‑2`.  
   *Exit condition:* `i == matrix.length‑1`.  
   *Invariant:* All rows above `i` have already satisfied the Toeplitz condition with their lower neighbours.  
2. **Inner loop (`j`)** – iterate columns from `0` to `matrix[0].length‑2`.  
   *Exit condition:* `j == matrix[0].length‑1`.  
   *Invariant:* For the current row `i`, all columns left of `j` have been verified against their down‑right neighbours.  
3. **Comparison** – check `matrix[i][j] != matrix[i + 1][j + 1]`.  
   *If true*, return `false` immediately because the diagonal containing these two cells is inconsistent.  
4. **Completion** – if both loops finish without early exit, every cell (except the last row and last column) matched its diagonal neighbour, so return `true`.  

**Edge handling:**  
- When `m == 1` or `n == 1` the inner bounds become `-1`, causing the loops to skip entirely; the matrix is trivially Toeplitz, and the method returns `true`.  
- The loops use `< length‑1` rather than `<=` to avoid out‑of‑bounds when accessing `i+1` or `j+1`.  

## Dry Run  
Input  

```
[[1,2,3],
 [4,1,2],
 [5,4,1]]
```

| i | j | Check (`matrix[i][j]` vs `matrix[i+1][j+1]`) | Action |
|---|---|----------------------------------------------|--------|
| 0 | 0 | 1 vs 1                                        | equal → continue |
| 0 | 1 | 2 vs 2                                        | equal → continue |
| 1 | 0 | 4 vs 4                                        | equal → continue |
| 1 | 1 | 1 vs 1                                        | equal → continue |

The loops terminate because `i` reaches `matrix.length‑1`. No mismatch was found, so the final state is `true`, confirming the matrix is Toeplitz.

## Complexity  
- **Time:** O(m · n) – each of the `m‑1` rows is scanned over `n‑1` columns, performing a constant‑time comparison.  
- **Space:** O(1) – the algorithm uses only a few integer counters (`i`, `j`) and no additional data structures; the output boolean does not count toward extra space.

## Solution (Java)

```java
class Solution {
    public boolean isToeplitzMatrix(int[][] matrix) {
        for (int i = 0; i < matrix.length - 1; i++) {
            for (int j = 0; j < matrix[0].length - 1; j++) {

                if (matrix[i][j] != matrix[i + 1][j + 1]) {
                    return false;
                }
            }
        }

        return true;
    }
}
```

---

**Runtime** 0 ms (beats 100.0%) · **Memory** 46.6 MB (beats 26.1%)

<sub>Synced by AILeetHub on 2026-09-12.</sub>
