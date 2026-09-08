# 977. Squares of a Sorted Array

![Easy](https://img.shields.io/badge/Difficulty-Easy-00b8a3?style=flat-square) [Open on LeetCode](https://leetcode.com/problems/squares-of-a-sorted-array/)

`Array` · `Two Pointers` · `Sorting`

## Intuition  
The only thing that changes the ordering of the array after squaring is the sign of the original numbers: negatives become positive and may jump to the far right of the sorted order. A straightforward way to restore order is to square every entry first and then rely on a general‑purpose sort to reorder the results. The naïve approach of building a new list, squaring, and then calling a library sort would cost the same amount of work; the insight here is that we can do the squaring in‑place, avoiding an extra pass for copying and keeping the code minimal. This follows the classic “transform‑then‑sort” pattern.

## Approach  
1. **Iterate over the input array** (`for (int i = 0; i < nums.length; i++)`).  
   - *Exit condition*: `i == nums.length`.  
   - *Invariant*: after each iteration, `nums[0..i]` contains the squares of the original values, while `nums[i+1..]` is still untouched.  
   - Action: `nums[i] *= nums[i]` squares the current element in place.  
   - Edge handling: if `nums.length` is `0` or `1`, the loop runs zero or one time respectively, correctly handling empty or single‑element inputs. No overflow concerns arise because `|nums[i]| ≤ 10⁴`, and `10⁴²` fits in a 32‑bit signed int.  

2. **Sort the transformed array** with `Arrays.sort(nums)`.  
   - The built‑in quick‑/tim‑sort runs in `O(n log n)` time and rearranges the squared values into non‑decreasing order.  
   - No additional data structures are allocated; the sort works directly on the same array.  

3. **Return the sorted array**. The method hands back the same reference that was passed in, now containing the desired result.

## Dry Run  
Input: `[-4, -1, 0, 3, 10]`

| i (loop index) | nums after squaring (`nums[i] *= nums[i]`) | Note |
|----------------|--------------------------------------------|------|
| 0 | `[16, -1, 0, 3, 10]` | `-4 → 16` |
| 1 | `[16, 1, 0, 3, 10]` | `-1 → 1` |
| 2 | `[16, 1, 0, 3, 10]` | `0 → 0` (unchanged) |
| 3 | `[16, 1, 0, 9, 10]` | `3 → 9` |
| 4 | `[16, 1, 0, 9, 100]` | `10 → 100` |
| end of loop | — | all elements squared |

After the loop finishes, `Arrays.sort(nums)` rearranges the array to `[0, 1, 9, 16, 100]`. This final ordering satisfies the required non‑decreasing sequence of squares.

## Complexity  
- **Time:** `O(n log n)` – the linear pass for squaring is `O(n)`, but the dominant cost is `Arrays.sort`, which runs in `O(n log n)` for `n` elements.  
- **Space:** `O(1)` extra – the algorithm modifies the input array in place and uses only a few primitive variables; the sorting routine’s internal stack depth is bounded by `log n` and is considered constant extra space for this analysis.

## Solution (Java)

```java
class Solution {
    public int[] sortedSquares(int[] nums) {
        for(int i = 0; i < nums.length; i++){
            nums[i] *= nums[i];
        }
        Arrays.sort(nums);
        return nums;
    }
}
```

---

**Runtime** 10 ms (beats 34.4%) · **Memory** 47.9 MB (beats 20.3%)

<sub>Synced by AILeetHub on 2026-09-08.</sub>
