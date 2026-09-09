# 619. Biggest Single Number

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/biggest-single-number/)

`Database`

## Intuition  
The only thing that matters is how many times each value appears. If a value’s count is exactly 1, it is a *single* number; otherwise it is discarded. Therefore the answer is simply the maximum among those values whose count equals 1. A naïve solution would scan the table once to collect all rows, then use a hash map or a second pass to filter out duplicates, which costs extra memory or an additional full scan. By grouping on `num` and applying `HAVING COUNT(*) = 1` we obtain the single numbers in a single aggregation step, and `MAX` picks the largest of them in the outer query. This is the classic “group‑by + having” pattern for frequency‑based filtering.

## Approach  
1. **Inner aggregation** – `SELECT num FROM MyNumbers GROUP BY num HAVING COUNT(*) = 1`  
   *Exit condition*: the `GROUP BY` finishes after every distinct `num` has been processed.  
   *Invariant*: for each processed distinct value, `COUNT(*)` reflects the exact number of its occurrences in the original table.  
   The `HAVING` clause discards any group whose count is not 1, leaving only the single numbers.  
2. **Outer selection** – `SELECT MAX(num) AS num FROM MyNumbers WHERE num IN ( … )`  
   *Exit condition*: the outer `SELECT` scans the whole table once more, checking each row against the set produced by the inner query.  
   *Invariant*: at any point, the current candidate for `MAX(num)` is the largest single number seen so far.  
   Because the inner query already guarantees uniqueness, the `WHERE … IN` filter safely restricts the outer scan to eligible rows, and `MAX` returns the greatest of them.  
3. **Result handling** – If the inner query yields no rows, the `MAX` function returns `NULL`, which matches the required “no single number” case. No extra `IF` logic is needed.

## Dry Run  
Input table `MyNumbers`  

| num |
|-----|
| 8   |
| 8   |
| 3   |
| 3   |
| 1   |
| 4   |

| Step | Inner group (num) | Count | `HAVING` passes? | Outer `MAX` candidate | Change |
|------|-------------------|-------|------------------|-----------------------|--------|
| 1    | 8                 | 2     | no               | –                     | none |
| 2    | 3                 | 2     | no               | –                     | none |
| 3    | 1                 | 1     | yes              | 1                     | set to 1 |
| 4    | 4                 | 1     | yes              | 4                     | updated to 4 (larger) |
| End  | –                 | –     | –                | **4**                 | final answer = largest single number |

The inner aggregation discards `8` and `3` because their counts exceed 1, leaving `{1,4}`. The outer scan updates the maximum from `1` to `4`, and the query returns `4`.

## Complexity  
- **Time:** O(n) – the inner `GROUP BY` scans the table once to build frequency groups, and the outer `SELECT` scans it a second time to apply the `IN` filter; both passes are linear in the number of rows `n`.  
- **Space:** O(k) – the database stores `k` distinct values (the groups) where `k ≤ n`; no additional structures proportional to `n` are allocated beyond the grouping hash. The output itself is a single scalar, which is excluded from the space count.

## Solution (MySQL)

```sql
SELECT MAX(num) AS num
FROM MyNumbers
WHERE num IN (
    SELECT num
    FROM MyNumbers
    GROUP BY num
    HAVING COUNT(*) = 1
);
```

---

**Runtime** 410 ms (beats 92.0%) · **Memory** 0B (beats 100.0%)

<sub>Synced by AILeetHub on 2026-09-09.</sub>
