# 441. Arranging Coins

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/arranging-coins/)

`Math` · `Binary Search`

## Intuition  
The staircase can be built row by row: the first row consumes 1 coin, the second consumes 2, the third 3, and so on. After we have placed `rows` complete rows, exactly `1 + 2 + … + rows = rows·(rows+1)/2` coins are gone, leaving `n` coins still unused. The key observation is that we can keep a running count of how many rows are already complete and, at each step, check whether the next row’s demand (`rows + 1` coins) fits into the remaining pile. This greedy check eliminates the need for a closed‑form formula or a binary‑search over the triangular numbers; we simply subtract the cost of each new row until we can’t afford the next one. The pattern is a **greedy accumulation** using a single loop.

## Approach  
1. **Initialize** `rows = 0`. At this point no rows are built and the whole `n` coins are still available.  
2. **Loop condition** `while (n >= rows + 1)`. The invariant inside the loop is:  
   *`n` equals the number of coins that have not been placed yet, and `rows` equals the count of completely built rows.*  
   Because the next row requires `rows + 1` coins, the condition guarantees we only enter the body when that many coins are still present.  
3. **Add a new row**: `rows++`. This records that we have just completed another full row.  
4. **Consume its coins**: `n = n - rows`. After the increment, `rows` holds the exact size of the row we just added, so subtracting it updates the remaining coin count correctly.  
5. **Repeat** until the condition fails, i.e., the remaining `n` is smaller than the coins needed for the next row.  
6. **Return** `rows`. At loop exit, `rows` is the maximal integer such that the sum of the first `rows` natural numbers does not exceed the original `n`.

*Edge considerations* – The code works for the smallest allowed input (`n = 1`) because the loop runs once (`rows` becomes 1, `n` becomes 0) and then stops. For very large `n` (up to `2^31‑1`) the loop still terminates because `rows` grows roughly as √(2n), never causing overflow in the subtraction step. The `<=` vs `<` choice is irrelevant here; the condition uses `>=` to ensure we have **at least** enough coins for the next row.

## Dry Run  

**Input:** `n = 5`

| Iteration | rows (after increment) | n (remaining) | Note                              |
|-----------|------------------------|---------------|-----------------------------------|
| 0 (start) | 0                      | 5             | initial state                     |
| 1         | 1                      | 4 (=5‑1)      | row 1 built, 1 coin used          |
| 2         | 2                      | 2 (=4‑2)      | row 2 built, 2 coins used         |
| 3         | – (stop)               | 2 (< 3)       | cannot afford row 3 (needs 3)    |

Loop exits after iteration 2; `rows = 2`, which is the number of complete rows.  

## Complexity  
- **Time:** O(k) = O(√n) – the loop executes once per completed row, and the number of rows `k` satisfies `k(k+1)/2 ≤ n`, giving `k = Θ(√n)`.  
- **Space:** O(1) – only a few integer variables (`rows`, `n`) are used, independent of the input size.

## Solution (Java)

```java
class Solution {
    public int arrangeCoins(int n) {
                int rows = 0;
         while (n >= rows + 1) {
            rows++;
            n = n - rows;
        }
        return rows;
    }
}
```

---

**Runtime** 7 ms (beats 18.9%) · **Memory** 42.8 MB (beats 45.7%)

<sub>Synced by AILeetHub on 2026-09-18.</sub>
