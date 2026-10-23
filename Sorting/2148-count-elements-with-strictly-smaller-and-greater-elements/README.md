# 2148. Count Elements With Strictly Smaller and Greater Elements 

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/count-elements-with-strictly-smaller-and-greater-elements/)

`Array` · `Sorting` · `Counting`

## Intuition  
The key observation is that an element can satisfy the “smaller‑and‑greater” condition **iff** it lies strictly between the global minimum and the global maximum of the whole array. Computing those two extremes once eliminates the need for any extra passes, hash maps, or sorting, which would otherwise cost O(n log n) or O(n) extra space. This reduces the problem to a simple linear scan using the two‑pointer‑style “track min/max while iterating” pattern.

## Approach  
1. **Initialize bounds** – `lo = Integer.MAX_VALUE` and `hi = Integer.MIN_VALUE`.  
2. **First pass (find extremes)** – For each `x` in `nums`:  
   - Update `lo = Math.min(lo, x)`.  
   - Update `hi = Math.max(hi, x)`.  
   *Invariant*: after processing the first *k* elements, `lo` holds the smallest value among them and `hi` holds the largest. The loop exits when all `n` elements have been examined.  
3. **Second pass (count interior elements)** – Reset `count = 0`. For each `x` in `nums`:  
   - If `x > lo && x < hi`, increment `count`.  
   *Invariant*: at the start of each iteration, `count` equals the number of previously seen elements that lie strictly between the final `lo` and `hi`. The loop ends after the last element, leaving `count` as the answer.  
4. **Return** `count`.  

Edge cases are handled by the initial extreme values: an empty or single‑element array (not possible per constraints) would yield `lo == hi`, making the second condition never true, so the answer correctly becomes 0. The comparison uses `>` and `<` (not `>=`/`<=`) to enforce strictness, matching the problem’s requirement.

## Dry Run  

Input: `nums = [11, 7, 2, 15]`

| Iter | x  | lo (after update) | hi (after update) | count (after check) | Note                              |
|------|----|-------------------|-------------------|----------------------|-----------------------------------|
| 1    | 11 | 11                | 11                | 0                    | First element sets both bounds   |
| 2    | 7  | 7                 | 11                | 0                    | 7 < hi but not > lo yet           |
| 3    | 2  | 2                 | 11                | 0                    | New minimum found                 |
| 4    | 15 | 2                 | 15                | 0                    | New maximum found                 |
| 5    | 11 | 2                 | 15                | 1                    | 11 > lo && 11 < hi → count++      |
| 6    | 7  | 2                 | 15                | 2                    | 7 > lo && 7 < hi → count++        |
| 7    | 2  | 2                 | 15                | 2                    | 2 equals lo → not counted         |
| 8    | 15 | 2                 | 15                | 2                    | 15 equals hi → not counted        |

After the second pass finishes, `count = 2`, which is exactly the number of elements that have both a smaller and a greater counterpart.

## Complexity  
- **Time:** O(n) – the algorithm makes two linear scans; the first advances `lo` and `hi` together, the second checks each element once.  
- **Space:** O(1) – only a few primitive variables (`lo`, `hi`, `count`) are used, independent of input size. (The output integer does not affect the asymptotic bound.)

## Solution (Java)

```java
class Solution {
    public int countElements(int[] nums) {
        int lo = Integer.MAX_VALUE, hi = Integer.MIN_VALUE;
        for (int x : nums) {
            lo = Math.min(lo, x);
            hi = Math.max(hi, x);
        }

        int count = 0;
        for (int x : nums) {
            if (x > lo && x < hi) count++;
        }
        return count;
    }
}
```

---

**Runtime** 1 ms (beats 58.2%) · **Memory** 42.8 MB (beats 97.7%)

<sub>Synced by AILeetHub on 2026-10-23.</sub>
