# 154. Find Minimum in Rotated Sorted Array II

![Hard](https://img.shields.io/badge/Difficulty-Hard-ff375f?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/find-minimum-in-rotated-sorted-array-ii/)

`Array` · `Binary Search`

## Intuition  
The minimum of a rotated sorted array is simply the smallest value that appears anywhere in the list. Because duplicates are allowed, the usual binary‑search invariant (“right half is strictly larger than left half”) can break, forcing us to fall back to a guarantee that works for any ordering: a single linear scan. The naïve O(n log n) or O(log n) binary‑search tricks are eliminated by the presence of equal elements, so the only safe way to “decrease overall operation steps as much as possible” is to examine each element exactly once. This is the classic *single‑pass linear scan* pattern.

## Approach  
1. **Initialize** `min` to `Integer.MAX_VALUE`.  
2. **Iterate** over the array with an enhanced for‑loop: `for (int num : nums)`.  
   - *Loop invariant*: after processing the first *k* elements, `min` holds the smallest value among those *k* elements.  
   - The loop exits when every element has been visited (`k == nums.length`).  
3. **Update** `min` on each iteration using `Math.min(min, num)`. This preserves the invariant because the new `min` is the smaller of the previous `min` and the current element.  
4. **Return** `min` after the loop finishes.  
   - Edge cases: the problem guarantees `n ≥ 1`, so the array is never empty; a single‑element array works automatically because the loop runs once and `min` becomes that element. No special handling for odd/even length or duplicates is required—the comparison works for any integer value.

## Dry Run  
Input: `nums = [2, 2, 2, 0, 1]`

| Iteration | `num` | `min` before | `min` after | Note |
|-----------|------|--------------|-------------|------|
| 0 | 2 | `∞` (MAX_VALUE) | 2 | First element sets baseline |
| 1 | 2 | 2 | 2 | Equal, no change |
| 2 | 2 | 2 | 2 | Equal, no change |
| 3 | 0 | 2 | 0 | Smaller value updates `min` |
| 4 | 1 | 0 | 0 | Larger than current `min`, stays 0 |

After the fifth iteration the loop ends; `min` equals `0`, which is the correct minimum of the array.

## Complexity  
- **Time:** **O(n)** – the `for` loop visits each of the `n` elements exactly once.  
- **Space:** **O(1)** – only a constant‑size integer variable `min` is used, independent of input size. (The output integer does not affect the space analysis.)

## Solution (Java)

```java
class Solution {
    public int findMin(int[] nums) {
        int min = Integer.MAX_VALUE;
        for(int num : nums){
            min = Math.min(min, num);
        }
        return min;
    }
}
```

---

**Runtime** 0 ms (beats 100.0%) · **Memory** 44.9 MB (beats 38.6%)

<sub>Synced by AILeetHub on 2026-09-09.</sub>
