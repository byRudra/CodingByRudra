# 766. Toeplitz Matrix

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/toeplitz-matrix/)

`Array` · `Matrix`

## Intuition  
A Toeplitz matrix requires every element to match the one that lies exactly one row below and one column to the right. Therefore, if we verify the equality `matrix[i][j] == matrix[i+1][j+1]` for every possible pair, the whole diagonal condition is automatically satisfied. The naïve way would be to walk each diagonal separately, which needs extra passes or auxiliary storage. By observing that each interior cell participates in exactly one such pair, we can collapse the verification into a single double‑loop that checks only adjacent diagonal neighbors.

## Approach  
1. **Outer loop (`i`)** – iterate `i` from `0` up to `matrix.length - 2`.  
   *Exit condition*: `i < matrix.length - 1`.  
   *Invariant*: All rows `0 … i‑1` have already been verified against the row below them.  
2. **Inner loop (`j`)** – iterate `j` from `0` up to `matrix[0].length - 2`.  
   *Exit condition*: `j < matrix[0].length - 1`.  
   *Invariant*: For the current row `i`, columns `0 … j‑1` satisfy the Toeplitz property with the next row.  
3. **Comparison** – inside the inner loop evaluate `if (matrix[i][j] != matrix[i+1][j+1])`.  
   - If the condition is true, the matrix violates the Toeplitz rule, so `return false` immediately.  
   - Otherwise continue; the pair `(i,j)` and `(i+1,j+1)` is consistent.  
4. **Completion** – after both loops finish without early exit, every admissible pair has been checked, so `return true`.  

**Edge handling**:  
- When `m == 1` or `n == 1` the outer or inner loop’s range becomes empty (`matrix.length - 1` or `matrix[0].length - 1` equals `0`). The loops skip entirely and the method returns `true`, which is correct because a single row/column trivially satisfies the Toeplitz condition.  
- The loops stop at `length‑1` to avoid an out‑of‑bounds access on `matrix[i+1][j+1]`. Using `<` rather than `<=` guarantees the last valid index is `length‑2`.  

## Dry Run  
Input  

```
[[1,2,3],
 [4,1,2],
 [5,4,1]]
```

| i | j | matrix[i][j] | matrix[i+1][j+1] | note                         |
|---|---|--------------|------------------|------------------------------|
| 0 | 0 | 1            | 1                | equal → continue             |
| 0 | 1 | 2            | 2                | equal → continue             |
| 1 | 0 | 4            | 4                | equal → continue             |
| 1 | 1 | 1            | 1                | equal → continue             |

The loops terminate after `i = 1` (last row that has a row beneath it) and `j = 1` (last column that has a column to its right). No mismatch was found, so the method returns `true`. The final state is that every checked pair matched, confirming the matrix is Toeplitz.

## Complexity  
- **Time:** `O(m·n)` – the double loop visits each interior cell exactly once; `fast`‑forwarding is not needed because each iteration advances one column and one row pair.  
- **Space:** `O(1)` – only a few integer counters (`i`, `j`) are used; the algorithm does not allocate extra data structures beyond the input matrix.

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
