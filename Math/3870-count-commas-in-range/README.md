# 3870. Count Commas in Range

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/count-commas-in-range/)

`Math`

## Intuition  
The only numbers that ever contain a comma are those with at least four digits, i.e. values ≥ 1000. Because the input bound is ≤ 10⁵, every such number contributes exactly **one** comma – there is never a second comma within the range. Therefore the total number of commas equals the count of integers from 1000 to n, which is `n‑999`. The naïve way would be to iterate over the whole interval and count commas per number, costing O(n) time; the observation that each qualifying number contributes a uniform amount lets us replace the loop with a constant‑time arithmetic expression. This is a classic “count‑by‑range” pattern.

## Approach  
1. **Check the lower bound.**  
   - `if (n < 1000) return 0;`  
   - *Exit condition*: when `n` is less than 1000 the loop would never execute, so the answer is zero. The invariant here is “no number processed so far has a comma”.  

2. **Initialize the accumulator.**  
   - `int count = 1;`  
   - The code starts with one comma because the smallest number that contributes a comma is 1000, and we will later add the remaining contributions. This choice of starting at 1 (instead of 0) mirrors the formula `1 + (n‑1000)`.  

3. **Add the remaining contributions.**  
   - `count += n - 1000;`  
   - The expression `n‑1000` counts how many numbers lie strictly after 1000 up to `n`. Adding this to the initial 1 yields `n‑999`, which is exactly the number of integers in `[1000, n]`. No loop is needed; the invariant “each processed number adds one comma” holds for the whole range.  

4. **Return the result.**  
   - `return count;`  
   - The final `count` now equals the total commas for the entire interval.

## Dry Run  
**Input:** `n = 1005`

| Step | `n` | `count` before | Operation                | `count` after | Note                              |
|------|-----|----------------|--------------------------|---------------|-----------------------------------|
| 1    | 1005| –              | `if (n < 1000)` false   | –             | Proceed to initialization         |
| 2    | 1005| –              | `count = 1`              | 1             | Account for number 1000           |
| 3    | 1005| 1              | `count += n - 1000` → `+5`| 6             | Numbers 1001‑1005 each add one    |
| 4    | 1005| 6              | `return count`           | 6             | Total commas = 6 (1000‑1005)      |

The algorithm stops after the single arithmetic step; the final `count = 6` matches the six commas appearing in “1,000” through “1,005”.

## Complexity  
- **Time:** O(1) – the method performs a constant number of arithmetic operations regardless of `n`.  
- **Space:** O(1) – only a few primitive variables (`n`, `count`) are used; no additional data structures are allocated.

## Solution (Java)

```java
class Solution {
    public int countCommas(int n) {
        if(n < 1000) return 0;
        int count = 1;
        count += n - 1000;
        return count;
    }
}
```

---

**Runtime** 0 ms (beats 100.0%) · **Memory** 42.6 MB (beats 39.9%)

<sub>Synced by AILeetHub on 2026-09-08.</sub>
