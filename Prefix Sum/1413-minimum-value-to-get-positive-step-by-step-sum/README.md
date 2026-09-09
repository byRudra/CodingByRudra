# 1413. Minimum Value to Get Positive Step by Step Sum

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/minimum-value-to-get-positive-step-by-step-sum/)

`Array` · `Prefix Sum`

## Intuition  
The crucial observation is that the step‑by‑step sum never drops below 1 exactly when the smallest prefix sum of the array (starting from zero) is at least ‑(startValue‑1). In other words, if we track the running total while scanning `nums`, the most negative value it ever reaches tells us how much we must lift the initial value to keep the whole walk positive. A naïve solution would try every possible start value or store all prefix sums and then search, both of which add extra passes or memory. By maintaining only the current sum and the minimum seen so far, we eliminate those overheads. This is a classic **prefix‑sum** pattern.

## Approach  
1. **Initialize** `sum = 0` and `minSum = 0`.  
   *Invariant*: before processing any element, `sum` equals the total of processed elements, and `minSum` is the smallest value `sum` has taken so far.  
2. **Iterate** over each `num` in `nums` with an enhanced for‑loop.  
   - Update `sum += num`.  
   - Update `minSum = Math.min(minSum, sum)`.  
   *Exit condition*: loop ends after the last element; the invariant guarantees `minSum` is the minimum prefix sum of the entire array.  
3. **Compute answer** as `1 - minSum`.  
   Since `minSum` is ≤ 0, adding `1 - minSum` to the original zero baseline shifts the whole walk upward so that the lowest point becomes 1. This single arithmetic step yields the smallest positive start value.  
**Edge handling**:  
- Empty or single‑element arrays are covered because the loop runs zero or one times, leaving `minSum` at 0 if no negative dip occurs, and the formula returns 1.  
- The code uses `<=` inside `Math.min` implicitly; we do not need an explicit comparison because `Math.min` already chooses the smaller of the two values.  
- No overflow concerns arise given the constraints (`|num| ≤ 100` and length ≤ 100), so plain `int` arithmetic is safe.

## Dry Run  

**Input**: `[-3, 2, -3, 4, 2]`

| Iteration | num | sum (after addition) | minSum (after min) | Note |
|-----------|-----|----------------------|--------------------|------|
| 0 (init)  | —   | 0                    | 0                  | start state |
| 1         | -3  | -3                   | -3                 | sum drops below previous min |
| 2         | 2   | -1                   | -3                 | sum rises but min stays |
| 3         | -3  | -4                   | -4                 | new low point |
| 4         | 4   | 0                    | -4                 | back to non‑negative |
| 5         | 2   | 2                    | -4                 | final sum positive |

After the loop, `minSum = -4`. The answer is `1 - (-4) = 5`, which is exactly the minimum start value that keeps every intermediate total ≥ 1.

## Complexity  
- **Time:** O(n) – the single `for` loop visits each of the `n` elements once, updating `sum` and `minSum` in constant time.  
- **Space:** O(1) – only two integer variables are stored regardless of input size; the output integer does not count toward auxiliary space.

## Solution (Java)

```java
class Solution {
    public int minStartValue(int[] nums) {
         int sum = 0;
        int minSum = 0;

        for (int num : nums) {
            sum += num;
            minSum = Math.min(minSum, sum);
        }

        return 1 - minSum;
    }
}
```

---

**Runtime** 0 ms (beats 100.0%) · **Memory** 42.9 MB (beats 46.7%)

<sub>Synced by AILeetHub on 2026-09-09.</sub>
