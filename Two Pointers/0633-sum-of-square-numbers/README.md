# 633. Sum of Square Numbers

![Medium](https://img.shields.io/badge/Difficulty-Medium-ffc01e?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/sum-of-square-numbers/)

`Math` · `Two Pointers` · `Binary Search`

## Intuition  
If we place one pointer at the smallest possible square (0²) and another at the largest square not exceeding c (⌊√c⌋²), the sum of the two squares moves monotonically: increasing the left pointer raises the sum, decreasing the right pointer lowers it. Therefore, after each step the pair (left, right) still brackets any possible solution, and we never need to revisit a previously examined pair. A naïve approach would try every a from 0 to ⌊√c⌋ and check whether c − a² is a perfect square, costing O(√c) extra hash‑lookups or binary searches. The two‑pointer invariant eliminates those extra structures and yields a single linear scan over the range.

## Approach  
1. **Initialize** `left = 0` and `right = (long) Math.sqrt(c)`.  
   *Invariant:* `0 ≤ left ≤ right ≤ ⌊√c⌋`.  
2. **Loop while** `left <= right`.  
   *Exit condition:* the pointers have crossed, meaning no pair remains.  
3. Compute `sum = left*left + right*right`.  
4. **If** `sum == c` → return `true`.  
5. **Else if** `sum < c` → increment `left` (`left++`).  
   *Reason:* with the current `right`, any larger `right` would only increase `sum`; we need a larger left square to approach c.  
6. **Else** (`sum > c`) → decrement `right` (`right--`).  
   *Reason:* the current `left` is already as small as possible; reducing `right` lowers the sum toward c.  
7. Repeat steps 3‑6.  
8. If the loop terminates, return `false`.  

Edge cases are handled automatically: for `c = 0` we start with `right = 0`, `sum = 0` and return `true`; for a single‑element range (`left == right`) the comparison still works, and the `<=` guard prevents an off‑by‑one miss when the correct pair is `(0, √c)`.

## Dry Run  

**Input:** `c = 5`  

| Iteration | left | right | sum = left²+right² | Change |
|-----------|------|-------|-------------------|--------|
| 1 | 0 | 2 | 0 + 4 = 4 | sum < c → left++ |
| 2 | 1 | 2 | 1 + 4 = 5 | sum == c → return true |

The algorithm stops after the second iteration because the pair (1, 2) satisfies 1² + 2² = 5, which is exactly the required representation.

## Complexity  
- **Time:** O(√c) – each loop moves either `left` forward or `right` backward, so at most √c + 1 iterations occur.  
- **Space:** O(1) – only a few primitive variables are used; the output boolean does not count toward extra space.

## Solution (Java)

```java
class Solution {
    public boolean judgeSquareSum(int c) {
        long left = 0;
        long right = (long) Math.sqrt(c);

        while (left <= right) {
            long sum = left * left + right * right;

            if (sum == c) {
                return true;
            } else if (sum < c) {
                left++;
            } else {
                right--;
            }
        }

        return false;
    }
}
```

---

**Runtime** 4 ms (beats 83.2%) · **Memory** 41.5 MB (beats 98.5%)

<sub>Synced by AILeetHub on 2026-10-17.</sub>
